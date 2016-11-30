package com.tec.grafmessage;

import android.content.Intent;
import android.graphics.Paint;
import android.media.JetPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tec.grafmessage.DataStrutures.ListaEnlazada;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    public ListView lista_usuarios;
    public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        lista_usuarios = (ListView) findViewById(R.id.contact_list);
        JoinActivity.myClient.setAplicationContext(getApplicationContext());
        JoinActivity.myClient.setListView(lista_usuarios);
        intent=new Intent(this,ChatActivity.class);
        lista_usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView= (TextView) view.findViewById(R.id.user);
                intent.putExtra("destinatario",textView.getText());
                startActivity(intent);
            }
        });
    }
}
