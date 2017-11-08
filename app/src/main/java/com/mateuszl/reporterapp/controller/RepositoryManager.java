package com.mateuszl.reporterapp.controller;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class RepositoryManager {
    private static RepositoryManager instance = null;
    private DatabaseReference root;
    private DatabaseReference eventsRoot;
    private DatabaseReference topicsRoot;
    private DatabaseReference topicEventsRoot;
    private DatabaseReference userTopicsRoot;

    protected RepositoryManager() {
        root = FirebaseDatabase.getInstance().getReference();
        topicEventsRoot = root.child("topicEvents");
        userTopicsRoot = root.child("userTopics");
        eventsRoot = root.child("events");
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

    public DatabaseReference getEventsRoot() {
        return eventsRoot;
    }

    public DatabaseReference getTopicsRoot() {
        return topicsRoot;
    }

    public DatabaseReference getUserTopicsRoot() {
        return userTopicsRoot;
    }

    public DatabaseReference getTopicEventsRoot() {
        return topicEventsRoot;
    }

    public String saveTopic(Topic topic, User user) {
        topic.setId(getNewKey(topicsRoot));
        topicsRoot.child(topic.getId()).setValue(topic);
        addTopicToUser(topic, user);
        return topic.getId();
    }

    public void deleteTopic(Topic topic, User user) {
        //todo
    }

    public void subscribeTopic(Topic topic, User user) {
        //todo
    }

    public void unsubscribeTopic(Topic topic, User user) {
        //todo
    }

    public String saveEvent(Event event, Topic topic) {
        event.setId(getNewKey(eventsRoot));
        eventsRoot.child(event.getId()).setValue(event);
        addEventToTopic(event, topic);
        return event.getId();
    }

    public void deleteEvent(Event event, Topic topic) {
        eventsRoot.child(event.getId()).removeValue();
        deleteEventFromTopic(event, topic);
    }

//    public Topic getTopicById(final String topicId) {
//        Topic topic = new Topic();
//
//        //todo 07.11  - get topic from db
//
//        this.topicsRoot.addValueEventListener(new ValueEventListener() {
//            // the value event will fire once for the initial state of the data, and then again every time the value of that data changes.
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                HashMap<String, String> map = (HashMap<String, String>) snapshot.child(topicId).getValue();
//
//                Topic topic = new Topic();
//                topic.setTitle(map.get("title"));
//                topic.setId(map.get("id"));
//                topic.setAuthor(map.get("author"));
//                topic.setDescription(map.get("description"));
//                topic.setTimestamp(map.get("timestamp"));
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//        if (topic.getTitle() == null) {
//            topic = new Topic();
//            topic.setId("AAAAdddddAAAAaaaa");
//            topic.setAuthor("AADddDDAaAaaaaa");
//            topic.setTitle("AAAddDDAADddaaaaa");
//        }
//        return topic;
//    }

    public Topic getTopicById(String topicId) {
        Topic topic = new Topic();
        TopicEventListener topicEventListener = new TopicEventListener(topicId);

//        this.topicsRoot.addValueEventListener(topicEventListener);
        this.topicsRoot.addListenerForSingleValueEvent(topicEventListener);

        topic = topicEventListener.getTopic();
        return topic;
    }

    public List<Event> getEventsForATopic(String topicId) {
        List<Event> eventsList = new ArrayList<>();
        //todo
        return eventsList;
    }

    private String getNewKey(DatabaseReference reference) {
        DatabaseReference newDBObjectRef = reference.push();
        return newDBObjectRef.getKey();
    }

    private void addEventToTopic(Event event, Topic topic) {
        topicEventsRoot.child(topic.getId()).child(event.getId()).setValue(true);
    }

    private void deleteEventFromTopic(Event event, Topic topic) {
        topicEventsRoot.child(topic.getId()).child(event.getId()).removeValue();
    }

    private void addTopicToUser(Topic topic, User user) {
        userTopicsRoot.child(user.getId()).child(topic.getId()).setValue(true);
    }

    private void deleteTopicFromUser(Topic topic, User user) {
        userTopicsRoot.child(user.getId()).child(topic.getId()).removeValue();
    }
}
