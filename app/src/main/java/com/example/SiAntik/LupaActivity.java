package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LupaActivity extends AppCompatActivity {
    Button buttonKembali,btnSimpan;
    EditText edtNik,edtNama,edtPass,edtPassKon;
    ImageButton btnEye;
    private boolean isPasswordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lupa_sandi);
        buttonKembali = (Button) findViewById(R.id.ButtonKembali);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnEye = (ImageButton) findViewById(R.id.btn_eyeLup);
        edtNik = (EditText) findViewById(R.id.EditTextNIKLupa);
        edtNama = (EditText) findViewById(R.id.EditTextNamaLupa);
        edtPass = (EditText) findViewById(R.id.EditTextPassBaru);
        edtPassKon = (EditText) findViewById(R.id.EditTextPassBaruKon);
        isPasswordVisible = false;

        edtNik.requestFocus();


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

        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Jika password sedang terlihat, sembunyikan teks
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPassKon.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // Jika password sedang tersembunyi, tampilkan teks
                    edtPass.setTransformationMethod(null);
                    edtPassKon.setTransformationMethod(null);
                }
                isPasswordVisible = !isPasswordVisible;
                // Perbarui ikon mata sesuai dengan status isPasswordVisible
//                int imgResource = isPasswordVisible ? R.drawable.ic_eye_open : R.drawable.ic_eye;
//                btnEye.setImageResource(imgResource);
            }
        });

        edtNik.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    edtNama.requestFocus();
                }
                return false;
            }
        });


        edtNik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtNik.getText().toString().length()<16){
                    edtNik.setError("Panjang NIK harus 16");
                } else if (edtNik.getText().toString().length()>16) {
                    edtNik.setError("Panjang NIK harus 16");
                } else{
                    edtNik.setError(null);
                }
            }
        });


        edtNama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    edtPass.requestFocus();
                }
                return false;
            }
        });

        edtPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    edtPassKon.requestFocus();
                }
                return false;
            }
        });

        edtPassKon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    btnSimpan.isPressed();
                }
                return false;
            }
        });

        edtPassKon.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    btnSimpan.isPressed();
                    return true;
                }
                return false;
            }
        });




    }

    private void resetPassword() {
        String nik = edtNik.getText().toString();
        String nama = edtNama.getText().toString().toLowerCase();
        String newPassword = edtPass.getText().toString();
        String confirmPassword = edtPassKon.getText().toString();
        int panjangPass = newPassword.length();

        if (nik.isEmpty() || nama.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Kata sandi baru dan konfirmasi kata sandi tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nik.length()<16 || nik.length()>16){
            Toast.makeText(this, "panjang NIK tidak sesuai", Toast.LENGTH_SHORT).show();
            return;
        }else if (panjangPass<8) {
            Toast.makeText(this,"panjang sandi minimal 8 karakter",Toast.LENGTH_SHORT).show();
            return;
        }else if (panjangPass>12) {
            Toast.makeText(this,"panjang sandi maksimal 12 karakter",Toast.LENGTH_SHORT).show();
            return;
        }
        performPasswordReset(nik, nama, newPassword);
    }

    private void performPasswordReset(String nik, String nama, String newPassword) {
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
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
