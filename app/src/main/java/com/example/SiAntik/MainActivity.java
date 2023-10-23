package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String nama;
    private String KEY_NAMA = "NAMA";
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        chipNavigationBar = findViewById(R.id.chipNav);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nama = extras.getString("NAMA");
        }

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
}