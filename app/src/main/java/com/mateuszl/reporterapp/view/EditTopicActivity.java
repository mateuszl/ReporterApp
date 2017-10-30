package com.mateuszl.reporterapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.model.User;

public class EditTopicActivity extends AppCompatActivity {

    private ImageButton acceptBtn;
    private String userName;
    private String action, currentTime;
    private EditText topicTitleEditText, topicDescriptionEditText;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        acceptBtn = (ImageButton) findViewById(R.id.accept_btn);
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

                    Long tsLong = System.currentTimeMillis() / 1000;
                    currentTime = tsLong.toString();

                    Topic newTopic = createNewTopic();

                    DatabaseReference topicsRoot = FirebaseDatabase.getInstance().getReference().child("topics");
                    DatabaseReference newTopicRef = topicsRoot.push();
                    newTopic.setId(newTopicRef.getKey());

                    DatabaseReference userTopicsRoot = FirebaseDatabase.getInstance().getReference().child("userTopics");
                    userTopicsRoot.child(user.getId()).child(newTopic.getId()).setValue(true);

                    topicsRoot.child(newTopic.getId()).setValue(newTopic);

                    Intent intent = new Intent(getApplicationContext(), TopicsActivity.class);
                    intent.putExtra("user_name", "mocked userName");
                    intent.putExtra("success", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Złe dane.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Topic createNewTopic() {
        Topic topic = new Topic();
        topic.setAuthor(userName);
        topic.setDescription(topicDescriptionEditText.getText().toString());
        topic.setTitle(topicTitleEditText.getText().toString());
        topic.setTimestamp(currentTime);
//        topic.setSubscribers(Arrays.asList("kebab", "ajzol")); //mocked !!
        return topic;
    }
}
