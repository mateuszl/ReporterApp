package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.model.User;

public class EditTopicActivity extends AppCompatActivity {

    private ImageButton acceptBtn;
    private String userName;
    private String action, currentTime;
    private EditText topicTitleEditText, topicDescriptionEditText;
    private User user;
    private RepositoryManager repositoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);
        repositoryManager = RepositoryManager.getInstance();

        acceptBtn = (ImageButton) findViewById(R.id.accept_new_topic_btn);
        topicTitleEditText = (EditText) findViewById(R.id.topic_title_editText);
        topicDescriptionEditText = (EditText) findViewById(R.id.topic_description_editText);

        userName = getIntent().getExtras().get("user_name").toString();
        action = getIntent().getExtras().get("action").toString();
        setTitle(action + "topic");

        //
        user = new User();
        user.setId("123");
        user.setName("Mateusz");
        //

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topicTitleEditText.getText().length() > 0 && topicDescriptionEditText.getText().length() > 0) {
                    //todo dodać sprawdzenie czy takie wydarzenie juz istnieje

                    Topic newTopic = createNewTopic();

                    String savedTopicId = repositoryManager.saveTopic(newTopic, user);

                    Intent intent = new Intent(getApplicationContext(), TopicsActivity.class);
                    intent.putExtra("user_name", "mocked userName");
                    intent.putExtra("success", true);
                    intent.putExtra("topicId", savedTopicId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Złe dane.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Topic createNewTopic() {
        Long tsLong = System.currentTimeMillis() / 1000;
        currentTime = tsLong.toString();

        Topic topic = new Topic();
        topic.setAuthor(userName);
        topic.setDescription(topicDescriptionEditText.getText().toString());
        topic.setTitle(topicTitleEditText.getText().toString());
        topic.setTimestamp(currentTime);
//        topic.setSubscribers(Arrays.asList("kebab", "ajzol")); //mocked !!
        return topic;
    }
}
