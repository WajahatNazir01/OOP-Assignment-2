import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sms {
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    private static int idCounter = 1;
    private String messageId;
    LocalDateTime ldt = LocalDateTime.now();
    DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd/MM/yyyy E HH:mm:ss");
    String date=dtf.format(ldt);
    public Sms(String content) {
        this.content = content;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy E HH:mm:ss"));
        this.isRead = false;
        this.messageId =String.format("%03d", idCounter++);
    }


    public void markAsRead() {
        this.isRead = true;
    }

    public String getMessageId() {
        return messageId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return isRead;
    }

    @Override
    public String toString() {

        return "[" + date + "] " + content;
    }
}


