package waed.dev.adminhoria.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class WhatsAppTweet {
    private String uuid;
    private String message;

    @ServerTimestamp
    private transient Timestamp timestamp;

    public WhatsAppTweet() {

    }

    public WhatsAppTweet(String uuid, String message, Timestamp timestamp) {
        this.uuid = uuid;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
