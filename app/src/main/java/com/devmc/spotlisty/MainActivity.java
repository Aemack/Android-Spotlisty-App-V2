package com.devmc.spotlisty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.devmc.spotlisty.Model.Song;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends Activity {

    private TextView userView;
    private TextView userNameView;
    private TextView followersView;
    private TabItem playlistsTab;
    private TabItem genresTab;
    private TabItem generateTab;
    private TabLayout tabLayout;
    private Song song;

    @Override
    public void onPause(){
        super.onPause();
        // put your code here...
        Log.i("RESUMING>>>",">>>>");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userView = (TextView) findViewById(R.id.user);
        userNameView = (TextView) findViewById(R.id.welcome_name);


        //Nav Tabs
        tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
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



        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY",0);
        userView.setText(sharedPreferences.getString("userid","No User"));
        userNameView.setText(sharedPreferences.getString("display_name",""));

    }
}