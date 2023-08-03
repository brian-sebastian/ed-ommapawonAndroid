<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class RumahMakanResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array|\Illuminate\Contracts\Support\Arrayable|\JsonSerializable
     */
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'restoran_nama' => $this->restoran_nama,
            'restoran_phone' => $this->restoran_phone,
            'restoran_email'=> $this->restoran_email,
            'restoran_deskripsi'=> $this->restoran_deskripsi,
            'restoran_alamat'=> $this->restoran_alamat,
            'restoran_latitude' => $this->restoran_latitude,
            'restoran_longitude' => $this->restoran_longitude,
            'restoran_oprasional' => $this-> restoran_oprasional,
            'restoran_delivery'=> $this->restoran_delivery,
            'restoran_delivery_tarif'=> $this->restoran_delivery_tarif,
            'jarak_pesanan'=> $this->jarak_pesanan,
            'restoran_delivery_minimum'=> $this->restoran_delivery_minimum,
            // 'restoran_pajak_pb_satu'=>$this->restoran_pajak_pb_satu,
            // 'restoran_balance'=>$this->restoran_balance,
            'restoran_distace'=>(number_format($this->distance,2)),
         //   'restoran_distace2'=>($this->distance),
            'restoran_order'=> $this->order->where("order_status","Selesai")->count(),
            'jumlah_kurir' => $this->kurir->where("kurir_delete","==",0)->count(),
            'restoran_token' => $this->token,
//            'restoran_kategori' => $this->kategori,
//            'restoran_menu' => $this->menu->where("menu_delete",0),
        ];
    }

    public function with($request)
    {
        return [
            'value' => '1',
            'message' => 'Success',
        ];
    }
}
