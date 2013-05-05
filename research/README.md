
androidでwebコンテナを動かすプロジェクト

	android_glassfish
		use glassfish 3.1
		dex が通らない
		(JDK7の関数を使用)
		
		use glassfish 3.0
		dex が通らない
		(Antirとか動的変換辺りを使っているため？)
		
	android_jetty
		use jetty_v8
		(jetty_v9 はSocketContenerが廃止 ＝＞ 代替実装コードが不明のため保留)

		use jetty_v9
		java7でコンパイルされたものが提供jarになっているため、実行時にエラー
	
	android_tomcat
		use tomcat 7
		(core.jar が javax.management を使用しているため、実行時に落ちる）
		＜コンパイル自体はant経由でのみ可能
		  javax系のライブラリを変更する必要が有
		
	android_jboss
		https://community.jboss.org/wiki/EmbeddedAndJavaSE
		情報が少なすぎて
		＜コンパイル自体はant経由でのみ実行可能 
		  javax系のライブラリを変更する必要が有
		dex が通らない
		(Antirとか動的変換辺りを使っているため？)
