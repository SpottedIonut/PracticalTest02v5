package ro.pub.cs.systems.eim.practicaltest02v5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ro.pub.cs.systems.eim.practicaltest02v5.network.ClientAsync;
import ro.pub.cs.systems.eim.practicaltest02v5.network.ServerThread;

public class PracticalTest02v5MainActivity extends AppCompatActivity {

    private int serverPort = 0;

    private ServerThread serverThread;
    private EditText serverPortEditText, clientAddressEditText, clientPortEditText, keyEditText, valueEditText;
    private EditText resultTextView;
    private Spinner action;
    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            serverPort = Integer.parseInt(serverPortEditText.getText().toString());
            Log.d("SERVER_PORT", "Server port = " + serverPort);
            serverThread = new ServerThread();
            serverThread.startServer(serverPort);
        }
    }

    private final ExecuteButtonClickListener executeButtonClickListener = new ExecuteButtonClickListener();
    private class ExecuteButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String execution = String.valueOf(action.getSelectedItemPosition());
            String key = keyEditText.getText().toString();
            String value = valueEditText.getText().toString();
            String port = clientPortEditText.getText().toString();
            String address = clientAddressEditText.getText().toString();

            ClientAsync clientAsync = new ClientAsync(resultTextView);
            clientAsync.execute(address, port, execution, key, value);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practical_test02v5_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        keyEditText = (EditText)findViewById(R.id.key_edit_text);
        valueEditText = (EditText)findViewById(R.id.value);
        resultTextView = (EditText)findViewById(R.id.result);

        action = (Spinner)findViewById(R.id.action);

        Button connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        Button weatherButton = (Button) findViewById(R.id.execute);
        weatherButton.setOnClickListener(executeButtonClickListener);


    }
}