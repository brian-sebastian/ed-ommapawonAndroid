<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class MenuResource extends JsonResource
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
            'id_restoran' => $this->id_restoran,
            'id_kategori' => $this->id_kategori,
            'menu_kategori' => $this->kategori->kategori_nama,
            'nama_makanan'=> $this->nama_makanan,
            'image'=> $this->image,
            'harga'=> $this->harga,
            'deskripsi'=> $this->deskripsi,
            'menu_ketersediaan'=> $this->menu_ketersediaan,
        ];
    }
}
