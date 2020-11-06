package pl.ape_it.airplayandroid.jap2server.internal;

import android.os.AsyncTask;

public class ServerTask extends AsyncTask<ControlServer, Integer, Integer> {

    @Override
    protected Integer doInBackground(ControlServer... controlServers) {
        controlServers[0].run();
        return null;
    }
}
