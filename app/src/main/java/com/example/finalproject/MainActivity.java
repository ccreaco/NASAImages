package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar tBar;
    private DrawerLayout drawer;
    private NavigationView navView;
    private EditText name;
    private Button addBtn;
    private String nameStr;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editText);
        addBtn = findViewById(R.id.addBtn);

        name.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

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

        prefs = this.getSharedPreferences("Username", Context.MODE_PRIVATE);

        addBtn.setOnClickListener(v -> {

            nameStr = name.getText().toString();
            editor = prefs.edit();
            editor.putString("name", nameStr);
            editor.commit();
            Toast.makeText(this, "Welcome " + nameStr + "!", Toast.LENGTH_SHORT).show();

        });

        String getName = prefs.getString("name", "");

        if(getName != "") {
            name.setText("Welcome back " + getName + "!");
        }

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
                        .setMessage("TO BE EDITED")
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
                message = "Home";
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