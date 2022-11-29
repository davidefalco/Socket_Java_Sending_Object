import java.io.Serializable;

public class Email implements Serializable {
    String from;
    String to;
    String subject;
    String message;

    public Email(String from, String to, String subject, String message){
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Email{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
