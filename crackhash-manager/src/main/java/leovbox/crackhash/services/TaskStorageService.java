package leovbox.crackhash.services;

import leovbox.crackhash.models.Status;
import leovbox.crackhash.models.TaskStatus;
import leovbox.crackhash.requests.RequestIdGenerator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskStorageService {
    private final Map<String, TaskStatus> tasks = new ConcurrentHashMap<>();

    public String createTask(int partsCount) {
        String taskId = RequestIdGenerator.generateRequestId().toString();
        tasks.put(taskId, new TaskStatus(partsCount));
        return taskId;
    }

    public void updateTask(String taskId, List<String> data) {
        TaskStatus taskStatus = tasks.get(taskId);
        if (taskStatus != null) {
            taskStatus.setData(data);
        }
    }

    public void updateStatus(String taskId, Status newStatus) {
        TaskStatus taskStatus = tasks.get(taskId);
        if (taskStatus != null) {
            taskStatus.setStatus(newStatus.toString());
        }
    }

    public TaskStatus getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    public boolean isTaskCompleted(String taskId) {
        TaskStatus taskStatus = tasks.get(taskId);
        return taskStatus != null && "COMPLETED".equals(taskStatus.getStatus());
    }

    public boolean addTaskData(String requestId, List<String> newData) {
        TaskStatus taskStatus = tasks.get(requestId);
        if (taskStatus == null) {
            return false;
        }

        // Добавляем новые данные
        taskStatus.addData(newData);

        // Проверяем, все ли части получены
        if (taskStatus.isComplete()) {
            taskStatus.setStatus(Status.READY.toString());
        }

        // Обновляем в хранилище
        tasks.put(requestId, taskStatus);
        return true;
    }
}