package br.com.atech.controler;

import java.util.HashMap;
import java.util.Map;

public class ProcessorMessageResource {

    private String timestamp;

    private String sender;

    private String priority;

    private String body;

    private final Map<String, String> properties = new HashMap<>();

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getBody() {
        return body;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

}
