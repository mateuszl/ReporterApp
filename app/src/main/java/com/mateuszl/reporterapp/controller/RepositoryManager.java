package com.mateuszl.reporterapp.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.model.User;

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
        eventsRoot.child(event.getId()).setValue(event.toMap());
        addEventToTopic(event, topic);
        return event.getId();
    }

    public void deleteEvent(Event event, Topic topic) {
        eventsRoot.child(event.getId()).removeValue();
        deleteEventFromTopic(event, topic);
    }

    public Topic getTopicById(final String topicId) {
        Topic topic = new Topic();

        //todo 07.11  - get topic from db

//        this.eventsRoot.child(topicId).addValueEventListener(new ValueEventListener() {
//            // the value event will fire once for the initial state of the data, and then again every time the value of that data changes.
//            Topic topic = new Topic();
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                snapshot.getChildrenCount();
//                snapshot.toString();
//                System.out.println("sssssssssss");
//
//                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
////                    topic.setTitle((String) messageSnapshot.child("title").getValue());
////                    topic.setId((String) messageSnapshot.child("id").getValue());
////                    topic.setAuthor((String) messageSnapshot.child("author").getValue());
////                    topic.setDescription((String) messageSnapshot.child("description").getValue());
////                    topic.setTimestamp((String) messageSnapshot.child("timestamp").getValue());
//                    topic = messageSnapshot.getValue(Topic.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
        this.eventsRoot.addValueEventListener(new ValueEventListener() {
            // the value event will fire once for the initial state of the data, and then again every time the value of that data changes.
            Topic topic = new Topic();

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                topic = (Topic) snapshot.child(topicId).getValue();

                snapshot.getChildrenCount();
                snapshot.toString();
                System.out.println(topic.getTitle());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (topic.getTitle() == null) {
            topic = new Topic();
            topic.setId("AAAAdddddAAAA");
            topic.setAuthor("AADddDDAaA");
            topic.setTitle("AAAddDDAADdd");
        }
        return topic;
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
