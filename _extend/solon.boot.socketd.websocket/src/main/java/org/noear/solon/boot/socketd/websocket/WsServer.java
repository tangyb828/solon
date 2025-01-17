package org.noear.solon.boot.socketd.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.message.Message;
import org.noear.solon.core.message.Session;
import org.noear.solon.socketd.ListenerProxy;
import org.noear.solon.socketd.ProtocolManager;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

@SuppressWarnings("unchecked")
public class WsServer extends WebSocketServer {
    public WsServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onStart() {
        System.out.println("Solon.Server:Websocket onStart...");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake shake) {
        ListenerProxy.getGlobal().onOpen(_SocketServerSession.get(conn));
    }

    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        ListenerProxy.getGlobal().onClose(_SocketServerSession.get(conn));

        _SocketServerSession.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String data) {

    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer data) {
        try {
            Session session = _SocketServerSession.get(conn);
            Message message = ProtocolManager.decode(data);

            ListenerProxy.getGlobal().onMessage(session, message);
        } catch (Throwable ex) {
            EventBus.push(ex);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ListenerProxy.getGlobal().onError(_SocketServerSession.get(conn), ex);
    }
}
