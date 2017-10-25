package com.mateuszl.reporterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Topic;

public class EditTopicActivity extends AppCompatActivity {

    private ImageButton acceptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        acceptBtn = (ImageButton) findViewById(R.id.accept_btn);

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) { //todo jesli dane w formularzu są poprawne

                    createNewTopic();

                    Intent intent = new Intent(getApplicationContext(), TopicsActivity.class);
                    intent.putExtra("user_name", "mocked userName");
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Złe dane.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNewTopic() {
        Topic topic = new Topic();
        //// TODO: 25.10.2017 pusznięcie topica na serwer
    }
}
