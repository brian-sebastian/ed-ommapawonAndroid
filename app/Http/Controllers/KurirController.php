<?php

namespace App\Http\Controllers;

use App\Kurir as AppKurir;
use App\Models\Kurir;
use App\Restoran;
use Illuminate\Http\Request;
use Kreait\Firebase\Factory;

class KurirController extends Controller
{
    protected $auth, $database;
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        return view('kurir.kurir', [
            'title' => 'Kurir',
            'kurirs' => AppKurir::paginate(15)
        ]);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        return view('kurir.create', [
            'title' => 'Tambah Menu',
            'rumah_makans' => Restoran::all()
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

        $factory = (new Factory)
        ->withServiceAccount(__DIR__.'/sms-otp-87b50-firebase-adminsdk-yih88-884fc40282.json')
        ->withDatabaseUri('https://sms-otp-87b50-default-rtdb.firebaseio.com/');

        $auth = $this->auth = $factory->createAuth();
        // $this->database = $factory->createDatabase();

        $validatedData = $request->validate([
            'nama_kurir' => 'required|unique:kurir|string|max:255',
            'nomor_telp_kurir' => 'required|integer',
            'email_kurir' => 'required|string|max:255',
            'id_restoran' => 'required',
        ]);

        // $data = AppKurir::all(
        //     // $validatedData
        //     );

        // AppKurir::create($$data);

        // $createdUser = AppKurir::create($data);
         AppKurir::create($validatedData);
        // $auth->createCustomToken($createdUser);

        return redirect('/kurir')->with('success', 'Menu berhasil ditambahkan');
    }

    /**
     * Display the specified resource.
     *
     * @param  \App\Models\Kurir  $kurir
     * @return \Illuminate\Http\Response
     */
    public function show(AppKurir $kurir)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Kurir  $kurir
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        $kurir = AppKurir::find($id);
        return view('kurir.edit', [
            'kurir' => $kurir,
            'title' => 'Edit Data kurir'
        ]);
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Kurir  $kurir
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $rules = [
            'nama_kurir' => 'required|string|max:255',
            'email_kurir' => 'required|string',
            'nomor_telp_kurir' => 'required|string|max:255',
        ];

        $validatedData = $request->validate($rules);

        $kurir = AppKurir::find($id);
        $kurir->update($validatedData);

        return redirect('/kurir')->with('success', 'Kurir berhasil diedit');
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Kurir  $kurir
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        $kurir = AppKurir::findOrFail($id);
        $kurir->delete();
        return redirect('/kurir')->with('success', 'kurir berhasil dihapus');
    }
}
