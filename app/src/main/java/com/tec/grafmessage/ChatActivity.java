package com.tec.grafmessage;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.logging.Handler;

public class ChatActivity extends AppCompatActivity {

    TextView chat;
    Button send_button;
    EditText mensaje;
    String mensajes="";
    Socket socket;
    DataOutputStream DOS;
    Client myClient = new Client(JoinActivity.ip,4444,"AaronSolera");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send_button = (Button) findViewById(R.id.send_button);
        mensaje = (EditText) findViewById(R.id.message_editText);
        chat = (TextView) findViewById(R.id.message_TextView);
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10000);
                        myClient.execute();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        send_Message("me conecte xD"+":"+"Connect");
        send_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                send_Message(mensaje.getText().toString()+":"+"Chat");
                mensaje.setText("");
            }
        });
    }
    public void send_Message(String mensaje){
        myClient.setMessage(mensaje);
        Toast toast = Toast.makeText(getApplicationContext(),"Envié: "+mensaje+"\n"+"Recibí: "+myClient.getMensaje()+"\n", Toast.LENGTH_LONG);
        toast.show();
    }
}
