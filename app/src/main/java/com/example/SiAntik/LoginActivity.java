package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.SiAntik.cadangan.ApiClient;
import com.example.SiAntik.cadangan.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    Button ButtonReg;
    Button ButtonLupa;
    Button ButtonLogin;
    private EditText etNikUser, etPassword;
    private ApiService apiService;
    private String KEY_NAMA = "NAMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        ButtonReg = (Button) findViewById(R.id.ButtonRegister);
        ButtonLupa = (Button) findViewById(R.id.ButtonLupa);
        ButtonLogin = (Button) findViewById(R.id.ButtonLogin);
//        etNikUser = (EditText) findViewById(R.id.EditTextUsername);
        etPassword = (EditText) findViewById(R.id.EditTextPass);
        username = (EditText) findViewById(R.id.EditTextUsername);
        apiService = ApiClient.getClient().create(ApiService.class);


        ButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentReg);
            }
        });

        ButtonLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLupa = new Intent(getApplicationContext(), LupaActivity.class);
                startActivity(intentLupa);
            }
        });

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String nama = username.getText().toString();
//                Intent intentLogin = new Intent(getApplicationContext(), MainActivity.class);
//                intentLogin.putExtra("NAMA", nama);
//                startActivity(intentLogin);
//                loginUser();
                loginUser1();
            }
        });

    }

    private void loginUser1() {

//        Toast.makeText(this, username.getText().toString() + " // " + etPassword.getText().toString(), Toast.LENGTH_SHORT).show();

        RetrofitClient.getConnection().create(RetrofitEndPoint.class)
                .login(username.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.body() != null && response.body().getStatus().equalsIgnoreCase("success")) {
                            // Membuat Intent untuk membuka aktivitas berikutnya
                            String nama = username.getText().toString();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("NAMA", nama);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }


    // LOGIN RETROFIT LAMA
//    private void loginUser() {
//        String nikUser = etNikUser.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        // Validasi input
//        if (nikUser.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Isi Nama dan kata sandi terlebih dahulu", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Panggil Retrofit untuk melakukan login
//        Call<User> call = apiService.loginUser(nikUser, password);
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    // Proses hasil login yang berhasil
//                    User user = response.body();
//                    String namaUser = user.getNamaUser();
//                    Toast.makeText(getApplicationContext(), "Login berhasil. Selamat datang, " + namaUser, Toast.LENGTH_SHORT).show();
//                    // ambil intent
//                    String nama = username.getText().toString();
//                    Intent intentLogin = new Intent(getApplicationContext(), MainActivity.class);
//                    intentLogin.putExtra("NAMA", nama);
//                    startActivity(intentLogin);
//                } else {
//                    // Proses hasil login yang gagal
//                    Toast.makeText(getApplicationContext(), "Login gagal. Periksa kembali NIK dan kata sandi.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                // Handle kegagalan koneksi atau panggilan API
//                Toast.makeText(getApplicationContext(), "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}