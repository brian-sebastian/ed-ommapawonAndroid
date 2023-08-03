<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Restoran extends Model
{
    //
    protected $table = 'restoran';

    protected $guarded = ['id'];

    public function kategori(){
        return $this->belongsToMany('App\Kategori','restoran_kategori','id_restoran','id_kategori')->withTimestamps();
    }

    public function  menu(){
        return $this->hasMany('App\Menu','id_restoran');
    }

    public function  kurir(){
        return $this->hasMany('App\Kurir','id_restoran');
    }

    public function  order(){
        return $this->hasMany('App\Order','id_restoran');
    }

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'updated_at'
    ];
}
