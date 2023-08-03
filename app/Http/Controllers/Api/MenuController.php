<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\MenuResource;
use App\Menu as AppMenu;
use App\Models\Menu;
use App\Restoran;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class MenuController extends Controller
{
     /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
   
    public function index(Request $request){
         //Ambil Semua Data Menu Makanan
        $id = $request->input('id');


        if($request->has('id_konsumen')) {

            $id_konsumen = $request->input('id_konsumen');

            $restoran = Restoran::where('id', $id)->first();
            if ($restoran) {


                // $menus = DB::select(DB::raw(' SELECT a.id,a.id_restoran,a.id_kategori,a.nama_makanan,a.image,a.harga,a.deskripsi,(SELECT  count(*) FROM favorit where id_menu = a.id) AS menu_jumlah_favorit,(SELECT  count(*) FROM favorit where id_menu = a.id AND id_konsumen = '.$id_konsumen.') AS menu_favorit  FROM `menu` a WHERE a.id_restoran ='.$id.' AND menu_delete=0 ;' ));

                //$kategori = DB::select(DB::raw('SELECT a.id_kategori,b.kategori_nama,b.kategori_deskripsi,count(*) AS jumlah_menu FROM `menu` a,`kategori` b  WHERE a.id_restoran ='.$id.' AND a.id_kategori = b.id GROUP BY a.id_kategori;' ));

                $m = AppMenu::where('id_restoran',$id)
                    ->where('menu_delete',0)
                    ->get();

                $m->map(function ($item) use ($id_konsumen) {
                    $item['kons'] = $id_konsumen;
                    return $item;
                });

                


                $kategori = DB::table('menu')
                    ->select('menu.id_kategori as id','kategori.kategori_nama','kategori.kategori_deskripsi',DB::raw('count(*) as total_menu'))
                    ->join('kategori', 'menu.id_kategori', '=', 'kategori.id')
                    ->where('menu.id_restoran', $id)
                    ->groupBy('menu.id_kategori','kategori.id','kategori.kategori_nama','kategori.kategori_deskripsi')
                    ->get();
                    
                $menuColl = MenuResource::collection($m);

                return [
                    'value' => '1',
                    'message' => 'success',
                    "restoran_menu" => $menuColl,
                   "restoran_kategori" =>$kategori
                ];
            } else {
                return [
                    'value' => '0',
                    'message' => 'Restoran Tidak Ditemukan Pertama',
                ];
            }

        }else {

            $restoran = Restoran::where('id', $id)->first();
            if ($restoran) {
                $menu = MenuResource::collection($restoran->menu->where('menu_delete', '0'));
                return [
                    'value' => '1',
                    'message' => 'success',
                    "restoran_menu" => $menu,
                    "restoran_kategori" => $restoran->kategori
                ];
            } else {
                return [
                    'value' => '0',
                    'message' => 'Restoran Tidak Ditemukan Kedua',
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
        $input = $request->all();

        if($request->hasFile("image")) {
            $foto = $request->file('image');
            $ext = $foto->getClientOriginalExtension();

            if ($request->file('image')->isValid()) {
                $foto_name = date('YmdHis') . ".$ext";
                $upload_patch = 'storage/foto-makanan';
                $request->file('image')->move($upload_patch, $foto_name);
                $input['image'] = $foto_name;
            }

            $menu = AppMenu::create($input);

            if ($menu){
                return [
                    'value' => '1',
                    'message' => 'Tambah Menu Berhasil',
                ];
            } else{
                return [
                    'value' => '0',
                    'message' => 'Tambah Menu Gagal',
                ];
            }


        }else {
            return [
                'value' => '0',
                'message' => 'Tambah Menu Gagal',
            ];
        }


    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    // public function showSatuan()
    // {
    //     //
    //     $satuan = Satuan::all();
    //     $satuanColl = SatuanResource::collection($satuan);
    //      return[ 'value' => '1',
    //          'message' => 'Succes',
    //          'satuan'=>$satuanColl];




    // }

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
        $menuReq = $request->all();

        $Menu = AppMenu::findOrFail($id);

            if ($Menu->update($menuReq)) {

                return [
                    'value' => '1',
                    'message' => 'Update Menu Berhasil',
                ];
            } else {
                return [
                    'value' => '0',
                    'message' => 'Update Menu Gagal',
                ];
            }
            
    }

    public function updateWithPhoto(Request $request,$id){
        
        $input = $request->all();
        $Menu = AppMenu::findOrFail($id);
        
        if($request->hasFile("image")) {
            $foto = $request->file('image');
            $ext = $foto->getClientOriginalExtension();

            if ($request->file('image')->isValid()) {
                $foto_name = date('YmdHis') . ".$ext";
                $upload_patch = 'storage/foto-makanan';
                $request->file('image')->move($upload_patch, $foto_name);
                $input['image'] = $foto_name;
            }

            $menu = $Menu->update($input);

            if ($menu){
                return [
                    'value' => '1',
                    'message' => 'Tambah Menu Berhasil',
                ];
            } else{
                return [
                    'value' => '0',
                    'message' => 'Tambah Menu Gagal',
                ];
            }


        }else {
            return [
                'value' => '0',
                'message' => 'Tambah Menu Gagal',
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
        $menu = AppMenu::findOrFail($id);

        $update =$menu->update(array('menu_delete' =>(1)));

        if ($update){
            return [
                'value' => '1',
                'message' => 'Hapus Menu Berhasil',
            ];
        } else{
            return [
                'value' => '0',
                'message' => 'Hapus Menu Gagal',
            ];
        }

    }
}
