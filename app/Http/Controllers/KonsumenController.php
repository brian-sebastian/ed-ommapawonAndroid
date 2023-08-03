<?php

namespace App\Http\Controllers;

use App\Konsumen as AppKonsumen;
use App\Models\Konsumen;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class KonsumenController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return view('konsumen.konsumen', [
            'title' => 'Konsumen',
            'konsumens' => AppKonsumen::paginate(15)
        ]);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
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
     * @param  \App\Models\Konsumen  $konsumen
     * @return \Illuminate\Http\Response
     */
    public function show(AppKonsumen $konsumen)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Konsumen  $konsumen
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        $konsumen = AppKonsumen::find($id);
        return view('konsumen.edit', [
            'konsumen' => $konsumen,
            'title' => 'Edit Data Konsumen'
        ]);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Konsumen  $konsumen
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $rules = [
            'nama_konsumen' => 'required|string|max:255',
            'email' => 'required|string',
            'nomor_telpon' => 'required|string|max:255',
        ];

        $validatedData = $request->validate($rules);

        $konsumen = AppKonsumen::find($id);
        $konsumen->update($validatedData);

        return redirect('/konsumen')->with('success', 'Konsumen berhasil diedit');
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Konsumen  $konsumen
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $konsumen = AppKonsumen::findOrFail($id);
        $konsumen->delete();
        return redirect('/konsumen')->with('success', 'Konsumen berhasil dihapus');
    }
}
