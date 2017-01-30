package arthur.favrat.channelmessaging;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by favrata on 27/01/2017.
 */
public class ChannelActivity extends Activity implements View.OnClickListener, OnDownloadCompleteListener {

    private Downloader dl;
    private ListView listView;

    private int channelId;

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    Handler handler = new Handler();

    final Runnable refresh = new Runnable() {
        public void run() {
            HashMap request = new HashMap<String, String>();
            request.put("accesstoken",settings.getString("access-token", null));
            request.put("channelid",Integer.toString(channelId));
            dl = new Downloader(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=getmessages", request);
            dl.setOnDownloadCompleteListner(ChannelActivity.this);
            dl.execute();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        channelId = Integer.parseInt(getIntent().getStringExtra("channelId"));
        Toast.makeText(getApplicationContext(),Integer.toString(channelId),Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_channel);
        listView = (ListView) findViewById(R.id.listView);

        settings = getSharedPreferences(PREFS_NAME, 0);

        handler.postDelayed(refresh, 1000);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        GetMessagesResponse r = gson.fromJson(content, GetMessagesResponse.class);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Message message : r.messages) {
            Map<String, String> messageList = new HashMap<String, String>(2);
            messageList.put("message", message.username + " : " + message.message);
            messageList.put("date", message.date);
            data.add(messageList);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"message", "date"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        listView.setAdapter(adapter);
    }

}