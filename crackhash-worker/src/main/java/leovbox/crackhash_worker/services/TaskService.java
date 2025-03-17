package leovbox.crackhash_worker.services;

import leovbox.crackhash_worker.models.TaskStatus;
import leovbox.crackhash_worker.requests.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

    private final BrutForceService brutForceService;

    @Autowired
    public TaskService(BrutForceService brutForceService) {
        this.brutForceService = brutForceService;
    }

    private final ConcurrentHashMap<String, CompletableFuture<TaskStatus>> tasks = new ConcurrentHashMap<>();

    public String startTask(TaskRequest request) {
        String taskId = generateTaskId();

        // Создаем асинхронную задачу
        CompletableFuture<TaskStatus> future = CompletableFuture.supplyAsync(() -> {
            // Логика выполнения задачи
            List<String> result = BrutForceService.BrutForce(request);
            return new TaskStatus("COMPLETED", result);
        });

        // Обработка завершения задачи
        future.whenComplete((status, error) -> {
            if (error != null) {
                System.err.println("Task failed: " + error.getMessage());
                tasks.remove(taskId); // Удаляем задачу при ошибке
            } else {
                System.out.println("Task completed: " + status);
                tasks.remove(taskId); // Удаляем задачу после успешного завершения
            }
        });

        // Сохраняем задачу в мапе
        tasks.put(taskId, future);
        return taskId;
    }

    public TaskStatus getTaskStatus(String taskId) {
        return brutForceService.getTaskStatus(taskId);
    }

    private String generateTaskId() {
        // Генерация уникального ID задачи (например, UUID)
        return java.util.UUID.randomUUID().toString();
    }
}