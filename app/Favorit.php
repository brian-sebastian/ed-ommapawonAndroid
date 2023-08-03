<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Favorit extends Model
{
    //
    protected $table = 'favorit';

    protected $guarded = ['id'];

    public function  konsumen(){
        return $this->belongsTo('App\Konsumen','id_konsumen');
    }

    public function  menu(){
        return $this->belongsTo('App\Menu','id_menu');
    }
}
