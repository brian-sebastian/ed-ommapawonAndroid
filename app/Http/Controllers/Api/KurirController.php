<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Kurir;
use App\Restoran;
use Illuminate\Http\Request;

class KurirController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $data = Kurir::all();
        if($data){
            return [
                "value" => "1",
                "message" => "succes",
                "kurir" => $data,
            ];
        }else{
            return [
                "value" => "0",
                "message" => "failed",
            ];
        }
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
          //
        //Mendaftar Kurir baru
        $phone = $request->kurir_phone;
        $kurir = Kurir::where('nomor_telp_kurir', $phone)->first();
        $restoran  = Restoran::where('restoran_phone', $phone)->first();
        if ($kurir) {
            return [
                'value' => '0',
                'message' => 'Opps! Nomor Handphone Telah Terdaftar',
            ];
        } else if($restoran){
            return [
                'value' => '0',
                'message' => 'Opps! Nomor Handphone Telah Terdaftar',
            ];
        }else {
            $data = $request->all();
            Kurir::create($data);
            return [
                'value' => '1',
                'message' => 'Berhasil Registrasi',
            ];
        }
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $kurir = Kurir::findOrFail($id);
        if($kurir->update($request->all())) {

            return [
                'value' => '1',
                'message' => 'Berhasil Update',
            ];
        }else{
            return [
                'value' => '0',
                'message' => 'Gagal Update',
            ];
        }
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
          //
          
          $kurir = Kurir::findOrFail($id);
          $delete =$kurir->delete();
          if($delete) {
  
              return [
                  'value' => '1',
                  'message' => 'Kurir Berhasil Dihapus',
              ];
          }else{
              return [
                  'value' => '0',
                  'message' => 'Gagal Menghapus Kurir',
              ];
          }
    }
}
