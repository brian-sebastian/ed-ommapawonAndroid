<?php

namespace App\Http\Controllers;

use App\Kategori;
use App\Menu as AppMenu;
use App\Models\Menu;
use App\Models\RumahMakan;
use App\Order;
use App\Restoran;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class MenuController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {   
        return view('daftar_menu.daftar_menu', [
            'title' => 'Daftar Menu',
            'menus' => AppMenu::paginate(3)
        ]);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        return view('daftar_menu.create', [
            'title' => 'Tambah Menu',
            'rumah_makans' => Restoran::all(),
            'kategoris' => Kategori::all()
        ]);
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {   
        $validatedData = $request->validate([
            'image' => 'image|file|max:5120',
            'nama_makanan' => 'required|unique:menu|string|max:255',
            'harga' => 'required|integer',
            'deskripsi' => 'required|string|max:255',
            'menu_ketersediaan' => 'required',
            'id_restoran' => 'required',
            'id_kategori' => 'required'
        ]);

        if($request->file('image')){
            $validatedData['image'] = $request->file('image')->store('foto-makanan');
        }

        AppMenu::create($validatedData);

        return redirect('/daftar_menu')->with('success', 'Menu berhasil ditambahkan');
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Menu  $menu
     * @return \Illuminate\Http\Response
     */
    public function show(AppMenu $menu)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Menu  $menu
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {   
        $menu = AppMenu::find($id);
        return view('daftar_menu.edit', [
            'menu' => $menu,
            'title' => 'Edit Menu'
        ]);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Menu  $menu
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $rules = [
            'image' => 'image|file|max:5120',
            'nama_makanan' => 'required|string|max:255',
            'harga' => 'required|integer',
            'deskripsi' => 'required|string|max:255',
        ];

        $validatedData = $request->validate($rules);

        if($request->file('image')){
            if($request->oldImage){
                Storage::delete($request->oldImage);
            }
            $validatedData['image'] = $request->file('image')->store('foto-makanan');
        }

        $menu = AppMenu::find($id);
        $menu->update($validatedData);

        return redirect('/daftar_menu')->with('success', 'Menu berhasil diedit');

    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Menu  $menu
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $menu = AppMenu::findOrFail($id);
        if($menu->image){
            Storage::delete($menu->image);
        }
        $menu->delete();
        return redirect('/daftar_menu')->with('success', 'Menu berhasil dihapus');
        
    }
}

       