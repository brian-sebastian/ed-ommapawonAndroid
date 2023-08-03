<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Uat extends Model
{
    //
    protected $table ='uat';


    protected $guarded = ['id'];

    public function konsumen(){
        return $this->belongsTo('App\Konsumen','id_konsumen');
    }
}
