package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Lista eventów
 */
public class TopicsActivity extends AppCompatActivity {

    private ImageButton addTopicBtn;
    private ListView topicsListView;
    private DatabaseReference root;
    private String topic_name, user_name;
    private Boolean success = false;
    private RepositoryManager repositoryManager;

    private List<Topic> topicsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        repositoryManager = RepositoryManager.getInstance();

        addTopicBtn = (ImageButton) findViewById(R.id.add_topic_btn);
//        topicsListTextView = (TextView) findViewById(R.id.topicsList_textView);
        topicsListView = (ListView) findViewById(R.id.topics_listView);

//        topicsListTextView.setMovementMethod(new ScrollingMovementMethod());

        user_name = getIntent().getExtras().get("user_name").toString();
        success = ((boolean) getIntent().getExtras().get("success"));
        String newTopicId = getIntent().getExtras().get("topicId").toString();
//        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle(user_name + " topics");

        if (success) {
            Toast.makeText(getApplicationContext(), "Topic Created !",
                    Toast.LENGTH_SHORT).show();
        }

        DatabaseReference topicsRoot = repositoryManager.getTopicsRoot();

        if (!newTopicId.isEmpty() && newTopicId!=null) {
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

        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Topic topicSelected = (Topic) parent.getAdapter().getItem(position);


                Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
                intent.putExtra("topicId", topicSelected.getId());
                startActivity(intent);

            }
        });


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
                Toast.makeText(getApplicationContext(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
//        topicsListTextView.setText("");
        addTopicsToListView(dataSnapshot);
    }
}
