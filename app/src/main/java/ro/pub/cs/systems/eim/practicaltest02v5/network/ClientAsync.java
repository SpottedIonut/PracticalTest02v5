package ro.pub.cs.systems.eim.practicaltest02v5.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v5.General.Utilities;


public class ClientAsync extends AsyncTask<String, String, Void> {

    private TextView resultTextView;

    public ClientAsync(EditText resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected Void doInBackground(String... params) { // address port city info
        Socket socket = null;
        try {
            String serverAddress = params[0];
            int serverPort = Integer.parseInt(params[1]);

            String action = params[2];
            String key = params[3];
            String value = params[4];
            if (value == null)
                value = "";
            Log.d("ServerTest", "An exception has occurred: " + action + "," + key + "," + value + "\n");

            socket = new Socket(serverAddress, serverPort);

            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferReader = Utilities.getReader(socket);



            if (action.equals("1")) {
                action = "PUT";
            } else {
                action = "GET";
            }

            printWriter.println(action + "," + key + "," + value + "\n");
            printWriter.flush();
            String result;

            while ((result = bufferReader.readLine()) != null) {
                if (result.equals("")) {
                    result = "none";
                }
                final String finalizedWeateherInformation = result;
                resultTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        resultTextView.setText(finalizedWeateherInformation);
                    }
                });
            }



        } catch (Exception exception) {
            Log.e("ServerTest", "An exception has occurred: " + exception.getMessage());
//            if (Constants.DEBUG) {
//                exception.printStackTrace();
//            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception exception) {
                Log.e("ServerTest", "An exception has occurred: " + exception.getMessage());
//                if (Constants.DEBUG) {
//                    exception.printStackTrace();
//                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {}

}
