package com.jukusoft.mmo.client.network;

import com.jukusoft.mmo.client.engine.utils.EncryptionUtils;
import com.jukusoft.mmo.client.game.WritableGame;
import com.jukusoft.mmo.client.game.connection.ServerManager;
import com.jukusoft.mmo.client.game.login.LoginManager;
import com.jukusoft.mmo.client.network.handler.NetHandler;
import com.jukusoft.mmo.client.network.handler.impl.AuthHandler;
import com.jukusoft.mmo.client.network.utils.MessageUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import org.junit.Test;
import org.mockito.Mockito;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.cert.X509Certificate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;

public class NClientTest {

    protected boolean b = false;

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

        client.addHandler(Protocol.MSG_TYPE_AUTH, new AuthHandler(client));
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
    public void testOnConnectionClosed () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.onConnectionClosed(null);
    }

    @Test (expected = NullPointerException.class)
    public void testAddNullHandler () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.addHandler((byte) 0x01, null);
    }

    @Test
    public void testAddHandler () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.addHandler((byte) 0x01, Mockito.mock(NetHandler.class));
    }

    @Test (expected = IllegalStateException.class)
    public void testAddHandlerTwice () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.addHandler((byte) 0x01, Mockito.mock(NetHandler.class));
        client.addHandler((byte) 0x01, Mockito.mock(NetHandler.class));
    }

    @Test (expected = NullPointerException.class)
    public void testHandleNullMessage () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.handleMessage(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandleEmptyMessage () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.handleMessage(Buffer.buffer());
    }

    @Test
    public void testHandleRTTMessage () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);

        Buffer content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_RTT, 0);
        client.handleMessage(content);
    }

    @Test
    public void testHandleRTTMessage1 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 0;
        client.sendDelay = 0;

        Buffer content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_RTT, 0);
        client.handleMessageWithDelay(content);
    }

    @Test
    public void testHandleRTTMessage2 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 50;
        client.sendDelay = 50;
        client.start();

        Buffer content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_RTT, 0);
        client.handleMessageWithDelay(content);

        client.stop();
    }

    @Test
    public void testSendMessageWithDelay () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 50;
        client.sendDelay = 50;
        client.start();
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

        Buffer content = MessageUtils.createPublicKeyRequest();
        client.send(content);

        client.stop();
    }

    @Test
    public void testHandlePublicKeyRequestMessage () throws NoSuchAlgorithmException {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 0;
        client.sendDelay = 0;
        client.start();

        Buffer content = this.createPublicKeyResponse(EncryptionUtils.generateKeyPair().getPublic(), false);
        client.handleMessage(content);

        client.stop();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandlePublicKeyRequestMessage1 () throws NoSuchAlgorithmException {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 0;
        client.sendDelay = 0;
        client.start();

        Buffer content = this.createPublicKeyResponse(EncryptionUtils.generateKeyPair().getPublic(), true);
        client.handleMessage(content);

        client.stop();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testHandlePublicKeyRequestMessageWithError () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 0;
        client.sendDelay = 0;
        client.start();

        Buffer content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, 0);
        content.setInt(Protocol.MSG_BODY_OFFSET, 10);
        content.setBytes(Protocol.MSG_BODY_OFFSET + 4, new byte[10]);
        client.handleMessage(content);

        client.stop();
    }

    @Test
    public void testHandlerException () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);
        client.receiveDelay = 0;
        client.sendDelay = 0;
        client.start();

        client.addHandler((byte) 0xFF, new NetHandler() {
            @Override
            public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) throws Exception {
                throw new IllegalArgumentException("test exception");
            }
        });

        Buffer content = MessageUtils.createMsg((byte) 0xFF, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE, 0);

        //no exception should be thrown, only a log output should be written to console
        client.handleMessage(content);

        client.stop();
    }

    protected static Buffer createPublicKeyResponse (PublicKey publicKey, boolean withError) {
        Buffer content = Buffer.buffer();

        content.setByte(0, Protocol.MSG_TYPE_PROXY);
        content.setByte(1, Protocol.MSG_EXTENDED_TYPE_PUBLIC_KEY_RESPONSE);
        content.setShort(2, Protocol.MSG_PROTOCOL_VERSION);
        content.setInt(4, 0);

        //convert public key to byte array
        byte[] array = EncryptionUtils.convertPublicKeyToByteArray(publicKey);

        //set length of public key
        if (withError) {
            content.setInt(Protocol.MSG_BODY_OFFSET, array.length + 4);
        } else {
            content.setInt(Protocol.MSG_BODY_OFFSET, array.length);
        }

        //set array
        content.setBytes(Protocol.MSG_BODY_OFFSET + 4, array);

        return content;
    }

    @Test
    public void testHandleMessage () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);

        Buffer content = MessageUtils.createMsg((byte) 0xFF, (byte) 0xFF, 0);
        client.handleMessage(content);

        content = MessageUtils.createMsg(Protocol.MSG_TYPE_PROXY, (byte) 0xFF, 0);
        client.handleMessage(content);

        content = MessageUtils.createMsg((byte) 0xFF, (byte) Protocol.MSG_EXTENDED_TYPE_RTT, 0);
        client.handleMessage(content);
    }

    @Test
    public void testHandleMessage1 () {
        WritableGame game = Mockito.mock(WritableGame.class);
        NClient client = new NClient(game);

        b = false;

        client.addHandler((byte) 0xFF, new NetHandler() {
            @Override
            public void handle(Buffer content, byte type, byte extendedType, NClient client, WritableGame game) {
                b = true;
            }
        });

        Buffer content = MessageUtils.createMsg((byte) 0xFF, (byte) 0xFF, 0);
        client.handleMessage(content);

        //check, if handler was executed
        assertEquals(true, b);
    }

    @Test
    public void testStartAndStop () {
        WritableGame game = Mockito.mock(WritableGame.class);

        NClient client = new NClient(game);
        client.start();
        client.stop();
    }

}
