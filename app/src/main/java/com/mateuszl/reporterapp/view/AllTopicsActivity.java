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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Widok z listą wszystkich aktywnych wydarzeń
 */

public class AllTopicsActivity extends AppCompatActivity {

    @BindView(R.id.topics_all_listView)
    public ListView topicsListView;

    private List<Topic> topicsList = new ArrayList<Topic>();
    private RepositoryManager repositoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_topics);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        setTitle("Trwające ydarzenia:");

        DatabaseReference topicsRoot = repositoryManager.getTopicsRoot();
        topicsRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addTopicsToListView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                clearAndAddTopicsToListView(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showMessage("Failed to load comments.");
            }
        });
    }

    @OnItemClick(R.id.topics_all_listView)
    public void openEventsActivity(AdapterView<?> parent, View view,
                                   int position, long id) {
        Topic topicSelected = (Topic) parent.getAdapter().getItem(position);
//todo passing an object with intent instead of strings
        Intent intent = new Intent(getApplicationContext(), TopicEventsActivity.class);
        intent.putExtra("topicId", topicSelected.getId());
        intent.putExtra("topicTitle", topicSelected.getTitle());
        intent.putExtra("topicTimestamp", topicSelected.getTimestamp());
        intent.putExtra("topicDescription", topicSelected.getDescription());
        intent.putExtra("topicAuthor", topicSelected.getAuthor());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.topics_all_listView)
    public boolean editEventMenu(View view, int position) {
        showMessage("Not implemented! pos: " + position);
        return true;
    }

    private void addTopicsToListView(DataSnapshot dataSnapshot) {
        Topic topic = dataSnapshot.getValue(Topic.class);
        topicsList.add(topic);

        TopicsAdapter topicsAdapter = new TopicsAdapter(this, topicsList);
        topicsListView.setAdapter(topicsAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void clearAndAddTopicsToListView(DataSnapshot dataSnapshot) {
        topicsList.clear();
        addTopicsToListView(dataSnapshot);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}