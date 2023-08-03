<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class RiwayatRestopay extends Model
{
    //
    //
    protected $table = 'riwayat_restopay';

    protected $guarded = ['id'];

    //Asessor
    public function getPenerimaTipeAttribute($penerima_tipe){
        return ucwords($penerima_tipe);
    }

    public function getPengirimTipeAttribute($pengirim_tipe){
        return ucwords($pengirim_tipe);
    }
}
