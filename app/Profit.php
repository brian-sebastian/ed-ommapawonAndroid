<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Profit extends Model
{
    //
    protected $table = 'profit';

    protected $guarded = ['id'];


    public function order(){
        return $this->belongsTo('App\Order','id_order');
    }

   
}
