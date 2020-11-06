package pl.ape_it.airplayandroid.jap2lib.rtsp;

public interface MediaStreamInfo {

    StreamType getStreamType();

    enum StreamType {
        AUDIO,
        VIDEO
    }
}
