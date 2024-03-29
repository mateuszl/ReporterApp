package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.controller.adapters.EventsAdapter;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

/**
 * Lista zdarzeń (eventów) w wyświetlanej relacji wydarzenia, należącej do zalogowanego użytkownika.
 */
public class UserEventsActivity extends AppCompatActivity {

    @BindView(R.id.send_event_btn)
    public ImageButton sendEventBtn;

    @BindView(R.id.send_event_editText)
    public EditText sendEventEditText;

    @BindView(R.id.events_listView)
    public ListView eventsListView;

    private Topic topic;
    private RepositoryManager repositoryManager;
    private List<Event> topicEventsList = new ArrayList<>();
    private EventsAdapter eventsAdapter = new EventsAdapter(this, topicEventsList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            sendEventBtn.setVisibility(View.VISIBLE);
            sendEventEditText.setVisibility(View.VISIBLE);
        } else {
            sendEventBtn.setVisibility(View.GONE);
            sendEventEditText.setVisibility(View.GONE);
        }

        sendEventBtn.setEnabled(false);
        sendEventEditText.getBackground().setColorFilter(45235, PorterDuff.Mode.SRC_IN);

        registerForContextMenu(eventsListView);

        this.topic = (Topic) getIntent().getSerializableExtra("Topic");

        if (this.topic != null) {
            if (this.topic.getTitle() == null || this.topic.getTitle().isEmpty()) {
                showMessage("topic title empty or null!!");
            } else {
                setTitle(this.topic.getTitle());
            }
        } else {
            showMessage("No such topic in DB!!");
            Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
            startActivity(intent);
        }

        repositoryManager.getEventsRoot(topic.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                addEventsToListView(dataSnapshot);
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
                Toast.makeText(getApplicationContext(), "Failed to load data.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        enableSendButton();
        scrollEventsListViewToBottom();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(topicEventsList.get(info.position).getContent());
        menu.add(Menu.NONE, 0, 0, "Usuń zdarzenie");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();

        if (menuItemIndex == 0) {
            repositoryManager.deleteEvent(topicEventsList.get(info.position), topic);
            topicEventsList.remove(info.position);
            eventsAdapter.notifyDataSetChanged();
            showMessage("Zdarzenie usunięto z bazy!");
        }

        return true;
    }

    @OnClick(R.id.send_event_btn)
    public void saveNewEvent(View view) {
        if (sendEventEditText.getText().length() < 1) {
            //todo podswietlenie pola/mrugniecie czy coś
        } else {
            Long currentTime = System.currentTimeMillis() / 1000;
            String time = currentTime.toString(); //todo zmienic na godzine:minuty:sekundy a nie całą datę

            Event event = new Event(sendEventEditText.getText().toString(), time);
            sendEventEditText.setText("");
            repositoryManager.saveEvent(event, topic);
            scrollEventsListViewToBottom();
        }
    }

    @OnClick(R.id.send_event_editText)
    public void editTextEvent(View view) {
        scrollEventsListViewToBottom();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addEventsToListView(DataSnapshot dataSnapshot) {
        topicEventsList.add(dataSnapshot.getValue(Event.class));

        eventsListView.setAdapter(eventsAdapter);

        scrollEventsListViewToBottom();
    }

    private void scrollEventsListViewToBottom() {
        eventsListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                eventsListView.setSelection(topicEventsList.size() - 1);
            }
        });
    }

    private void enableSendButton() {
        sendEventEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    sendEventBtn.setEnabled(false);
                } else {
                    sendEventBtn.setEnabled(true);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
        startActivity(intent);
    }
}