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
import com.mateuszl.reporterapp.controller.EventsStringAdapter;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia.
 */
public class EventsActivity extends AppCompatActivity {

    private final String TAG = "EventsActivity LOG ";
    private ImageButton sendBtn;
    private EditText addEventEditText;
    private ListView eventsListView;
    private String currentTime;
    private Topic topic;
    private RepositoryManager repositoryManager;
    private List<String> topicEventsIdsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        repositoryManager = RepositoryManager.getInstance();

        sendBtn = (ImageButton) findViewById(R.id.send_btn);
        addEventEditText = (EditText) findViewById(R.id.addEvent_editText);
        eventsListView = (ListView) findViewById(R.id.events_listView);

        addEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

        this.topic = new Topic();

//        user_name = getIntent().getExtras().get("user_name").toString();
        this.topic.setId(getIntent().getExtras().get("topicId").toString());
        this.topic.setTitle(getIntent().getExtras().get("topicTitle").toString());
        this.topic.setTimestamp(getIntent().getExtras().get("topicTimestamp").toString());
        this.topic.setDescription(getIntent().getExtras().get("topicDescription").toString());
        this.topic.setAuthor(getIntent().getExtras().get("topicAuthor").toString());


        if (this.topic != null) {
            if (this.topic.getTitle() == null || this.topic.getTitle().isEmpty()) {
                Toast.makeText(getApplicationContext(), "topic title empty or null!!",
                        Toast.LENGTH_SHORT).show();
            } else {
                setTitle("Topic: " + this.topic.getTitle());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No such topic in DB!!",
                    Toast.LENGTH_SHORT).show();
            //todo wyjście do listy topiców
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEventEditText.getText().length() < 1) {
                    //todo odswietlenie pola/mrugniecie czy coś
                } else {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    currentTime = tsLong.toString();

                    Event event = new Event(addEventEditText.getText().toString(), currentTime, topic.getId());

                    addEventEditText.setText("");

                    repositoryManager.saveEvent(event, topic);
                }
            }
        });

        repositoryManager.getTopicEventsRoot().child(topic.getId()).addChildEventListener(new ChildEventListener() {
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
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addEventsToListView(DataSnapshot dataSnapshot) {
//        Iterator i = dataSnapshot.getChildren().iterator();
//        while (i.hasNext()) {
//            String content = (String) ((DataSnapshot) i.next()).getValue();
//            String event_id = (String) ((DataSnapshot) i.next()).getValue(); //ID występuje w bazie jako klucz obiektu
//            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
//            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID
//            Log.d(TAG, "clearAndAddEventsToListView: New event apped."); // with ID: " + event_id);
//            eventsListTextView.append(getDate(timestamp) + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
//        }


//        Event event = dataSnapshot.getValue(Event.class);
//        eventsList.add(event);
//        EventsAdapter eventsAdapter = new EventsAdapter(this, eventsList);
//        eventsListView.setAdapter(eventsAdapter);

        String eventIdKey = dataSnapshot.getKey();
        topicEventsIdsList.add(eventIdKey);

        EventsStringAdapter eventsStringAdapter = new EventsStringAdapter(this, topicEventsIdsList);
        eventsListView.setAdapter(eventsStringAdapter);

//        eventsStringAdapter.notifyDataSetChanged();
//        eventsListView.invalidateViews();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
//        eventsListTextView.setText("");
//        eventsListView.removeAllViews(); //todo check !
        addEventsToListView(dataSnapshot);
    }
}
