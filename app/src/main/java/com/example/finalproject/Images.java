package com.example.finalproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
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


public class Images extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    private static String imgurl ;
    private static String hdurl;
    private static String title;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        nasaImage = findViewById(R.id.nasaImage);
        nasaTitle = findViewById(R.id.nasaTitle);
        nasaDate = findViewById(R.id.nasaDate);
        nasaLink = findViewById(R.id.nasaURL);
        nasaHDUrl = findViewById(R.id.nasaHD);
        saveButton = findViewById(R.id.saveBtn);

        progBar = findViewById(R.id.progressBar);
       progBar.getProgressDrawable().setColorFilter(
              Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

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


        saveButton.setOnClickListener(v -> {
            try {
                File dir = getFilesDir();
                String fileName = title;
                FileOutputStream fOs = openFileOutput(fileName, Context.MODE_PRIVATE);
                nasaPic.compress(Bitmap.CompressFormat.JPEG, 80, fOs);
                Snackbar.make(v, "Picture: '" + title + "' has been saved.", Snackbar.LENGTH_LONG).show();
                fOs.flush();
                fOs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch (item.getItemId()) {

            case R.id.savedPicturesList:
                Intent home = new Intent(this, ImageStorage.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);

        return true;
    }


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

                Log.d("object", results);
                 imgDate = img.getString("date");
                 imgurl = img.getString("url");
                 hdurl = img.getString("hdurl");
                 title = img.getString("title");

                Log.d("date", imgDate);
                Log.d("imgurl", imgurl);

                imageURL = new URL(imgurl);

                connection = (HttpURLConnection) imageURL.openConnection();

                inputStreamPic = new BufferedInputStream(connection.getInputStream());

                reader = new BufferedReader(new InputStreamReader(inputStreamPic, "UTF-8"), 8);

                nasaPic = BitmapFactory.decodeStream(inputStreamPic);

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

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progBar.setProgress(values[0]);


        }

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
