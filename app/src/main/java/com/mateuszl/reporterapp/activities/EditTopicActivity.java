package com.mateuszl.reporterapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.mateuszl.reporterapp.R;

public class EditTopicActivity extends AppCompatActivity {

    private ImageButton acceptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);


        acceptBtn = (ImageButton) findViewById(R.id.accept_btn);

    }
}
