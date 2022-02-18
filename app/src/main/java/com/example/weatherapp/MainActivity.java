package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityname;

    TextView resultText;


    public void findWeather(View view){

        Log.i("Cityname", cityname.getText().toString());

        //method to hide input keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(cityname.getWindowToken(), 0);


        try {

            String encodedCityName = URLEncoder.encode(cityname.getText().toString(), "UTF-8");

            DownoadTask task = new DownoadTask();

            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find the city!", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname = findViewById(R.id.editText);

        resultText = findViewById(R.id.resultText);

    }

    public class DownoadTask extends AsyncTask<String, Void,String>{


        @Override
        protected String doInBackground(String... urls) {

            String result = "";

            URL url;

            HttpURLConnection urlConnection;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while (data != -1){

                    char current = (char)data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for (int i = 0; i < jsonArray.length(); i++){

                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    String main = "";

                    String description = "";

                    main = jsonPart.getString("main");

                    description = jsonPart.getString("description");

                    if (main != "" && description != ""){

                        message += main + " : " + description + "\r\n";

                    }

//                    Log.i("main", jsonPart.getString("main"));
//
//                    Log.i("description", jsonPart.getString("description"));

                }

                if (message != ""){

                    resultText.setText(message);

                }else {

                    Toast.makeText(getApplicationContext(), "Could not find weather!", Toast.LENGTH_LONG).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
