<?php

namespace App;


class Firebase
{
    public function  send ($registration_ids,$message){
        $fields = array(
            'registration_ids' => $registration_ids,
            'data' => $message,
        );
        return $this->sendPushNotification($fields);
    }

    private function sendPushNotification($fields){
        //importing db
        //$this->db;

      
        $url = 'https://fcm.googleapis.com/fcm/send';
        
       

        $headers = array(
            //key new
            'Authorization: key=AAAAzACGPZ8:APA91bF9GX28SV6_W_rO7KrSQ0exHfkw5PDFjhs_0WzrRY0cyFp6MQGJd35s0fFrIAcNz9U-ojOhQR1jtznXvtzHfp498k2NlIiHRAE6M932j1JX9W8ZAautxtR0_uKuT7Gkuw2MWshc',
            'Content-Type: application/json'
        );


        //Initializing curl to open a connection
        $ch = curl_init();

        //Setting the curl url
        curl_setopt($ch, CURLOPT_URL, $url);

        //setting the method as post
        curl_setopt($ch, CURLOPT_POST, true);

        //adding headers
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        //disabling ssl support
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        //adding the fields in json format
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        //finally executing the curl request
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }

        //Now close the connection
        curl_close($ch);

        //and return the result
        return $result;
    }
}