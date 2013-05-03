
androidでwebコンテナを動かすプロジェクト

	android_glassfish
		use glassfish 3.1
		dex が通らない
		(JDK7の関数を使用)
		
	android_jetty
		use jetty_v8
		(jetty_v9 はSocketContenerが廃止 ＝＞ 代替実装コードが不明のため保留)
	
	
	android_tomcat
		use tomcat 7
		(javax.management を使用しているため、実行時に落ちる）
		＜コンパイル自体はant経由でのみ可能
	