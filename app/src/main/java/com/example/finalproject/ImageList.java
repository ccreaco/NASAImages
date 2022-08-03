package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class ImageList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Declaring all required variables
    private TextView textview;
    private TextView emptyView;
    private Toolbar tBar;
    private DrawerLayout drawer;
    private NavigationView navView;
    private ListView listView;
    private ImageView imageView;
    private ArrayList<String> fileList = new ArrayList<>();
    private SQLiteDatabase db;
    private MyOpener myOpener = new MyOpener(this);
    private NASAImage ns = new NASAImage();
    private String title;
    private String imgDate;
    private String imgurl;
    private String hdurl;
    private String description;
    private String copyright;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_downloads);

        //Load the objects
        textview = findViewById(R.id.textViewName);
        emptyView = findViewById(R.id.empty);
        listView = findViewById(R.id.image_list);
        imageView = findViewById(R.id.image_download);

        //Getting the text that is saved in shared preferences and setting it to the textview
        SharedPreferences sp = getApplicationContext().getSharedPreferences("Username", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        textview.setText(name + "'s downloaded pictures");

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

        //Setting the swipe refresh to the layout
        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
        refresher.setOnRefreshListener( () -> refresher.setRefreshing(false)  );

        //Getting the files list from the directory and adding it to an array
        File dir = getFilesDir();
        File[] files = dir.listFiles();
        String[] getFiles = new String[fileList().length];
        for (int i = 0; i < getFiles.length; i++) {
            fileList.add(getFiles[i] = files[i].getName());
        }

        //An array adapter that will take in the filesList array and set it to the list view
        //The emptyview will set a specific text if the list is empty.
        ArrayAdapter<String> directoryList = new ArrayAdapter<>(this, R.layout.listview_layout, fileList);
        listView.setAdapter(directoryList);
        listView.setEmptyView(emptyView);

        /*
        On item click listener for the listview that will allow a user to open a picture,
        or delete a picture. When an item is selected an alert dialog box will populate and display
        the information about the picture that was saved to the database.
        When a picture is deleted, it will be deleted from the directory as well as the information,
        from the database will be removed.
        */

        listView.setOnItemClickListener((p, b, pos, id) -> {

            String n = fileList.get(pos);
            File imgFile = new File(dir, n);

            loadDataFromDatabase();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(title)
                    //set message with details loaded from the database
                    .setMessage("Title: " + title +
                                "\nDate: " + imgDate +
                                "\nDescription: " + description)

                    //will open up a page to view the photo
                    .setPositiveButton("OPEN", (click, arg) -> {

                       try {
                            FileInputStream input = new FileInputStream(imgFile);
                            Bitmap image = BitmapFactory.decodeStream(input);
                            imageView.setImageBitmap(image);
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    })
                    //will delete the photo
                    .setNegativeButton("DELETE", (click, arg) -> {

                        boolean dbDelete = myOpener.deleteImage(title);
                        boolean fileDelete = imgFile.delete();
                        if (dbDelete || fileDelete) {
                            Snackbar.make(b, "Image: '" + title + "' has been deleted.", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(b, "Image: '" + title + "' was not deleted.", Snackbar.LENGTH_LONG).show();
                        }
                      directoryList.remove(n);
                        directoryList.notifyDataSetChanged();

                    imageView.setImageBitmap(null);


                    })
                    //will return back to main page
                    .setNeutralButton("CANCEL", (click, arg) ->{

                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);

        });


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
                        .setMessage("The list below are the downloaded pictures. Click a picture to view! A prompt will ask if you'd like to delete a picture. ")
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

    /*
    Method that calls the myOpener class, gets a connection to the database,
    then gets all of the information from the database and saves them to variables.
     */
    private void loadDataFromDatabase() {

        db = myOpener.getWritableDatabase();
        Cursor results = db.rawQuery("SELECT * FROM " + myOpener.TABLE_NAME + ";", null);

        int ColIndex = results.getColumnIndex(myOpener.COL_1);
        int titleColIndex = results.getColumnIndex(myOpener.COL_2);
        int imgdateColIndex = results.getColumnIndex(myOpener.COL_3);
        int imgurlColIndex = results.getColumnIndex(myOpener.COL_4);
        int hdurlColIndex = results.getColumnIndex(myOpener.COL_5);
        int copyrightColIndex = results.getColumnIndex(myOpener.COL_6);
        int descriptionColIndex = results.getColumnIndex(myOpener.COL_7);

        while (results.moveToNext()) {
            long id = results.getLong(ColIndex);
            title = results.getString(titleColIndex);
            imgDate = results.getString(imgdateColIndex);
            imgurl = results.getString(imgurlColIndex);
            hdurl = results.getString(hdurlColIndex);
            copyright = results.getString(copyrightColIndex);
            description = results.getString(descriptionColIndex);


        }


    }


    }

