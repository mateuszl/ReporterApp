package com.mateuszl.reporterapp.controller.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Event;

import java.util.List;

import static com.mateuszl.reporterapp.utils.Utils.getTime;

public class EventsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Event> events;

    public EventsAdapter(Activity a, List<Event> eventList) {
        activity = a;
        events = eventList;
    }

    public int getCount() {
        return events.size();
    }

    public Object getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
            vi = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.event_list_item, parent, false);
        }
        TextView content = (TextView) vi.findViewById(R.id.event_content_textView); // content
        TextView date = (TextView) vi.findViewById(R.id.event_timestamp_textView); // date

        Event event = null;
        event = events.get(position);

        // Setting all values in listview
        content.setText(event.getContent());
        date.setText(getTime(event.getTimestamp()));
        return vi;
    }
}