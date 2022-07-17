package com.example.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class Images extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar tBar;
    private DrawerLayout drawer;
    private NavigationView navView;
    private static URL nasaURL;
    private static URL imageURL;
    private static InputStream inputStream;
    private static InputStream inputStreamPic;
    private static String NASAURL = "https://api.nasa.gov/planetary/apod?api_key=O9o5jg2qGMWJaCrVbyHAPApXWT2Klgzc6nr9YzW6&date=";
    private static String selectedDate;
    private static Bitmap nasaPic;
    private static ImageView nasaImage;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        nasaImage = findViewById(R.id.nasaImage);

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

       // NASAImages nasaImages = new NASAImages();
       // nasaImages.execute(NASAURL + selectedDate);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId()) {

            case R.id.item1:
                message = "You clicked item 1!";
                break;
            case R.id.item2:
                message = "You clicked item 2!";
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

        Toast.makeText(this,  message, Toast.LENGTH_SHORT).show();
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
            String imgDate = img.getString("date");
           String imgurl = img.getString("url");
           String hdurl = img.getString("hdurl");
           String title = img.getString("title");

           Log.d("date", imgDate);
           Log.d("imgurl", imgurl);

                imageURL = new URL(imgurl);

                connection = (HttpURLConnection) imageURL.openConnection();

                inputStreamPic = new BufferedInputStream(connection.getInputStream());

                reader = new BufferedReader(new InputStreamReader(inputStreamPic, "UTF-8"), 8);

           nasaPic = BitmapFactory.decodeStream(inputStreamPic);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);
            nasaImage.setImageBitmap(nasaPic);

        }
    }
    }
