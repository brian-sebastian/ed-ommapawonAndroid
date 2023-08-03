<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\LaporanResource;
use App\Http\Resources\MenuResource;
use App\Http\Resources\RumahMakanCollection;
use App\Http\Resources\RumahMakanResource;
use App\Konsumen as AppKonsumen;
use App\Menu as AppMenu;
use App\Order;
use App\Restoran;
use Carbon\Carbon;
use Illuminate\Http\Request;

class RumahMakanController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(Request $request)
    {
        //ambil parameter lokasi user
        $lat = $request->input('lat');
        $long = $request->input('long');
        $id_konsumen = $request->input('id_konsumen');


         //ambil satu restoran untuk cart
         if ($request->has('id_restoran')){
            $id_restoran =  $request->input('id_restoran');

            $resto =$this->get_restaurant($lat,$long,$id_restoran);
            $konsumen =  AppKonsumen::findOrFail($id_konsumen);
           // $saldo =  $konsumen->konsumen_balance;

            if(optional($resto)->count() > 0 ) {
                $restoran = new RumahMakanResource($resto);
               $menu = $restoran->menu->where('menu_delete',0);
                $menu_item = MenuResource::collection($menu);
                return [
                    'value' => '1',
                    'message' => 'Restoran Ditemukan',
                    'data' => $restoran,
                    'menu' => $menu_item,
                ];
            }else{
                return [
                    'value' => '0',
                    'message' => 'Restoran Tidak Ditemukan pertama',
                ];
            }


        }else if($request->has('cari')) {
            $key = $request->input('cari');

            //cari restoran terdekat
            $re =$this->get_restaurant_near($lat,$long,300);


            //ubah kearray
            $koleksi = $re->toArray();
            $items = array();

            $menu = array();
            foreach ($koleksi as $resto){
                //ambil restoran yang memungkinkan untuk mengantarkan
                if ($resto['distance'] < $resto['jarak_pesanan']){
                    $Restoran = Restoran::findOrFail($resto['id']);
                    
                    //ambil restoran yang memiliki menu saja
                    if($Restoran->menu->where('menu_delete',0)->count() > 0) {
                        //set id restoran yang sudah di seleksi
                        $items[] = $resto['id'];
                        //get menu
                        $tem=$Restoran->menu;
                        for ($x = 0; $x < $Restoran->menu->count(); $x++) {
                            $menu[] = $tem[$x];
        
                        }
        
                    }
                }
            }

            //ambil data restoran berdasarkan restoran yang memungkinkan
            $restoran_coll = $re->whereIn('id',$items);

            $restoranCollection = $restoran_coll->reject(function($element) use ($key) {
                return stripos($element->restoran_nama, $key) === false;
            });
            $x = collect($menu);
            $menuCollection = $x->reject(function($element) use ($key) {
                return stripos(strtolower($element->nama_makanan), $key) === false;
            });

            //jika restoran memungkinkan besar dari 0 , tampilkan seluruh restoran
            if($restoranCollection->count()> 0 || $menuCollection->count() > 0){
                $b = RumahMakanResource::collection($restoranCollection);
                $c =  $b->sortByDesc('restoran_order');
                $hasilMenu = MenuResource::collection($menuCollection);
                $hasilRestoran = new RumahMakanCollection($c);

                return [
                    'value' => '1',
                    'message' => 'succes',
                    'rumah_makan' => $hasilRestoran,
                    'menu' => $hasilMenu,
                ];

                //jika tidak ada restoran memungkinkan
            }else{
                return [
                    'value' => '0',
                    'message' => 'Restoran Tidak Ditemukan Kedua',
                ];
            }

        }else{
            //cari restoran terdekat
            $re =$this->get_restaurant_near($lat,$long,300);


            //ubah kearray
            $koleksi = $re->toArray();
            $items = array();
            foreach ($koleksi as $resto){
                //ambil restoran yang memungkinkan untuk mengantarkan
                if ($resto['distance'] < $resto['jarak_pesanan']){
                    $Restoran = Restoran::findOrFail($resto['id']);
                    //ambil restoran yang memiliki menu saja
                    if($Restoran->menu->where('menu_delete',0)->count() > 0) {
                        //set id restoran yang sudah di seleksi
                        $items[] = $resto['id'];
                    }
                }
            }

            //ambil data restoran berdasarkan restoran yang memungkinkan
            $restoran_coll = $re->whereIn('id',$items);

            //jika restoran memungkinkan besar dari 0 , tampilkan seluruh restoran
            if($restoran_coll->count() > 0){
                $b = RumahMakanResource::collection($restoran_coll);
            //    $c =  $b->sortByDesc('restoran_distace2');
                return new RumahMakanCollection($b);

                //jika tidak ada restoran memungkinkan
            }else{
                return [
                    'value' => '0',
                    'message' => 'Restoran Tidak Ditemukan Ketiga',
                ];
            }
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
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        $restoran = Restoran::findOrFail($id);


        if($restoran != null){
             return new RumahMakanResource($restoran);
        }else{
            return array('not found');
        }
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
        //
        $restoran = Restoran::findOrFail($id);
        if($restoran->update($request->all())) {
            // return new KonsumenResource($konsumen);
            return [
                'value' => '1',
                'message' => 'Berhasil Update',
                'resto' => $restoran,
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
    }

    public function laporan($id)
    {
        //
        $now = Carbon::now();
        $month = $now->month;
        $year = $now->year;
        $order = Order::where('id_restoran', $id)
            ->whereDate('created_at', '=', $now)
            ->get();
        $orderMonth = Order::where('id_restoran', $id)
            ->whereYear('created_at', '=', $year)
            ->whereMonth('created_at', '=', $month)
            ->get();

        $menu = AppMenu::where('id_restoran', $id)
            ->where('menu_delete','0')
            ->get();

        $a = LaporanResource::collection($menu);


        return[
            'jumlah_order' => $order->where('order_status','Proses')->count(),
            'jumlah_pengantaran' =>$order->where('order_status','Pengantaran')->count(),
            'jumlah_selesai' =>$order->where('order_status','Selesai')->count(),
            'jumlah_batal' =>$order->where('order_status','Batal')->count(),
            'order_month_selesai' => $orderMonth->where('order_status','Selesai')->count(),
            'order_month_batal' => $orderMonth->where('order_status','Batal')->count(),
            'menu' => $a,
        ];

    }


    public function get_restaurant_near($latitude, $longitude, $radius){

        $offers = Restoran::select('restoran.*')
            ->selectRaw('( 6371* acos( cos( radians(?) ) *
                           cos( radians( restoran_latitude ) )
                           * cos( radians( restoran_longitude ) - radians(?)
                           ) + sin( radians(?) ) *
                           sin( radians( restoran_latitude ) ) )
                         ) AS distance', [$latitude, $longitude, $latitude])
            ->havingRaw("distance <?",[$radius])
            ->where("restoran_status" , "=" , "aktif")
            // ->where("restoran_balance" , ">" , 50)
            ->orderBy('distance')
            ->get();

        return $offers;
    }

    public function get_restaurant($latitude, $longitude, $id){

        $offers = Restoran::select('restoran.*')
            ->selectRaw('( 6371* acos( cos( radians(?) ) *
                           cos( radians( restoran_latitude ) )
                           * cos( radians( restoran_longitude ) - radians(?)
                           ) + sin( radians(?) ) *
                           sin( radians( restoran_latitude ) ) )
                         ) AS distance', [$latitude, $longitude, $latitude])
            ->where("restoran_status" , "=" , "aktif")
            ->where("id" , "=",$id)
            // ->where("restoran_balance" , ">" , 50)
            ->first();

        return $offers;
    }
}
