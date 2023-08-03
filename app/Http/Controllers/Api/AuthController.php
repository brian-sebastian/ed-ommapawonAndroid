<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Konsumen;
use App\Models\Kurir;
use App\Http\Resources\KonsumenResource;
use App\Http\Resources\KurirResource;
use App\Http\Resources\RumahMakanResource;
use App\Konsumen as AppKonsumen;
use App\Kurir as AppKurir;
use App\Restoran;
use Illuminate\Http\Request;

class AuthController extends Controller
{
    public function ommapawon($phone){
        $konsumen = AppKonsumen::where('nomor_telpon',$phone)->first();
        if ($konsumen) {
            return new KonsumenResource($konsumen);
        } else {
            return response()->json([
                'value' => '0',
                'message' => 'Nomor Ponsel Tidak Terdaftar',
            ]);
        }
    }

    public function kurir($phone){
        $restoran = Restoran::where('restoran_phone',$phone)->first();
        if ($restoran) {
            $data = new RumahMakanResource($restoran);
            if($restoran->restoran_status == "aktif") {
                return [
                    "value" => "1",
                    "message" => "succes",
                    "tipe" => "restoran",
                    "restoran" => $restoran,
                ];
            }else{
                return [
                    'value' => '0',
                    'message' => 'Restoran dalam tahap review',
                ];
            }
        } else{
            $kurir = AppKurir::where('nomor_telp_kurir',$phone)->first();
            if($kurir){

                $data =  new KurirResource($kurir);
                if($kurir->restoran->restoran_status == "aktif") {
                    return [
                        "value" => "1",
                        "message" => "succes",
                        "tipe" => "kurir",
                        "kurir" => $kurir,
                    ];
                }else{
                    return [
                        'value' => '0',
                        'message' => 'Harap hubungi restoran anda',
                    ];
                }


            }else{
                return [
                    'value' => '0',
                    'message' => 'Nomor Ponsel Tidak Terdaftar, Harap Hubungi Admin Omma Pawon',
                ];
            }
        }


    }
}
