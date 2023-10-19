package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String nama;
    private String KEY_NAMA = "NAMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nama = extras.getString("NAMA");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.beranda:
                        selectedFragment = new BerandaFragment();
                        break;
                    case R.id.lapor:
                        selectedFragment = new LaporFragment();
                        break;
                    case R.id.profil:
                        selectedFragment = new ProfilFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                }

                return true;
            }
        });

        // Set the initial fragment to display
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BerandaFragment())
                .commit();
    }
}