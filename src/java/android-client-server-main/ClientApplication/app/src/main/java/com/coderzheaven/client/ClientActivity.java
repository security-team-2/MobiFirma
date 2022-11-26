package com.coderzheaven.client;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SERVER_PORT = 9999;
    public static final String SERVER_IP = "10.0.2.2";

    private ClientThread clientThread;
    private Thread thread;
    private LinearLayout msgList;
    private Handler handler;
    private int clientTextColor;
    private EditText edMessage;

    private CheckBox cSabanas;
    private CheckBox cCamas;
    private CheckBox cMesas;
    private CheckBox cSillas;
    private CheckBox cSillones;

    private EditText tSabanas;
    private EditText tCamas;
    private EditText tMesas;
    private EditText tSillas;
    private EditText tSillones;

    private String employeeId;
    private byte[] privateKey = new byte[32];

    private Map<String,Integer> pedido= new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setTitle("Client");
        clientTextColor = ContextCompat.getColor(this, R.color.green);
        handler = new Handler();
        edMessage = findViewById(R.id.edMessage);

        cSabanas = (CheckBox) findViewById(R.id.checkBox_sabanas);
        cCamas = (CheckBox) findViewById(R.id.checkBox_camas);
        cMesas = (CheckBox) findViewById(R.id.checkBox_mesas);
        cSillas = (CheckBox) findViewById(R.id.checkBox_sillas);
        cSillones = (CheckBox) findViewById(R.id.checkBox_sillones);

        tSabanas = findViewById(R.id.editNumber_sabanas);
        tCamas = findViewById(R.id.editNumber_camas);
        tMesas = findViewById(R.id.editNumber_mesas);
        tSillas = findViewById(R.id.editNumber_sillas);
        tSillones = findViewById(R.id.editNumber_sillones);

        employeeId = "3a951933a06c";

        readFile();

        try{

            clientThread = new ClientThread();
            thread = new Thread(clientThread);
            thread.start();

        }catch (Exception e){
            System.out.println(e);
        }

    }

    public TextView textView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        String m = message + " [" + getTime() +"]";
        TextView tv = new TextView(this);
        tv.setTextColor(color);
        tv.setText(m);
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    private void hideConnectServerBtn(){
        //handler.post(() -> findViewById(R.id.btnConnectServer).setVisibility(View.GONE));
    }

    public void showMessage(final String message, final int color) {
        //handler.post(() -> msgList.addView(textView(message, color)));
    }

    private void removeAllViews(){
        handler.post(() -> msgList.removeAllViews());
    }

    public void readFile(){
        File fichero = new File("privateK.txt");
        Scanner s = null;

        try {
            // Leemos el contenido del fichero
            s = new Scanner(fichero);

            String linea = null;
            while (s.hasNextLine()) {
                linea = s.nextLine();
                // Imprimimos la linea
            }
            privateKey = linea.getBytes();


        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {
            try {
                if (s != null)
                    s.close();
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.send_data) {

            String pp = edMessage.getText().toString().trim();
            Integer sabanasMessage = null;
            Integer camasMessage = null;
            Integer sillasMessage = null;
            Integer sillonesMessage = null;
            Integer mesasMessage = null;
            boolean t = true;

            if(cSabanas.isChecked() & !TextUtils.isEmpty(tSabanas.getText())){
                sabanasMessage = Integer.valueOf(tSabanas.getText().toString().trim());
                if (sabanasMessage<0 | sabanasMessage>300){
                    t = false;
                }else{
                    pedido.put("sabanas",sabanasMessage);
                }
            }
            if(cCamas.isChecked() & !TextUtils.isEmpty(tCamas.getText())){
                camasMessage = Integer.valueOf(tCamas.getText().toString().trim());
                if (camasMessage<0 | camasMessage>300){
                    t = false;
                }else{
                    pedido.put("camas",camasMessage);
                }
            }
            if(cMesas.isChecked() & !TextUtils.isEmpty(tMesas.getText())){
                mesasMessage = Integer.valueOf(tMesas.getText().toString().trim());
                if (mesasMessage<0 | mesasMessage>300){
                    t = false;
                }else{
                    pedido.put("mesas", mesasMessage);
                }
            }
            if(cSillas.isChecked() & !TextUtils.isEmpty(tSillas.getText())){
                sillasMessage = Integer.valueOf(tSillas.getText().toString().trim());
                if (sillasMessage<0 | sillasMessage>300){
                    t = false;
                }else{
                    pedido.put("sillas", sillasMessage);
                }
            }
            if(cSillones.isChecked() & !TextUtils.isEmpty(tSillones.getText())){
                sillonesMessage = Integer.valueOf(tSillones.getText().toString().trim());
                if (sillonesMessage<0 | sillonesMessage>300){
                    t = false;
                }else{
                    pedido.put("sillones",sillonesMessage);
                }
            }

            if (null != clientThread) {
                //clientThread.sendMessage(pp);
                if((sabanasMessage!=null | camasMessage!=null | sillasMessage!=null | sillonesMessage!=null | mesasMessage!=null)&t){
                    try{
                        String data = employeeId + String.valueOf(pedido);

                        clientThread.sendMessage(employeeId +" | "+ String.valueOf(pedido)+ " | Firma");
                    } finally {
                        Toast.makeText(ClientActivity.this, "Query sent correctly", Toast.LENGTH_SHORT).show();
                    }

                }else if(!t){
                    Toast.makeText(ClientActivity.this, "The maximum is 300", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Select at least one element and the quantity", Toast.LENGTH_SHORT).show();
                }



            }
        }
    }

    class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                //Here we are creating the socket for the connection
                socket = new Socket(serverAddr, SERVER_PORT);

                while (!Thread.currentThread().isInterrupted()) {
                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (null == message || "Disconnect".contentEquals(message)) {
                        boolean interrupted = Thread.interrupted();
                        message = "Server Disconnected: " + interrupted;
                        //showMessage(message, Color.RED);
                        break;
                    }
                    //showMessage("Server: " + message, clientTextColor);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(() -> {
                try {
                    if (null != socket) {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }

    String getTime() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }
}