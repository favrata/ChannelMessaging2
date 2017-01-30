package arthur.favrat.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
public class ChannelListActivity extends Activity implements View.OnClickListener, OnDownloadCompleteListener, AdapterView.OnItemClickListener {

    private Downloader dl;
    private ListView listView;

    private ArrayList<Channel> listChannels;

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_channellist);
        listView = (ListView) findViewById(R.id.listView);

        settings = getSharedPreferences(PREFS_NAME, 0);

        HashMap request = new HashMap<String, String>();
        request.put("accesstoken",settings.getString("access-token", null));
        //Toast.makeText(getApplicationContext(),settings.getString("access-token", null),Toast.LENGTH_LONG).show();
        dl = new Downloader(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=getchannels", request);
        dl.setOnDownloadCompleteListner(this);
        dl.execute();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        GetChannelsResponse r = gson.fromJson(content, GetChannelsResponse.class);
        listChannels = r.channels;
        //Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),Integer.toString(r.code),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (Channel channel : r.channels) {
            Map<String, String> channelList = new HashMap<String, String>(2);
            channelList.put("name", channel.name);
            channelList.put("connectedusers", "Utilisateur(s) connecté(s) : " + Integer.toString(channel.connectedusers));
            data.add(channelList);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_2,
                new String[] {"name", "connectedusers"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Channel channel = listChannels.get(position);
        //Toast.makeText(getApplicationContext(),channel.name,Toast.LENGTH_LONG).show();
        Intent intentChannelActivity = new Intent(getApplicationContext(), ChannelActivity.class);
        intentChannelActivity.putExtra("channelId", Integer.toString(channel.channelID));
        startActivity(intentChannelActivity);

    }
}
