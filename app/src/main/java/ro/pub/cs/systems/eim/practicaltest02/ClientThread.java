package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String operation;
    private TextView result;

    private Socket socket;

    public ClientThread(String address, int port, String operation, TextView result) {
        this.address = address;
        this.port = port;
        this.operation = operation;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("mylog", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("mylog", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(operation);
            printWriter.flush();

            String resultinfo;
            while ((resultinfo = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = resultinfo;
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("mylog", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("mylog", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

                }
            }
        }
    }
}
