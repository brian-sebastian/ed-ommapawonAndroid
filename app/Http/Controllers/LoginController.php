<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class LoginController extends Controller
{

    public function index()
    {
        return view('login', [
            'title' => 'Login',
        ]);
    }

    public function login(Request $request){
        $credentials = $request->validate([
            'username' => 'required', 
            'password' => 'required'
        ]);

        if($credentials['username'] == 'admin' && $credentials['password'] == 'admin'){
            if (Auth::attempt($credentials)) {
                    $request->session()->regenerate();
         
                    return redirect()->intended('/dashboard');
                }
            return redirect('/dashboard');
        }else{
            return back()->with('loginError', 'Username Atau Password Salah');
        }
    }

    public function logout(Request $request){
        Auth::logout();
 
        $request->session()->invalidate();
     
        $request->session()->regenerateToken();
     
        return redirect('/');
    }
}
