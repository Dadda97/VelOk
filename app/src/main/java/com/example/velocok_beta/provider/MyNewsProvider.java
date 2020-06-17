package com.example.velocok_beta.provider;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.velocok_beta.BuildConfig;
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

public class MyNewsProvider extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "MyNewsProvider";
    private SharedPreferences mPreferences;
    private int numberOfNews;
    private String[] titles = null;
    private String[] bodies = null;
    private String[] images = null;
    private LinearLayout newsParent;

    public MyNewsProvider(LinearLayout parent) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        newsParent = parent;
        numberOfNews = newsParent.getChildCount();

        titles = new String[numberOfNews];
        images = new String[numberOfNews];
        bodies = new String[numberOfNews];


    }

    public String getNewsTitle(int i) {
        return titles[i];
    }

    public String getNewsImagesURL(int i) {
        return images[i];
    }

    public String getNewsBody(int i) {
        return bodies[i];
    }

    private String buildURL() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        String category = mPreferences.getString("news_preferences", "");
        String dateString = formatter.format(date);
        String apiString = "&apiKey=".concat(BuildConfig.NEWS_KEY);
        String STARTURL = "https://newsapi.org/v2/everything?language=it&sortby=relevancy&domains=ansa.it&from=";
        if (!category.equals("none")) {
            STARTURL = "https://newsapi.org/v2/top-headlines?country=it&category=".concat(category);
            return STARTURL.concat(apiString);
        } else {
            return STARTURL.concat(dateString).concat(apiString);
        }
    }

    private void getNewsFromApi() {
        try {
            InputStream is = new URL(buildURL()).openStream();
            try {

                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                JSONArray JsonArticles = (json.getJSONArray("articles"));

                for (int i = 0; i < numberOfNews; i++) {
                    titles[i] = ((JSONObject) JsonArticles.get(i)).getString("title");
                    images[i] = ((JSONObject) JsonArticles.get(i)).getString("urlToImage");
                    bodies[i] = ((JSONObject) JsonArticles.get(i)).getString("content");
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
        getNewsFromApi();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        for (int i = 0; i < numberOfNews; i++) {

            LinearLayout news = (LinearLayout) newsParent.getChildAt(i);

            TextView text = (TextView) news.getChildAt(0);
            text.setText(this.getNewsTitle(i));

            ImageView image = (ImageView) news.getChildAt(1);
            image.setContentDescription(this.getNewsTitle(i));
            Picasso.get().load(this.getNewsImagesURL(i)).resize(500, 500)
                    .centerCrop().into(image);
        }
    }
}
