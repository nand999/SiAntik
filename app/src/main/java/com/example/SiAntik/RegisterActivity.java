package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Button buttonKembali,btnDaftar;
    EditText edtId,edtNama,edtbRt,edtPass,edtPassCon,edtNo;
    ImageButton btnEye;
    private boolean isPasswordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register2);

        buttonKembali  = (Button) findViewById(R.id.ButtonKembali1);
        btnDaftar = (Button) findViewById(R.id.btn_daftar);
        btnEye = (ImageButton) findViewById(R.id.btn_eyeReg);
        edtId = (EditText) findViewById(R.id.EditTextNIK);
        edtNama = (EditText) findViewById(R.id.EditTextNama);
        edtbRt = (EditText) findViewById(R.id.EditTextRt);
        edtNo = (EditText) findViewById(R.id.EditTextNo);
        edtPass = (EditText) findViewById(R.id.EditTextPassReg);
        edtPassCon = (EditText) findViewById(R.id.EditTextPassCon);
        buttonKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKembali = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentKembali);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nik = edtId.getText().toString();
                String nama = edtNama.getText().toString();
                String pass = edtPass.getText().toString();
                String passKon = edtPassCon.getText().toString();
                String rt_rw = edtbRt.getText().toString();
                String no_rumah = edtNo.getText().toString();
                int panjangNIK = edtId.getText().toString().length();
                int panjangPass = pass.length();

                if (pass.isEmpty() || passKon.isEmpty() || no_rumah.isEmpty() || rt_rw.isEmpty() || nik.isEmpty() || nama.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Lengkapi semua data terlebih dahulu",Toast.LENGTH_SHORT).show();
                    return;
                } else if (panjangNIK<12 || panjangNIK > 12){
                    Toast.makeText(RegisterActivity.this,"panjang NIK tidak sesuai",Toast.LENGTH_SHORT).show();
                    return;
                } else if (panjangPass<8) {
                    Toast.makeText(RegisterActivity.this,"panjang sandi minimal 8 karakter",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pass.equals(passKon)) {
                    Toast.makeText(RegisterActivity.this,"sandi dan konfirmasi sandi tidak sesuai",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidRtRwFormat(rt_rw)) {
                    Toast.makeText(RegisterActivity.this, "Format RT/RW harus 00/00", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika format RT/RW tidak valid
                }else if (!isValidNoFormat(no_rumah)) {
                    Toast.makeText(RegisterActivity.this, "Format No Rumah harus 00", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika format RT/RW tidak valid
                } else {
                    daftarPengguna();
                }
            }
        });

        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Jika password sedang terlihat, sembunyikan teks
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPassCon.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // Jika password sedang tersembunyi, tampilkan teks
                    edtPass.setTransformationMethod(null);
                    edtPassCon.setTransformationMethod(null);
                }
                isPasswordVisible = !isPasswordVisible;
                // Perbarui ikon mata sesuai dengan status isPasswordVisible
//                int imgResource = isPasswordVisible ? R.drawable.ic_eye_open : R.drawable.ic_eye;
//                btnEye.setImageResource(imgResource);
            }
        });


    }




    private void daftarPengguna() {
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        // Ganti dengan detail registrasi Anda
        String idakun = edtId.getText().toString();
        String nama_user = edtNama.getText().toString();
        String rt_rw = edtbRt.getText().toString();
        String no = edtNo.getText().toString();
        String password_user = edtPass.getText().toString();

        Call<UserResponse> panggilan = layananApi.register(idakun, nama_user, rt_rw, no, password_user);

        panggilan.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        String status = userResponse.getStatus();
                        String message = userResponse.getMessage();

                        if ("success".equals(status)) {
                            UserModel pengguna = userResponse.getData();

                            // intent kembali ke login
                            Intent intentKembali = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intentKembali);
                            // toast berhasil
                            Toast.makeText(RegisterActivity.this, "berhasil mendaftar, silahkan login", Toast.LENGTH_SHORT).show();

                            if (pengguna != null) {
                                // Registrasi berhasil, akses detail pengguna
                                String idAkun = pengguna.getNik_user();
                                String namaPengguna = pengguna.getNama_user();
                                String rt_rw = pengguna.getRt_rw();
                                // Lakukan sesuatu dengan data pengguna yang berhasil diregistrasi
                            }

                        } else {
                            // Registrasi gagal, tangani pesan kesalahan jika diperlukan
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Tangani kesalahan respons HTTP
                    Toast.makeText(RegisterActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Tangani kegagalan komunikasi (misalnya, masalah jaringan)
                Toast.makeText(RegisterActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Metode untuk memeriksa format RT/RW
    private boolean isValidRtRwFormat(String rt){
        // Format yang diinginkan adalah "00/00"
        String regexPattern = "\\d{2}/\\d{2}";
        return rt.matches(regexPattern);
    }

    private boolean isValidNoFormat(String rt){
        // Format yang diinginkan adalah "00/00"
        String regexPattern = "\\d{2}";
        return rt.matches(regexPattern);
    }


}

