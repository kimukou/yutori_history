package com.example.android_glassfish;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;

import java.io.File;

/*
 ref http://d.hatena.ne.jp/backpaper0/20110525

*/

public class glassFishActivity extends Activity {

    private final String TAG = glassFishActivity.this.getClass().getSimpleName();
    private final int port = 8080;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        initServer();
        WebView wv = new WebView(this);
        wv.loadUrl(String.format("http://localhost:%d/sample/hello", port));
        setContentView(wv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        stopServer();
    }

    private GlassFishRuntime gfr;
    private GlassFish gf;
    private void initServer() {
        try {
            gfr = GlassFishRuntime.bootstrap();
            GlassFishProperties props = new GlassFishProperties();
            props.setPort("http-listener", port);

            // GlassFishのインスタンス作ってサーバ起動？
            gf = gfr.newGlassFish(props);
            gf.start();

/*
            // JDBCコネクションプールとJDBCリソース作成
            gf.getCommandRunner().run(
                    "create-jdbc-connection-pool",
                    "--datasourceclassname",
                    "org.apache.derby.jdbc.EmbeddedDataSource40",
                    "--property",
                    "user=sa:password=pwd:databaseName=gftest",
                    "gftest");

            gf.getCommandRunner().run(
                    "create-jdbc-resource",
                    "--connectionpoolid",
                    "gftest",
                    "jdbc/gftest");
*/
            // デ　プ　ロ　イ　(｀・ω・´)ｼｬｷｰﾝ
            gf.getDeployer().deploy(
                    new File("src/main/webapp"),
                    "--contextroot",
                    "sample",
                    "--name",
                    "sampleapp");

            String msg = String.format("server port(%d) start", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (GlassFishException ex) {
            Log.e(TAG, "initServer",ex);
        }
    }

    private void stopServer() {
        try {
            if(gf!=null)gf.stop();
            if(gfr!=null)gfr.shutdown();

            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (GlassFishException ex) {
            Log.e(TAG, "stopServer",ex);
        }
    }

}
