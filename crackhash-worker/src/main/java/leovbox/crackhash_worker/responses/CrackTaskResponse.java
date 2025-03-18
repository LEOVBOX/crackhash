package leovbox.crackhash_worker.responses;

import java.util.List;

public class CrackTaskResponse {
    private String requestId; // Идентификатор задачи
    private List<String> data; // Результат (найденные слова)
    private String status; // Статус задачи (например, "COMPLETED")

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}