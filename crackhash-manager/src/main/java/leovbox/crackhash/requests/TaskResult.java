package leovbox.crackhash.requests;

import leovbox.crackhash.models.Status;

import java.util.ArrayList;
import java.util.List;

public class TaskResult {
    private String requestId; // Идентификатор задачи
    private List<String> data; // Результат выполнения задачи
//    private Status status;

    // Геттеры и сеттеры
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
}