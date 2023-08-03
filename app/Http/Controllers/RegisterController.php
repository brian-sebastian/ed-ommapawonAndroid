<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

use App\Models\User;

class RegisterController extends Controller
{
    public function index(){
        return view('register',[
            'title' => 'Registrasi'
        ]);
    }

    public function store(Request $request){

        $validatedData = $request->validate([
            'name' => 'required',
            'username' => 'required|min:5|unique:admins',
            'email' => 'required',
            'password' => 'required|min:5'

        ]);

        $validatedData['password'] = Hash::make($validatedData['password']);

        User::create($validatedData);
        //$request->session()->flash('success', 'Registrasi Berhasil');
        return redirect('/')->with('success', 'Registrasi Berhasil');

    }
}
