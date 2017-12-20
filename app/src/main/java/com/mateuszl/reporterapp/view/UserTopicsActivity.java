package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.adapters.TopicsAdapter;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.utils.TopicAction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Widok z listą aktywnych wydarzeń zalogowanego użytkownika
 */
public class UserTopicsActivity extends AppCompatActivity {

    @BindView(R.id.add_topic_btn)
    public ImageButton addTopicBtn;

    @BindView(R.id.topics_user_listView)
    public ListView topicsListView;
    FirebaseUser currentUser;
    private List<Topic> topicsList = new ArrayList<Topic>();
    private RepositoryManager repositoryManager;
    TopicsAdapter topicsAdapter = new TopicsAdapter(this, topicsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_topics);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);
        registerForContextMenu(topicsListView);

        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }
        setTitle("Moje wydarzenia");

        topicsListView.setSelection(topicsList.size() - 1);

        DatabaseReference userTopicsRoot = repositoryManager.getUserTopicsRoot().child(this.currentUser.getUid());
        userTopicsRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addTopicsToListView(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(topicsList.get(info.position).getTitle());
        menu.add(Menu.NONE, 0, 0, "Edytuj wydarzenie");
        menu.add(Menu.NONE, 1, 1, "Usuń wydarzenie");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        switch (menuItemIndex) {
            case 0: //edycja wydarzenia
                Intent intent = new Intent(getApplicationContext(), EditTopicActivity.class);
                intent.putExtra("action", TopicAction.EDIT);
                intent.putExtra("Topic", topicsList.get(info.position));
                startActivity(intent);

                break;
            case 1: //usuwanie wydarzenia
                repositoryManager.deleteTopic(topicsList.get(info.position));
                topicsList.remove(info.position);
                topicsAdapter.notifyDataSetChanged();
                showMessage("Wydarzenie usunięto z serwera!");
                break;
        }

        return true;
    }

    @OnItemClick(R.id.topics_user_listView)
    public void openEventsActivity(AdapterView<?> parent, View view,
                                   int position, long id) {
        Topic topicSelected = (Topic) parent.getAdapter().getItem(position);
//todo passing an object with intent instead of strings
        Intent intent = new Intent(getApplicationContext(), UserEventsActivity.class);
        intent.putExtra("Topic", topicSelected);
        startActivity(intent);
    }

    @OnClick(R.id.add_topic_btn)
    public void openCreateTopicActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), EditTopicActivity.class);
        intent.putExtra("action", TopicAction.CREATE);
        startActivity(intent);
    }

    private void addTopicsToListView(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null && (Boolean) dataSnapshot.getValue()) {
            String topicId = dataSnapshot.getKey();

            topicsListView.setAdapter(topicsAdapter);
            repositoryManager.retrieveTopicsByIdForListView(topicId, topicsList, topicsAdapter);
        } else {
            //// TODO: 24/11/2017 coś (pominięcie tego topicu)
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
