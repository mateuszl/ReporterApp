package com.mateuszl.reporterapp.controller;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.controller.listeners.EventDbEventListener;
import com.mateuszl.reporterapp.controller.listeners.TopicDbEventListener;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.model.User;

import java.util.List;

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

    public String saveTopic(Topic topic, FirebaseUser user) {
        topic.setId(getNewKey(topicsRoot));
        topicsRoot.child(topic.getId()).setValue(topic);
        addTopicToUser(topic, user);
        return topic.getId();
    }

    public void deleteTopic(Topic topic) {
        //todo
    }

    /**
     * W metodzie użyte zostały parametry, które pozwalają na przekazanie obiektów potrzebnych
     * do asynchronicznej aktualizacji danych w widoku.
     *
     * @param topicId
     * @param topicList
     * @param topicsAdapter
     */
    public void retrieveTopicsByIdForListView(String topicId, List<Topic> topicList, TopicsAdapter topicsAdapter) {
        TopicDbEventListener topicDbEventListener = new TopicDbEventListener(topicId, topicList, topicsAdapter);
        this.topicsRoot.addListenerForSingleValueEvent(topicDbEventListener);
    }

    public String saveEvent(Event event, Topic topic) {
        DatabaseReference topicEventsRef = getEventsRoot(topic.getId());
        event.setId(getNewKey(topicEventsRef));
        topicEventsRef.child(event.getId()).setValue(event);
        return event.getId();
    }

    public void deleteEvent(Event event, Topic topic) {
        getEventsRoot(topic.getId()).child(event.getId()).removeValue();
    }

    public void addTopicToUser(Topic topic, FirebaseUser user) {
        userTopicsRoot.child(user.getUid()).child(topic.getId()).setValue(true);
    }

    public void deleteTopicFromUser(Topic topic, User user) {
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
