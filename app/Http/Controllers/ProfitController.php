<?php

namespace App\Http\Controllers;

use App\Models\Penghasilan_Harian;
use App\Profit;
use Illuminate\Http\Request;
use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\DB;

class ProfitController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        // return view('penghasilan_harian.penghasilan_harian', [
        //     'title' => 'Penghasilan Harian',
        //     'penghasilan_harians' => Penghasilan_Harian::paginate(15)
        // ]);
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
     * @param  \App\Models\Penghasilan_Harian  $penghasilan_Harian
     * @return \Illuminate\Http\Response
     */
    public function show(Profit $penghasilan_Harian)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\Penghasilan_Harian  $penghasilan_Harian
     * @return \Illuminate\Http\Response
     */
    public function edit(Profit $penghasilan_Harian)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Penghasilan_Harian  $penghasilan_Harian
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, Profit $penghasilan_Harian)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Penghasilan_Harian  $penghasilan_Harian
     * @return \Illuminate\Http\Response
     */
    public function destroy(Profit $penghasilan_Harian)
    {
        //
    }

    public function harian()
    {
        //
        $now = Carbon::now();
        $yesterday = Carbon::yesterday();
        $month = $now->month;
        $year = $now->year;

        $dataProfitHarian = Profit::whereDate('created_at', '=', $now)->get();

        $dataProfitHarianKemarin = Profit::whereDate('created_at', '=', $yesterday)->get();


        return view('penghasilan_harian.penghasilan_harian',compact('dataProfitHarian','dataProfitHarianKemarin'), [
            'title' => 'Penghasilan Harian',
            'penghasilan_harians' => Profit::paginate(15)
        ]);
    }


    public function bulanan(){
        $now = Carbon::now();
        $month = $now->month;
        $year = $now->year;

        $dataProfitHarian = Profit::whereDate('created_at', '=', $now)->get();

        $dataProfitBulanan = Profit::select(
            DB::raw('sum(biaya) as total'),
            DB::raw("DATE_FORMAT(created_at,'%M %Y') as months")
        )
            ->groupBy('months')
            ->where('status','sukses')
            ->get();

        return view('penghasilan_bulanan.penghasilan_bulanan',compact('dataProfitBulanan'), 
            [ 
                'title' =>'Penghasilan Bulanan', 
                'penghasilan_bulanans' => Profit::paginate(15)
            ]);
    }
    
}
