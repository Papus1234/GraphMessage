package com.tec.grafmessage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class Client extends AsyncTask<Void, Void, Void> {

    BufferedReader reader;
    String dstAddress;
    int dstPort;
    String response = "";
    String name="";
    Socket socket = null;
    PrintWriter printWriter;
    String mensaje="";
    InputStreamReader inputStreamReader;

    Client(String addr, int port,String name){
        this.name=name;
        dstAddress = addr;
        dstPort = port;
    }
    @Override
    public void onPreExecute(){
    }
    public void setMessage(String message){
        this.response = this.name+":"+message;
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            socket = new Socket(dstAddress, dstPort);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
        try {
            Thread.sleep(1000);
            printWriter.println(this.response);
            if (reader.readLine() != mensaje) {
                mensaje += reader.readLine();
            }
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
        return mensaje;
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
        mensaje+="EL hilo se ha cerrado";
    }

}
