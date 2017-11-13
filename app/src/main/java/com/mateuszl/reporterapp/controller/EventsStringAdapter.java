package com.mateuszl.reporterapp.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Event;

import java.util.List;

import static com.mateuszl.reporterapp.utils.Utils.getDate;

/**
 * Created by mateuszl on 13.11.2017.
 */

public class EventsStringAdapter extends BaseAdapter {

    private Activity activity;
    private List<String> eventIds;
    RepositoryManager repositoryManager;

    public EventsStringAdapter(Activity a, List<String> t) {
        activity = a;
        eventIds = t;
        repositoryManager = new RepositoryManager();
    }

    public int getCount() {
        return eventIds.size();
    }

    public Object getItem(int position) {
        return eventIds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.event_list_item, parent, false);
        }
        TextView content = (TextView) vi.findViewById(R.id.event_content); // content
        TextView date = (TextView) vi.findViewById(R.id.event_timestamp); // date

        String eventId = eventIds.get(position);
        Event event = repositoryManager.getEventById(eventId);

        // Setting all values in listview
        content.setText(event.getContent());
        date.setText(getDate(event.getTimestamp()));
        return vi;
    }
}