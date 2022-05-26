package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    int port = 0;
    ServerSocket ssocket = null;
    public ServerThread(int port) throws IOException {
        this.port = port;
        try {
            this.ssocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            Log.e("mylog", "Socket error");
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("mylog", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = ssocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e("mylog", "[SERVER THREAD] An exception has occurred: " + clientProtocolException.getMessage());
        } catch (IOException ioException) {
            Log.e("mylog", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (ssocket != null) {
            try {
                ssocket.close();
            } catch (IOException ioException) {
                Log.e("mylog", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

}
