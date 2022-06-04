package com.what_to_read;

public class Message_Details {
    String message_id;
    String sender_email;
    String sender_name;
    String message;

    public Message_Details(String message_id, String sender_email, String sender_name, String message) {
        this.message_id = message_id;
        this.sender_email = sender_email;
        this.sender_name = sender_name;
        this.message = message;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "message_id='" + message_id + '\'' +
                ", sender_email='" + sender_email + '\'' +
                ", sender_name='" + sender_name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
