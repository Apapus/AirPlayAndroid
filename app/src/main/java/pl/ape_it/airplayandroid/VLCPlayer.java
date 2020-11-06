package pl.ape_it.airplayandroid;


import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import pl.ape_it.airplayandroid.jap2server.MirrorDataConsumer;

public class VLCPlayer implements MirrorDataConsumer {

    private PipedInputStream input;
    private PipedOutputStream output;

    public VLCPlayer() {
        output = new PipedOutputStream();
        input = new PipedInputStream();

        //TODO
        /*  in a new thread */
        try {
            input = new PipedInputStream(output);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        //END
    }


    @Override
    public void onData(byte[] bytes) {
        try {
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
