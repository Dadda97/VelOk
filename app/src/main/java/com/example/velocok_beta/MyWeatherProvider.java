package com.example.velocok_beta;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public class MyWeatherProvider extends AsyncTask<Void,Void,Void> {

    private static  final String TAG = "MyWeatherProvider";
    private static  final String STARTURL="http://api.openweathermap.org/data/2.5/forecast?q=genoa&units=metric&appId=";
    private static  final String ICONURL="http://openweathermap.org/img/wn/";
    private int numberOfForecast;
    private String[] icons =  null;
    private String[] temps = null;
    private String[] times= null;
    private LinearLayout weatherParent;

    public MyWeatherProvider(LinearLayout parent){
        weatherParent = parent;
        numberOfForecast=  weatherParent.getChildCount();     //number of weather defined by XML

        icons = new String[numberOfForecast];
        temps = new String[numberOfForecast];
        times = new String[numberOfForecast];



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

                for (int i = 0; i < numberOfForecast; i++) {
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

    @Override
    protected Void doInBackground(Void... voids) {
        getWeatherFromApi();
        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        for (int i = 0; i< numberOfForecast;i++){

            LinearLayout forecast= (LinearLayout) weatherParent.getChildAt(i);

            TextView hour = (TextView) forecast.getChildAt(0);
            hour.setText(this.getWeatherTime(i));

            ImageView image= (ImageView) forecast.getChildAt(1);
            image.setContentDescription(this.getWeatherIconURL(i));
            Picasso.get().load(this.getWeatherIconURL(i)).resize(500, 500)
                    .centerCrop().into(image);

            TextView temp = (TextView) forecast.getChildAt(2);
            temp.setText(this.getWeatherTemp(i));
        }
        Log.d(TAG,"FINISHED Init Weather....");

    }
}
