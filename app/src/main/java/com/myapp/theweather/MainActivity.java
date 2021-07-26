package com.myapp.theweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView editText, main_txt, feels_text,get_sr,get_ss, get_hum,get_pr;
    private EditText city_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.get_btn);
        editText = findViewById(R.id.get_temp);
        city_txt = findViewById(R.id.city_name);
        main_txt = findViewById(R.id.get_main);
        feels_text = findViewById(R.id.feels_like);
        get_sr = findViewById(R.id.get_sunrise);
        get_hum = findViewById(R.id.get_humidity);
        get_ss = findViewById(R.id.get_sunset);
        get_pr = findViewById(R.id.get_pressure);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (city_txt.equals("")) {
                    Toast.makeText(MainActivity.this, "Please Enter a City name", Toast.LENGTH_SHORT).show();
                }
                else {
                    getTemp();
                }

            }
        });
    }

    private void getTemp() {
       // String KEY = "YOUR_API_KEY";
        String City = city_txt.getText().toString();
        String URL = "https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=YOUR_API_KEY";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsonObject = response.getJSONObject("main");
                    String Temperature = jsonObject.getString("temp");
                    String Temp_feels = jsonObject.getString("feels_like");
                    String pressure = jsonObject.getString("pressure");
                    String Humidity = jsonObject.getString("humidity");
                    Double temp_C = Double.parseDouble(Temperature) - 273.15;
                    Double temp_C_feels = Double.parseDouble(Temp_feels) - 273.15;
                    get_pr.setText("Pressure:  "+pressure + " Pa");
                    get_hum.setText("Humidity:  "+Humidity + "%");
                    editText.setText("Temperature: "+temp_C.toString().substring(0,5) + " C");
                    feels_text.setText("Feels Like : "+temp_C_feels.toString().substring(0,4)+ " C");

                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String main = jsonObjectWeather.getString("main");
                    main_txt.setText("Weather Description:  " +main);

                    JSONObject jsonObject2 = response.getJSONObject("sys");
                    String sunset = jsonObject2.getString("sunset");
                    long dv = Long.valueOf(sunset)*1000;
                    Date df = new java.util.Date(dv);
                    String vv = new SimpleDateFormat("MM/dd/yyyy , hh:mma").format(df);
                    get_ss.setText("Sunset at:  " +vv);

                    String sunrise = jsonObject2.getString("sunrise");
                    long dv1 = Long.valueOf(sunrise)*1000;
                    Date df1 = new java.util.Date(dv1);
                    String vv1 = new SimpleDateFormat("MM/dd/yyyy , hh:mma").format(df1);
                    get_sr.setText("Sunrise at:  " +vv1);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }
}