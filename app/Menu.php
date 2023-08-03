<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Menu extends Model
{
    //
    protected $table = 'menu' ;



    protected $guarded = ['id'];

    protected $hidden = [
        'updated_at','menu_delete','created_at'
    ];

   public function restoran(){
       return $this->belongsTo('App\Restoran','id_restoran');
   }

   public function kategori(){
       return $this->belongsTo('App\Kategori','id_kategori');
   }


    // public function satuan(){
    //     return $this->belongsTo('App\Satuan','id_satuan');
    // }

    public function order(){
        return $this->belongsToMany('App\Order','detail_order','id_menu','id_order');
    }

    // public function  favorit(){
    //     return $this->hasMany('App\Favorit','id_menu');
    // }



    //Asessor
    public function getMenuNamaAttribute($menu_nama){
        return ucwords($menu_nama);
    }


    //Mutator
//    public function setMenuNamaAttribute($menu_nama){
//        return strtolower($menu_nama);
//    }

}
