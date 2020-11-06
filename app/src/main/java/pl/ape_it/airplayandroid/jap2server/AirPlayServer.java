package pl.ape_it.airplayandroid.jap2server;

import android.content.Context;
import android.os.AsyncTask;

import pl.ape_it.airplayandroid.jap2lib.AirPlayBonjour;
import pl.ape_it.airplayandroid.jap2server.internal.ControlServer;
import pl.ape_it.airplayandroid.jap2server.internal.ServerTask;

public class AirPlayServer {

    private final AirPlayBonjour airPlayBonjour;
    private final MirrorDataConsumer mirrorDataConsumer;
    private final ControlServer controlServer;

    private final String serverName;
    private final int airPlayPort;
    private final int airTunesPort;

    public AirPlayServer(String serverName, int airPlayPort, int airTunesPort,
                         MirrorDataConsumer mirrorDataConsumer) {
        this.serverName = serverName;
        airPlayBonjour = new AirPlayBonjour(serverName);
        this.airPlayPort = airPlayPort;
        this.airTunesPort = airTunesPort;
        this.mirrorDataConsumer = mirrorDataConsumer;
        controlServer = new ControlServer(airPlayPort, airTunesPort, mirrorDataConsumer);
    }

    public void start(Context context) throws Exception {
        airPlayBonjour.start(airPlayPort, airTunesPort, context);
        new Thread(controlServer).start();
//        ServerTask.execute(controlServer);
//        AsyncTask.execute(controlServer);
    }

    public void stop() {
        airPlayBonjour.stop();
    }

    // TODO On client connected / disconnected
}
