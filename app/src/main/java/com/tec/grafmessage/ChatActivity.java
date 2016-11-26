package com.tec.grafmessage;

import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.logging.Handler;

public class ChatActivity extends AppCompatActivity {

    TextView chat;
    String mensajes="";
    Socket socket;
    DataOutputStream DOS;
    Client myClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        myClient= new Client(JoinActivity.ip,4444,JoinActivity.UserName);
        chat = (TextView) findViewById(R.id.message_TextView);
        send_Message("me conecte xD"+":"+"Connect");
        myClient.execute();
        /*new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();*/
    }
    public void send_Message(String mensaje){
        myClient.setMessage(mensaje);
        Toast toast = Toast.makeText(getApplicationContext(),"Envié: "+mensaje+"\n"+"Recibí: "+myClient.getMensaje()+"\n", Toast.LENGTH_LONG);
        toast.show();
    }
    public class Client extends AsyncTask<Void, Void, Void> {

        BufferedReader reader;
        String dstAddress;
        int dstPort;
        String response = "";
        String name="";
        Socket socket = null;
        PrintWriter printWriter;
        String mensajes="";
        InputStreamReader inputStreamReader;
        Button send_button;
        EditText mensaje;

        Client(String addr, int port, final String name){
            this.name=name;
            dstAddress = addr;
            dstPort = port;
            send_button = (Button) findViewById(R.id.send_button);
            mensaje = (EditText) findViewById(R.id.message_editText);
            send_button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    printWriter.println(name+":"+mensaje.getText().toString()+":"+"Chat");
                    mensaje.setText("");
                }
            });
        }
        @Override
        public void onPreExecute(){
        }
        public void setMessage(String message){
            response= this.name+":"+message;
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                socket = new Socket(dstAddress, dstPort);
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
                printWriter.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    Thread.sleep(1000);
                    mensajes = reader.readLine();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        /**
         public void reading(Context context) throws IOException {
         while(reader != null) {
         while ((mensaje = reader.readLine()) != null) {
         Toast toast = Toast.makeText(context, mensaje, Toast.LENGTH_LONG);
         toast.show();
         }
         }
         }**/
        public String getMensaje(){
            return mensajes;
        }
        /**
         public void Listener(final Context context) {
         Thread listener = new Thread() {
        @Override
        public void run() {
        while(true){
        try {
        reading(context);
        sleep(3000);
        }catch (IOException e)
        {
        e.printStackTrace();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }
        }
        };
         listener.setPriority(Thread.MAX_PRIORITY);
         listener.start();
         }**/

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mensajes+="EL hilo se ha cerrado";
        }

    }
}

