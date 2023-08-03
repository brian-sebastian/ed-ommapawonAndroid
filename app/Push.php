<?php
/**
 * Created by PhpStorm.
 * User: abdialam
 * Date: 11/13/18
 * Time: 10:18
 */

namespace App;


class Push
{
    //notification title
    private $title;

    //notification message
    private $message;



    //initializing values in this constructor
    function __construct($title, $message) {
        $this->title = $title;
        $this->message = $message;

    }

    //getting the push notification
    public function getPush() {
        $res = array();
        $res['data']['title'] = $this->title;
        $res['data']['message'] = $this->message;

        return $res;
    }

}