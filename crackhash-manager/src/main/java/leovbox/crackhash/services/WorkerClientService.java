package leovbox.crackhash.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import leovbox.crackhash.requests.CrackRequest;
import leovbox.crackhash.requests.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WorkerClientService {

    @Value("${alphabet}")
    private String alphabet;

    private final RestTemplate restTemplate;

    @Value("${worker.urls}")
    private List<String> workerUrls;

    @PostConstruct
    public void printWorkerUrls() {
        System.out.println("WorkerClientService: WORKER_URLS from application.properties: " + workerUrls);
    }

    @Autowired
    public WorkerClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Отправляет задачи всем Worker'ам.
     *
     * @param taskRequest Запрос на выполнение задачи.
     */
    public void sendTasksToWorkers(CrackRequest taskRequest, String taskId) {
        // Распределяем задачи поровну по всем воркерам
        for (int i = 0; i < workerUrls.size(); i++) {
            TaskRequest partRequest = new TaskRequest();
            partRequest.setAlphabet(alphabet);
            partRequest.setHash(taskRequest.getHash());
            partRequest.setMaxLength(taskRequest.getMaxLength());
            partRequest.setPartNumber(i); // Номер части
            partRequest.setPartCount(workerUrls.size()); // Общее количество частей
            partRequest.setTaskId(taskId);

            sendTaskToWorker(workerUrls.get(i), partRequest);
        }
    }

    /**
     * Отправляет задачу конкретному Worker'у.
     *
     * @param workerUrl   URL Worker'а.
     * @param taskRequest Запрос на выполнение задачи.
     */
    private void sendTaskToWorker(String workerUrl, TaskRequest taskRequest) {
        String url = workerUrl + "/internal/api/worker/hash/crack/task";

        try {
            String requestBody = new ObjectMapper().writeValueAsString(taskRequest);
            System.out.println("Sending request to Worker: " + requestBody);
        } catch (Exception e) {
            System.err.println("Failed to serialize request body: " + e.getMessage());
        }


        // Создаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем тело запроса
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(taskRequest, headers);

        // Отправляем POST-запрос на Worker
        try {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            System.out.println("Task sent to Worker: " + taskRequest.getPartNumber());
        } catch (Exception e) {
            System.err.println("Failed to send task to Worker: " + e.getMessage());
        }
    }
}