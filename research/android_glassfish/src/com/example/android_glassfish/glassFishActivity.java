package com.example.android_glassfish;

import java.io.File;

import org.glassfish.api.deployment.DeployCommandParameters;
import org.glassfish.api.embedded.ContainerBuilder;
import org.glassfish.api.embedded.EmbeddedDeployer;
import org.glassfish.api.embedded.EmbeddedFileSystem;
import org.glassfish.api.embedded.Server;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

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

    private Server server;
    private EmbeddedDeployer deployer ;
    private void initServer() {
    	
        try {
        	Server.Builder builder = new Server.Builder(TAG);
        	EmbeddedFileSystem.Builder efsb = new EmbeddedFileSystem.Builder();
            efsb.autoDelete(false);
            efsb.installRoot(Environment.getRootDirectory());
            EmbeddedFileSystem efs = efsb.build();
            builder.embeddedFileSystem(efs);
            
            server = builder.build();
            server.addContainer(ContainerBuilder.Type.web);
            server.createPort(port);
            server.start();
            
            deployer = server.getDeployer();
            DeployCommandParameters params = new DeployCommandParameters();
            params.property.put("--contextroot", "sample");
            params.property.put("--name", "sampleapp");
            deployer.deploy(new File("src/main/webapp"),params);

            String msg = String.format("server port(%d) start", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "initServer",ex);
        }
    }

    private void stopServer() {
        try {
        	if(deployer!=null)deployer.undeployAll();
            if(server!=null)server.stop();

            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG, "stopServer",ex);
        }
    }

}
