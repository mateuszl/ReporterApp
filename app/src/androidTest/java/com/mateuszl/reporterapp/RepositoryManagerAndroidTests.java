package com.mateuszl.reporterapp;


import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RepositoryManagerAndroidTests {

    RepositoryManager repositoryManager;


    @Test
    public void saveDummyTopicTest() throws InterruptedException {
        repositoryManager = RepositoryManager.getInstance();
        repositoryManager.saveTopic(generateTopic(), "test user id");

    }

    private Topic generateTopic(){
        Topic topic = new Topic();

        topic.setTitle("Test title");
        topic.setDescription("Test description");
        topic.setTimestamp(Utils.generateTimestamp());
        topic.setAuthor("Test author");

        System.out.println("1111111111111111111");

        return topic;
    }

}
