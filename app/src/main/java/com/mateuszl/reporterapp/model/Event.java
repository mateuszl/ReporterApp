package com.mateuszl.reporterapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa reprezentująca pojedyńcze zdarzenie podczas relacjonowanego wydarzenia.
 */
public class Event {
    private String id;
    private String content;
    private Integer timestamp;
    private String topic; //topic ID

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("content", getContent());
        result.put("timestamp", getTimestamp());
        result.put("topic", getTopic());
        return result;
    }
}
