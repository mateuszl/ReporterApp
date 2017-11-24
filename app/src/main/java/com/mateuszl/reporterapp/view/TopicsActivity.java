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
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * Lista eventów
 */
public class TopicsActivity extends AppCompatActivity {

    @BindView(R.id.add_topic_btn)
    public ImageButton addTopicBtn;

    @BindView(R.id.topics_listView)
    public ListView topicsListView;

    private String user_name;
    private Boolean success = false;
    private List<Topic> topicsList = new ArrayList<Topic>();
    private RepositoryManager repositoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        user_name = getIntent().getExtras().get("user_name").toString();
        success = ((boolean) getIntent().getExtras().get("success"));
        String newTopicId = getIntent().getExtras().get("topicId").toString();
//        topic_name = getIntent().getExtras().get("topic_name").toString();
        setTitle(user_name + " topics");

        if (success) {
            showMessage("Topic Created !");
        }

        if (!newTopicId.isEmpty() && newTopicId != null) {
            //// TODO: 30.10.2017 podswietlenie nowego topicu albo od razu wejscie w jego eventsy
        }

        DatabaseReference topicsRoot = repositoryManager.getTopicsRoot();
        topicsRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Object value = dataSnapshot.getValue();
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
                showMessage("Failed to load comments.");
            }
        });
    }

    @OnItemClick(R.id.topics_listView)
    public void openEventsActivity(AdapterView<?> parent, View view,
                                   int position, long id){
        Topic topicSelected = (Topic) parent.getAdapter().getItem(position);
//todo passing an object with intent instead of strings
        Intent intent = new Intent(getApplicationContext(), EventsActivity.class);
        intent.putExtra("topicId", topicSelected.getId());
        intent.putExtra("topicTitle", topicSelected.getTitle());
        intent.putExtra("topicTimestamp", topicSelected.getTimestamp());
        intent.putExtra("topicDescription", topicSelected.getDescription());
        intent.putExtra("topicAuthor", topicSelected.getAuthor());
        startActivity(intent);
    }

    @OnItemLongClick(R.id.topics_listView)
    public boolean editEventMenu(View view, int position){
        showMessage("Not implemented! pos: " + position);
        return true;
    }

    @OnClick(R.id.add_topic_btn)
    public void openEditTopicActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), EditTopicActivity.class);
        intent.putExtra("user_name", "mocked userName");
        intent.putExtra("action", "Create new ");
        startActivity(intent);
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

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
