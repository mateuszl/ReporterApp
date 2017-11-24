package com.mateuszl.reporterapp.controller.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 24/11/2017.
 */

public class TopicDbEventListener implements ValueEventListener {

    private String topicId;
    private Topic topic;
    private List<Topic> topicList;
    private TopicsAdapter topicsAdapter;

    public TopicDbEventListener(String topicId, List<Topic> topicList, TopicsAdapter topicsAdapter) {
        this.topicId = topicId;
        this.topicList = topicList;
        this.topicsAdapter = topicsAdapter;
        this.topic = new Topic();
    }

    public Topic getTopic() {
        return topic;
    }

    @Override
    public void onDataChange(DataSnapshot snapshot) {
        topic = new Topic();
        HashMap<String, String> map = (HashMap<String, String>) snapshot.child(topicId).getValue();

        if (map != null && !map.isEmpty()) {

            topic.setTitle(map.get("title"));
            topic.setId(map.get("id"));
            topic.setAuthor(map.get("author"));
            topic.setDescription(map.get("description"));
            topic.setTimestamp(map.get("timestamp"));
        }

        topicList.add(topic);
        topicsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
