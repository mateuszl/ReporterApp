package com.mateuszl.reporterapp.controller.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mateuszl.reporterapp.model.Event;

/**
 * Implementacja Value Event Listenera dla bazy Firebase, która pozwala na asynchroniczną manipulację
 * obiektem typu Event pobieranym z bazy danych.
 * W momencie dodania listenera to danej referencji uruchomiona zostaje jednokrotnie metoda
 * onDataChange(), co pozwala na odczyt danych podlegających tej referencji.
 */
public class EventDbEventListener implements ValueEventListener {

    private Event event;

    public EventDbEventListener() {
        event = new Event();
    }

    public Event getEvent() {
        return event;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        this.event = dataSnapshot.getValue(Event.class);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
