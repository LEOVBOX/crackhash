package leovbox.crackhash.models;

import java.util.ArrayList;
import java.util.List;

public class TaskStatus {
    private String status;
    private List<String> data;
    private int expectedParts = 0;
    private int receivedParts = 0;

    public void setExpectedParts(int expectedParts) {
        this.expectedParts = expectedParts;
    }

    public int getExpectedParts() {
        return expectedParts;
    }

    public TaskStatus(int expectedParts) {
        this.status = Status.IN_PROGRESS.toString();
        this.data = new ArrayList<>();
        this.expectedParts = expectedParts;
        this.receivedParts = 0;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void addData(List<String> newData) {
        if (newData != null) {
            this.data.addAll(newData);
        }
        receivedParts++;
        System.out.println("Task " + receivedParts + " / " + expectedParts);
        if (receivedParts >= expectedParts) {
            this.status = Status.READY.toString();
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getData() {
        return this.data;
    }

    public boolean isComplete() {
        return receivedParts >= expectedParts;
    }
}
