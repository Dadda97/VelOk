package com.example.velocok_beta;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyWeatherProvider {

    private static  final String TAG = "MyWeatherProvider";
    private static  final String STARTURL="http://api.openweathermap.org/data/2.5/forecast?q=genoa&units=metric&appId=";
    private static  final String ICONURL="http://openweathermap.org/img/wn/";
    private int numberOfIcons;
    private String[] icons =  null;
    private String[] temps = null;
    private String[] times= null;


    public MyWeatherProvider(int num){
        numberOfIcons = num;
        icons = new String[numberOfIcons];
        temps = new String[numberOfIcons];
        times = new String[numberOfIcons];

        getWeatherFromApi();

    }

    public String getWeatherIconURL(int i){
        return icons[i];
    }

    public String getWeatherTime(int i){
        return times[i];
    }

    public String getWeatherTemp(int i){
        return temps[i];
    }

    private String buildURL(){
        String apiString= "&apiKey=".concat(BuildConfig.WEATHER_KEY);
        return STARTURL.concat(apiString);
    }
    private void getWeatherFromApi() {
        Log.d(TAG,buildURL());
        try {
            InputStream is = new URL(buildURL()).openStream();
            try {

                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                JSONArray JsonForecasts =(json.getJSONArray("list"));
                Log.d(TAG,JsonForecasts.toString());

                for (int i = 0; i < numberOfIcons; i++) {
                    JSONObject forecast=(JSONObject)JsonForecasts.get(i);
                    int unixTime =forecast.getInt("dt")+3600;
                    SimpleDateFormat df =new SimpleDateFormat("HH:mm");
                    times[i] = df.format(new Date(unixTime*1000L));
                    icons[i] = ICONURL.concat(((JSONObject)(forecast.getJSONArray("weather").get(0))).getString("icon")).concat("@2x.png");
                    temps[i] = (forecast.getJSONObject("main").getString("temp")).concat("°C");
                }

            } finally {
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
