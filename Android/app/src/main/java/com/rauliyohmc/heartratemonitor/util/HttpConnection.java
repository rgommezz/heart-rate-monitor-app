package com.rauliyohmc.heartratemonitor.util;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Rauliyohmc on 12/05/15.
 */
public class HttpConnection {

    private final static String TAG = HttpConnection.class.getSimpleName();

    private HttpClient httpClient;
    private HttpPost httpPost;
    public final static String URL_HEART_RATE = "";
    public final static String URL_HEART_RATE_LOCAL = "http://localhost:3000/api/heartrate"; //TODO set your local ip address instead of localhost
    public final static String URL_LOCATION = "";
    public final static String URL_LOCATION_LOCAL = "http://localhost:3000/api/location"; //TODO set your local ip address instead of localhost

    //constructor
    public HttpConnection(String endpoint){
        httpClient = new DefaultHttpClient();
        // make POST request to the given URL
        httpPost = new HttpPost(endpoint);
    }

    public void sendHeartRateToWebServer(int heartRate){
        InputStream inputStream = null;
        String result;
        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            // Telling the web server to disconnect with the flag heartRate = 0
            if(heartRate == 0){
                jsonObject.put("emitting", "false");
            } else {
                jsonObject.put("emitting", "true");
            }
            jsonObject.put("heartRate", heartRate);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // Receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // Convert inputStream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            Log.d(TAG, "Server response: " + result);

        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }

    public void sendLocationToWebServer(String latitude, String longitude) {
        InputStream inputStream = null;
        String result;
        try {
            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // Receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // Convert inputStream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            Log.d(TAG, "Server response: " + result);

        } catch (Exception e){
            if (e.getMessage() != null) Log.d(TAG, e.getMessage());
            else Log.d(TAG, "Error sending the Location");
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public void disconnect() {
        httpPost = null;
        httpClient = null;
    }
}
