package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;


public class ImageStorage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView textview;
    private Toolbar tBar;
    private DrawerLayout drawer;
    private NavigationView navView;
    private ListView listView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_storage);

        textview = findViewById(R.id.textViewName);
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        textview.setText(name + "'s downloaded pictures");
        Log.d("name", name);

        listView = findViewById(R.id.savedPicturesList);

        tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tBar.setNavigationIcon(R.drawable.icons8_nasa_48);

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);


        File dir = getFilesDir();
        File[] fileList = dir.listFiles();
        String[] getFiles = new String[fileList().length];
        for (int i = 0; i < getFiles.length; i++) {
            getFiles[i] = fileList[i].getName();
            Log.d("Files", fileList[i].getName());

        }

      //  ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getFiles);
      //  listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId()) {

            case R.id.savedPicturesList:
                Intent home = new Intent(this, ImageStorage.class);
                message = "Downloaded NASA images..";
                startActivity(home);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("HELP")
                        .setMessage("The list below are the downloaded pictures. If you'd like to delete a picture, please select the file from the list and click delete. ")
                        .create()
                        .show();
                message = "HELP is coming...";
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {
            case R.id.home:
                Intent home = new Intent(this, MainActivity.class);
                message = "Home";
                startActivity(home);
                break;
            case R.id.about:
                Intent nextPage = new Intent(this, AboutNASA.class);
                message = "About NASA";
                startActivity(nextPage);
                break;
            case R.id.image:
                Intent images = new Intent(this, Images.class);
                message = "Images";
                startActivity(images);
                break;
            case R.id.exit:
                message = "Goodbye";
                finishAffinity();
                break;

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
        return false;
    }


}