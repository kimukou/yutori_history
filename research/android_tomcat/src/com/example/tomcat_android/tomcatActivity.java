package com.example.tomcat_android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

public class tomcatActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final String TAG = tomcatActivity.this.getClass().getSimpleName();
    private Tomcat _server;
    private final int port = 8080;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initServer();
        WebView wv = new WebView(this);
        wv.loadUrl(String.format("http://localhost:%d/hello", port));
        setContentView(wv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        stopServer();
    }


    private void initServer() {
        _server = new Tomcat();
        _server.setPort(8080);

        try {
            /*
        	 * 	[TODO]Error Occuerd
        	 	Could not find class 'org.apache.catalina.core.StandardContext', 
        	 	referenced from method org.apache.catalina.startup.Tomcat.addContext
				
				Reson	android not support javax.management.MBeanRegistration
        	*/
            Context ctx = _server.addContext("/", Environment.getRootDirectory().getAbsolutePath());//, new File(".").getAbsolutePath());
            Tomcat.addServlet(ctx, "hello", new HelloServlet());
            ctx.addServletMapping("/*", "hello");

            _server.start();
            _server.getServer().await();

            String msg = String.format("server port(%d) start", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "initServer", ex);
        }
    }

    private void stopServer() {
        if (_server == null) return;
        try {
            _server.stop();
            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "stopServer", ex);
        }
    }

}
