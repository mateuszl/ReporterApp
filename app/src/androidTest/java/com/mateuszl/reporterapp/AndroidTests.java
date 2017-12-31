package com.mateuszl.reporterapp;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Topic;
import com.mateuszl.reporterapp.utils.Utils;
import com.mateuszl.reporterapp.view.AllTopicsActivity;
import com.mateuszl.reporterapp.view.LoginActivity;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AndroidTests {

    private RepositoryManager repositoryManager;

    private static Topic topic;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule(LoginActivity.class);

    @Test
    public void loginAppPanelViewTest(){
        onView(withId(R.id.user_panel)).check(matches(isDisplayed()));
    }

    @Test
    public void ASaveDummyTopicTest() throws InterruptedException {
        repositoryManager = RepositoryManager.getInstance();
        topic = repositoryManager.saveTopic(generateTopic(), "test user id");
        Assert.assertNotNull(topic);
        Assert.assertNotNull(topic.getId());
    }

    @Test()
    public void BRemoveDummyTopicTest(){
        repositoryManager = RepositoryManager.getInstance();
        Assert.assertNotNull(topic);
        repositoryManager.deleteTopic(topic);
    }

    private Topic generateTopic(){
        Topic topic = new Topic();

        topic.setTitle("Test title");
        topic.setDescription("Test description");
        topic.setTimestamp(Utils.generateTimestamp());
        topic.setAuthor("test user id");

        return topic;
    }

}
