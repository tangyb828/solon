package org.noear.solon.boot.smarthttp;

import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.boot.smarthttp.http.SmartHttpContextHandler;
import org.noear.solon.boot.smarthttp.http.FormContentFilter;
import org.noear.solon.boot.smarthttp.websocket.WebSocketHandleImp;
import org.noear.solon.boot.smarthttp.websocket._SessionManagerImpl;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.core.Plugin;
import org.noear.solon.socketd.SessionManager;
import org.smartboot.http.HttpBootstrap;

public final class XPluginImp implements Plugin {

    HttpBootstrap _server = null;

    public static String solon_boot_ver() {
        return "smart http 1.0.21/" + Solon.cfg().version();
    }

    @Override
    public void start(SolonApp app) {
        if (app.enableHttp() == false) {
            return;
        }

        XServerProp.init();

        long time_start = System.currentTimeMillis();

        SmartHttpContextHandler _handler = new SmartHttpContextHandler();

        _server = new HttpBootstrap();
        _server.setBannerEnabled(false);
        _server.pipeline().next(_handler);

        if (app.enableWebSocket()) {
            _server.wsPipeline().next(new WebSocketHandleImp());

            SessionManager.register(new _SessionManagerImpl());
        }

        System.out.println("solon.Server:main: SmartHttpServer 1.0.21(smarthttp)");

        try {

            _server.setThreadNum(Runtime.getRuntime().availableProcessors() + 2)
                    .setPort(app.port())
                    .start();

            app.before("**", MethodType.ALL, -9, new FormContentFilter());

            long time_end = System.currentTimeMillis();

            String connectorInfo = "solon.Connector:main: smarthttp: Started ServerConnector@{HTTP/1.1,[http/1.1]";
            if (app.enableWebSocket()) {
                System.out.println(connectorInfo + "[WebSocket]}{0.0.0.0:" + app.port() + "}");
            } else {
                System.out.println(connectorInfo + "}{0.0.0.0:" + app.port() + "}");
            }

            System.out.println("solon.Server:main: smarthttp: Started @" + (time_end - time_start) + "ms");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.shutdown();
            _server = null;

            System.out.println("solon.Server:main: smarthttp: Has Stopped " + solon_boot_ver());
        }
    }
}

