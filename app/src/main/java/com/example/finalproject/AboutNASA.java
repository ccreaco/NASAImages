package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class AboutNASA extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declaring all required variables
    private Toolbar tBar;
    private DrawerLayout drawer;
    private NavigationView navView;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Load the toolbar and setting the support action
        tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //Load the drawer and setting the drawer listener to the action bar toggle
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Setting the title of the toolbar and setting it to a icon
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       tBar.setNavigationIcon(R.drawable.icons8_nasa_48);

        //Loading the navigation view and setting the item select listener to this activity
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

    }

    //Method to select items from the toolbar with a toast message
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId()) {

            case R.id.savedPicturesList:
                Intent home = new Intent(this, ImageList.class);
                message = "Downloaded NASA images..";
                startActivity(home);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("HELP")
                        .setMessage("This is the ABOUT NASA page. To download an image, please select the NASA icon and then Images.")
                        .create()
                        .show();
                message = "HELP is coming...";
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        return true;
    }

    //Method that inflates the toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    //On click method for the navigation drawer, with a toast to display what page is next
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {
            case R.id.home:
                Intent nextPage = new Intent(this, MainActivity.class);
                message = "Home";
                startActivity(nextPage);
                break;
            case R.id.about:
                message = "About NASA";
                break;
            case R.id.image:
                Intent images = new Intent(this, DownloadImages.class);
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

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return false;
    }
}