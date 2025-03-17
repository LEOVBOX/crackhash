package leovbox.crackhash_worker.models;

import java.util.List;

public class TaskStatus {
    private String status;
    private List<String> result;

    public TaskStatus(String status, List<String> data) {
        this.status = status;
        this.result = data;
    }

    public TaskStatus() {
        this.status = "IN_PROGRESS";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
