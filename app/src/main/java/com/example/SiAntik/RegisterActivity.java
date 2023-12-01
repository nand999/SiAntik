package com.example.SiAntik;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
        edtId.requestFocus();
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
                String nama = edtNama.getText().toString().toLowerCase();
                String pass = edtPass.getText().toString();
                String passKon = edtPassCon.getText().toString();
                String rt_rw = edtbRt.getText().toString();
                String no_rumah = edtNo.getText().toString();
                int panjangNIK = edtId.getText().toString().length();
                int panjangPass = pass.length();

                if (pass.isEmpty() || passKon.isEmpty() || no_rumah.isEmpty() || rt_rw.isEmpty() || nik.isEmpty() || nama.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Lengkapi semua data terlebih dahulu",Toast.LENGTH_SHORT).show();
                    return;
                } else if (panjangNIK<16 || panjangNIK > 16){
                    Toast.makeText(RegisterActivity.this,"panjang NIK tidak sesuai",Toast.LENGTH_SHORT).show();
                    return;
                }else if ( panjangNIK > 16){
                    Toast.makeText(RegisterActivity.this,"panjang NIK tidak sesuai",Toast.LENGTH_SHORT).show();
                    return;
                } else if (panjangPass<8) {
                    Toast.makeText(RegisterActivity.this,"panjang sandi minimal 8 karakter",Toast.LENGTH_SHORT).show();
                    return;
                } else if (pass.contains(" ")) {
                    Toast.makeText(RegisterActivity.this, "Sandi tidak boleh mengandung spasi", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!pass.equals(passKon)) {
                    Toast.makeText(RegisterActivity.this,"sandi dan konfirmasi sandi tidak sesuai",Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidRtRwFormat(rt_rw)) {
                    Toast.makeText(RegisterActivity.this, "Format RT/RW harus 00/00", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika format RT/RW tidak valid
                } else if (!isValidNoFormat(no_rumah)) {
                    Toast.makeText(RegisterActivity.this, "Format No Rumah harus 00", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika format RT/RW tidak valid
                } else if (edtNama.getText().toString().substring(0,1).contains(" ")){
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada spasi diawal dan diakhir nama", Toast.LENGTH_SHORT).show();
                } else if ((edtNama.getText().toString().substring((edtNama.getText().length() -1),(edtNama.getText().toString().length())).contains(" "))) {
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada spasi diawal dan diakhir nama", Toast.LENGTH_SHORT).show();
                } else if (edtId.getText().toString().substring(0,1).contains(" ")){
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada spasi diawal dan diakhir NIK", Toast.LENGTH_SHORT).show();
                } else if ((edtId.getText().toString().substring((edtId.getText().length() -1),(edtId.getText().toString().length())).contains(" "))) {
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada spasi diawal dan diakhir NIK", Toast.LENGTH_SHORT).show();
                } else if (edtId.getText().toString().contains(" ")) {
                    Toast.makeText(RegisterActivity.this, "Tidak boleh ada spasi diawal dan diakhir NIK", Toast.LENGTH_SHORT).show();
                }  else if (!isValidRwLimit(rt_rw)) {
                    Toast.makeText(RegisterActivity.this, "RW hanya sampai RW 06", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika RW tidak sesuai batasan
                } else if (!isValidRtForRw(rt_rw)) {
                    Toast.makeText(RegisterActivity.this, "RT tidak sesuai dengan RW", Toast.LENGTH_SHORT).show();
                    return; // Berhenti dari metode jika RT tidak sesuai dengan RW
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
                    btnEye.setImageResource(R.drawable.ic_eye_close);
                } else {
                    // Jika password sedang tersembunyi, tampilkan teks
                    edtPass.setTransformationMethod(null);
                    edtPassCon.setTransformationMethod(null);
                    btnEye.setImageResource(R.drawable.ic_eye);
                }
                isPasswordVisible = !isPasswordVisible;
                // Perbarui ikon mata sesuai dengan status isPasswordVisible
//                int imgResource = isPasswordVisible ? R.drawable.ic_eye_open : R.drawable.ic_eye;
//                btnEye.setImageResource(imgResource);
            }
        });

            edtId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE ){
                      edtNama.requestFocus();
                    }
                    return false;
                }
            });

        edtNama.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    edtbRt.requestFocus();
                }
                return false;
            }
        });

        edtbRt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    edtNo.requestFocus();
                }
                return false;
            }
        });

        edtNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    edtPassCon.requestFocus();
                }
                return false;
            }
        });

        edtPassCon.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT ){
                    btnDaftar.isPressed();
                    return true;
                }
                return false;
            }
        });

        edtPassCon.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    btnDaftar.isPressed();
                    return true;
                }
                return false;
            }
        });
    }




    private void daftarPengguna() {
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        // Ganti dengan detail registrasi Anda
        String idakun = edtId.getText().toString();
        String nama_user = edtNama.getText().toString().toLowerCase();
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

                        } else if ("ada0".equals(status)) {
                            Toast.makeText(RegisterActivity.this,"NIK telah terdaftar", Toast.LENGTH_SHORT).show();
                            
                        } else if ("ada1".equals(status)) {
                            Toast.makeText(RegisterActivity.this,"Nama telah terdaftar", Toast.LENGTH_SHORT).show();

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
        // Format yang diinginkan adalah "00/00 / "RT/RW"
        String regexPattern = "\\d{2}/\\d{2}";
        return rt.matches(regexPattern);
    }

    private boolean isValidNoFormat(String rt){
        // Format yang diinginkan adalah "00/00" / "RT/RW"
        String regexPattern = "\\d{2}";
        return rt.matches(regexPattern);
    }

    private boolean isValidRwLimit(String rt_rw) {
        // Mendapatkan nilai RW dari input
        String[] rwSplit = rt_rw.split("/");
        int rw = Integer.parseInt(rwSplit[1]);

        // Batasan RW tidak lebih dari 06
        return rw <= 6;
    }

    // Tambahkan method untuk memvalidasi RT sesuai dengan RW
    private boolean isValidRtForRw(String rt_rw) {
        // Mendapatkan nilai RW dari input
        String[] rwSplit = rt_rw.split("/");
        int rw = Integer.parseInt(rwSplit[1]);

        // Mendapatkan nilai RT dari input
        int rt = Integer.parseInt(rwSplit[0]);

        // Logika untuk menyesuaikan jumlah RT untuk setiap RW
        // Misalnya, jika RW 01, hanya menerima RT 01-10, jika RW 02, hanya menerima RT 01-08, dan seterusnya

        // Contoh logika validasi untuk setiap RW
        switch (rw) {
            case 1:
                return rt >= 1 && rt <= 7;
            case 2:
                return rt >= 1 && rt <= 9;
            case 3:
                return rt >= 1 && rt <= 13;
            case 4:
                return rt >= 1 && rt <= 4;
            case 5:
                return rt >= 1 && rt <= 8;
            case 6:
                return rt >= 1 && rt <= 9;
            default:
                return false; // Untuk RW yang tidak diatur, dianggap tidak valid
        }
    }


}

