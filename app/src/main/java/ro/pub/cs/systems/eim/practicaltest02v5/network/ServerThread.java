package ro.pub.cs.systems.eim.practicaltest02v5.network;

import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;

    private EditText resultEditText;

    private int port;

    public HashMap<String, String> getData() {
        return data;
    }

    public HashMap<String, String> getTimes() {
        return times;
    }

    private HashMap<String, String> data, times;

    public ServerThread() {
        this.data = new HashMap<String, String>();
        this.times = new HashMap<String, String>();
    }

    public void startServer(int port) {
        this.port = port;
        isRunning = true;
        start();
        Log.v("Server", "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e("Server", "An exception has occurred: " + ioException.getMessage());
//            if (Constants.DEBUG) {
//                ioException.printStackTrace();
//            }
        }
        Log.v("Server", "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, ServerThread.this);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e("Server", "An exception has occurred: " + ioException.getMessage());
//            if (Constants.DEBUG) {
//                ioException.printStackTrace();
//            }
        }
    }
}