<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Konsumen extends Model
{
    //
    protected $table = 'konsumen';

    protected $guarded = ['id'];


    public function  uat(){
        return $this->hasMany('App\Uat','id_konsumen');
    }

    public function  order(){
        return $this->hasMany('App\Order','id_konsumen');
    }

    // public function  favorit(){
    //     return $this->hasMany('App\Favorit','id_konsumen');
    // }


    public function getKonsumenNamaAttribute($konsumen_nama){
        return ucwords($konsumen_nama);
    }
}
