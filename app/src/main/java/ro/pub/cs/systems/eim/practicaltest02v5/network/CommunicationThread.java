package ro.pub.cs.systems.eim.practicaltest02v5.network;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02v5.General.Utilities;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunicationThread extends Thread {

    private Socket socket;
    private ServerThread serverThread;

    public CommunicationThread(Socket socket, ServerThread serverThread) {
        this.socket = socket;
        this.serverThread = serverThread;
    }

    @Override
    public void run() {

        String message = "";
        try {
            Log.v("CommunicationThread", "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());

            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);

            String input = bufferedReader.readLine();

            String action = input.split(",")[0];
            String key = input.split(",")[1];
            String value = input.split(",")[2];

            HashMap<String, String> data = serverThread.getData();
            HashMap<String, String> times = serverThread.getTimes();

            // Create an OkHttpClient instance
            OkHttpClient client = new OkHttpClient();
            Request request = null;

            try {
                String getstr = "https://time.now/developer/api/timezone/UTC";
                request = new Request.Builder()
                        .url(getstr)
                        .build();

                if (request == null) {
                    return;
                }
                Log.d("communicationThread", "OkHttp request: " + getstr);

                // Execute the request and get the response
                Response response = client.newCall(request).execute();
                String result;
                if (response.isSuccessful() && response.body() != null) {
                    result = response.body().string();
                } else {
                    result = "Error: " + response.code() + " " + response.message();
                }

                JSONObject root = new JSONObject(result);
                String time = root.getString("unixtime");


                if (action.equals("PUT")) {
                    data.put(key, value);
                    times.put(key, time);
                    Log.d("communicationThread", "PUT: " + time);
                } else {

                    if (!data.containsKey(key)) {
                        message = "none\n";
                    } else {
                        String old_time = times.get(key);
                        int intOldTime = Integer.parseInt(old_time);
                        int intTime = Integer.parseInt(time);
                        Log.d("communicationThread", "intOldTime: " + intOldTime + "intTime" + intTime);
                        if (intTime - intOldTime <= 10) {
                            message = data.get(key);
                        } else {
                            message = "none\n";
                        }
                    }
                }

                Log.d("communicationThread", "data: " + data + "\ntimes" + times);





                Log.d("communicationThread", "OkHttp result: " + message);

                printWriter.println(message);
                printWriter.flush();

            } catch (IOException e) {
                Log.e("communicationThread", "OkHttp request failed: " + e.getMessage());
                String errorMessage = "Error: " + e.getMessage();
            }

            Log.v("CommunicationThread", "Connection closed");
        } catch (Exception exception) {
            Log.e("CommunicationThread", "An exception has occurred: " + exception.getMessage());
//            if (Constants.DEBUG) {
//                exception.printStackTrace();
//            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}