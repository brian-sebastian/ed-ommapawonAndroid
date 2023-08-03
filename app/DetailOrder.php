<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class DetailOrder extends Model
{
    //
   protected $table = 'detail_order';
   protected $guarded = ['id'];

   public function order(){
       return $this->belongsTo('App\Order','id_order');

   }
   public function menu(){
       return $this->belongsTo('App\Menu','id_menu');
   }


}

