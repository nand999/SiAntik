package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaActivity extends AppCompatActivity {
    Button buttonKembali,btnSimpan;
    EditText edtNik,edtNama,edtPass,edtPassKon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_sandi);
        buttonKembali = (Button) findViewById(R.id.ButtonKembali);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        edtNik = (EditText) findViewById(R.id.EditTextNIKLupa);
        edtNama = (EditText) findViewById(R.id.EditTextNamaLupa);
        edtPass = (EditText) findViewById(R.id.EditTextPassBaru);
        edtPassKon = (EditText) findViewById(R.id.EditTextPassBaruKon);

        buttonKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentKembali = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentKembali);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String nik = edtNik.getText().toString();
        String nama = edtNama.getText().toString();
        String newPassword = edtPass.getText().toString();
        String confirmPassword = edtPassKon.getText().toString();

        if (nik.isEmpty() || nama.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Kata sandi baru dan konfirmasi kata sandi tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        // Panggil fungsi untuk melakukan reset sandi di sini, sesuai dengan logika Anda
        performPasswordReset(nik, nama, newPassword);
    }

    private void performPasswordReset(String nik, String nama, String newPassword) {
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        // Ganti dengan endpoint yang sesuai untuk reset kata sandi
        Call<PasswordResetResponse> panggilan = layananApi.resetPassword(nik, nama, newPassword);

        panggilan.enqueue(new Callback<PasswordResetResponse>() {
            @Override
            public void onResponse(Call<PasswordResetResponse> call, Response<PasswordResetResponse> response) {
                if (response.isSuccessful()) {
                    PasswordResetResponse resetResponse = response.body();
                    if (resetResponse != null) {
                        String status = resetResponse.getStatus();
                        String message = resetResponse.getMessage();

                        if ("success".equals(status)) {
                            // Reset kata sandi berhasil
                            Toast.makeText(LupaActivity.this, message, Toast.LENGTH_SHORT).show();

                            // Navigasi kembali ke aktivitas Login atau aktivitas lain sesuai kebutuhan
                            Intent intent = new Intent(LupaActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Reset kata sandi gagal, tangani pesan kesalahan jika diperlukan
                            Toast.makeText(LupaActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Tangani kesalahan respons HTTP
                    Toast.makeText(LupaActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PasswordResetResponse> call, Throwable t) {
                // Tangani kegagalan komunikasi (misalnya, masalah jaringan)
                Toast.makeText(LupaActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
