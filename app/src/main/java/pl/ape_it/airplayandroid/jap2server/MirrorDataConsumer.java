package pl.ape_it.airplayandroid.jap2server;

@FunctionalInterface
public interface MirrorDataConsumer {

    void onData(byte[] data);
}
