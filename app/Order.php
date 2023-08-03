<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Carbon;

class Order extends Model
{
    //
    protected $table = 'order';

    protected $guarded = ['id'];

    public function  konsumen(){
        return $this->belongsTo('App\Konsumen','id_konsumen');
    }

    public function  restoran(){
        return $this->belongsTo('App\Restoran','id_restoran');
    }


    public function order_detail(){
        return $this->belongsToMany('App\Menu','detail_order','id_order','id_menu')
            ->withPivot('qty','harga')
            ->withTimestamps();
    }


    public function getOrderStatusAttribute($order_status){
        return ucwords($order_status);
    }

    public function getCreatedAtAttribute(){
        return Carbon::parse($this->attributes['created_at'])
        ->translatedFormat('l, d F Y');
    }

}
