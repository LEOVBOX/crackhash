package leovbox.crackhash.requests;

import java.util.stream.IntStream;

public class TaskRequest {
    ///  Алфавит, доступный для составления слов посредством перестановок.
    //private String alphabet;

    private String alphabet;

    public String getAlphabet() {
        return this.alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    ///  Порядковый номер части, которую нужно перебрать воркеру.
    private int partNumber;
    ///  Количество частей, на которое разделено все пространство слов (перестановок с повторениями).
    private int partCount;
    /// Максимальная длинна захэшированного слова.
    private int maxLength;

    /// Хэш, который нужно расшифровать.
    private String hash;

    /// Идентификатор задачи, сгенерированный на менеджере.
    private String taskId;

    public int getPartNumber() {
        return partNumber;
    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int mazLength) {
        this.maxLength = mazLength;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
