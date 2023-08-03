<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Kategori extends Model
{
    //
    protected $table = 'kategori';

    protected $hidden = ['pivot','created_at','updated_at']; // doesn't work

    protected $guarded = ['id'];

    public function restoran(){
        return $this->belongsToMany('App\Restoran','restoran_kategori','id_kategori','id_restoran');
    }

    public function menu(){
        return $this->hasMany('App\Menu','id_kategori');
    }




    public function getKategoriNamaAttribute($kategori_nama){
        return ucwords($kategori_nama);
    }
}
