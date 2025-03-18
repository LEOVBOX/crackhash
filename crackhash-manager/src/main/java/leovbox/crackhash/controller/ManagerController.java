package leovbox.crackhash.controller;

import io.micrometer.common.lang.Nullable;
import leovbox.crackhash.requests.CrackRequest;
import leovbox.crackhash.requests.RequestIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/api/hash")
public class ManagerController {

    enum Status {
        IN_PROGRESS, ERROR, READY
    }
    HashMap<String, Status> requests = new HashMap<>();

    public ManagerController() {
        requests.put("Gouda", Status.IN_PROGRESS);
    }
    public boolean isActive = false;
    public String name = "Worker1";

    private UUID createNewTask(CrackRequest crackRequest) {

        // TODO: Написать логику создания таски, проверки условий и тд
        UUID requestId = RequestIdGenerator.generateRequestId();
        requests.put(requestId.toString(), Status.IN_PROGRESS);
        return requestId;
    }

    @PostMapping("/crack")
    public ResponseEntity<?> crack(@RequestBody CrackRequest crackRequest) {
        if (crackRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Хэш не может быть пустым"));
        }

        UUID RequestId = createNewTask(crackRequest);

        return ResponseEntity.ok(RequestId.toString());
    }

    @GetMapping("/status") ResponseEntity<?> status(String requestId) {
        return ResponseEntity.ok(new StatusResponseDTO(requests.get(requestId).toString(), null));
    }
}

class StatusRequest {

}

class ErrorResponse {
    private String message;
    public ErrorResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}

class StatusResponseDTO {
    private String status;
    @Nullable
    private ArrayList<String> result;

    public StatusResponseDTO(String status, ArrayList<String> result) { this.status = status; }
    public String getStatus() { return status; }

    public ArrayList<String> getResult() { return result; }
}