package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.UserPlaylistsService;
import com.devmc.spotlisty.Model.Playlist;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PlaylistsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private UserPlaylistsService userPlaylistService;
    private ArrayList<Playlist> userPlaylists;

    //Nav Buttons
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_playlists_activity);
        getSupportActionBar().hide();
        recyclerView = (RecyclerView) findViewById(R.id.playlists_recycler_view);
        userPlaylistService = new UserPlaylistsService(getApplicationContext());

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


        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //Call to get playlists
        getPlaylists();
    }

    //Get playlists from userPlaylistService
    private void getPlaylists(){
        userPlaylistService.getUserPlaylists(() -> {
            userPlaylists = userPlaylistService.getUserPlaylists();

            //Call to update playlists
            updatePlaylists();
        });
    }

    private void updatePlaylists(){
        // Creates adapter, passing in user playlists
        mAdapter = new com.devmc.spotlisty.PlaylistAdapter(userPlaylists);



        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }
}
