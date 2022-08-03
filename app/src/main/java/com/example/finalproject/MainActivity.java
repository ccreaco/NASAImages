package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar tBar;
    private ImageView imageView;
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

        //Load the objects
        addBtn = findViewById(R.id.addBtn);

        //Load the name object and setting the edit text to white
        name = findViewById(R.id.editText);
        name.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

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

        //Getting the shared preferences information
        prefs = this.getSharedPreferences("Username", Context.MODE_PRIVATE);

        /*
        The add button will get the name from the edit text and set it to a string.
        If the string is empty, it will not saved to shared preferences and post a toast
        message asking to enter your name.
        If the string is not blank, it will save the name to shared preferences, and post a toast
        message welcoming the user with the name.
        */
        addBtn.setOnClickListener(v -> {

            nameStr = name.getText().toString();

            if(nameStr.equals("")) {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            } else {
                editor = prefs.edit();
                editor.putString("name", nameStr);
                editor.commit();
                Toast.makeText(this, "Welcome " + nameStr + "!", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        If there is already a name saved in shared preferences, then the edit text will display
        a welcome message with the persons name received from shared preferences.
        */
        String getName = prefs.getString("name", "");

        if(getName != "") {
            name.setText("Welcome back " + getName + "!");
        }

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
                        .setMessage("Please add your name to get started. Select the NASA icon to get the menu.")
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
                message = "Home";
                break;
            case R.id.about:
                Intent nextPage = new Intent(this, AboutNASA.class);
                message = "About NASA";
                startActivity(nextPage);
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

        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
        return false;
    }
}