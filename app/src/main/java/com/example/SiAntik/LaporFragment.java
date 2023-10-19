package com.example.SiAntik;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import android.net.Uri;
import android.database.Cursor;


import static android.app.Activity.RESULT_OK;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.SiAntik.cadangan.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LaporFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 102;

    private ImageButton btnSelectImage;
    private ImageView imageView;
    private Button btnSend,btnClear;
    private EditText edtDes;

    private ApiService apiService;

    public LaporFragment() {
        // Required empty public constructor
    }

    public static LaporFragment newInstance(String param1, String param2) {
        LaporFragment fragment = new LaporFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lapor, container, false);

        btnSelectImage = rootView.findViewById(R.id.btn_pilGam);
        imageView = rootView.findViewById(R.id.imageView);
        btnSend = rootView.findViewById(R.id.btn_kirim);
        btnClear = rootView.findViewById(R.id.btn_clear);
        edtDes = rootView.findViewById(R.id.edtDes);


        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://your-api-base-url/") // Ganti dengan URL API Anda
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(ApiService.class);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtDes.setText("");
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendImageToServer().execute();
            }
        });

        // Mengecek dan meminta izin
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        }

        return rootView;
    }

    private void selectImage() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri", "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Tambahkan Foto");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Ambil Foto")) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            } else if (items[item].equals("Pilih dari Galeri")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih File"), SELECT_FILE);
            } else if (items[item].equals("Batal")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Gambar diambil dari kamera
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);

                // Tampilkan pesan untuk memastikan bahwa gambar telah diambil
                Toast.makeText(requireActivity(), "Gambar dari kamera diambil", Toast.LENGTH_SHORT).show();
            } else if (requestCode == SELECT_FILE) {
                // Gambar dipilih dari galeri
                Uri selectedImageUri = data.getData();
                String imagePath = getPathFromUri(selectedImageUri);
                if (imagePath != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(requireActivity(), "Gagal mendapatkan path file gambar", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION || requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            // Proses hasil izin di sini

            // Cek apakah izin diberikan atau tidak
            boolean izinDiberikan = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    izinDiberikan = false;
                    break;
                }
            }

            if (izinDiberikan) {
                // Izin diberikan, lanjutkan dengan tindakan selanjutnya
                selectImage();
            } else {
                // Izin tidak diberikan, berikan pemberitahuan kepada pengguna
                Toast.makeText(requireActivity(), "Izin diperlukan untuk memilih gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class SendImageToServer extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Mengambil gambar dari ImageView
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                // Kompresi gambar jika ukurannya lebih besar dari 64 KB
                int maxSizeKB = 64;
                int maxSizeBytes = maxSizeKB * 1024;
                int currentSize = bitmap.getByteCount() / 1024;

                if (currentSize > maxSizeKB) {
                    float scale = (float) Math.sqrt(maxSizeBytes / (float) currentSize);
                    int width = Math.round(bitmap.getWidth() * scale);
                    int height = Math.round(bitmap.getHeight() * scale);

                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                }

                // Mengonversi gambar menjadi byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Mengonversi byte array menjadi string base64
                String imageData = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // Membuat objek ImageModel
                ImageModel imageModel = new ImageModel(imageData);

                // Memanggil Retrofit untuk mengirim gambar ke server
                Call<Void> call = apiService.uploadImage(imageModel);
                Response<Void> response = call.execute();

                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            if (isSuccess) {
                Toast.makeText(requireActivity(), "Gambar Terkirim ke Database", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireActivity(), "Gagal Mengirim Gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
