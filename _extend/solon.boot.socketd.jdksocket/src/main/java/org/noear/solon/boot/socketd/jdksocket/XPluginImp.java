package org.noear.solon.boot.socketd.jdksocket;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.core.Plugin;
import org.noear.solon.socketd.SessionManager;

public class XPluginImp implements Plugin {
    BioServer _server;

    public static String solon_boot_ver(){
        return "jdksocket-socketd/" + Solon.cfg().version();
    }

    @Override
    public void start(SolonApp app) {
        //注册会话管理器
        SessionManager.register(new _SessionManagerImpl());

        if (app.enableSocketD() == false) {
            return;
        }

        long time_start = System.currentTimeMillis();

        System.out.println("solon.Server:main: java.net.ServerSocket(jdksocket-socketd)");

        int _port = app.cfg().getInt("server.socket.port", 0);
        if (_port < 1) {
            _port = 20000 + app.port();
        }

        try {
            _server = new BioServer();
            _server.start(_port);

            long time_end = System.currentTimeMillis();

            System.out.println("solon.Connector:main: jdksocket-socketd: Started ServerConnector@{[Socket]}{0.0.0.0:" + _port + "}");
            System.out.println("solon.Server:main: jdksocket-socketd: Started @" + (time_end - time_start) + "ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() throws Throwable {
        if(_server == null){
            return;
        }

        _server.stop();
        _server = null;
        System.out.println("solon.Server:main: jdksocket-socketd: Has Stopped " + solon_boot_ver());
    }
}
