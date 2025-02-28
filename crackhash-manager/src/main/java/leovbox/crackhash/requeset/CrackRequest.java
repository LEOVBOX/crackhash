package leovbox.crackhash.requeset;


public class CrackRequest {
    private String hash;
    private int maxLength;

    // Геттеры и сеттеры
    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public int getMaxLength() { return maxLength; }
    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
}
