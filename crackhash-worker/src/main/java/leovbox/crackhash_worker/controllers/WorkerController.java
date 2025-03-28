package leovbox.crackhash_worker.controllers;

import leovbox.crackhash_worker.models.TaskStatus;
import leovbox.crackhash_worker.requests.TaskRequest;
import leovbox.crackhash_worker.services.BrutForceService;
import leovbox.crackhash_worker.services.TaskService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/api/worker")
public class WorkerController {
    private final TaskService taskService;
    @Autowired
    public WorkerController(TaskService taskService) {
        this.taskService = taskService;
    }

    ///  Метод для обработки POST запроса на перебор указанной части (partNumber) перестановок
    @PostMapping("/hash/crack/task")
    public ResponseEntity<String> postTask(@RequestBody TaskRequest request) {
        taskService.startTask(request);

        return ResponseEntity.ok().body("Task started" + request.getTaskId());
    }

    /// Метод для обработки GET запроса на получение статуса отправленной задачи
    @GetMapping("/status")
    public ResponseEntity<TaskStatus> getStatus(@RequestParam String taskId) {
        TaskStatus status = taskService.getTaskStatus(taskId);
        return ResponseEntity.ok(status);
    }
}
