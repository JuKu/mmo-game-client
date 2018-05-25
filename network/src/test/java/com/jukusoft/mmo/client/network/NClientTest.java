package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.login.LoginManager;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import io.vertx.core.net.impl.NetSocketImpl;
import org.junit.Test;
import org.mockito.Mockito;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NClientTest {

    @Test
    public void testConstructor () {
        WritableGame game = Mockito.mock(WritableGame.class);

        new NClient(game);
    }

    @Test (expected = NullPointerException.class)
    public void testLoadNullConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(null);
    }

    @Test (expected = FileNotFoundException.class)
    public void testLoadNotExistentFileConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(new File("not-existent-file.cfg"));
    }

    @Test
    public void testLoadConfig () throws IOException {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.loadConfig(new File("../config/network.cfg"));
    }

    @Test
    public void testConnect () {
        WritableGame game = Mockito.mock(WritableGame.class);

        ServerManager.ConnectRequest req = new ServerManager.ConnectRequest(Mockito.mock(ServerManager.Server.class), Mockito.mock(Handler.class));

        NClient client = new NClient(game);
        client.connect(req, Future.failedFuture("test"));
    }

    @Test
    public void testConnect1 () {
        WritableGame game = Mockito.mock(WritableGame.class);

        ServerManager.ConnectRequest req = new ServerManager.ConnectRequest(Mockito.mock(ServerManager.Server.class), Mockito.mock(Handler.class));

        NClient client = new NClient(game);
        client.start();
        client.connect(req, Future.succeededFuture(new NetSocket() {
            @Override
            public NetSocket exceptionHandler(Handler<Throwable> handler) {
                return null;
            }

            @Override
            public NetSocket handler(Handler<Buffer> handler) {
                return null;
            }

            @Override
            public NetSocket pause() {
                return null;
            }

            @Override
            public NetSocket resume() {
                return null;
            }

            @Override
            public NetSocket endHandler(Handler<Void> endHandler) {
                return null;
            }

            @Override
            public NetSocket write(Buffer data) {
                return null;
            }

            @Override
            public NetSocket setWriteQueueMaxSize(int maxSize) {
                return null;
            }

            @Override
            public NetSocket drainHandler(Handler<Void> handler) {
                return null;
            }

            @Override
            public String writeHandlerID() {
                return null;
            }

            @Override
            public NetSocket write(String str) {
                return null;
            }

            @Override
            public NetSocket write(String str, String enc) {
                return null;
            }

            @Override
            public NetSocket sendFile(String filename, long offset, long length) {
                return null;
            }

            @Override
            public NetSocket sendFile(String filename, long offset, long length, Handler<AsyncResult<Void>> resultHandler) {
                return null;
            }

            @Override
            public SocketAddress remoteAddress() {
                return null;
            }

            @Override
            public SocketAddress localAddress() {
                return null;
            }

            @Override
            public void end() {

            }

            @Override
            public void close() {

            }

            @Override
            public NetSocket closeHandler(Handler<Void> handler) {
                return null;
            }

            @Override
            public NetSocket upgradeToSsl(Handler<Void> handler) {
                return null;
            }

            @Override
            public NetSocket upgradeToSsl(String serverName, Handler<Void> handler) {
                return null;
            }

            @Override
            public boolean isSsl() {
                return false;
            }

            @Override
            public SSLSession sslSession() {
                return null;
            }

            @Override
            public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
                return new X509Certificate[0];
            }

            @Override
            public String indicatedServerName() {
                return null;
            }

            @Override
            public boolean writeQueueFull() {
                return false;
            }
        }));
        client.stop();
    }

    @Test
    public void testConnect2 () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        ServerManager.getInstance().setSelectServer(new ServerManager.Server("127.0.0.1", 10, "test", "test", true));
        ServerManager.getInstance().connect(Mockito.mock(Handler.class));
        client.stop();

        ServerManager.getInstance().setSelectServer(null);
        ServerManager.getInstance().setConnectionExecutor(null);
    }

    @Test
    public void testLogin () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        ServerManager.getInstance().setSelectServer(new ServerManager.Server("127.0.0.1", 10, "test", "test", true));

        LoginManager.getInstance().login("user", "password", Mockito.mock(Handler.class));

        client.stop();
    }

    @Test
    public void testExecuteRTTCheck () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.rttMsgReceived.set(false);

        //this method shouldnt execute anything, because client is waiting for server response
        client.executeRTTCheck();
        assertEquals(false, client.rttMsgReceived.get());
    }

    @Test
    public void testExecuteRTTCheck1 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.rttMsgReceived.set(true);

        client.socket = new NetSocket() {
            @Override
            public NetSocket exceptionHandler(Handler<Throwable> handler) {
                return null;
            }

            @Override
            public NetSocket handler(Handler<Buffer> handler) {
                return null;
            }

            @Override
            public NetSocket pause() {
                return null;
            }

            @Override
            public NetSocket resume() {
                return null;
            }

            @Override
            public NetSocket endHandler(Handler<Void> endHandler) {
                return null;
            }

            @Override
            public NetSocket write(Buffer data) {
                return null;
            }

            @Override
            public NetSocket setWriteQueueMaxSize(int maxSize) {
                return null;
            }

            @Override
            public NetSocket drainHandler(Handler<Void> handler) {
                return null;
            }

            @Override
            public String writeHandlerID() {
                return null;
            }

            @Override
            public NetSocket write(String str) {
                return null;
            }

            @Override
            public NetSocket write(String str, String enc) {
                return null;
            }

            @Override
            public NetSocket sendFile(String filename, long offset, long length) {
                return null;
            }

            @Override
            public NetSocket sendFile(String filename, long offset, long length, Handler<AsyncResult<Void>> resultHandler) {
                return null;
            }

            @Override
            public SocketAddress remoteAddress() {
                return null;
            }

            @Override
            public SocketAddress localAddress() {
                return null;
            }

            @Override
            public void end() {

            }

            @Override
            public void close() {

            }

            @Override
            public NetSocket closeHandler(Handler<Void> handler) {
                return null;
            }

            @Override
            public NetSocket upgradeToSsl(Handler<Void> handler) {
                return null;
            }

            @Override
            public NetSocket upgradeToSsl(String serverName, Handler<Void> handler) {
                return null;
            }

            @Override
            public boolean isSsl() {
                return false;
            }

            @Override
            public SSLSession sslSession() {
                return null;
            }

            @Override
            public X509Certificate[] peerCertificateChain() throws SSLPeerUnverifiedException {
                return new X509Certificate[0];
            }

            @Override
            public String indicatedServerName() {
                return null;
            }

            @Override
            public boolean writeQueueFull() {
                return false;
            }
        };

        client.executeRTTCheck();
        assertEquals(false, client.rttMsgReceived.get());
    }

    @Test (expected = IllegalStateException.class)
    public void testExecuteRTTCheck2 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.rttMsgReceived.set(true);
        client.socket = null;

        client.executeRTTCheck();
        assertEquals(false, client.rttMsgReceived.get());
    }

    @Test
    public void testStartAndStop () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        client.stop();
    }

}
