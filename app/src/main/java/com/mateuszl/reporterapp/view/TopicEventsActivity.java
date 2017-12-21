package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Widok z listą wszystkich zdarzeń w wybranym wydarzeniu.
 */
public class TopicEventsActivity extends AppCompatActivity {

    @BindView(R.id.events_all_listView)
    public ListView eventsListView;

    private RepositoryManager repositoryManager;
    private List<Event> eventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter = new EventsAdapter(this, eventsList);
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_events);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        this.topic = new Topic();

        this.topic.setId(getIntent().getExtras().get("topicId").toString());
        this.topic.setTitle(getIntent().getExtras().get("topicTitle").toString());


        if (this.topic != null) {
            if (this.topic.getTitle() == null || this.topic.getTitle().isEmpty()) {
                showMessage("Brak tytułu wydarzenia!");
            } else {
                setTitle(this.topic.getTitle());
            }
        } else {
            showMessage("Błąd bazy danych!!");
            Intent intent = new Intent(getApplicationContext(), AllTopicsActivity.class);
            startActivity(intent);
        }

        repositoryManager.getEventsRoot(topic.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addEventsToListView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                updateEventInListView(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeEventFromListView(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showMessage("Błąd bazy danych!");
            }
        });
        scrollEventsListViewToBottom();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addEventsToListView(DataSnapshot dataSnapshot) {
        eventsList.add(dataSnapshot.getValue(Event.class));

        eventsListView.setAdapter(eventsAdapter);
        eventsAdapter.notifyDataSetChanged();

        scrollEventsListViewToBottom();
    }

    /**
     * Ustawia wybór na ostatnią pozycję listy, co powoduje przesunięcie widoku na dół listy.
     */
    private void scrollEventsListViewToBottom() {
        eventsListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                eventsListView.setSelection(eventsList.size() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AllTopicsActivity.class);
        startActivity(intent);
    }

    private void removeEventFromListView(DataSnapshot dataSnapshot) {
        Iterator<Event> iterator = eventsList.iterator();
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getId().equalsIgnoreCase(dataSnapshot.getValue(Event.class).getId())) {
                eventsList.remove(event);
                eventsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private void updateEventInListView(DataSnapshot dataSnapshot) {
        Iterator<Event> iterator = eventsList.iterator();
        Event eventUpdated = dataSnapshot.getValue(Event.class);
        while (iterator.hasNext()) {
            Event event = iterator.next();
            if (event.getId().equalsIgnoreCase(eventUpdated.getId())) {
                int index = eventsList.indexOf(event);
                eventsList.remove(index);
                eventsList.add(index, eventUpdated);
                eventsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }
}