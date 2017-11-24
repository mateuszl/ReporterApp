package com.mateuszl.reporterapp.model;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa reprezentująca użytkownika systemu.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private List<String> topicsCreated; //topics IDs

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getTopicsCreated() {
        return topicsCreated;
    }

    public void setTopicsCreated(List<String> topicsCreated) {
        this.topicsCreated = topicsCreated;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("name", getName());
        result.put("email", getEmail());
        result.put("topicsCreated", getTopicsCreated());
        return result;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this.toMap());
    }
}
