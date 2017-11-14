package com.mateuszl.reporterapp.model;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Asus on 13.11.2017.
 */

public class Topic2 {
    private String id;
    private String title;
    private String description;
    private String timestamp;
    private String author; //authors User ID
//    private List<String> subscribers; //todo subscribers User IDs
    private Map<String, Event> events; //events IDs

    public Topic2() {
//        this.setId(UUID.randomUUID().toString());
//        setSubscribers(new ArrayList<String>());
        setEvents(new HashMap<String, Event>());
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

//    public List<String> getSubscribers() {
//        return subscribers;
//    }
//
//    public void setSubscribers(List<String> subscribers) {
//        this.subscribers = subscribers;
//    }

    /**
     * List with Events Ids
     *
     * @return
     */
    public Map<String, Event> getEvents() {
        if (events==null){
            setEvents(new HashMap<String, Event>());
        }
        return events;
    }

    public void setEvents(Map<String, Event> events) {
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
//        result.put("subscribers", getSubscribers());
        result.put("events", getEvents());
        return result;
    }



    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this.toMap());
    }
}