package com.mateuszl.reporterapp.controller;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.controller.listeners.TopicDbEventListener;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.List;

/**
 * W implementacji zastosowany został typowy przykład menedżera warstwy perzystencji aplikacji.
 * Uzyto tu znanego wzorca projektowego pod tytułem Singleton, chcąc skorzystać z jego oczywistych zalet.
 */
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

    /**
     * Klasyczny przykład Singletonu, pozwalający na uzyskiwanie instancji menedżera.
     *
     * @return instancja Repository Manager, tworzona jeśli jest taka potrzeba
     */
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

    public Topic saveTopic(Topic topic, String userId) {
        topic.setId(getNewKey(topicsRoot));
        topicsRoot.child(topic.getId()).setValue(topic);
        addTopicToUser(topic, userId);
        return topic;
    }

    public void editTopic(Topic topic) {
        topicsRoot.child(topic.getId()).child("title").setValue(topic.getTitle());
        topicsRoot.child(topic.getId()).child("description").setValue(topic.getDescription());
    }

    public void deleteTopic(Topic topic) {
        getTopicsRoot().child(topic.getId()).removeValue();
        deleteTopicFromUser(topic, topic.getAuthor());
    }

    public void saveEvent(Event event, Topic topic) {
        DatabaseReference topicEventsRef = getEventsRoot(topic.getId());
        event.setId(getNewKey(topicEventsRef));
        topicEventsRef.child(event.getId()).setValue(event);
    }

    public void deleteEvent(Event event, Topic topic) {
        getTopicsRoot().child(topic.getId()).child("events").child(event.getId()).removeValue();
    }

    public void cleanDatabase() {
        root.setValue(null);
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

    private void addTopicToUser(Topic topic, String userId) {
        userTopicsRoot.child(userId).child(topic.getId()).setValue(true);
    }

    private void deleteTopicFromUser(Topic topic, String userId) {
        userTopicsRoot.child(userId).child(topic.getId()).removeValue();
    }

    private String getNewKey(DatabaseReference reference) {
        DatabaseReference newDBObjectRef = reference.push();
        return newDBObjectRef.getKey();
    }
}
