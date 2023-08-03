<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class KurirResource extends JsonResource
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
            'nama_kurir'=> $this->nama_kurir,
            'nomor_telp_kurir' => $this->nomor_telp_kurir,
            'email_kurir' => $this->email_kurir
        ];
    }
}
