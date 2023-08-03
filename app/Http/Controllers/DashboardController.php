<?php

namespace App\Http\Controllers;

use App\DetailOrder;
use App\Konsumen;
use App\Order;
use App\Profit;
use App\Restoran;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Http;
use League\CommonMark\Extension\SmartPunct\EllipsesParser;
use PDO;
use PHPUnit\Framework\Constraint\IsEmpty;


use function PHPUnit\Framework\isEmpty;

class DashboardController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {


         $now = Carbon::now();
         $month = $now->month;
         $year = $now->year;
 
         $dataOrder = Order::whereDate('created_at', '=', $now)->get();

         
        

         //Antrian
         $statusAntrian = Order::where('order_status','=','antrian')->get();
      
         //Transfer
         $statusMenunggu = Order::where('order_status','=','menunggu')->get();

         return view('dashboard', compact('dataOrder'),[
                'title' => 'Dashboard',
                'restorans' => Restoran::all(),
                // 'json' => $json,
                'orderantrian' => $statusAntrian,
                'ordermenunggu' => $statusMenunggu
               
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
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
          //Transfer
       $transfer = Order::find($id);
    
       return view('edit', [
      
        'detailpesanan' => $transfer,
        'title' => 'Edit Menu'
    ]);
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
        $rules = ['order_status' => 'required'];

        $validatedData = $request->validate($rules);
      
        $transfer = Order::find($id);
        $transfer->update($validatedData);
        return redirect('/dashboard')->with('success','Berhasil diedit');
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

    
}
