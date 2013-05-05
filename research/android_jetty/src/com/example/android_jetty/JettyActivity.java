package com.example.android_jetty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import android.widget.Toast;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.strumsoft.websocket.phonegap.WebSocketFactory;

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
        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
        java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
        
        initServer();
        
        WebView wv = initWebView();
        wv.addJavascriptInterface(new WebSocketFactory(wv), "WebSocketFactory");
        
        //wv.loadUrl(String.format("http://localhost:%d/hello",port));
        wv.loadUrl("file:///android_asset/index.html");
        setContentView(wv);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
        stopServer();
    }


    private void initServer() {
/*
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
*/
        _server = new Server(port);

        _server.setStopAtShutdown(true);
        _server.setGracefulShutdown(1000);
        ServletContextHandler root = new ServletContextHandler(_server, "/", ServletContextHandler.SESSIONS);

        // デフォルトのWebインタフェースの設定
        // ドキュメントルートは起動時のカレントディレクトリ
        root.setResourceBase("./");
        root.addServlet(DefaultServlet.class, "/*");
        root.addServlet(HelloServlet.class, "/hello");

//        HandlerList list = new HandlerList();
//        list.addHandler(root);
//        _server.setHandler(list);

        // WebSocket 受け付ける Servlet を登録
        // チャット機能の本体
        ChatRoom chatroom = new ChatRoom();
        // ChatRoom を抱えた WebSocket を返すファクトリ
        ChatWebSocketFactory wfactory = new ChatWebSocketFactory(chatroom);
        ServletHolder wsh = new ServletHolder(new WSServlet(wfactory));
        root.addServlet(wsh, "/ws/*");

        try {
            _server.start();
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
            _server.stop();
            String msg = String.format("server port(%d) stop", port);
            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Log.e(TAG,"stopServer",ex);
        }
    }



    private Activity activity;
    private boolean debug_f=true;
    private boolean no_cashed = false;
    private WebView initWebView(){
        activity =this;

        WebView mWebView = new WebView(activity);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        //          webSettings.setSavePassword(false);
        webSettings.setJavaScriptEnabled(true);
        //webSettings.setBuiltInZoomControls(true); /Zoom
        //webSettings.setSupportZoom(true);
        //webSettings.setAppCacheEnabled(true); //キャッシュ
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setAllowFileAccess(true);	//ファイルアクセス
        //webSettings.setPluginsEnabled(false);	//プラグイン有効/無効

        webSettings.setSupportMultipleWindows(true);
        //mWebView.setVerticalScrollBarEnabled(true);//スクロールバー
        mWebView.setHorizontalScrollBarEnabled(false);

        //右10pxの余白を消す
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setVerticalScrollbarOverlay(true);

        // 画像の自動ロードを無効に
        //webSettings.setLoadsImagesAutomatically(false);

        //キャッシュ戦略 http://dayafterneet.blogspot.jp/2011/08/androidwebview_23.html
        if(no_cashed){//キャッシュ無し
            mWebView.clearCache(true);//キャッシュクリア
            //webSettings.setAppCacheEnabled(false);
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//キャッシュは無効に
        }
        else{
            //webSettings.setAppCacheEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        }


        //alert dialog対策
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result){
                if(debug_f){
                    Log.v(TAG,"[onJsAlert](url,msg)"+ url +"," + message);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                if(debug_f){
                    Log.v(TAG,"[onJsConfirm](url,msg)"+ url +"," + message);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                if(debug_f){
                    Log.v(TAG,"[onJsPrompt](url,msg)"+ url +"," + message);
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //mWebView.setWebViewClient(new MyWebViewClient());

        //画面読み込み
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();

        return mWebView;
    }

}
