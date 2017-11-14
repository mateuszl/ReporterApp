package com.mateuszl.reporterapp.controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic2;
import com.mateuszl.reporterapp.model.User;

public class RepositoryManager {
    private static RepositoryManager instance = null;
    private DatabaseReference root;
    private DatabaseReference topicsRoot;
    private DatabaseReference userTopicsRoot;

    protected RepositoryManager() {
        root = FirebaseDatabase.getInstance().getReference();
        userTopicsRoot = root.child("userTopics");
        topicsRoot = root.child("topics");
    }

    public static RepositoryManager getInstance() {
        if (instance == null) {
            instance = new RepositoryManager();
        }
        return instance;
    }

    public DatabaseReference getRoot() {
        return root;
    }

    public DatabaseReference getEventsRoot(String topicId) {
        return topicsRoot.child(topicId).child("events");
    }

    public DatabaseReference getTopicsRoot() {
        return topicsRoot;
    }

    public DatabaseReference getUserTopicsRoot() {
        return userTopicsRoot;
    }

    public String saveTopic(Topic2 topic, User user) {
        topic.setId(getNewKey(topicsRoot));
        topicsRoot.child(topic.getId()).setValue(topic);
        addTopicToUser(topic, user);
        return topic.getId();
    }

    public void deleteTopic(Topic2 topic, User user) {
        //todo
    }

    public void subscribeTopic(Topic2 topic, User user) {
        //todo
    }

    public void unsubscribeTopic(Topic2 topic, User user) {
        //todo
    }

    public Topic2 getTopicById(String topicId) {
        TopicDbEventListener topicDbEventListener = new TopicDbEventListener(topicId);
        this.topicsRoot.addListenerForSingleValueEvent(topicDbEventListener);
        return topicDbEventListener.getTopic();
    }

    public String saveEvent(Event event, Topic2 topic) {
        DatabaseReference topicEventsRef = getEventsRoot(topic.getId());
        event.setId(getNewKey(topicEventsRef));
        topicEventsRef.child(event.getId()).setValue(event);
        return event.getId();
    }

    public void deleteEvent(Event event, Topic2 topic) {
        getEventsRoot(topic.getId()).child(event.getId()).removeValue();
    }

    public void addTopicToUser(Topic2 topic, User user) {
        userTopicsRoot.child(user.getId()).child(topic.getId()).setValue(true);
    }

    public void deleteTopicFromUser(Topic2 topic, User user) {
        userTopicsRoot.child(user.getId()).child(topic.getId()).removeValue();
    }

    public Event getEventById(String eventId, String topicId) {
        EventDbEventListener eventListener = new EventDbEventListener();
        getEventsRoot(topicId).child(eventId).addListenerForSingleValueEvent(eventListener);
        return eventListener.getEvent();
    }

    private String getNewKey(DatabaseReference reference) {
        DatabaseReference newDBObjectRef = reference.push();
        return newDBObjectRef.getKey();
    }
}
