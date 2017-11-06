package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

/**
 * Lista eventów
 */
public class TopicsActivity extends AppCompatActivity {


    // All static variables
    public static final String URL = "https://api.androidhive.info/music/music.xml";
    // XML node keys
    public static final String AUTHOR = "author"; // parent node
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TIMESTAMP = "timestamp";
//    public static final String KEY_THUMB_URL = "thumb_url";


    private ImageButton addTopicBtn;
    private TextView topicsListTextView;
    private ListView topicsListView;
    private DatabaseReference root;
    private String topic_name, user_name;
    private Boolean success = false;
    private RepositoryManager repositoryManager;

    private List<String> topicList = new ArrayList<>();

    private List<Topic> topicsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        repositoryManager = RepositoryManager.getInstance();

        addTopicBtn = (ImageButton) findViewById(R.id.add_topic_btn);
//        topicsListTextView = (TextView) findViewById(R.id.topicsList_textView);
        topicsListView = (ListView) findViewById(R.id.topics_listView);

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

        DatabaseReference topicsRoot = repositoryManager.getTopicsRoot();


        /* todo
        FirebaseListAdapter myAdapter = new FirebaseListAdapter<Topic>(this, Topic.class, android.R.layout.simple_list_item_1, topicsRoot) {

            protected void populateView(View v, Topic model, int position) {

                ((TextView)v.findViewById(R.id.topics_listView)).setText(model.getTitle());
            }

        };
        topicsListView.setAdapter(myAdapter);
*/


        if (!newTopicId.isEmpty()) {
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
//        Iterator i = dataSnapshot.getChildren().iterator();
//        while (i.hasNext()) {
//            String author = (String) ((DataSnapshot) i.next()).getValue();
//            String description = (String) ((DataSnapshot) i.next()).getValue();
//            String topicId = (String) ((DataSnapshot) i.next()).getValue();
//            String timestamp = (String) ((DataSnapshot) i.next()).getValue();
//            String title = (String) ((DataSnapshot) i.next()).getValue();
//            topicsListTextView.append(getDate(timestamp) + "; Title: " + title + "; Desc.: " + description + " \n");
//        }

        Topic topic = dataSnapshot.getValue(Topic.class);
        topicList.add("Name: " + topic.getTitle() + " || Id: " + topic.getId());


        topicsList.add(topic);

        TopicsAdapter topicsAdapter = new TopicsAdapter(this, topicsList);
        topicsListView.setAdapter(topicsAdapter);

        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

//        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.topic_list_item, topicList);
//        topicsListView.setAdapter(stringArrayAdapter);
    }

    private void clearAndAddTopicsToListView(DataSnapshot dataSnapshot) {
        topicsListTextView.setText("");
        addTopicsToListView(dataSnapshot);
    }
}
