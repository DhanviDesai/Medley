package com.example.partyplay;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.partyplay.adapters.AddedAdapter;
import com.example.partyplay.adapters.DeviceAdapter;
import com.example.partyplay.util.TrackDetails;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddMusic extends AppCompatActivity implements DeviceAdapter.ItemClickListener {

    //Add music by searching from spotify api
    //Connect to server by adding under a player
    //Bluetooth Client

    public static final int MY_PERMISSIONS_BLUETOOTH =1;
    public static final int MY_PERMISSIONS_BLUETOOTH_ADMIN =2;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION =3;
    public static final int MY_PERMISSIONS_BLUETOOTH_PRIVILIGED=4;
    public static final int MY_ACTIVITY = 8;
    public static final int MY_PERMISSIONS_INTERNET = 6;
    public static UUID MY_UUID;

    private static final String CLIENT_ID = "225aa80622024095bfe9de03a202cd86";
    private static final String REDIRECT_URI = "http://192.168.43.3:5000/";
    private static final int REQUEST_CODE = 1337;
    public static final int REQUEST_ENABLE_BT = 4;
    private RequestQueue mRequestQueue;
    private TextView whoTag,playerName;
    LinearLayout playerTag;
    ImageView searchTag;
    BluetoothAdapter bluetoothAdapter;
    ConstraintLayout layoutMain;
    View maskView;
    String name=null;
    CardView cardView;
    RecyclerView DeviceRecycler;
    static ArrayList<BluetoothDevice> details;
    ImageView greyAdd;
    TextView instruction,addSongs;
    DeviceAdapter adapter;
    AddedAdapter addedAdapter;
    LinearLayout transitLayout;
    ArrayList<String> addedSongs;
    RecyclerView added;
    private String token;
    ArrayList<TrackDetails> trackDetails;
    TextView playerIndicator;
    FloatingActionButton addPlayer;
    CardView cardView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);


        Toolbar tl = findViewById(R.id.toolbarAddMusic);
        tl.setTitle("");
        tl.setSubtitle("");
        setSupportActionBar(tl);

        playerTag = findViewById(R.id.playerTag);
        cardView = findViewById(R.id.cardView);
        playerName = findViewById(R.id.playerName);
        DeviceRecycler = findViewById(R.id.cardRecycler);
        greyAdd = findViewById(R.id.greyAdd);
        instruction = findViewById(R.id.instruction);
        cardView1 = findViewById(R.id.holdingCards);
        details = new ArrayList<>();
        addSongs = findViewById(R.id.textView2);
        addPlayer = findViewById(R.id.addPlayer);
        maskView = findViewById(R.id.maskView);
        MY_UUID = UUID.fromString(getResources().getString(R.string.UUID));
        layoutMain = findViewById(R.id.layoutMain);
        addedSongs= new ArrayList<>();
        added = findViewById(R.id.recyclerView);
        playerIndicator = findViewById(R.id.playerIndicator);
        trackDetails= new ArrayList<>();
        transitLayout = findViewById(R.id.transitLayout);

        adapter = new DeviceAdapter(this,details);
        adapter.setmItemClickListener(this);
        DeviceRecycler.setLayoutManager(new LinearLayoutManager(this));
        DeviceRecycler.setAdapter(adapter);
        addedAdapter = new AddedAdapter(this,trackDetails);
        added.setAdapter(addedAdapter);
        added.setLayoutManager(new LinearLayoutManager(this));

        Intent i;
        i = getIntent();
        if(i.getSerializableExtra("SelectedSong")!=null) {
            Toast.makeText(this, i.getSerializableExtra("SelectedSong").toString(), Toast.LENGTH_SHORT).show();
        }

        toggleVisibility(name==null,addedSongs == null);


        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        permissions();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        onBluetooth();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
//        registerReceiver(pairingReceiver,filter1);

