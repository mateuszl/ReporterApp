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
import java.util.Random;

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
        int i = 0;
        do {
            Topic topic = generateTopic(i);
            Topic savedTopic = repositoryManager.saveTopic(topic, user);
            generateEvents(savedTopic);
            i++;
            System.out.println("Topic generated nr: " + i);
        } while (i < topicsNumber);

        System.out.println("[][][] data generated!");
    }

    private void generateEvents(Topic topic) {
        int i = 0;
        do {
            Event event = generateEvent(i);
            repositoryManager.saveEvent(event, topic);
            i++;
        } while (i < eventsNumber);
    }

    private Topic generateTopic(int i) {
        Topic topic = new Topic();
        //
        dataFactory.getFirstName();
        dataFactory.getFirstName();
        dataFactory.getCity();
        // pierwsze generowane dane zawsze sie powtarzają, ze względu na implementacje użytego generatora,
        // dlatego pierwsze wyniki zostają pominięte
        String name1 = dataFactory.getFirstName();
        topic.setTitle("Mecz " + name1 + " i " + dataFactory.getFirstName() + " w " + dataFactory.getCity());
        topic.setAuthor(user.getUid());
        topic.setDescription(generateRandomText(name1, 3,30));
        String timestamp = generateDateAndTimestamp(i);
        topic.setTimestamp(timestamp);
        return topic;
    }

    private Event generateEvent(int i) {
        Event event = new Event();
        event.setContent(generateRandomText(dataFactory.getFirstName(),4,20));
        long eventDateLong = Long.decode(startTopicDate) + 65 + i * 105;
        event.setTimestamp(String.valueOf(eventDateLong));
        return event;
    }

    private String generateDateAndTimestamp(int i) {
        String startDateStr = "02/03/2017 10:23:34";

        String tempTimestamp = generateTimestampFromDateString(startDateStr);

        long dateLong = Long.decode(tempTimestamp) + i * 456763;

        this.startTopicDate = String.valueOf(dateLong);

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
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = null;
        try {
            date = (Date) formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private String generateRandomText(String name, int from, int to) {
        StringBuilder str = new StringBuilder();
        int wordsCount = dataFactory.getNumberBetween(from, to);
        str.append(name);
        for (int i = 0; i < wordsCount; i++) {
            str.append(" ");
            String randomWord = dataFactory.getRandomWord(3, 12);
            str.append(randomWord);
        }
        str.append(".");
        return str.toString();
    }
}
