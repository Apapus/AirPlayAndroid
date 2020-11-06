package pl.ape_it.airplayandroid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import pl.ape_it.airplayandroid.jap2server.AirPlayServer;

public class MainActivity extends AppCompatActivity {

    private AirPlayServer airPlayServer;
    private VLCPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new VLCPlayer();
        this.airPlayServer = new AirPlayServer("MonsterAirPlay",
                15614, 5001, player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            airPlayServer.start(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        airPlayServer.stop();
        super.onDestroy();
    }
}
