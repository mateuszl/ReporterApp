package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Widok z listą wszystkich wydarzeń istniejących w bazie danych.
 */
public class AllTopicsActivity extends AppCompatActivity {

    @BindView(R.id.topics_all_listView)
    public ListView topicsListView;

    private List<Topic> topicsList = new ArrayList<Topic>();
    private TopicsAdapter topicsAdapter = new TopicsAdapter(this, topicsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
        RepositoryManager repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        setTitle("Trwające wydarzenia:");

        DatabaseReference topicsRoot = repositoryManager.getTopicsRoot();
        topicsRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addTopicsToListView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                updateTopicInListView(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeTopicFromListView(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showMessage("Błąd bazy danych!");
            }
        });
    }

    @OnItemClick(R.id.topics_all_listView)
    public void openEventsActivity(AdapterView<?> parent, View view,
                                   int position, long id) {
        Topic topicSelected = (Topic) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getApplicationContext(), TopicEventsActivity.class);
        intent.putExtra("topicId", topicSelected.getId());
        intent.putExtra("topicTitle", topicSelected.getTitle());
        startActivity(intent);
    }

    private void addTopicsToListView(DataSnapshot dataSnapshot) {
        Topic topic = dataSnapshot.getValue(Topic.class);
        topicsList.add(topic);
        topicsListView.setAdapter(topicsAdapter);
        reportFullyDrawn();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void removeTopicFromListView(DataSnapshot dataSnapshot) {
        Iterator<Topic> iterator = topicsList.iterator();
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            if (topic.getId().equalsIgnoreCase(dataSnapshot.getValue(Topic.class).getId())) {
                topicsList.remove(topic);
                topicsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void updateTopicInListView(DataSnapshot dataSnapshot) {
        Iterator<Topic> iterator = topicsList.iterator();
        Topic topicUpdated = dataSnapshot.getValue(Topic.class);
        while (iterator.hasNext()) {
            Topic topic = iterator.next();
            if (topic.getId().equalsIgnoreCase(topicUpdated.getId())) {
                int index = topicsList.indexOf(topic);
                topicsList.remove(index);
                topicsList.add(index, topicUpdated);
                topicsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}