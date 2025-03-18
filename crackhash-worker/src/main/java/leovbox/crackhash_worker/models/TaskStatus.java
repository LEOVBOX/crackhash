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
        this.status = Status.IN_PROGRESS.toString();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public String getStatus() {
        return this.status;
    }

    public List<String> getResult() {
        return this.result;
    }
}

