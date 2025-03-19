package leovbox.crackhash.controller;

import leovbox.crackhash.models.TaskStatus;
import leovbox.crackhash.requests.TaskResult;
import leovbox.crackhash.services.TaskStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/manager/hash/crack")
public class InternalController {
    private final TaskStorageService taskStorageService;

    public InternalController(TaskStorageService taskStorageService) {
        this.taskStorageService = taskStorageService;
    }
    /**
     * Обрабатывает PATCH-запрос с результатом от Worker'а.
     *
     * @param taskResult Результат выполнения задачи.
     * @return Ответ 200 OK, если запрос успешно обработан.
     */
    @PostMapping("/request")
    public ResponseEntity<?> updateTask(@RequestBody TaskResult taskResult) {
        String requestId = taskResult.getRequestId();
        List<String> data = taskResult.getData();

        boolean result = taskStorageService.addTaskData(requestId, data);
        if (!result) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Задача с ID " + requestId + " не найдена"));
        }

        return ResponseEntity.ok().build();
    }
}
