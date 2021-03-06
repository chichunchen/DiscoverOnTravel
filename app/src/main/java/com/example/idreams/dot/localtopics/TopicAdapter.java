package com.example.idreams.dot.localtopics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.idreams.dot.R;

import java.util.ArrayList;

/**
 * Created by chichunchen on 2015/7/15.
 */
public class TopicAdapter extends ArrayAdapter<Topic> {
    private static final String LOG_TAG = "TopicAdapter";

    public TopicAdapter(Context context, ArrayList<Topic> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Topic topic = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.topic_item, parent, false);
        }

        TextView tvTopicTitle = (TextView) convertView.findViewById(R.id.tv_topic_title);
        String line = topic.title;
        tvTopicTitle.setText(line);
        return convertView;
    }
}