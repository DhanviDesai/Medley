package com.example.partyplay;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyplay.adapters.ResultsAdapter;
import com.example.partyplay.util.AlbumDetails;
import com.example.partyplay.util.ArtistDetails;
import com.example.partyplay.util.PlaylistDetails;
import com.example.partyplay.util.TrackDetails;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements ResultsAdapter.ItemClickListener {
    RequestQueue mRequestQueue;
    private String token;
    ArrayList<TrackDetails> trackDetails;
    ArrayList<AlbumDetails> albumDetails;
    ArrayList<ArtistDetails> artistDetails;
    ArrayList<PlaylistDetails> playlistDetails;
    EditText search;
    RecyclerView recyclerView;
    ImageView imageView;
    TextView textView;
     ResultsAdapter adapter;
     ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i = getIntent();
        token = i.getStringExtra("Token");
        //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();

        trackDetails = new ArrayList<>();
        albumDetails = new ArrayList<>();
        artistDetails = new ArrayList<>();
        playlistDetails = new ArrayList<>();

        recyclerView = findViewById(R.id.resultsRecycler);
        imageView  = findViewById(R.id.imageView);
        textView = findViewById(R.id.textInstruction);
        progressBar= findViewById(R.id.progressBar);

        mRequestQueue = Volley.newRequestQueue(this);

        search = findViewById(R.id.searchText);

        adapter = new ResultsAdapter(this,albumDetails,trackDetails,artistDetails,playlistDetails);
        recyclerView.setAdapter(adapter);
        adapter.setmItemClickListener(SearchActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("Before Text Changec");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("on Text Changec");
                progressBar.setVisibility(View.VISIBLE);

                getData(s.toString());
                imageView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                System.out.println("Size = "+trackDetails.size());


            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("After Text Changec");

            }
        });

    }

    private void getData(String search){
        albumDetails.clear();
        trackDetails.clear();
        playlistDetails.clear();
        artistDetails.clear();
        String base_url = getResources().getString(R.string.base_url);
        try {
            search = URLEncoder.encode(search, "UTF-8");
            String url = base_url + "q=" + search + "&type=track,album,playlist&limit=10";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {


                    try {
                        JSONObject track = (JSONObject) response.get("tracks");
                        JSONArray tracks_array = (JSONArray) track.get("items");
                        for(int i=0;i<tracks_array.length();i++){
                            String image640=null;
                            String image300 = null;
                            String image64 = null;
                            JSONObject obj = tracks_array.getJSONObject(i);
                            JSONObject album = obj.getJSONObject("album");
                            String album_name=album.getString("name");
                            String album_href = album.getString("href");
                            String album_uri = album.getString("uri");
                            String artists=null;
                            JSONArray track_album_artist = album.getJSONArray("artists");
                            if(track_album_artist.length()>1){
                                artists = "Various artists";
                            }
                            JSONArray images = album.getJSONArray("images");
                            for(int j=0;j<images.length();j++){
                                JSONObject image = images.getJSONObject(j);
                                if(image.getInt("height") == 640 && image.getInt("width") == 640){
                                    image640 = image.getString("url");
                                }
                                if(image.getInt("height") == 300 && image.getInt("width") == 300){
                                    image300 = image.getString("url");
                                }
                                if(image.getInt("height") == 64 && image.getInt("width") == 64){
                                    image64 = image.getString("url");
                                }
                            }
                            String track_name = obj.getString("name");
                            int durationms = obj.getInt("duration_ms");
                            String track_uri = obj.getString("uri");
                            String type = obj.getString("type");
                            TrackDetails d = new TrackDetails(track_name,image640,image300,image64,album_href,album_name,artists
                                    ,null,track_uri,album_uri,null,durationms,type);
                            trackDetails.add(d);

                        }

                        JSONObject albums = response.getJSONObject("albums");
                        JSONArray albums_items = albums.getJSONArray("items");
                        for(int a =0;a<albums_items.length();a++){
                            String image640=null;
                            String image300 = null;
                            String image64 = null;
                            JSONObject obj = albums_items.getJSONObject(a);
                            String album_name= obj.getString("name");
                            String album_uri = obj.getString("uri");
                            String album_href = obj.getString("href");
                            String type = obj.getString("type");
                            JSONArray album_images = obj.getJSONArray("images");
                            for(int i = 0;i<album_images.length();i++){
                                JSONObject image = album_images.getJSONObject(i);
                                if(image.getInt("height") == 640 && image.getInt("width") == 640){
                                    image640 = image.getString("url");
                                }
                                if(image.getInt("height") == 300 && image.getInt("width") == 300){
                                    image300 = image.getString("url");
                                }
                                if(image.getInt("height") == 64 && image.getInt("width") == 64){
                                    image64 = image.getString("url");
                                }

                            }

                            AlbumDetails oneAlbum = new AlbumDetails(album_href,album_uri,image640,image300,image64,album_name,type);
                            albumDetails.add(oneAlbum);
                        }




                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + token);
                    return params;

                }
            };

            mRequestQueue.add(request);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if(aint!=-1) {
            Intent i = new Intent();
            i.putExtra("SelectedSong", trackDetails.get(aint));
            setResult(Activity.RESULT_OK, i);
        }
        super.onBackPressed();
    }

    int aint=-1;

    @Override
    public void onItemClick(int position) {
        aint = position;
        onBackPressed();


    }
}
