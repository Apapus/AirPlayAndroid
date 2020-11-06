package pl.ape_it.airplayandroid.jap2lib;


import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import javax.jmdns.JmmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Registers airplay/airtunes service mdns
 */
public class AirPlayBonjour {

    private static final String AIRPLAY_SERVICE_TYPE = "_airplay._tcp";
    private static final String AIRTUNES_SERVICE_TYPE = "_raop._tcp";

    private final String serverName;

    public AirPlayBonjour(String serverName) {
        this.serverName = serverName;
    }

    public void start(int airPlayPort, int airTunesPort, Context context) throws IOException {
        NsdManager systemService = (NsdManager)context.getSystemService(Context.NSD_SERVICE);

        NsdServiceInfo sInfo = new NsdServiceInfo();
        sInfo.setServiceName("MyOwnBonjour");
        sInfo.setServiceType(AIRPLAY_SERVICE_TYPE);
        sInfo.setPort(airPlayPort);

        airPlayMDNSProps().forEach(sInfo::setAttribute);


        systemService.registerService(sInfo, NsdManager.PROTOCOL_DNS_SD, new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d("ServiceRegistration", String.format("faield to register airplay_Service with code %s", errorCode));
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.d("ServiceRegistration", "Registered airplay_Service");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        });



        String airTunesServerName = "010203040506@" + serverName;
        NsdServiceInfo airTunes = new NsdServiceInfo();
        airTunes.setServiceName(airTunesServerName);
        airTunes.setServiceType(AIRTUNES_SERVICE_TYPE);
        airTunes.setPort(airTunesPort);

        airTunesMDNSProps().forEach(sInfo::setAttribute);

        systemService.registerService(airTunes, NsdManager.PROTOCOL_DNS_SD, new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d("ServiceRegistration", String.format("faield to register airtunes_Service with code %s", errorCode));
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                Log.d("ServiceRegistration", "Registered airtunes_Service");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        });


    }

    public void stop() {
        JmmDNS.Factory.getInstance().unregisterService(airPlayService);
        Log.d(this.getClass().getSimpleName(),"{} service is unregistered" +
                        " " +
                airPlayService.getName());
        JmmDNS.Factory.getInstance().unregisterService(airTunesService);
        Log.d(this.getClass().getSimpleName(),
                "{} service is unregistered" + "  " +
                airTunesService.getName());
    }

    private Map<String, String> airPlayMDNSProps() {
        HashMap<String, String> airPlayMDNSProps = new HashMap<>();
        airPlayMDNSProps.put("deviceid", "01:02:03:04:05:06");
        airPlayMDNSProps.put("features", "0x5A7FFFF7,0x1E");
        airPlayMDNSProps.put("srcvers", "220.68");
        airPlayMDNSProps.put("flags", "0x4");
        airPlayMDNSProps.put("vv", "2");
        airPlayMDNSProps.put("model", "AppleTV2,1");
        airPlayMDNSProps.put("rhd", "5.6.0.0");
        airPlayMDNSProps.put("pw", "false");
        airPlayMDNSProps.put("pk", "b07727d6f6cd6e08b58ede525ec3cdeaa252ad9f683feb212ef8a205246554e7");
        airPlayMDNSProps.put("pi", "2e388006-13ba-4041-9a67-25dd4a43d536");
        return airPlayMDNSProps;
    }

    private Map<String, String> airTunesMDNSProps() {
        HashMap<String, String> airTunesMDNSProps = new HashMap<>();
        airTunesMDNSProps.put("ch", "2");
        airTunesMDNSProps.put("cn", "0,1,2,3");
        airTunesMDNSProps.put("da", "true");
        airTunesMDNSProps.put("et", "0,3,5");
        airTunesMDNSProps.put("vv", "2");
        airTunesMDNSProps.put("ft", "0x5A7FFFF7,0x1E");
        airTunesMDNSProps.put("am", "AppleTV2,1");
        airTunesMDNSProps.put("md", "0,1,2");
        airTunesMDNSProps.put("rhd", "5.6.0.0");
        airTunesMDNSProps.put("pw", "false");
        airTunesMDNSProps.put("sr", "44100");
        airTunesMDNSProps.put("ss", "16");
        airTunesMDNSProps.put("sv", "false");
        airTunesMDNSProps.put("tp", "UDP");
        airTunesMDNSProps.put("txtvers", "1");
        airTunesMDNSProps.put("sf", "0x4");
        airTunesMDNSProps.put("vs", "220.68");
        airTunesMDNSProps.put("vn", "65537");
        airTunesMDNSProps.put("pk", "b07727d6f6cd6e08b58ede525ec3cdeaa252ad9f683feb212ef8a205246554e7");
        return airTunesMDNSProps;
    }
}
