package com.mateuszl.reporterapp.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mateuszl.reporterapp.R;
import com.mateuszl.reporterapp.view.TopicsActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asus on 06.11.2017.
 */

public class TopicsAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
//    public ImageLoader imageLoader;

    public TopicsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.topic_list_item, null);

        TextView title = (TextView) vi.findViewById(R.id.title); // title
        TextView artist = (TextView) vi.findViewById(R.id.description); // artist name
        TextView duration = (TextView) vi.findViewById(R.id.timestamp); // duration
        ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image); // thumb image

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        // Setting all values in listview
        title.setText(song.get(TopicsActivity.TITLE));
        artist.setText(song.get(TopicsActivity.DESCRIPTION));
        duration.setText(song.get(TopicsActivity.TIMESTAMP));
//        imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
}