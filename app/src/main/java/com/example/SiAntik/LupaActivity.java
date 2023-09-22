package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LupaActivity extends AppCompatActivity {
    Button buttonKembali;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_sandi);
        buttonKembali = (Button) findViewById(R.id.ButtonKembali);
        buttonKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKembali = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentKembali);
            }
        });

    }
}
