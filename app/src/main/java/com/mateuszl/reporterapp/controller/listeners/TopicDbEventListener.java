package com.mateuszl.reporterapp.controller.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;

import java.util.HashMap;
import java.util.List;

/**
 * Implementacja Event Listenera dla bazy Firebase. W momencie dodania listenera to danej referencji
 * uruchomiona zostaje jednokrotnie metoda onDataChange(), co pozwala na odczyt danych podlegających
 * tej referencji.
 */
public class TopicDbEventListener implements ValueEventListener {

    private String topicId;
    private Topic topic;
    private List<Topic> topicList;
    private TopicsAdapter topicsAdapter;

    /**
     * W konstruktorze użyte zostały parametry, które pozwalają na przekazanie obiektów potrzebnych
     * do asynchronicznej aktualizacji danych w widoku.
     *
     * @param topicId       Id topica którego chcemy pozyskać z bazy.
     * @param topicList     Lista topiców istniejąca w widoku, do której dopięty jest Adapter oraz
     *                      do której asynchronicznie dodany zostanie pozyskany obiekt Topic.
     * @param topicsAdapter Adapter służący do 'spinania' danych z listy obiektów z listą widoku.
     *                      Przekazywany aby móc go asynchronicznie odświeżać.
     */
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
