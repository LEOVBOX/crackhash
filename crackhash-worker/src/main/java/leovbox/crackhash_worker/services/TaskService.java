package leovbox.crackhash_worker.services;

import leovbox.crackhash_worker.models.TaskStatus;
import leovbox.crackhash_worker.requests.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {
    private final BrutForceService brutForceService;
    private final RestTemplate restTemplate;

    @Value("${manager.url}")
    private String managerUrl;

    @Autowired
    public TaskService(BrutForceService brutForceService, RestTemplate restTemplate) {
        this.brutForceService = brutForceService;
        this.restTemplate = restTemplate;
    }

    private final ConcurrentHashMap<String, TaskStatus> tasks = new ConcurrentHashMap<>();

    public String startTask(TaskRequest request) {
        // Создаем асинхронную задачу
        // Создаем новую задачу и добавляем её в ConcurrentHashMap
        TaskStatus taskStatus = new TaskStatus("IN_PROGRESS", new ArrayList<>());
        tasks.put(request.getTaskId(), taskStatus);

        // Запускаем асинхронное выполнение задачи с помощью BrutForceService
        CompletableFuture<Void> future = brutForceService.startBrutForceAsync(request, taskStatus);

        // Обработка завершения задачи
        future.whenComplete((result, error) -> {
            if (error != null) {
                // Если произошла ошибка, обновляем статус задачи
                taskStatus.setStatus("FAILED");
                System.err.println("Task failed: " + error.getMessage());
            } else {
                // Если задача завершена успешно, обновляем статус
                taskStatus.setStatus("COMPLETED");
                System.out.println("Task completed: " + taskStatus);
            }

            // Отправляем результат на Manager
            sendResultToManager(request.getTaskId(), taskStatus);
        });

        return request.getTaskId();
    }

    public TaskStatus getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    /**
     * Метод для отправки результата задачи на Manager.
     *
     * @param taskId Идентификатор задачи.
     * @param status Статус задачи (результат выполнения).
     */
    private void sendResultToManager(String taskId, TaskStatus status) {
        String url = managerUrl + "/internal/api/manager/hash/crack/request";

        // Создаем заголовки запроса
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем тело запроса (в данном случае это TaskStatus)
        HttpEntity<TaskStatus> requestEntity = new HttpEntity<>(status, headers);

        // Отправляем POST-запрос на Manager
        try {
            restTemplate.exchange(managerUrl, HttpMethod.POST, requestEntity, Void.class);
            System.out.println("Result sent to Manager for task: " + taskId);
        } catch (Exception e) {
            System.err.println("Failed to send result to Manager: " + e.getMessage());
        }
    }
}