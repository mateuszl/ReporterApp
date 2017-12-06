package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.adapters.EventsAdapter;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemLongClick;

/**
 * Created by Asus on 24/11/2017.
 */

public class TopicEventsActivity extends AppCompatActivity {

    private final String TAG = "UserEventsActivity LOG ";

    @BindView(R.id.events_all_listView)
    public ListView eventsListView;

    private Topic topic;
    private RepositoryManager repositoryManager;
    private List<Event> topicEventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_events);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        this.topic = new Topic();

        this.topic.setId(getIntent().getExtras().get("topicId").toString());
        this.topic.setTitle(getIntent().getExtras().get("topicTitle").toString());
        this.topic.setTimestamp(getIntent().getExtras().get("topicTimestamp").toString());
        this.topic.setDescription(getIntent().getExtras().get("topicDescription").toString());
        this.topic.setAuthor(getIntent().getExtras().get("topicAuthor").toString());


        if (this.topic != null) {
            if (this.topic.getTitle() == null || this.topic.getTitle().isEmpty()) {
                showMessage("topic title empty or null!!");
            } else {
                setTitle(this.topic.getTitle());
            }
        } else {
            showMessage("No such topic in DB!!");
            //todo wyjście do listy topiców
        }

        repositoryManager.getEventsRoot(topic.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addEventsToListView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                clearAndAddEventsToListView(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load data.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        scrollEventsListViewToBottom();
    }

    @OnItemLongClick(R.id.events_all_listView)
    public boolean editEventMenu(View view, int position) {
        showMessage("Not implemented! pos: " + position);
        return true;
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addEventsToListView(DataSnapshot dataSnapshot) {
        topicEventsList.add(dataSnapshot.getValue(Event.class));

        EventsAdapter eventsAdapter = new EventsAdapter(this, topicEventsList);
        eventsListView.setAdapter(eventsAdapter);

        scrollEventsListViewToBottom();
    }

    private void scrollEventsListViewToBottom() {
        eventsListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                eventsListView.setSelection(topicEventsList.size() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AllTopicsActivity.class);
        startActivity(intent);
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
//        eventsListTextView.setText("");
//        eventsListView.removeAllViews(); //todo check !
        addEventsToListView(dataSnapshot);
    }
}
