<?php

namespace App\Mail;

use Illuminate\Bus\Queueable;
use Illuminate\Mail\Mailable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Contracts\Queue\ShouldQueue;

class TolakMailable extends Mailable
{
    use Queueable, SerializesModels;

    public $restoran;

    /**
     * Create a new message instance.
     *
     * @return void
     */
    public function __construct($restoran)
    {
        //
        $this->restoran = $restoran;
    }

    /**
     * Build the message.
     *
     * @return $this
     */
    public function build()
    {
        $restoran = $this->restoran;
        return $this->view('emails.ditolak',compact('restoran'));
    }
}
