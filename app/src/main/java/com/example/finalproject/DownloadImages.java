package com.example.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class DownloadImages extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar tBar;
    private static DrawerLayout drawer;
    private static NavigationView navView;
    private static Button saveButton;
    private static ProgressBar progBar;
    private static URL nasaURL;
    private static URL imageURL;
    private static InputStream inputStream;
    private static InputStream inputStreamPic;
    private static String NASAURL = "https://api.nasa.gov/planetary/apod?api_key=O9o5jg2qGMWJaCrVbyHAPApXWT2Klgzc6nr9YzW6&date=";
    private static String selectedDate;
    private static Bitmap nasaPic;
    private static ImageView nasaImage;
    private static TextView nasaTitle;
    private static TextView nasaDate;
    private static TextView nasaLink;
    private static TextView nasaHDUrl;
    private static String imgDate;
    private static String imgurl;
    private static String hdurl;
    private static String title;
    private static String description;
    private static String copyright;
    private static NASAImage nasaImageInfo;
    private static long newID;
    private MyOpener myOpener;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        //Load the objects
        nasaImage = findViewById(R.id.nasaImage);
        nasaTitle = findViewById(R.id.nasaTitle);
        nasaDate = findViewById(R.id.nasaDate);
        nasaLink = findViewById(R.id.nasaURL);
        nasaHDUrl = findViewById(R.id.nasaHD);
        saveButton = findViewById(R.id.saveBtn);

        //Load the progress bar and setting the color
        progBar = findViewById(R.id.progressBar);
        progBar.getProgressDrawable().setColorFilter(
                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

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

        //Creating an object of the MyOpener class
        myOpener = new MyOpener(this);

        //The on click listener for the save button. This takes the current picture and saves the image to the directory.
        //This method also inserts the details about the image into the database
        saveButton.setOnClickListener(v -> {

            File dir = getFilesDir();
            String fileName = title + ".jpeg";
            File imgFile = new File(dir, fileName);

            try {

                //If the image already exists, then display a message
                if (imgFile.exists()) {

                    Snackbar.make(v, "Picture: '" + title + "' was previously saved.", Snackbar.LENGTH_LONG).show();

                    //If the image does not exist, then continue with saving
                } else {

                    //Creating a new file and saving it to the device file explorer
                    FileOutputStream fOs = openFileOutput(fileName, Context.MODE_PRIVATE);
                    nasaPic.compress(Bitmap.CompressFormat.JPEG, 80, fOs);
                    Snackbar.make(v, "Picture: '" + title + "' has been saved.", Snackbar.LENGTH_LONG).show();

                    //Adding the image details to the database
                    newID = myOpener.insertData(title, imgDate, imgurl, hdurl, copyright, description);

                    //If the image detailed are inserted into the database, display a log message
                    if (newID == -1) {
                        Log.d("Image Database", "Data not inserted.");

                    } else {
                        Log.d("Image Database", "Data  inserted.");
                    }

                    fOs.flush();
                    fOs.close();
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        });


    }

    //Method to select items from the toolbar with a toast message
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch (item.getItemId()) {

            case R.id.savedPicturesList:
                Intent home = new Intent(this, ImageList.class);
                message = "Downloaded NASA images..";
                startActivity(home);
                break;
            case R.id.help:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("HELP")
                        .setMessage("In order to select a picture, please click the date button and click the desired date. The picture and it's details will populate. If you'd like to save a copy of this picture, please select the 'Save Picture' button. To view saved pictures, click the downloaded folder in the top right next to the HELP icon.")
                        .create()
                        .show();

                message = "HELP is coming...";
                break;
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        return true;
    }

    //Method that inflates the toolbar
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

        switch (item.getItemId()) {
            case R.id.home:
                Intent home = new Intent(this, MainActivity.class);
                message = "Home";
                startActivity(home);
                break;
            case R.id.about:
                Intent NASA = new Intent(this, AboutNASA.class);
                message = "About NASA";
                startActivity(NASA);
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

    //DatePicker class that includes a fragment, and executes the Asynctask once a date is selected
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            selectedDate = year + "-" + month + "-" + day;
            Log.d("selectedDate", selectedDate);
            NASAImages nasaImages = new NASAImages();
            nasaImages.execute(NASAURL + selectedDate);

        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    //AsyncTask class that opens a connection to the NASA image url, creates a string from the inputstream and
    //creates a JSON object. Specific strings are obtained from the object and stored into variables. A connection to the date
    //URL is then set into the inputstream and the picture is set to a Bitmap and then to the imageview.

    private static class NASAImages extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            try {
                nasaURL = new URL(strings[0]);

                HttpURLConnection connection = (HttpURLConnection) nasaURL.openConnection();

                inputStream = new BufferedInputStream(connection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }

                String results = sb.toString();

                JSONObject img = new JSONObject(results);

                imgDate = img.getString("date");
                imgurl = img.getString("url");
                hdurl = img.getString("hdurl");
                title = img.getString("title");
                copyright = img.getString("copyright");
                description = img.getString("explanation");

                nasaImageInfo = new NASAImage(imgDate, imgurl, hdurl, title, copyright, description);

                imageURL = new URL(imgurl);

                connection = (HttpURLConnection) imageURL.openConnection();

                inputStreamPic = new BufferedInputStream(connection.getInputStream());

                reader = new BufferedReader(new InputStreamReader(inputStreamPic, "UTF-8"), 8);

                nasaPic = BitmapFactory.decodeStream(inputStreamPic);

                //For loop for the thread/progress of the progress bar
                for (int i = 0; i < 100; i++) {
                    try {
                        publishProgress(i);
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        //Sets the progress bar to the appropriate value
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progBar.setProgress(values[0]);


        }

        //Post execute sets the imagebitmap to the image obtained and sets the appropriate textviews with information about the picture,
        //including a link to the HD url.
        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            nasaImage.setImageBitmap(nasaPic);
            nasaDate.setText("Date: " + imgDate);
            nasaTitle.setText(title);
            nasaLink.setText("URL: " + imgurl);
            nasaHDUrl.setText("Click here to see the HD picture online: " + hdurl);
        }
    }
}
