package com.tec.grafmessage;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements DeviceListFragment.OnFragmentInteractionListener  {

    private DeviceListFragment mDeviceListFragment;
    private BluetoothAdapter BTAdapter;
    public static int REQUEST_BLUETOOTH = 1;
    ArrayList<DeviceItem> deviceItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        // Phone does not support Bluetooth so let the user know and exit.
        if (BTAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_BLUETOOTH);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mDeviceListFragment = DeviceListFragment.newInstance(BTAdapter);
        fragmentManager.beginTransaction().replace(R.id.container, mDeviceListFragment).commit();

        setContentView(R.layout.activity_main);
    }

    private void gotoJoinActivity() {
        Intent intent=new Intent(MainActivity.this,JoinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void cerrarSesion(View view) {
        Intent intent=new Intent(this,ChatActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
