package com.mateuszl.reporterapp.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.model.Event;

/**
 * Created by Asus on 13.11.2017.
 */

public class EventDbEventListener implements ValueEventListener {

    private String eventId;
    private Event event;

    public Event getEvent() {
        return event;
    }

    public EventDbEventListener(String eventId) {
        this.eventId = eventId;
        event = new Event();
    }
//
//    @Override
//    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//        Object object =  dataSnapshot.child(eventId).getValue();
//        String s1 = dataSnapshot.getKey();
//        Object object2 = dataSnapshot.getValue();
//        Object object3 = dataSnapshot.getChildren();
//        System.out.println(object);
//
////        HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.child(eventId).getValue();
////
////        if (map != null || !map.isEmpty()) {
////
////            event.setId(map.get("id"));
////            event.setContent(map.get("content"));
////            event.setTimestamp(map.get("timestamp"));
////            event.setTopic(map.get("event"));
////        }
////
////        if (event.getContent() == null) {
////            event = new Event();
////            event.setId("AAAAddd HARDCODED");
////            event.setContent("AADddDDAa HARDCODED");
////            event.setTopic("AAAddD HARDCODED");
////        }
//    }


//    @Override
//    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//        Object value1 = dataSnapshot.getValue();
//        System.out.println(value1);
////        Event value = dataSnapshot.getValue(Event.class);
//    }

//
//    @Override
//    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//    }
//
//    @Override
//    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//    }
//
//    @Override
//    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Event event1 = dataSnapshot.getValue(Event.class);
        this.event = event1;

//        HashMap<String, String> map = (HashMap<String, String>) snapshot.child(eventId).getValue();
//
//        if (map != null || !map.isEmpty()) {
//
//            event.setId(map.get("id"));
//            event.setContent(map.get("content"));
//            event.setTimestamp(map.get("timestamp"));
//            event.setTopic(map.get("event"));
//        }
//
//        if (event.getContent() == null) {
//            event = new Event();
//            event.setId("AAAAddd HARDCODED");
//            event.setContent("AADddDDAa HARDCODED");
//            event.setTopic("AAAddD HARDCODED");
//        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
