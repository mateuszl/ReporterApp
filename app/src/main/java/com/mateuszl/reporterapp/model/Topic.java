package com.mateuszl.reporterapp.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Model danych dla Wydarzenia, które jest
 */
public class Topic implements Serializable {
    private String id;
    private String title;
    private String description;
    private String timestamp;
    private String author; //authors ID
    private Map<String, Event> events; //events IDs

    public Topic() {
        // domyślny konstruktor potrzebny do odwołań DataSnapshot.getValue(Topic.class)
    }

    public Topic(String title, String description, String timestamp, String author, Map<String, Event> events) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.author = author;
        this.events = events;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Id użytkownika który utworzył Wydarzenie
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * List with Events Ids
     *
     * @return
     */
    public Map<String, Event> getEvents() {
        if (events == null) {
            setEvents(new HashMap<String, Event>());
        }
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}