package com.mateuszl.reporterapp.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.model.Topic;

import java.util.List;

import static com.mateuszl.reporterapp.utils.Utils.getDate;

/**
 * Created by Asus on 06.11.2017.
 */

public class TopicsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private List<Topic> topics;
//    public ImageLoader imageLoader;

    public TopicsAdapter(Activity a, List<Topic> t) {
        activity = a;
        topics = t;
//        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //fixme if needed OPTION 1
//        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return topics.size();
    }

    public Object getItem(int position) {
        return topics.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
//            vi = inflater.inflate(R.layout.topic_list_item, null); //fixme if needed OPTION 1
            vi = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.topic_list_item, parent, false); //OPTION 2
        }
        TextView title = (TextView) vi.findViewById(R.id.title); // title
        TextView description = (TextView) vi.findViewById(R.id.description); // description name
        TextView date = (TextView) vi.findViewById(R.id.timestamp); // date
//        ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

        Topic topic = null;
        topic = topics.get(position);

        // Setting all values in listview
        title.setText(topic.getTitle());
        description.setText(topic.getDescription());
        date.setText(getDate(topic.getTimestamp()));
//        imageLoader.DisplayImage(topic.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}