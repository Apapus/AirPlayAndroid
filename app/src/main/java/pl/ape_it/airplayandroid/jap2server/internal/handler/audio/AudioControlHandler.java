package pl.ape_it.airplayandroid.jap2server.internal.handler.audio;

import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class AudioControlHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        ByteBuf content = msg.content();
        int contentLength = content.readableBytes();
        byte[] contentBytes = new byte[contentLength];
        content.readBytes(contentBytes);
        int type = contentBytes[1] & ~0x80;
        Log.d(this.getClass().getSimpleName(),
                "Got audio control packet, type: {}, length: {}" + " " +  type+ " " +
                contentLength);
    }
}
