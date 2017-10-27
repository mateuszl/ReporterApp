package com.mateuszl.reporterapp.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Klasa reprezentująca wydarzenie sportowe, którego dotyczy relacja.
 */
public class Topic {
    private String id;
    private String title;
    private String description;
    private String timestamp;
    private String author; //authors User ID
    private List<String> subscribers; //subscribers User IDs
    private List<String> events; //events IDs

    public Topic() {
        this.setId(UUID.randomUUID().toString());
        setSubscribers(new ArrayList<String>());
        setEvents(new ArrayList<String>());
        // Default constructor required for calls to DataSnapshot.getValue(Topic.class)
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
     * Authors User Id;
     *
     * @return
     */
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<String> subscribers) {
        this.subscribers = subscribers;
    }

    /**
     * List with Events Ids
     *
     * @return
     */
    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("id", getId()); //// TODO: 25.10.2017 make sure not needed here!
        result.put("title", getTitle());
        result.put("description", getDescription());
        result.put("author", getAuthor());
        result.put("timestamp", getTimestamp());
        result.put("subscribers", getSubscribers());
        result.put("events", getEvents());
        return result;
    }
}
