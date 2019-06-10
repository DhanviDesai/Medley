package com.example.partyplay;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.io.IOException;
import java.util.UUID;

public class PlayMusic extends AppCompatActivity {
    public static final int MY_PERMISSIONS_BLUETOOTH =1;
    public static final int MY_PERMISSIONS_BLUETOOTH_ADMIN =2;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION =3;
    public static final int MY_PERMISSIONS_BLUETOOTH_PRIVILIGED=5;
    public static final int REQUEST_ENABLE_BT = 4;
    private UUID MY_UUID;
    BluetoothAdapter bluetoothAdapter;
    private static final String CLIENT_ID = "225aa80622024095bfe9de03a202cd86";
    private static final String REDIRECT_URI = "http://192.168.43.3:5000/";
    private SpotifyAppRemote mSpotifyAppRemote;


    //Spotify SDK for playing songs on spotify
    //Bluetooth to advertise that the device is the player

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        //Toast.makeText(this, "Play Music Acticity", Toast.LENGTH_SHORT).show();

        MY_UUID = UUID.fromString(getResources().getString(R.string.UUID));

        permissions();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        onBluetooth();

        Toolbar tl = findViewById(R.id.toolbar);
        setSupportActionBar(tl);

        final RippleBackground bg = findViewById(R.id.content);
       // ImageView im = findViewById(R.id.centerImage);
//        im.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bg.startRippleAnimation();
//                Log.i("Animation","Started Animation");
////                AcceptThread thread = new AcceptThread();
////                thread.run();
//            }
//        });



    }

    public class AcceptThread extends  Thread {
        private final BluetoothServerSocket mmServerSocket;

        AcceptThread(){
            BluetoothServerSocket tmp = null;
                try {
                    tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("PartyPlay",MY_UUID);
                } catch (IOException e) {
                    Log.e("Connection","Couldnt listen",e);
                }

            mmServerSocket = tmp;
        }

        public void run(){
            BluetoothSocket socket = null;
            while(true){
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e("Socket","Couldnt accept",e);
                }

                if(socket!=null){
                    manageConnection(socket);
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e("Socket","Couldnt close",e);
                    }
                    break;
                }
            }
        }

    }


    void manageConnection(BluetoothSocket socket){

    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.i("MainActivity", "Connected! Yay!");
                            spotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX7K31D69s4M1");
                        // Now you can start interacting with App Remote
                        Toast.makeText(PlayMusic.this, "Connected", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
        //mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

    }

    private void onBluetooth(){
        if (bluetoothAdapter != null) {
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT :
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, "Bluetooth switched on", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Bluetooth switched off", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        onBluetooth();
    }

    private void permissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    MY_PERMISSIONS_BLUETOOTH);
        }
        else{
            Toast.makeText(this, "Permission Bluetooth given", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
        else{
            Toast.makeText(this, "Permission Coarse given", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    MY_PERMISSIONS_BLUETOOTH_ADMIN);
        }
        else{
            Toast.makeText(this, "Permission Admin given", Toast.LENGTH_SHORT).show();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_PRIVILEGED) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED},
                    MY_PERMISSIONS_BLUETOOTH_PRIVILIGED);
        }
        else{
            Toast.makeText(this, "Permission Admin given", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_BLUETOOTH :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Bluetooth accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Bluetooth rejected", Toast.LENGTH_SHORT).show();
                }
                break ;
            case MY_PERMISSIONS_BLUETOOTH_ADMIN :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Bluetooth Admin accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Bluetooth Admin rejected", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Location Accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Location rejected", Toast.LENGTH_SHORT).show();
                }
                case MY_PERMISSIONS_BLUETOOTH_PRIVILIGED :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Priviliged Accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Priviliged rejected", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
