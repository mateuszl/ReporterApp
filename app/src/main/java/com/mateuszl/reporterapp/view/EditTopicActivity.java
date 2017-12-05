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
import com.mateuszl.reporterapp.model.User;
import com.mateuszl.reporterapp.utils.DataGenerator;

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

    private User user;
    private RepositoryManager repositoryManager;
    private String action, currentTime;
    private FirebaseUser currentUser;

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

        action = getIntent().getExtras().get("action").toString();
        setTitle(action + "topic");

    }

    @OnClick(R.id.accept_new_topic_btn)
    public void acceptBtnOnClick(View view) {
        if (topicTitleEditText.getText().length() > 0 && topicDescriptionEditText.getText().length() > 0) {
            //todo dodać sprawdzenie czy takie wydarzenie juz istnieje

            if (topicTitleEditText.getText().toString().equalsIgnoreCase("!generate")) {
                String text = topicDescriptionEditText.getText().toString();
                try {
                    String[] strings = text.split(";");
                    int topics = Integer.decode(strings[0]);
                    int events = Integer.decode(strings[1]);

                    DataGenerator dataGenerator = new DataGenerator(topics, events, currentUser);
                    dataGenerator.generateData();

                    Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
                    intent.putExtra("topicId", "");
                    startActivity(intent);

                    showMessage("Data generated!");
                } catch (Exception e) {
                    showMessage("Data generating error");
                    Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
                    intent.putExtra("topicId", "");
                    startActivity(intent);
                }
            } else {

                Topic newTopic = createNewTopic();

                Topic savedTopic = repositoryManager.saveTopic(newTopic, this.currentUser);

                Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
                intent.putExtra("topicId", savedTopic.getId());
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

        Topic topic = new Topic();

        if (this.currentUser != null) {
            topic.setAuthor(this.currentUser.getUid());
        } else {
            startActivity(LoginActivity.createIntent(this));
            finish();
            return null; //todo potrzebne?
        }

        topic.setDescription(topicDescriptionEditText.getText().toString());
        topic.setTitle(topicTitleEditText.getText().toString());
        topic.setTimestamp(currentTime);
        return topic;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), UserTopicsActivity.class);
        intent.putExtra("topicId", "");
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
