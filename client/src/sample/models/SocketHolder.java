package sample.models;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public final class SocketHolder extends Holder {
    private Socket socket;

    private final static SocketHolder INSTANCE = new SocketHolder();

    private SocketHolder() {}

    public static SocketHolder getInstance() {
        return INSTANCE;
    }

    public void set(Socket socket) {
        this.socket = socket;
    }

    public Socket get() {
        return this.socket;
    }

}
