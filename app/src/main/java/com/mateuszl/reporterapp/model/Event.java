package com.mateuszl.reporterapp.model;

import com.google.gson.Gson;

import java.util.UUID;

/**
 * Klasa reprezentująca pojedyńcze zdarzenie podczas relacjonowanego wydarzenia.
 */
public class Event {
    private String id;
    private String content;
    private String timestamp;

    public Event() {
        // domyślny konstruktor potrzebny do odwołań DataSnapshot.getValue(Event.class)
    }

    public Event(String content, String timestamp) {
        this.setId(UUID.randomUUID().toString());
        this.content = content;
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
