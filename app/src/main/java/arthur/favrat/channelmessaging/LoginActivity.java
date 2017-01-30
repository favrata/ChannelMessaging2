package arthur.favrat.channelmessaging;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends Activity implements View.OnClickListener, OnDownloadCompleteListener {

    private Downloader dl;

    private EditText editTextIdentifiant;
    private EditText editTextMdp;
    private Button buttonValider;

    public static final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextIdentifiant = (EditText) findViewById(R.id.editTextIdentifiant);
        editTextMdp = (EditText) findViewById(R.id.editTextMdp);
        buttonValider = (Button) findViewById(R.id.buttonValider);

        buttonValider.setOnClickListener(this);

        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonValider)
            login();
    }

    private Boolean login() {
        HashMap request = new HashMap<String, String>();
        request.put("username",editTextIdentifiant.getText().toString());
        request.put("password",editTextMdp.getText().toString());
        dl = new Downloader(getApplicationContext(), "http://www.raphaelbischof.fr/messaging/?function=connect", request);
        dl.setOnDownloadCompleteListner(this);
        dl.execute();
        return true;
    }

    @Override
    public void onDownloadComplete(String content) {
        Gson gson = new Gson();
        ConnectResponse r = gson.fromJson(content, ConnectResponse.class);
        String resultMsg = "Une erreur inattendue s'est produite";
        //Toast.makeText(getApplicationContext(),Integer.toString(r.code),Toast.LENGTH_SHORT).show();
        if(r.code == 500) {
            resultMsg = "L'identifiant ou le mot de passe est invalide";
        }
        if(r.code == 200) {
            resultMsg = "Connexion réussie";
            editor.putString("access-token", r.accesstoken);
            editor.commit();
            Intent intentChannelListActivity = new Intent(getApplicationContext(), ChannelListActivity.class);
            startActivity(intentChannelListActivity);
        }
        Toast.makeText(getApplicationContext(),resultMsg,Toast.LENGTH_LONG).show();
    }
}
