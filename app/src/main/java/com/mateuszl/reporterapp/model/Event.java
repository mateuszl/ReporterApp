package com.mateuszl.reporterapp.model;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Klasa reprezentująca pojedyńcze zdarzenie podczas relacjonowanego wydarzenia.
 */
public class Event {
    private String id;
    private String content;
    private String timestamp;
    private String topic; //topic ID

    public Event() {
//        this.setId(UUID.randomUUID().toString());
        // Default constructor required for calls to DataSnapshot.getValue(Event.class)
    }

    public Event(String content, String timestamp, String topic) {
        this.setId(UUID.randomUUID().toString());
        this.content = content;
        this.timestamp = timestamp;
        this.topic = topic;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Exclude
    public Map<String, String> toMap() {
        HashMap<String, String> eventMap = new HashMap<>();
        eventMap.put("id", getId()); //ID umieszczane jest w bazie jako klucz obiektu. Żeby nie duplikować danych można rozwazyc nie umieszczanie ID w modelu
        eventMap.put("content", getContent());
        eventMap.put("timestamp", getTimestamp());
        eventMap.put("topic", getTopic());
        return eventMap;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this.toMap());
    }
}
