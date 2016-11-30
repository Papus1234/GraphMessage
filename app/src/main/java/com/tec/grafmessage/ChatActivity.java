package com.tec.grafmessage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {

    Button send_button;
    TextView chat;
    EditText mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send_button = (Button) findViewById(R.id.send_button);
        chat= (TextView) findViewById(R.id.message_TextView);
        mensaje = (EditText) findViewById(R.id.message_editText);
        JoinActivity.myClient.setDestinatario(getIntent().getStringExtra("destinatario"));
        JoinActivity.myClient.setMessageEditText(mensaje);
        JoinActivity.myClient.setMessageView(chat);
        JoinActivity.myClient.sendMessage(send_button);
    }
}

