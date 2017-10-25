package com.mateuszl.reporterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.R;

import java.util.Iterator;

import static com.mateuszl.reporterapp.utils.Utils.getDate;

/**
 * Lista eventów
 */
public class TopicsActivity extends AppCompatActivity {

    private ImageButton addTopicBtn;
    private TextView topicsListTextView;
    private DatabaseReference root;
    private String topic_name, user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        addTopicBtn = (ImageButton) findViewById(R.id.add_topic_btn);
        topicsListTextView = (TextView) findViewById(R.id.eventsList_textView);
        topicsListTextView.setMovementMethod(new ScrollingMovementMethod());


        user_name = "mockedName"; //todo getIntent().getExtras().get("user_name").toString();
//        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle(user_name + " topics");

        root = FirebaseDatabase.getInstance().getReference().child("events");

        addTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditTopicActivity.class);
                intent.putExtra("user_name", "mocked userName");
                startActivity(intent);
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
            topicsListTextView.append(getDate(timestamp) + " ID: " + event_id + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }
    }

    private void clearAndAddEventsToListView(DataSnapshot dataSnapshot) {
        topicsListTextView.setText("");
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String content = (String) ((DataSnapshot) i.next()).getValue();
            String event_id = (String) ((DataSnapshot) i.next()).getValue();
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String topic_id = (String) ((DataSnapshot) i.next()).getValue(); //// TODO: 25.10.2017 Topic name for now, change for ID
            topicsListTextView.append(getDate(timestamp) + " ID: " + event_id + "; Msg: " + content + "; T. Id: " + topic_id + " \n");
        }
    }
}
