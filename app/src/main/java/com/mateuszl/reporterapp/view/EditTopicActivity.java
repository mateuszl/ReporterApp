package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.utils.DataGenerator;
import com.mateuszl.reporterapp.utils.TopicAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTopicActivity extends AppCompatActivity {

    @BindView(R.id.accept_new_topic_btn)
    public ImageButton acceptBtn;

    @BindView(R.id.topic_title_editText)
    public EditText topicTitleEditText;

    @BindView(R.id.topic_description_editText)
    public EditText topicDescriptionEditText;

    private RepositoryManager repositoryManager;
    private String currentTime;
    private TopicAction action;
    private FirebaseUser currentUser;
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);
        repositoryManager = RepositoryManager.getInstance();
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return;
        }

        action = (TopicAction) getIntent().getExtras().get("action");

        switch (action) {
            case CREATE:
                setTitle("Tworzenie wydarzenia");
                break;
            case EDIT:
                this.topic = (Topic) getIntent().getSerializableExtra("Topic");
                setTitle("Edycja " + topic.getTitle());
                setFields(topic);
                break;
        }
    }

    private void setFields(Topic topic) {
        topicTitleEditText.setText(topic.getTitle());
        topicDescriptionEditText.setText(topic.getDescription());
    }

    @OnClick(R.id.accept_new_topic_btn)
    public void acceptBtnOnClick(View view) {
        if (topicTitleEditText.getText().length() > 0 && topicDescriptionEditText.getText().length() > 0) {
            //todo dodać sprawdzenie czy takie wydarzenie juz istnieje

            //generowanie danych po wpisaniu specjalnych znaków w pola edycji
            if (topicTitleEditText.getText().toString().equalsIgnoreCase("!gen@")) { //todo uprościć
                String numbers = topicDescriptionEditText.getText().toString();
                showMessage("Generowanie danych...");
                try {
                    String[] strings = numbers.split(";");
                    int topics = Integer.decode(strings[0]);
                    int events = Integer.decode(strings[1]);

                    DataGenerator dataGenerator = new DataGenerator(topics, events, currentUser);
                    dataGenerator.generateData();

                    showMessage("Dane wygenerowano! " + numbers);
                } catch (Exception e) {
                    showMessage("Błąd podczas generowania danych...");
                }

            } else if (topicTitleEditText.getText().toString().equalsIgnoreCase("!del@")) {
                repositoryManager.cleanDatabase();
                showMessage("Baza danych wyczyszczona!");
            } else {
                switch (action) {
                    case EDIT:
                        topic.setTitle(topicTitleEditText.getText().toString());
                        topic.setDescription(topicDescriptionEditText.getText().toString());
                        repositoryManager.editTopic(topic);
                        break;
                    case CREATE:
                        Topic newTopic = createNewTopic();
                        Topic savedTopic = repositoryManager.saveTopic(newTopic, this.currentUser);
                        break;
                }
                Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
                startActivity(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Złe dane.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Topic createNewTopic() {
        Long tsLong = System.currentTimeMillis() / 1000;
        currentTime = tsLong.toString();

        Topic newTopic = new Topic();

        if (this.currentUser != null) {
            newTopic.setAuthor(this.currentUser.getUid());
        } else {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return null; //todo potrzebne?
        }

        newTopic.setDescription(topicDescriptionEditText.getText().toString());
        newTopic.setTitle(topicTitleEditText.getText().toString());
        newTopic.setTimestamp(currentTime);
        return newTopic;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
