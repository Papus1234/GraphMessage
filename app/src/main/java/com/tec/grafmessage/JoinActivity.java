package com.tec.grafmessage;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class JoinActivity extends AppCompatActivity {

    LoginButton loginButton;
    EditText ip_adress;
    TextView intro;
    CallbackManager callbackManager;
    Intent intent;
    public static String ip="";
    public boolean infacebook=false;
    public static String UserName;
    static Client myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_join);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        callbackManager = CallbackManager.Factory.create();
        ip_adress = (EditText) findViewById(R.id.ip_editText);
        intro = (TextView) findViewById(R.id.intro_messaje);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                infacebook=true;
                gotoMainActivity();
                UserName = Profile.getCurrentProfile().getName();
                intro.setText("Bienvenido a GraphMessage " + UserName);
            }
            @Override
            public void onCancel() {
                infacebook=false;
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    public void Ingresar(View view){
        ip= ip_adress.getText().toString();
        if(infacebook)
        {
            Toast toast = Toast.makeText(getApplicationContext(), ip, Toast.LENGTH_LONG);
            toast.show();
            myClient= new Client(JoinActivity.ip,4444,JoinActivity.UserName);
            myClient.execute();
            myClient.setMessage("En línea"+":"+"Connect");
            startActivity(intent);
        }
    }

    private void gotoMainActivity() {
        intent=new Intent(this,UserListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class Client extends AsyncTask<Void, Void, Void> {

        ArrayList<String> usuarios;
        BufferedReader reader;
        int dstPort;
        String response = "", name= "", destinatario="sin destinatario", cadena_mensajes="", mensajes = "No hay mensajes nuevos", dstAddress;
        Socket socket = null;
        PrintWriter printWriter;
        InputStreamReader inputStreamReader;
        Handler handler;
        TextView chatView=null;
        EditText mensaje=null;
        ListView listView=null;
        Context context=null;
        boolean incluir_usuario;
        ArrayAdapter arrayAdapter;

        Client(String addr, int port, final String name){
            this.name=name;
            dstAddress = addr;
            dstPort = port;
        }
        public void setListView(ListView listView){
            this.listView=listView;
        }
        public void setAplicationContext(Context context){
            this.context=context;
        }
        public void setDestinatario(String destinatario){
            this.destinatario =destinatario;
        }
        public void sendMessage(Button button){
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mensaje!=null){
                        printWriter.println(name+":"+mensaje.getText().toString()+"/"+ destinatario +":"+"Chat");
                        mensaje.setText("");
                    }
                }
            });
        }

        public void setMessage(String message){
            response= this.name+":"+message;
        }
        public void setMessageView(TextView textView){
            chatView = textView;
        }
        public void setMessageEditText(EditText editText){

            mensaje = editText;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                Looper.prepare();
                socket = new Socket(dstAddress, dstPort);
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStreamReader);
                printWriter.println(response);
                handler = new Handler();
                usuarios=new ArrayList<String>();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true){
                try {
                    Thread.sleep(1000);
                    mensajes = reader.readLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            incluir_usuario=true;
                            if(mensajes.split(":")[2].compareTo("Connect")==0){
                                for(int u=0;u<usuarios.size();u++){
                                    if(usuarios.get(u).compareTo(mensajes.split(":")[0])==0){
                                        incluir_usuario=false;
                                        break;
                                    }else{
                                        incluir_usuario=true;
                                    }
                                }
                                if(incluir_usuario){
                                    usuarios.add(mensajes.split(":")[0]);
                                }
                            }
                            Toast toast_tmp = Toast.makeText(getApplicationContext(),usuarios.toString(), Toast.LENGTH_LONG);
                            toast_tmp.show();
                            if(context != null){
                                if(listView != null){
                                    arrayAdapter = new ArrayAdapter<String>(context,R.layout.users,usuarios);
                                    listView.setAdapter(arrayAdapter);
                                }
                            }
                            cadena_mensajes+=mensajes+"\n";
                            if(chatView!=null){
                                chatView.setText(cadena_mensajes);
                            }
                            Toast toast = Toast.makeText(getApplicationContext(),"Envié: "+mensajes+"\n"+"Recibí: "+JoinActivity.myClient.getMensaje()+"\n", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
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

