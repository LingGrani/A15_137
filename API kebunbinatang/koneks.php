<?php
$server		= "localhost";
$user		= "root";
$password	= ""; // Silahkan isi password sesuai dengan password database Anda *jika menggunakan password
$namadb	    = "pam_ucp";

$conn = mysqli_connect($server, $user, $password, $namadb) or die("Koneksi gagal!");
