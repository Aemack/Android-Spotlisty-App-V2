package com.devmc.spotlisty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devmc.spotlisty.Connectors.GenresService;
import com.devmc.spotlisty.Model.Genre;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class GenresActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Genre> genreSeeds;


    private TabLayout tabLayout;

    private GenresService genresService;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genres_activity);

        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.playlists_recycler_view);

        genresService = new GenresService(getApplicationContext());

        //Nav Buttons
        tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(2);
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
        getGenres();
    }

    public void getGenres(){

        genresService.getGenreSeeds(() -> {
            genreSeeds = genresService.getGenreSeeds();


            updateList();
        });
    }

    public void updateList(){
        mAdapter = new com.devmc.spotlisty.GenresAdapter(genreSeeds);
        // Sets adapter on recycle view
        recyclerView.setAdapter(mAdapter);
    }

}
