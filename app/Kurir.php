<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class Kurir extends Model
{
    //
    protected $table ='kurir';


    protected $guarded = ['id'];

    public function restoran(){
        return $this->belongsTo('App\Restoran','id_restoran');
    }

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'updated_at'
    ];

    //Asessor
    public function getKurirNamaAttribute($kurir_nama){
        return ucwords($kurir_nama);
    }
}
