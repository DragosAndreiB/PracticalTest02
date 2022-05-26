package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("mylog", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("mylog", "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i("mylog", "[COMMUNICATION THREAD] Waiting for parameters from client");
            String operation = bufferedReader.readLine();
            if (operation == null || operation.isEmpty()) {
                Log.e("mylog", "[COMMUNICATION THREAD] Error receiving parameters from client");
                return;
            }
            String[] tokens = operation.split(",");
            int result = 0;
            if(tokens[0].equals("add")){
                result = Integer.parseInt(tokens[1]) + Integer.parseInt(tokens[2]);
            }
            else if(tokens[0].equals("mul")){
                result = Integer.parseInt(tokens[1]) * Integer.parseInt(tokens[2]);
                Thread.sleep(8000);
            }
            else {
                Log.e("mylog", "[COMMUNICATION THREAD] Operation not found");
            }
            Log.i("mylog", "First operator = " + tokens[1]);
            Log.i("mylog", "Second operator = " + tokens[2]);
            Log.i("mylog", "Result = " + result);

            printWriter.println(result);
            printWriter.flush();
        } catch (IOException | InterruptedException ioException) {
            Log.e("mylog", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("mylog", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                }
            }
        }
    }

}
