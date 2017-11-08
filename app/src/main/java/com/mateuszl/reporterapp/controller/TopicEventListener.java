package com.mateuszl.reporterapp.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.model.Topic;

import java.util.HashMap;

/**
 * Created by mateuszl on 08.11.2017.
 */

public class TopicEventListener implements ValueEventListener {

    private String topicId;
    private Topic topic;

    public Topic getTopic() {
        return topic;
    }

    public TopicEventListener(String topicId) {
        this.topicId = topicId;
        topic = new Topic();
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        HashMap<String, String> map = (HashMap<String, String>) snapshot.child(topicId).getValue();

        if (map != null || !map.isEmpty()) {

            topic.setTitle(map.get("title"));
            topic.setId(map.get("id"));
            topic.setAuthor(map.get("author"));
            topic.setDescription(map.get("description"));
            topic.setTimestamp(map.get("timestamp"));
        }

        if (topic.getTitle() == null) {
            topic = new Topic();
            topic.setId("AAAAdddddAAAAaaaa");
            topic.setAuthor("AADddDDAaAaaaaa");
            topic.setTitle("AAAddDDAADddaaaaa");
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
