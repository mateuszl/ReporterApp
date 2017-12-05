package com.mateuszl.reporterapp.utils;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.mateuszl.reporterapp.controller.RepositoryManager;
import com.mateuszl.reporterapp.model.Event;
import com.mateuszl.reporterapp.model.Topic;

import org.fluttercode.datafactory.impl.DataFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataGenerator {
    private int topicsNumber;
    private int eventsNumber;
    private FirebaseUser user;
    private RepositoryManager repositoryManager;
    private DataFactory dataFactory;
    private String startTopicDate;

    public DataGenerator(int topicsNumber, int eventsNumber, FirebaseUser user) {
        this.topicsNumber = topicsNumber;
        this.eventsNumber = eventsNumber;
        this.user = user;
        this.repositoryManager = RepositoryManager.getInstance();
        this.dataFactory = new DataFactory();
    }

    public void generateData() {
        System.out.println("[][][] generating data...");
        //todo
        int i = 0;
        do {
            Topic topic = generateTopic(i);
            Topic savedTopic = repositoryManager.saveTopic(topic, user);
            generateEvents(savedTopic);
            i++;
        } while (i == topicsNumber);

        System.out.println("[][][] data generated!");
    }

    private void generateEvents(Topic topic) {
        int i = 0;
        do {
            Event event = generateEvent(i);
            repositoryManager.saveEvent(event, topic);
            i++;
        } while (i == eventsNumber);
    }

    private Topic generateTopic(int i) {
        if (i > 11) i = dataFactory.getNumberUpTo(11);
        Topic topic = new Topic();
        topic.setTitle("Mecz " + dataFactory.getFirstName() + " i " + dataFactory.getFirstName() + " w " + dataFactory.getCity());
        topic.setAuthor(user.getUid());
        topic.setDescription(dataFactory.getRandomText(3, 12) + ".");
        String timestamp = generateDateAndTimestamp(i);
        topic.setTimestamp(timestamp);
        return topic;
    }

    private Event generateEvent(int i) {
        Event event = new Event();
        event.setContent(dataFactory.getFirstName() + " " + dataFactory.getRandomChars(2, 10));
        long eventDateLong = Long.decode(startTopicDate) + i * 1000;
        event.setTimestamp(String.valueOf(eventDateLong));
        return event;
    }

    private String generateDateAndTimestamp(int i) {
        String startDateStr = i + 1 + "/01/2017";
        Date startDate = getDateFromString(startDateStr);
        Date randomDate = dataFactory.getDate(startDate, 1, 26);
        startTopicDate = generateTimestampFromDate(randomDate);
        return startTopicDate;
    }

    private String generateTimestampFromDateString(String str_date) {
        Date date = getDateFromString(str_date);
        Long longdate = date.getTime() / 1000;
        return longdate.toString();
    }

    private String generateTimestampFromDate(Date date) {
        Long longdate = date.getTime() / 1000;
        return longdate.toString();
    }

    @Nullable
    private Date getDateFromString(String str_date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
