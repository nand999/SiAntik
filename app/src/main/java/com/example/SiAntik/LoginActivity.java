package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    Button ButtonReg;
    Button ButtonLupa;
    Button ButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        ButtonReg = (Button) findViewById(R.id.ButtonRegister);
        ButtonLupa = (Button) findViewById(R.id.ButtonLupa);
        ButtonLogin = (Button) findViewById(R.id.ButtonLogin);

        ButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intentReg);
            }
        });

        ButtonLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLupa = new Intent(getApplicationContext(),LupaActivity.class);
                startActivity(intentLupa);
            }
        });

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentLogin);
            }
        });

    }
}