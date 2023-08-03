<?php

namespace App\Http\Controllers\Api;

use App\Firebase;
use App\Http\Resources\KonsumenResource;
use App\Models\Konsumen;
use App\Http\Controllers\Controller;
use App\Konsumen as AppKonsumen;
use App\Push;
use App\Restoran;
use App\RiwayatRestopay;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class KonsumenController extends Controller
{

    public function login(Request $request){

        // Validasi
        $konsumen = AppKonsumen::where('nomor_telpon', $request->nomor_telpon)->first();
        if($konsumen){
            return response()->json([
                'value' => 1,
                'message' => 'Selamat Datang '.$konsumen->nama_konsumen,
                'konsumen' => $konsumen
            ]);
        }
            return response()->json([
                'value' => 0,
                'message' => 'Nomor Handphone Tidak Terdaftar',
            ]);
    }



    public function store(Request $request){
        //Mendaftar Konsumen baru
        $phone = $request->nomor_telpon;
        $konsumen = AppKonsumen::where('nomor_telpon', $phone)->first();
        if ($konsumen) {
            return response()->json([
                'value' => '0',
                'message' => 'Opps! Nomor Handphone Telah Terdaftar',
            ]);
        } else {
        $data = $request->all();
        AppKonsumen::create($data);
            return response()->json([
                'value' => '1',
                'message' => 'Berhasil Registrasi',
                'konsumen' => $data
            ]);
    }

    }

    public function show($id)
    {
        //
        $konsumen = AppKonsumen::findOrFail($id);

        return new KonsumenResource($konsumen);
    }



  

    public function update(Request $request, $id)
    {

       $konsumen = AppKonsumen::findOrFail($id);
       if($konsumen->update($request->all())) {
          // return new KonsumenResource($konsumen);
           return response()->json([
               'value' => '1',
               'message' => 'Berhasil Update',
           ]);
       }else{
           return response()->json([
               'value' => '0',
               'message' => 'Gagal Update',
           ]);
       }

    }

 

    public function TopUp(Request $request)
    {
        //
        $input = $request->all();
        $id_konsumen = $input['id_konsumen'];
        $id_restoran = $input['id_restoran'];
        $value = $input['value'];

        $restoran = Restoran::findOrFail($id_restoran);
        $konsumen = AppKonsumen::findOrFail($id_konsumen);


        if($restoran->restoran_balance > $value){


            $updateResto = $restoran->update(array('restoran_balance' =>($restoran->restoran_balance - $value)));
            $updateKonsumen = $konsumen->update(array('konsumen_balance' =>$konsumen->konsumen_balance + $value));

            if($updateResto && $updateKonsumen){
                //create push
                $title = "Top Up Saldo";
                $message = "Saldo Anda telah ditambah Rp.".$value;
                $push = new Push($title,$message);

                //get push
                $pushNotification = $push->getPush();

                $deviceToken =   AppKonsumen::where('id',$id_konsumen)->pluck('token')->toArray();

                    $firebase = new Firebase();
                    $msg = $firebase->send($deviceToken,$pushNotification);

                if($request->has('id_kurir' )) {
                    $id_kurir = $input['id_kurir'];
                    $insertHistory = RiwayatRestopay::create(['penerima_id' => $konsumen->id,
                        'penerima_phone' => $konsumen->konsumen_phone,
                        'nominal' => $value,
                        'jenis_transaksi' => 'topup',
                        'penerima_tipe' => 'konsumen',
                        'pengirim_id' => $id_kurir,
                        'pengirim_tipe' => 'kurir',]);

                }else {
                    $insertHistory = RiwayatRestopay::create(['penerima_id' => $konsumen->id,
                        'penerima_phone' => $konsumen->konsumen_phone,
                        'nominal' => $value,
                        'jenis_transaksi' => 'topup',
                        'penerima_tipe' => 'konsumen',
                        'pengirim_id' => $id_konsumen,
                        'pengirim_tipe' => 'restoran',]);
                }

                return [
                    'value' => '1',
                    'message' => 'Berhasil Transfer',

                ];
            }elseif ($updateResto || $updateKonsumen){
                if ($updateResto){
                    $updateBack = $restoran->update(array('restoran_balance' =>($restoran->restoran_balance + $value)));
                    return [
                        'value' => '0',
                        'message' => 'Gagal transfer',
                    ];

                }
                if ($updateKonsumen){
                    $updateBack = $konsumen->update(array('konsumen_balance' =>$konsumen->konsumen_balance - $value));
                    return [
                        'value' => '0',
                        'message' => 'Gagal transfer',
                    ];

                }
            }


        }else{
            return [
                'value' => '0',
                'message' => 'Saldo Tidak Mencukupi',

            ];
        }

// barang () => ({
//)



    }

}
