package com.mateuszl.reporterapp.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Event;

import java.util.Iterator;

import static com.mateuszl.reporterapp.utils.Utils.getDate;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia.
 */
public class EventActivity extends AppCompatActivity {

    private ImageButton sendBtn;
    private EditText addEventEditText;
    private TextView eventsListTextView;
    private DatabaseReference root;
    private String topic_name, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        sendBtn = (ImageButton) findViewById(R.id.send_btn);
        addEventEditText = (EditText) findViewById(R.id.addEvent_editText);
        eventsListTextView = (TextView) findViewById(R.id.eventsList_textView);
        eventsListTextView.setMovementMethod(new ScrollingMovementMethod());

        addEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

//        user_name = getIntent().getExtras().get("user_name").toString();
        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle("Topic: " + topic_name);

        root = FirebaseDatabase.getInstance().getReference().child("events");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addEventEditText.getText().length() < 1) {
                    //todo odswietlenie pola/mrugniecie czy coś
                } else {
                    Long tsLong = System.currentTimeMillis()/1000;
                    currentTime = tsLong.toString();

                    Event event = new Event(addEventEditText.getText().toString(), currentTime, topic_name); //automatycznie Event uzyskuje ID

                    addEventEditText.setText("");

                    root.child(event.getId()).setValue(event);
                }
            }
        });

        root.addChildEventListener(new ChildEventListener() {
                                               @Override
                                               public void onChildAdded (DataSnapshot dataSnapshot, String previousChildName){
                                                   addEventsToListView(dataSnapshot);
                                               }

                                               @Override
                                               public void onChildChanged (DataSnapshot dataSnapshot, String previousChildName){

                                               }

                                               @Override
                                               public void onChildRemoved (DataSnapshot dataSnapshot){
                                                   clearAndAddEventsToListView(dataSnapshot);
                                               }

                                               @Override
                                               public void onChildMoved (DataSnapshot dataSnapshot, String previousChildName){

                                               }

                                               @Override
                                               public void onCancelled (DatabaseError databaseError){
                                                   Toast.makeText(getApplicationContext(), "Failed to load comments.",
                                                           Toast.LENGTH_SHORT).show();
                                               }
                                           });

    }

    private void addEventsToListView(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
            String event_id = (String) ((DataSnapshot) i.next()).getValue();
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID
            eventsListTextView.append(getDate(timestamp) + " ID: " + event_id + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
        eventsListTextView.setText("");
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
            String event_id = (String) ((DataSnapshot) i.next()).getValue();
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID
            eventsListTextView.append(getDate(timestamp) + " ID: " + event_id + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }
    }
}
