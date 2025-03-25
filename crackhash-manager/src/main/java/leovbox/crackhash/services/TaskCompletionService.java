package leovbox.crackhash.services;

import jakarta.annotation.PostConstruct;
import leovbox.crackhash.models.Status;
import leovbox.crackhash.requests.CrackRequest;
import org.springframework.scheduling.annotation.Async;
import leovbox.crackhash.requests.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TaskCompletionService {

    private final TaskStorageService taskStorageService;
    private final RestTemplate restTemplate;
    private final WorkerClientService workerClientService;

    @Value("${worker.urls}")
    private List<String> workerUrls;

    @PostConstruct
    public void printWorkerUrls() {
        System.out.println("WORKER_URLS from application.properties: " + workerUrls);
    }

    public int getWorkersCount() {
        return workerUrls.size();
    }

    @Value("${timeout.ms}")
    private int timeout;

    @PostConstruct
    public void printTimout() {
        System.out.println("Timeout = " + timeout);

    }

    @Autowired
    public TaskCompletionService(TaskStorageService taskStorageService, RestTemplate restTemplate,
                                 WorkerClientService workerClientService) {
        this.taskStorageService = taskStorageService;
        this.restTemplate = restTemplate;
        this.workerClientService = workerClientService;
    }

    /**
     * Ожидает завершения задачи, периодически опрашивая Worker'ов.
     *
     * @param taskId Идентификатор задачи.
     * @return Результаты выполнения задачи.
     */
    @Async
    public CompletableFuture<Void> executeTaskAsync(String taskId, CrackRequest crackRequest) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Отправляем задачи всем Worker'ам
                workerClientService.sendTasksToWorkers(crackRequest, taskId);

                // Ожидаем завершения задачи с таймаутом (например, 60 секунд)
                long startTime = System.currentTimeMillis();
                while (!taskStorageService.isTaskCompleted(taskId)) {
                    if (System.currentTimeMillis() - startTime > timeout) { // Таймаут 60 секунд
                        taskStorageService.updateStatus(taskId, Status.ERROR);
                        System.out.println(taskId + "timeout");
                        break;
                    }
                    Thread.sleep(1000); // Ждем 1 секунду
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                taskStorageService.updateStatus(taskId, Status.ERROR);
            }
        });
    }


    /**
     * Получает статус задачи от Worker'а.
     *
     * @param workerUrl URL Worker'а.
     * @param taskId    Идентификатор задачи.
     * @return Результат выполнения задачи или null, если запрос не удался.
     */
    private TaskResult getTaskStatusFromWorker(String workerUrl, String taskId) {
        String url = workerUrl + "/internal/api/worker/hash/crack/task";

        // Создаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем тело запроса (в данном случае это taskId)
        HttpEntity<String> requestEntity = new HttpEntity<>(taskId, headers);

        // Отправляем POST-запрос на Worker
        try {
            return restTemplate.postForObject(url, requestEntity, TaskResult.class);
        } catch (Exception e) {
            System.err.println("Failed to get task status from Worker: " + e.getMessage());
            return null;
        }
    }
}