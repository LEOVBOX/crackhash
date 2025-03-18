package leovbox.crackhash.services;

import leovbox.crackhash.requests.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkerClientService {

    private final RestTemplate restTemplate;

    @Value("${worker.url}") // URL Worker'а из конфигурации
    private String workerUrl;

    @Autowired
    public WorkerClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Отправляет задачу на Worker.
     *
     * @param taskRequest Запрос на выполнение задачи.
     */
    public void sendTaskToWorker(TaskRequest taskRequest) {
        String url = workerUrl + "/internal/api/worker/hash/crack/task";

        // Создаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем тело запроса
        HttpEntity<TaskRequest> requestEntity = new HttpEntity<>(taskRequest, headers);

        // Отправляем POST-запрос на Worker
        try {
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            System.out.println("Task sent to Worker: " + taskRequest.getTaskId());
        } catch (Exception e) {
            System.err.println("Failed to send task to Worker: " + e.getMessage());
        }
    }
}