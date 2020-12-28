package dk.brightworks.chat;

public class ChatLine {
    private long date;
    private String message;
    private String username;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "ChatLine{" +
                "date=" + date +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
