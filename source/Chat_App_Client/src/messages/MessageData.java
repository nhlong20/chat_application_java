package messages;

import java.io.Serializable;

/**
 * client.messages
 *
 * @created by ASUS - StudentID : 18120449
 * @Date 6/14/2021 - 12:42 PM
 * @Description
 */
public class MessageData implements Serializable {
    private String sender;
    private String receiver;
    private String type;
    private String data;

    public MessageData() {
    }

    public MessageData(String type) {
        this.type = type;
        this.data = null;
        this.sender = null;
        this.receiver = null;
    }

    public MessageData(String type, String data, String sender, String receiver) {
        this.type = type;
        this.data = data;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return sender + ":" + data;
    }
}
