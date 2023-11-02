package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String nama,nik;
    private String KEY_NAMA = "NAMA";
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        chipNavigationBar = findViewById(R.id.chipNav);

//        Window window = MainActivity.getWindow();
//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//// finally change the color
//        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.ijo));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nama = extras.getString("NAMA");
            nik = extras.getString("NIK");
        }
       bottomMenu();



        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nik = sharedPreferences.getString("NIK", null);

        if (nik != null) {
            String nama = sharedPreferences.getString("NAMA", "");
            String rt = sharedPreferences.getString("RT", "");
            String noRumah = sharedPreferences.getString("NO_RUMAH", "");

            // Lakukan sesuatu dengan data pengguna (misalnya, tampilkan fragment utama atau yang sesuai)
//            Toast.makeText(getApplicationContext(), "nik: " + nik + " " + " no: " + noRumah, Toast.LENGTH_SHORT).show();
            bottomMenu();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }






    }

    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener
                (new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int i) {
                        Fragment fragment = null;
                        switch (i){
                            case R.id.menu_beranda:
                                fragment = new BerandaFragment();
                                break;
                            case R.id.menu_lapor:
                                fragment = new LaporFragment();
                                break;
                            case R.id.menu_profil:
                                fragment = new ProfilFragment();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,
                                        fragment).commit();
                    }
                });

        chipNavigationBar.setItemSelected(R.id.menu_beranda,
                true);
        // Set the initial fragment to display
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BerandaFragment())
                .commit();
    }
    public void updateNamaBeranda(String namaBaru) {
        BerandaFragment berandaFragment = (BerandaFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (berandaFragment != null) {
            berandaFragment.updateNama(namaBaru);
        }
    }

    @Override
    public void onBackPressed() {
        // Tampilkan peringatan
        new AlertDialog.Builder(this)
                .setTitle("Kembali ke login")
                .setMessage("Apakah Anda yakin ingin kembali ke halaman login?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Keluar dari aplikasi
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Batal keluar
                        dialog.dismiss();
                    }
                })
                .show();
    }
}


//bottomnav lama

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                Fragment selectedFragment = null;
//
//                switch (menuItem.getItemId()) {
//                    case R.id.beranda:
//                        selectedFragment = new BerandaFragment();
//                        break;
//                    case R.id.lapor:
//                        selectedFragment = new LaporFragment();
//                        break;
//                    case R.id.profil:
//                        selectedFragment = new ProfilFragment();
//                        break;
//                }
//
//                if (selectedFragment != null) {
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, selectedFragment)
//                            .commit();
//                }
//
//                return true;
//            }
//        });