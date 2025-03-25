package leovbox.crackhash.controller;

import leovbox.crackhash.models.TaskStatus;
import leovbox.crackhash.requests.CrackRequest;
import leovbox.crackhash.requests.TaskResult;
import leovbox.crackhash.services.TaskCompletionService;
import leovbox.crackhash.services.TaskStorageService;
import leovbox.crackhash.services.WorkerClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hash")
public class ManagerController {
    private final WorkerClientService workerClientService;
    private final TaskStorageService taskStorageService;
    private final TaskCompletionService taskCompletionService;

    @Autowired
    public ManagerController(WorkerClientService workerClientService,
                             TaskStorageService taskStorageService,
                             TaskCompletionService taskCompletionService) {
        this.workerClientService = workerClientService;
        this.taskStorageService = taskStorageService;
        this.taskCompletionService = taskCompletionService;
    }

    /**
     * Обрабатывает запрос на взлом хэша.
     *
     * @param crackRequest Запрос на взлом хэша.
     * @return Идентификатор задачи.
     */
    @PostMapping("/crack")
    public ResponseEntity<?> crack(@RequestBody CrackRequest crackRequest) {
        if (crackRequest == null || crackRequest.getHash() == null || crackRequest.getHash().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Хэш не может быть пустым"));
        }

        // Создаем новую задачу
        String taskId = taskStorageService.createTask(taskCompletionService.getWorkersCount());

        // Запускаем выполнение задачи асинхронно
        taskCompletionService.executeTaskAsync(taskId, crackRequest)
                .thenAccept(result -> {
                    // Обработка завершения задачи
                    System.out.println("Task " + taskId + " completed");
                })
                .exceptionally(ex -> {
                    // Обработка ошибок
                    System.out.println("Task " + taskId + " failed: " + ex.getMessage());
                    return null;
                });

        // Возвращаем идентификатор задачи
        return ResponseEntity.ok(taskId);
    }

    /**
     * Возвращает статус задачи.
     *
     * @param requestId Идентификатор задачи.
     * @return Статус задачи и результат (если готов).
     */
    @GetMapping("/status")
    public ResponseEntity<?> status(@RequestParam String requestId) {
        TaskStatus statusResponse = taskStorageService.getTaskStatus(requestId);
        if (statusResponse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Задача с ID " + requestId + " не найдена"));
        }
        return ResponseEntity.ok(statusResponse);
    }

}

class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}