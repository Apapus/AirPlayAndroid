package pl.ape_it.airplayandroid.jap2server.internal.handler.control;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import pl.ape_it.airplayandroid.jap2server.internal.handler.session.Session;
import pl.ape_it.airplayandroid.jap2server.internal.handler.session.SessionManager;

@ChannelHandler.Sharable
public class FairPlayHandler extends ControlHandler {

    public FairPlayHandler(SessionManager sessionManager) {
        super(sessionManager);
    }

    @Override
    protected boolean handleRequest(ChannelHandlerContext ctx, Session session, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        if ("/fp-setup".equals(uri)) {
            DefaultFullHttpResponse response = createResponseForRequest(request);
            session.getAirPlay().fairPlaySetup(new ByteBufInputStream(request.content()),
                    new ByteBufOutputStream(response.content()));
            return sendResponse(ctx, request, response);
        }
        return false;
    }
}