//        cardView.post(new Runnable() {
//            @Override
//            public void run() {
//                clickedSearch();
//            }
//        });




        mRequestQueue = Volley.newRequestQueue(this);
    }

    private final  BroadcastReceiver pairingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_PAIRING_REQUEST.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String ble_pin="1234";
                device.setPin(ble_pin.getBytes());
                device.createBond();
            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "In Resume", Toast.LENGTH_SHORT).show();
        Intent i = getIntent();
        if(i.getSerializableExtra("SelectedSong")!=null){
            Toast.makeText(this, i.getSerializableExtra("SelectedSong").toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = getIntent();
        if(i.getSerializableExtra("SelectedSong")!=null){
            Toast.makeText(this, i.getSerializableExtra("SelectedSong").toString(), Toast.LENGTH_SHORT).show();
        }
    }



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                details.add(device);
                adapter.notifyDataSetChanged();
                //Toast.makeText(context, deviceName, Toast.LENGTH_SHORT).show();

            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        onBluetooth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                   // Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
                    Log.i("Token",response.getAccessToken());
                    token = response.getAccessToken();
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }

        if(requestCode == MY_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(this, "InResult", Toast.LENGTH_SHORT).show();
                if (data.getSerializableExtra("SelectedSong") != null) {
                   // Toast.makeText(this, data.getSerializableExtra("SelectedSong").toString(), Toast.LENGTH_SHORT).show();
                    gotSong((TrackDetails) data.getSerializableExtra("SelectedSong"));
                }
            }
        }
    }



    public class ConnectThread extends  Thread {
        private final BluetoothSocket mmServerSocket;
        private final BluetoothDevice mmDevice;

         ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;
            //device.setPairingConfirmation(true);
            try {
                tmp= device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e("Connection","Sockets listen() failed",e);
            }
            mmServerSocket =  tmp;
        }

        public void run(){
           // bluetoothAdapter.cancelDiscovery();
                try {
                     mmServerSocket.connect();
                } catch (IOException e) {
                    Log.e("Connection","Socket formation failed",e);
                    return;
                }

                manageMyConnectedSocket(mmServerSocket);
        }

    }


    public void manageMyConnectedSocket(BluetoothSocket socket){
        unregisterReceiver(pairingReceiver);

    }


    @SuppressLint("RestrictedApi")
    public void clickedSearch(View view) {
        view.setVisibility(View.GONE);
        int x = cardView.getWidth()/2;
        int y = cardView.getHeight()/2;
        int startRadius = 0;
        int endRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(cardView, x, y, startRadius, endRadius);

        maskView.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);
        anim.start();
        bluetoothAdapter.startDiscovery();

    }

    @Override
    public void onItemClick(int i) {
        bluetoothAdapter.cancelDiscovery();
        //unregisterReceiver(receiver);
        int x = cardView.getWidth()/2;
        int y = cardView.getHeight()/2;
        int endRadius = 0;
        int startRadius = (int) Math.hypot(layoutMain.getWidth(), layoutMain.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(cardView, x, y, startRadius, endRadius);
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                maskView.setVisibility(View.GONE);
                cardView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
        //cardView.setVisibility(View.GONE);
        BluetoothDevice device = details.get(i);
        name = device.getName();
        toggleVisibility(name==null,addedSongs==null);
        ConnectThread thread = new ConnectThread(device);
        thread.run();


    }


    private void gotSong(TrackDetails track){

        toggleVisibility(name==null,addedSongs == null);
        trackDetails.add(track);
        addedAdapter.notifyDataSetChanged();

    }

    @SuppressLint("RestrictedApi")
    private void toggleVisibility(boolean condition, boolean songs){

        if(name==null && addedSongs.size()>0){
            Log.i("In Start","In first Condition");
            playerIndicator.setVisibility(View.GONE);
            addPlayer.setVisibility(View.VISIBLE);
            addSongs.setVisibility(View.GONE);
            cardView1.setVisibility(View.GONE);
        }
        else if(name!=null && addedSongs.size()>0){
            Log.i("In Start","In second Condition");
            playerIndicator.setVisibility(View.VISIBLE);
            playerName.setText(name);
            addPlayer.setVisibility(View.GONE);
            transitLayout.setVisibility(View.VISIBLE);
            greyAdd.setVisibility(View.VISIBLE);
            instruction.setVisibility(View.VISIBLE);
        }
        else {
            Log.i("In Start","In third Condition");
            addSongs.setVisibility(View.VISIBLE);
            cardView1.setVisibility(View.VISIBLE);
        }

    }

    private void onBluetooth(){
        if (bluetoothAdapter != null) {
            if(!bluetoothAdapter.isEnabled()){
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void permissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    MY_PERMISSIONS_BLUETOOTH);
        }
        else{
           // Toast.makeText(this, "Permission Bluetooth given", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
        else{
           // Toast.makeText(this, "Permission Coarse given", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                    MY_PERMISSIONS_BLUETOOTH_ADMIN);
        }
        else{
           // Toast.makeText(this, "Permission Admin given", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},MY_PERMISSIONS_INTERNET);
        }
    }


    public void searchSongs(View view) {
        LinearLayout transit = findViewById(R.id.transitLayout);


        Intent i = new Intent(this,SearchActivity.class);
        i.putExtra("Token",token);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,transit,"transitLayout");
        startActivityForResult(i,MY_ACTIVITY,options.toBundle());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MY_PERMISSIONS_BLUETOOTH :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Toast.makeText(this, "Bluetooth accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Bluetooth rejected", Toast.LENGTH_SHORT).show();
                }
                break ;
            case MY_PERMISSIONS_BLUETOOTH_ADMIN :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   // Toast.makeText(this, "Bluetooth Admin accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Bluetooth Admin rejected", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION :
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   // Toast.makeText(this, "Location Accepted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Location rejected", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_INTERNET:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   // Toast.makeText(this, "Internet Permission given", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Internt not given", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_music_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.changePlayer){
            Toast.makeText(this,"Changing Player",Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
