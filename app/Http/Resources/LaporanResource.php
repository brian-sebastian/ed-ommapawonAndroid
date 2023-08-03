<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class LaporanResource extends JsonResource
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
            'nama_makanan' => $this->nama_makanan,
            // 'menu_jumlah_favorit' => $this->favorit->count(),
            'menu_jumlah_dipesan' => $this->order->count(),
        ];
    }
}
