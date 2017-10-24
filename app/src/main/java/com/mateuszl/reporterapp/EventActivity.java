package com.mateuszl.reporterapp;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia.
 */
public class EventActivity extends AppCompatActivity {

    private ImageButton send_btn;
    private EditText addEventEditText;
    private TextView eventsListTextView;
    private DatabaseReference root;
    private String temp_key, topic_name, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        send_btn = (ImageButton) findViewById(R.id.send_btn);
        addEventEditText = (EditText) findViewById(R.id.addEvent_editText);
        eventsListTextView = (TextView) findViewById(R.id.eventsList_textView);
        eventsListTextView.setMovementMethod(new ScrollingMovementMethod());

        addEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

//        user_name = getIntent().getExtras().get("user_name").toString();
        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle("Topic: " + topic_name);

        root = FirebaseDatabase.getInstance().getReference().child("events");

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map<String, Object> map = new HashMap<>();
//                temp_key = root.push().getKey();
//
//                root.updateChildren(map);
//                DatabaseReference events_root = root.child(temp_key);

                if (addEventEditText.getText().length() < 1) {
                    //todo odswietlenie pola/mrugniecie czy coś
                } else {
                    Long tsLong = System.currentTimeMillis()/1000;
                    currentTime = tsLong.toString();

                    Event event = new Event(addEventEditText.getText().toString(), currentTime, topic_name);

                    addEventEditText.setText("");

                    root.child(event.getId()).setValue(event);
                }
            }
        });

        root.addChildEventListener(new

                                           ChildEventListener() {
                                               @Override
                                               public void onChildAdded (DataSnapshot dataSnapshot, String s){

                                                   appendToTopicEvents(dataSnapshot);
                                               }

                                               @Override
                                               public void onChildChanged (DataSnapshot dataSnapshot, String s){

                                               }

                                               @Override
                                               public void onChildRemoved (DataSnapshot dataSnapshot){

                                               }

                                               @Override
                                               public void onChildMoved (DataSnapshot dataSnapshot, String s){

                                               }

                                               @Override
                                               public void onCancelled (DatabaseError databaseError){

                                               }
                                           });

    }

    private void appendToTopicEvents(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
            String event_id = (String) ((DataSnapshot) i.next()).getValue();
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID

            eventsListTextView.append(getDate(timestamp) + " ID: " + event_id + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }

    }

    private String getDate(String timestamp){
            try{
                Calendar calendar = Calendar.getInstance();
                TimeZone tz = TimeZone.getDefault();
                calendar.setTimeInMillis(Long.decode(timestamp) * 1000);
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date currenTimeZone = (Date) calendar.getTime();
                return sdf.format(currenTimeZone);
            }catch (Exception e) {
                return "XX ERROR XX";
            }
    }
}
