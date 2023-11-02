package com.example.SiAntik;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LaporFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 102;

    private ImageButton btnSelectImage;
    private ImageView imageView;
    private Button btnSend,btnClear;
    private EditText edtDes;
    Bitmap FixBitmap;
    String ImageTag = "image_tag";
    String ImageName = "image_data";
    String Deskripsi = "deskripsi";
    ProgressDialog progressDialog;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String GetImageNameFromEditText;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;

    private ApiService apiService;
    private int imageType = -1;


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

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String nik = extras.getString("NIK");
        }

        btnSelectImage = rootView.findViewById(R.id.btn_pilGam);
        imageView = rootView.findViewById(R.id.imageView);
        btnSend = rootView.findViewById(R.id.btn_kirim);
        btnClear = rootView.findViewById(R.id.btn_clear);
        edtDes = rootView.findViewById(R.id.edtDes);


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSemua();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageNameFromEditText = edtDes.getText().toString();
                if (FixBitmap != null) {
                    UploadImageToServer();
                    clearSemua();
                } else {
                    Toast.makeText(getContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Ambil Dari Galeri",
                "Ambil Foto Dari Kamera"
        };
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallery();
                        break;
                    case 1:
                        takePhotoFromCamera();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        imageType = CAMERA;
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    imageView.setImageBitmap(FixBitmap);
                    btnSend.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(FixBitmap);
            btnSend.setVisibility(View.VISIBLE);
        }
    }


    public void UploadImageToServer() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        if (FixBitmap != null) {
            if (imageType == CAMERA) {
                // Jika gambar diambil dari kamera, gunakan format PNG untuk menghindari kompresi
                FixBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            } else {
                // Jika gambar diambil dari galeri, gunakan format JPEG dengan kualitas 100
                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = ProgressDialog.show(getContext(), "Foto sedang di unggah", "Mohon Tunggu....", false, false);
                }

                @Override
                protected void onPostExecute(String string1) {
                    super.onPostExecute(string1);
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), string1, Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(Void... params) {
                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<String, String>();
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String nik_user = sharedPref.getString("NIK", ""); // Mendapatkan nik_user dari SharedPreferences

                    String deskripsi = edtDes.getText().toString();

                    HashMapParams.put(ImageTag, GetImageNameFromEditText);
                    HashMapParams.put(ImageName, ConvertImage);
                    HashMapParams.put(Deskripsi, deskripsi);
                    HashMapParams.put("nik_user", nik_user);

                    String FinalData = imageProcessClass.ImageHttpRequest("http://172.16.103.9:8080/test_siantik/mobile/upGambar.php", HashMapParams);

                    return FinalData;
                }
            }
            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
            AsyncTaskUploadClassOBJ.execute();
        } else {
            // Tampilkan pesan kesalahan karena FixBitmap masih null
            Toast.makeText(getContext(), "Gambar belum dipilih.", Toast.LENGTH_SHORT).show();
        }
    }


    public class ImageProcessClass {
        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }

    public void clearSemua() {
        imageView.setImageResource(0); // Mengosongkan ImageView
        edtDes.setText(""); // Mengosongkan EditText
    }




}
