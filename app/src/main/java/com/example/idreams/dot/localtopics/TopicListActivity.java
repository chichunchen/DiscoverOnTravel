package com.example.idreams.dot.localtopics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.idreams.dot.BaseActivity;
import com.example.idreams.dot.MainActivity;
import com.example.idreams.dot.R;
import com.example.idreams.dot.utils.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopicListActivity extends BaseActivity {

    private static final String LOG_TAG = "TopicListActivity";
    private String url = "top_article/ptt";
    private static String board_string;
    TopicAdapter adapter;
    ProgressDialog progressbar;
    ArrayList<Topic> topicArrayList;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);

        // Get board name from BoardActivity.
        Intent intent = getIntent();
        board_string = intent.getStringExtra("BoardName");

        // Bind model with Listview.
        topicArrayList = new ArrayList<Topic>();
        adapter = new TopicAdapter(getApplicationContext(), topicArrayList);
        listView = (ListView) findViewById(R.id.topic_listview);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), TopicContentActivity.class);
                        String content_url = topicArrayList.get(position).url;
                        Log.e(LOG_TAG, content_url);
                        intent.putExtra("ContentURL", content_url);
                        startActivity(intent);
                    }
                }
        );
        listView.setAdapter(adapter);

        progressbar = ProgressDialog.show(this, "下載資料", "請稍待片刻...", true);
        getData();
    }

    private void getData() {
        Log.e(LOG_TAG, "getdata()");
        RequestParams params = new RequestParams();
        params.put("period", 10);
        params.put("limit", 50);
        params.put("board", board_string);
        params.put("token", MainActivity.tokenstring);
        RestClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray resultJsonArray = response.getJSONArray("result");
                    for (int i = 0; i < resultJsonArray.length(); i++) {
                        JSONObject j = resultJsonArray.getJSONObject(i);
                        String board = j.getString("board");
                        String title = j.getString("title");
                        String url = j.getString("url");
                        String push = j.getString("push");
                        String time = j.getString("time");
//                        Log.e(LOG_TAG, "onSuccess" + board + title + url + push + time);
                        adapter.add(new Topic(board, title, url, push, time));
                    }


                } catch (Exception err) {
                    Log.e(LOG_TAG, err.getMessage());
                }

                progressbar.dismiss();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(LOG_TAG, "Fail json! " + throwable.getMessage());
                progressbar.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(LOG_TAG, "Fail! " + throwable.getMessage());
                progressbar.dismiss();
            }
        });
    }
}