package com.devmc.spotlisty.Connectors;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.devmc.spotlisty.Model.GenerationOptions;
import com.devmc.spotlisty.Model.Playlist;
import com.devmc.spotlisty.Model.Song;
import com.devmc.spotlisty.VolleyCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.util.Log;
import android.widget.Toast;


public class GeneratePlaylistService {
    private ArrayList<Song> playlist = new ArrayList<>();
    private ArrayList<Song> finalPlaylist;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String genre;
    private String trackIds;
    private ArrayList<String> recentlyPlayedArray;
    private GenerationOptions generationOptions;
    private int attempts;
    private JSONArray trackArray;

    public GeneratePlaylistService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY",0);
        queue = Volley.newRequestQueue(context);
    }

    public void setGenerationOptions(GenerationOptions generationOptions, VolleyCallBack callBack){
        this.generationOptions = generationOptions;
        switch (generationOptions.getType()){
            case "recent":
                setRecentlyPlayed(callBack);
                break;
            case "genre":
                setGenre(generationOptions.getGenre(),callBack);
                break;
            case "userPlaylist":
                setUsersPlaylistTrackIds(generationOptions.getSeedTracks(),callBack);
                break;
        }
    }


    public ArrayList<Song> getTracks(){
        return playlist;
    }

    public void setRecentlyPlayed(VolleyCallBack callBack){
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played?limit=50";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("items");
            recentlyPlayedArray = new ArrayList<String>();
            for (int n = 0; n < jsonArray.length(); n++){
                try{
                    JSONObject object= jsonArray.getJSONObject(n);
                    JSONObject trackObject = object.getJSONObject("track");
                    recentlyPlayedArray.add(trackObject.getString("id"));

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            Random random = new Random();
            int randInt;
            for (int n = 0; n < 5; n++){
                randInt = random.nextInt(jsonArray.length());
                if (n==0){
                    trackIds = recentlyPlayedArray.get(randInt);
                } else {
                    trackIds = trackIds+","+recentlyPlayedArray.get(randInt);
                }
            }

            callBack.onSuccess();
            //generateTracks(callBack);
        }, error -> {
            //TODO: HANDLE ERROR
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token","");
                String auth = "Bearer "+token;
                headers.put("Authorization",auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }


    public void setGenre(String genre, VolleyCallBack callBack){
        this.genre = genre;
        callBack.onSuccess();
    }

    public void setUsersPlaylistTrackIds(String userTrackIds, VolleyCallBack callBack){
        this.trackIds = userTrackIds;
        callBack.onSuccess();
    }

    public ArrayList<Song> getTracks(final VolleyCallBack callBack){
        return playlist;
    }



    public ArrayList<Song> generateTracks(final VolleyCallBack callBack){
        String endpoint ="https://api.spotify.com/v1/recommendations?";

        String genType = generationOptions.getType();



        if (generationOptions != null){
            if (generationOptions.getSize() > 0){
                endpoint = endpoint + "limit="+generationOptions.getSize();
            } else {
                endpoint = endpoint + "limit=20";
            }

            if (genType.equals("recent")) {
                endpoint = endpoint + "&seed_tracks=" + trackIds;
            } else  if (genType.equals("userPlaylist") ){
                endpoint = endpoint + "&seed_tracks=" + generationOptions.getSeedTracks();
            } else {
                endpoint = endpoint + "&seed_genres=" + genre;
            }

            if (generationOptions.getMode() != null){
                if (generationOptions.getMode().equals("major")){
                    endpoint = endpoint + "&max_mode=1&min_mode=1";
                } else if (generationOptions.getMode().equals("minor")) {
                    endpoint = endpoint + "&max_mode=0&min_mode=0";
                }
            }

            if (generationOptions.getTempo() > 0){
                endpoint = endpoint+ "&target_tempo="+generationOptions.getTempo();
            }

            if (generationOptions.getPopularity() > 0){
                endpoint = endpoint + "&target_popularity="+generationOptions.getPopularity();
            }

            if (generationOptions.getValence() > 0){
                float fl = generationOptions.getValence()/100f;
                endpoint = endpoint + "&target_valence="+fl;
            }

            if (generationOptions.getKey() >= 0){
                endpoint = endpoint+"&max_key="+generationOptions.getKey()+"&min_key="+generationOptions.getKey();
            }

        }

        Log.i("ENDPOINT >>>","="+endpoint);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null, response -> {
            Gson gson = new Gson();
            JSONArray jsonArray = response.optJSONArray("tracks");
            for (int n = 0; n < jsonArray.length(); n++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(n);
                    JSONObject albumObject = object.getJSONObject("album");

                    JSONArray artistsArray = albumObject.getJSONArray("artists");
                    JSONObject artistObject = artistsArray.getJSONObject(0);

                    JSONArray imagesArray = albumObject.getJSONArray("images");
                    JSONObject imageObject = imagesArray.getJSONObject(0);

                    String trackName = object.getString("name");
                    String trackId = object.getString("id");
                    String album = albumObject.getString("name");
                    String artist = artistObject.getString("name");
                    String imageUrl = imageObject.getString("url");

                    Song song = new Song(trackId,trackName,album, artist, imageUrl);
                    playlist.add(song);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            callBack.onSuccess();
        }, error -> {
            //TODO: HANDLE ERROR
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token","");
                String auth = "Bearer "+token;
                headers.put("Authorization",auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return playlist;
    }
}

