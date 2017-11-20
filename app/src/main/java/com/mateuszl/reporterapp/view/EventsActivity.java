package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.EventsAdapter;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia.
 */
public class EventsActivity extends AppCompatActivity {

    private final String TAG = "EventsActivity LOG ";

    @BindView(R.id.send_event_btn)
    public ImageButton sendBtn;

    @BindView(R.id.send_event_editText)
    public EditText sendEventEditText;

    @BindView(R.id.events_listView)
    public ListView eventsListView;

    private Topic topic;
    private RepositoryManager repositoryManager;
    private List<Event> topicEventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        sendEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

        this.topic = new Topic();

//        user_name = getIntent().getExtras().get("user_name").toString();
        this.topic.setId(getIntent().getExtras().get("topicId").toString());
        this.topic.setTitle(getIntent().getExtras().get("topicTitle").toString());
        this.topic.setTimestamp(getIntent().getExtras().get("topicTimestamp").toString());
        this.topic.setDescription(getIntent().getExtras().get("topicDescription").toString());
        this.topic.setAuthor(getIntent().getExtras().get("topicAuthor").toString());


        if (this.topic != null) {
            if (this.topic.getTitle() == null || this.topic.getTitle().isEmpty()) {
                showMessage("topic title empty or null!!");
            } else {
                setTitle("Topic: " + this.topic.getTitle());
            }
        } else {
            showMessage("No such topic in DB!!");
            //todo wyjście do listy topiców
        }

        repositoryManager.getEventsRoot(topic.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Object value = dataSnapshot.getValue();
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

    @OnClick(R.id.send_event_btn)
    public void saveNewEvent(View view) {
        if (sendEventEditText.getText().length() < 1) {
            //todo odswietlenie pola/mrugniecie czy coś
        } else {
            Long currentTime = System.currentTimeMillis() / 1000;
            Event event = new Event(sendEventEditText.getText().toString(), currentTime.toString(), topic.getId());
            sendEventEditText.setText("");
            repositoryManager.saveEvent(event, topic);
        }
    }

    @OnLongClick(R.id.send_event_btn)
    public void editEventMenu(View view){
        showMessage("Not implemented!");
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
        Intent intent = new Intent(getApplicationContext(), TopicsActivity.class);
        intent.putExtra("user_name", "User Name HC(" + UUID.randomUUID().toString().substring(0, 8) + ")"); //// TODO: 24.10.2017 hardcoded
        intent.putExtra("success", false);
        intent.putExtra("topicId", "");
        startActivity(intent);
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
//        eventsListTextView.setText("");
//        eventsListView.removeAllViews(); //todo check !
        addEventsToListView(dataSnapshot);
    }
}