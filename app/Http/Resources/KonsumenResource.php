<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class KonsumenResource extends JsonResource
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
            'nama_konsumen'=> $this->nama_konsumen,
            'email' => $this->email,
            'nomor_telpon' => $this->nomor_telpon,
            'token' => $this->token,
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
