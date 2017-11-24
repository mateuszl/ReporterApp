package com.mateuszl.reporterapp.controller.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.model.Event;

/**
 * Created by Asus on 13.11.2017.
 */

public class EventDbEventListener implements ValueEventListener {

    private Event event;

    public Event getEvent() {
        return event;
    }

    public EventDbEventListener() {
        event = new Event();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.event = dataSnapshot.getValue(Event.class);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
