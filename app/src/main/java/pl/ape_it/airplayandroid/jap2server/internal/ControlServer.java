package pl.ape_it.airplayandroid.jap2server.internal;

import android.util.Log;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.rtsp.RtspDecoder;
import io.netty.handler.codec.rtsp.RtspEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import pl.ape_it.airplayandroid.jap2server.MirrorDataConsumer;
import pl.ape_it.airplayandroid.jap2server.internal.handler.control.FairPlayHandler;
import pl.ape_it.airplayandroid.jap2server.internal.handler.control.HeartBeatHandler;
import pl.ape_it.airplayandroid.jap2server.internal.handler.control.PairingHandler;
import pl.ape_it.airplayandroid.jap2server.internal.handler.control.RTSPHandler;
import pl.ape_it.airplayandroid.jap2server.internal.handler.mirroring.MirroringHandler;
import pl.ape_it.airplayandroid.jap2server.internal.handler.session.SessionManager;
import java.net.InetSocketAddress;

public class ControlServer implements Runnable {

    private final PairingHandler pairingHandler;
    private final FairPlayHandler fairPlayHandler;
    private final RTSPHandler rtspHandler;
    private final HeartBeatHandler heartBeatHandler;

    private final int airTunesPort;

    public ControlServer(int airPlayPort, int airTunesPort, MirrorDataConsumer mirrorDataConsumer) {
        this.airTunesPort = airTunesPort;
        SessionManager sessionManager = new SessionManager();
        pairingHandler = new PairingHandler(sessionManager);
        fairPlayHandler = new FairPlayHandler(sessionManager);
        rtspHandler = new RTSPHandler(airPlayPort, airTunesPort, sessionManager, mirrorDataConsumer);
        heartBeatHandler = new HeartBeatHandler(sessionManager);
    }

    @Override
    public void run() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = eventLoopGroup();
        EventLoopGroup workerGroup = eventLoopGroup();
        try {
            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(serverSocketChannelClass())
                    .localAddress(new InetSocketAddress(airTunesPort))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(
                                    new RtspDecoder(),
                                    new RtspEncoder(),
                                    new HttpObjectAggregator(64 * 1024),
                                    new LoggingHandler(LogLevel.INFO),
                                    pairingHandler,
                                    fairPlayHandler,
                                    rtspHandler,
                                    heartBeatHandler);
                        }
                    })
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            Log.d(this.getClass().getSimpleName(),"Control server listening " +
                    "on port: {}" +  airTunesPort);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Log.d(this.getClass().getSimpleName(),"Control server stopped");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private EventLoopGroup eventLoopGroup() {
        return Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    private Class<? extends ServerSocketChannel> serverSocketChannelClass() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}
