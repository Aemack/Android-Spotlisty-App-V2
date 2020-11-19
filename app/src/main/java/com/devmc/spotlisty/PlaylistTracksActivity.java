package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.GeneratePlaylistService;
import com.devmc.spotlisty.Connectors.PlaylistTracksService;
import com.devmc.spotlisty.Connectors.UserPlaylistsService;
import com.devmc.spotlisty.Model.Playlist;
import com.devmc.spotlisty.Model.Song;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Random;

public class PlaylistTracksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private PlaylistTracksService playlistTracksService;
    private Song song;
    private ArrayList<Song> tracks;
    private String playlistId;
    private String trackIds;

    private TabLayout tabLayout;

    private Button generateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist_tracks_activity);
        recyclerView = findViewById(R.id.playlist_tracks_recycler_view);


        getSupportActionBar().hide();
        //Nav Buttons
        tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(3);
        tab.select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case 1:
                        Intent i = new Intent(getApplicationContext(), GeneratePlaylistActivity.class);
                        i.putExtra("generateType","recent");
                        startActivity(i);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), GenresActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), PlaylistsActivity.class));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                switch(tab.getPosition()) {
                    case 0:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case 1:
                        Intent i = new Intent(getApplicationContext(), GeneratePlaylistActivity.class);
                        i.putExtra("generateType","recent");
                        startActivity(i);
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), GenresActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), PlaylistsActivity.class));
                        break;
                }
            }
        });


        //Generate Button
        generateButton = (Button) findViewById(R.id.generate_from_playlist_button);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), GeneratePlaylistActivity.class);
                trackIds = getTrackIds();
                i.putExtra("generateType","userPlaylist");
                i.putExtra("seedTracks",trackIds);
                startActivity(i);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            playlistId = extras.getString("playlistId");
        }

        playlistTracksService = new PlaylistTracksService(getApplicationContext());
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Call to get tracks
        getTracks();
    }


    private String getTrackIds() {
        String trackIdsString = "";
        Random rand = new Random();
        int iter = 0;
        while (iter < 5){
            int num = rand.nextInt(tracks.size());
            String id = tracks.get(num).getId();
            if (iter == 0){
                trackIdsString = id;
            } else {
                trackIdsString = trackIdsString + "," + id;
            }
            iter ++;
        }
        return trackIdsString;
    }



    //Get tracks from playlistTracksService
    private void getTracks(){

        playlistTracksService.setPlaylistId(playlistId);

        playlistTracksService.getPlaylistTracks(() -> {
            tracks = playlistTracksService.getPlaylistTracks();

            //Call to update playlists
            updateTracks();
        });
    }

    private void updateTracks(){
        // Creates adapter, passing in user playlists
        mAdapter = new com.devmc.spotlisty.TracksAdapter(tracks);




        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }

}
