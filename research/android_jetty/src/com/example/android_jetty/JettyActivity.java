package com.example.android_jetty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final String TAG = JettyActivity.this.getClass().getSimpleName();
    private Server _server;
    private final int port = 8080;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initServer();
        WebView wv = new WebView(this);
        wv.loadUrl(String.format("http://localhost:%d/hello",port));
        setContentView(wv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        stopServer();
    }


    private void initServer() {
        _server = new Server();
        SocketConnector connector = new SocketConnector();
        connector.setPort(port);
        _server.addConnector(connector);
        ServletContextHandler servletContext = new ServletContextHandler();
        ServletHolder holder = new ServletHolder(HelloServlet.class);
        servletContext.addServlet(holder, "/hello");

        HandlerList list = new HandlerList();
        list.addHandler(servletContext);
        _server.setHandler(list);

        try {
            _server.start();
            int port = _server.getConnectors()[0].getLocalPort();
            String msg = String.format("server port(%d) start", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG,"initServer",ex);
        }
    }

    private void stopServer() {
        if (_server==null)return;
        try {
            int port = _server.getConnectors()[0].getLocalPort();
            _server.stop();
            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG,"stopServer",ex);
        }
    }

}
