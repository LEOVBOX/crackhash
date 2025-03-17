package leovbox.crackhash_worker.requests;

import java.util.stream.IntStream;

public class TaskRequest {
    ///  Алфавит, доступный для составления слов посредством перестановок.
    //private String alphabet;

    private Character[] alphabet;

    public void setAlphabet(Character[] alphabet) {
        this.alphabet = alphabet;
    }

    public Character[] getAlphabet() {
        return this.alphabet;
    }

    public void setAlphabet(String alphabetString) {
        char[] alphabetArray = alphabetString.toCharArray();
        this.alphabet = IntStream.range(0, alphabetArray.length)
                .mapToObj(i -> alphabetArray[i])
                .toArray(Character[]::new);
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

//    public String getAlphabet() {
//        return alphabet;
//    }

//    public void setAlphabet(String alphabet) {
//        this.alphabet = alphabet;
//    }

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
