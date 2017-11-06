package com.mateuszl.reporterapp.view;

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
import com.mateuszl.reporterapp.controller.RepositoryManager;

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
    private Boolean success = false;
    private RepositoryManager repositoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        repositoryManager = RepositoryManager.getInstance();

        addTopicBtn = (ImageButton) findViewById(R.id.add_topic_btn);
        topicsListTextView = (TextView) findViewById(R.id.topicsList_textView);
        topicsListTextView.setMovementMethod(new ScrollingMovementMethod());

        user_name = getIntent().getExtras().get("user_name").toString();
        success = ((boolean) getIntent().getExtras().get("success"));
        String newTopicId = getIntent().getExtras().get("topicId").toString();
//        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle(user_name + " topics");

        if (success) {
            Toast.makeText(getApplicationContext(), "Topic Created !",
                    Toast.LENGTH_SHORT).show();
        }

        if (!newTopicId.isEmpty()){
            //// TODO: 30.10.2017 podswietlenie nowego topicu albo od razu wejscie w jego eventsy
        }


        root = FirebaseDatabase.getInstance().getReference().child("topics");

        addTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditTopicActivity.class);
                intent.putExtra("user_name", "mocked userName");
                intent.putExtra("action", "Create new ");
                startActivity(intent);
            }
        });

        repositoryManager.getTopicsRoot().addChildEventListener(new ChildEventListener() {
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
                Toast.makeText(getApplicationContext(), "Failed to load comments.",

                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTopicsToListView(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            String author = (String) ((DataSnapshot) i.next()).getValue();
            String description = (String) ((DataSnapshot) i.next()).getValue();
            String topicId = (String) ((DataSnapshot) i.next()).getValue();
            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
            String title = (String) ((DataSnapshot) i.next()).getValue();
            topicsListTextView.append(getDate(timestamp) + "; Title: " + title + "; Desc.: " + description + " \n");
        }
    }

    private void clearAndAddTopicsToListView(DataSnapshot dataSnapshot) {
        topicsListTextView.setText("");
        addTopicsToListView(dataSnapshot);
    }
}
