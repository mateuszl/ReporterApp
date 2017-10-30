package com.mateuszl.reporterapp.view;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.Repository;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.Iterator;

import static com.mateuszl.reporterapp.utils.Utils.getDate;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia.
 */
public class EventsActivity extends AppCompatActivity {

    private final String TAG = "EventsActivity LOG ";
    private ImageButton sendBtn;
    private EditText addEventEditText;
    private TextView eventsListTextView;
    private String topicId, currentTime;
    private Topic topic;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        repository = Repository.getInstance();

        sendBtn = (ImageButton) findViewById(R.id.send_btn);
        addEventEditText = (EditText) findViewById(R.id.addEvent_editText);
        eventsListTextView = (TextView) findViewById(R.id.eventsList_textView);
        eventsListTextView.setMovementMethod(new ScrollingMovementMethod());

        addEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

//        user_name = getIntent().getExtras().get("user_name").toString();
        topicId = getIntent().getExtras().get("topicId").toString();

        this.topic = repository.getTopicById(topicId);
        if (topic != null) {
            setTitle("Topic: " + topic.getTitle());
        } else {
            Toast.makeText(getApplicationContext(), "No such topic in DB!!",
                    Toast.LENGTH_SHORT).show();
        }

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEventEditText.getText().length() < 1) {
                    //todo odswietlenie pola/mrugniecie czy coś
                } else {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    currentTime = tsLong.toString();

                    Event event = new Event(addEventEditText.getText().toString(), currentTime, topicId); //automatycznie Event uzyskuje ID

                    addEventEditText.setText("");

                    repository.saveEvent(event, topic);
                }
            }
        });

        repository.getEventsRoot().addChildEventListener(new ChildEventListener() {
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
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
//            String event_id = (String) ((DataSnapshot) i.next()).getValue(); //ID występuje w bazie jako klucz obiektu
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID
            Log.d(TAG, "clearAndAddEventsToListView: New event apped."); // with ID: " + event_id);
            eventsListTextView.append(getDate(timestamp) + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
        eventsListTextView.setText("");
        addEventsToListView(dataSnapshot);
    }
}
