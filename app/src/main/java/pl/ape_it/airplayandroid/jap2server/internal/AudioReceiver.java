package pl.ape_it.airplayandroid.jap2server.internal;

import android.util.Log;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import pl.ape_it.airplayandroid.jap2server.internal.handler.audio.AudioHandler;

public class AudioReceiver implements Runnable {

    private final AudioHandler audioHandler;

    public AudioReceiver(AudioHandler audioHandler) {
        this.audioHandler = audioHandler;
    }

    @Override
    public void run() {
        int port = 4998;
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workerGroup = eventLoopGroup();

        try {
            bootstrap
                    .group(workerGroup)
                    .channel(datagramChannelClass())
                    .localAddress(new InetSocketAddress(port))
                    .handler(new ChannelInitializer<DatagramChannel>() {
                        @Override
                        public void initChannel(final DatagramChannel ch) {
                            ch.pipeline().addLast(audioHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind().sync();
            Log.d(this.getClass().getSimpleName(),"Audio receiver listening " +
                    "on port: {}" + " " + port);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Log.d(this.getClass().getSimpleName(),"Audio receiver interrupted");
        } finally {
            Log.d(this.getClass().getSimpleName(),"Audio receiver stopped");
            workerGroup.shutdownGracefully();
        }
    }

    private EventLoopGroup eventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends DatagramChannel> datagramChannelClass() {
        return Epoll.isAvailable() ? EpollDatagramChannel.class : NioDatagramChannel.class;
    }
}
