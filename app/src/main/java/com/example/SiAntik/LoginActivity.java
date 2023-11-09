package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.SiAntik.cadangan.ApiClient;
import com.example.SiAntik.cadangan.ApiService;
import com.example.SiAntik.cadangan.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    Button ButtonReg;
    Button ButtonLupa;
    Button ButtonLogin;
    ImageButton btnEye;
    private EditText etNikUser, etPassword;
    private ApiService apiService;
    private String KEY_NAMA = "NAMA";
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login1);
        ButtonReg = findViewById(R.id.ButtonRegister);
        ButtonLupa = findViewById(R.id.ButtonLupa);
        ButtonLogin = findViewById(R.id.ButtonLogin);
        btnEye = findViewById(R.id.btn_eye);
        etPassword = findViewById(R.id.EditTextPass);
        username = findViewById(R.id.EditTextUsername);
        apiService = ApiClient.getClient().create(ApiService.class);

        // Mendapatkan SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Mengecek apakah ada username yang disimpan
        String lastUsername = sharedPreferences.getString("LAST_USERNAME", "");

        // Mengisi kembali EditText jika ada username yang disimpan
        if (!lastUsername.isEmpty()) {
            username.setText(lastUsername);
        }

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
                if (username.getText().length() == 0 || etPassword.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (etPassword.getText().length()<8){
                    Toast.makeText(LoginActivity.this, "Panjang sandi minimal 8 karakter", Toast.LENGTH_SHORT).show();
                    return;
                } else {loginUser1();}
            }
        });

        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Jika password sedang terlihat, sembunyikan teks
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                    btnEye.setImageResource(R.drawable.ic_eye_close);
                } else {
                    // Jika password sedang tersembunyi, tampilkan teks
                    etPassword.setTransformationMethod(null);
                    isPasswordVisible = true;
                    btnEye.setImageResource(R.drawable.ic_eye);
                }
            }
        });

        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == keyEvent.KEYCODE_ENTER){
                    etPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == KeyEvent.KEYCODE_ENTER){

                    ButtonLogin.isPressed();
//                    loginUser1();
                    return true;
                }
                return false;
            }
        });

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int actionId, KeyEvent keyEvent) {
                if (actionId == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    ButtonLogin.isPressed();
                    return true;
                }
                return false;
            }
        });
    }

    private void loginUser1() {

        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        Call<UserResponse> call = retrofitEndPoint.login(username.getText().toString(), etPassword.getText().toString());
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body() != null && response.body().getStatus().equalsIgnoreCase("success")) {
                    // Login berhasil
                    UserModel userModel = response.body().getData();
                    if (userModel != null) {
                        String nama = userModel.getNama_user();
                        String nik = userModel.getNik_user();
                        String rt = userModel.getRt_rw();
                        String no = userModel.getNo_rumah();

                        // Simpan data pengguna di SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("NIK", nik);
                        editor.putString("NAMA", nama);
                        editor.putString("RT", rt);
                        editor.putString("NO_RUMAH", no);

                        // Simpan juga username yang dimasukkan pengguna
                        editor.putString("LAST_USERNAME", username.getText().toString());
                        editor.apply();

                        // Sekarang, Anda dapat menggunakannya sesuai kebutuhan
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("NAMA", nama);
                        intent.putExtra("NIK", nik);
                        intent.putExtra("RT", rt);
                        intent.putExtra("NO_RUMAH", no);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal mengambil data pengguna", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Tampilkan peringatan
        new AlertDialog.Builder(this)
                .setTitle("Keluar Aplikasi")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
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