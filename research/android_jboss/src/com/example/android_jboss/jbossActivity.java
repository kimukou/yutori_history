package com.example.android_jboss;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.jboss.embedded.Bootstrap;

public class jbossActivity extends Activity
{
    private final String TAG = jbossActivity.this.getClass().getSimpleName();
    private Bootstrap _server;
    private final int port = 8080;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initServer();
        WebView wv = new WebView(this);

        wv.loadUrl(String.format("http://localhost:%d/hello",port));
        //wv.loadUrl("file:///android_asset/index.html");
        setContentView(wv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        stopServer();
    }


    private void initServer() {
        _server = Bootstrap.getInstance();
        try {
            _server.bootstrap();

            //int port = _server.getConnectors()[0].getLocalPort();
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
            //int port = _server.getConnectors()[0].getLocalPort();
            _server.shutdown();
            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG,"stopServer",ex);
        }
    }
}
