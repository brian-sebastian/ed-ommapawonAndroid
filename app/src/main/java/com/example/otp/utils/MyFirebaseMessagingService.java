package com.example.otp.utils;

import android.content.Intent;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.otp.activities.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String SIMPAN = "Simpan";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "onMessageReceived: "+ remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);

            }catch (Exception e){
                Log.d(TAG, "onMessageReceived: "+e.getMessage());
            }
        }
    }

    @Override
    public void onNewToken(@NonNull String token){
        //mendapatkan token registrasi
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        //Menyimpan Token
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
        Log.d(SIMPAN, "Token : " + token );
    }


    private void sendPushNotification(JSONObject json){
        Log.d(TAG, "sendPushNotification: "+json.toString());

        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");

            //createing an intent for the notification
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());

            //creating an Intent for the notification
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);

            mNotificationManager.showSmallNotification(title,message,intent);
        }catch (JSONException e){
            Log.d(TAG, "sendPushNotification: "+e.getMessage());
        }catch (Exception e){
            Log.d(TAG, "sendPushNotification: "+e.getMessage());
        }
    }

}
