package com.example.SiAntik;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.SiAntik.cadangan.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LaporFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_STORAGE_PERMISSION = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 102;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 103;

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
    private File imageFile;

    private ApiService apiService;
    private int imageType = -1;
    private String statusMessage;
    private boolean isAlreadyReported = false;
    private static final int IMAGE_WIDTH = 960; // Lebar gambar yang lebih tinggi
    private static final int IMAGE_HEIGHT = 1280; // Ganti dengan tinggi yang Anda inginkan
    private static final int IMAGE_QUALITY = 100; // Kualitas gambar (0-100)
    private Uri photoURI;
    private Uri imageUri;
    private ContentValues values;
    private Bitmap thumbnail;



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
        if (getArguments() != null) {
            statusMessage = getArguments().getString("status");

            if (statusMessage != null && statusMessage.equals("sudah_lapor")) {
                isAlreadyReported = true;
                btnSelectImage.setEnabled(false); // Menonaktifkan tombol pilih gambar
            }
        }
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

        if (getArguments() != null) {
            statusMessage = getArguments().getString("status");

            if (statusMessage != null && statusMessage.equals("sudah_lapor")) {
                isAlreadyReported = true;
                btnSelectImage.setEnabled(false); // Menonaktifkan tombol pilih gambar
            }
        }

        checkLaporanStatus();




        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSemua();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAlreadyReported) {
                    // Menampilkan pesan peringatan jika sudah melapor
                    showAlreadyReportedWarning();
                } else {
                    showPictureDialog();
                }
            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetImageNameFromEditText = edtDes.getText().toString();
                if (FixBitmap != null) {

                    new AlertDialog.Builder(getContext())
                            .setTitle("Kirim Laporan")
                            .setMessage("Apakah Anda yakin laporan sudah benar?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    UploadImageToServer();
                                    clearSemua();
                                    alertSudah();
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(getContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

//    private void showPictureDialog() {
//        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getContext());
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Ambil Dari Galeri",
//                "Ambil Foto Dari Kamera"
//        };
//        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        choosePhotoFromGallery();
//                        break;
//                    case 1:
//                        takePhotoFromCamera();
//                        break;
//                }
//            }
//        });
//        pictureDialog.show();
//    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        imageType = CAMERA;

        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CAMERA);
        startActivityForResult(intent, CAMERA);
    }


    //gpt baru

    private void showPictureDialog() {
        // Memeriksa apakah izin kamera telah diberikan
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kamera
            requestCameraPermission();
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        } else {
            // Jika izin sudah diberikan, tampilkan dialog pilihan aksi
            displayPictureDialog();
        }
    }

    // Metode untuk meminta izin kamera
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA_PERMISSION);
    }

    // Metode untuk meminta izin penyimpanan
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_STORAGE_PERMISSION);
    }

    private void displayPictureDialog() {
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

    // Metode untuk menangani hasil permintaan izin
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, tampilkan dialog pilihan aksi
                displayPictureDialog();
            } else {
                // Izin ditolak, berikan pesan kepada pengguna
                Toast.makeText(getContext(), "Izin kamera ditolak", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, tampilkan dialog pilihan aksi
                displayPictureDialog();
            } else {
                // Izin ditolak, berikan pesan kepada pengguna
                Toast.makeText(getContext(), "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }





    //gpt baru



//    private File createImageFile() {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp;
//        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = null;
//
//        try {
//            image = File.createTempFile(imageFileName, ".jpg", storageDir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return image;
//    }

                        //LAMA
//    private void takePhotoFromCamera() {
//        imageType = CAMERA;
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
//    }





//    private void takePhotoFromCamera() {
//        // Check for runtime permissions
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Request permission if it is not granted
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
//        } else {
//            // Proceed with taking a photo
//            startCameraIntent();
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with taking a photo
//                startCameraIntent();
//            } else {
//                // Permission denied, show a message or handle accordingly
//                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private void startCameraIntent() {
        if (imageUri != null) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA);
        } else {
            Toast.makeText(getContext(), "Failed to start camera", Toast.LENGTH_SHORT).show();
        }
    }




            //LAMA
//private Bitmap handleCapturedImage(Intent data) {
//    byteArrayOutputStream = new ByteArrayOutputStream();
//    if (data == null || data.getExtras() == null || !data.getExtras().containsKey("data")) {
//        // Handling jika data dari pengambilan gambar tidak valid
//        Toast.makeText(getContext(), "Gagal mengambil gambar", Toast.LENGTH_SHORT).show();
//        return null;
//    }
//
//    FixBitmap = (Bitmap) data.getExtras().get("data");
//
//    BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inSampleSize = calculateInSampleSize(IMAGE_WIDTH, IMAGE_HEIGHT, FixBitmap.getWidth(), FixBitmap.getHeight());
//    Bitmap scaledBitmap = Bitmap.createScaledBitmap(FixBitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
//
////    // Simpan gambar ke berkas
////    File imageFile = createImageFile();
////    if (imageFile != null) {
////        try (FileOutputStream out = new FileOutputStream(imageFile)) {
////            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, out);
////        } catch (IOException e) {
////            e.printStackTrace();
////            Toast.makeText(getContext(), "Failed to create image file.", Toast.LENGTH_SHORT).show();
////            return null;
////        }
////    }
//
//    return scaledBitmap; // Mengembalikan gambar yang telah diubah (scaledBitmap) yang seharusnya disimpan.
//}

    private Bitmap handleCapturedImage(Intent data) {
        byteArrayOutputStream = new ByteArrayOutputStream();
        if (imageUri == null) {
            // Handling jika URI gambar tidak valid
            Toast.makeText(getContext(), "Failed to get image URI", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {
            thumbnail = MediaStore.Images.Media.getBitmap(
                    getActivity().getContentResolver(), imageUri);

            // Resize image if needed
            thumbnail = Bitmap.createScaledBitmap(thumbnail, IMAGE_WIDTH, IMAGE_HEIGHT, true);

            // Set the scaled bitmap to your FixBitmap variable
            FixBitmap = thumbnail;

            return FixBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to handle captured image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }






    private int calculateInSampleSize(int reqWidth, int reqHeight, int width, int height) {
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

            //LAMA
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == GALLERY) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//                    FixBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
//                    imageView.setImageBitmap(FixBitmap);
////                    btnSelectImage.setImageBitmap(FixBitmap);
//                    btnSend.setVisibility(View.VISIBLE);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getContext(), "Gagal!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }  else if (requestCode == CAMERA) {
//            FixBitmap = handleCapturedImage(data);
//            if (FixBitmap != null) {
//                imageView.setImageBitmap(FixBitmap);
////                btnSelectImage.setImageBitmap(FixBitmap);
//                btnSend.setVisibility(View.VISIBLE);
//            }
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == REQUEST_CAMERA) {
//            FixBitmap = handleCapturedImage(data);
//            if (FixBitmap != null) {
//                imageView.setImageBitmap(FixBitmap);
//                btnSend.setVisibility(View.VISIBLE);
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && data != null) {
            // Handle image from gallery
            Uri contentURI = data.getData();
            try {
                FixBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                imageView.setImageBitmap(FixBitmap);
                btnSend.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Gagal!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            // Handle image from camera
            FixBitmap = handleCapturedImage(data);
            if (FixBitmap != null) {
                imageView.setImageBitmap(FixBitmap);
//                btnSelectImage.setImageBitmap(FixBitmap);
                btnSend.setVisibility(View.VISIBLE);
            }
        }
    }






//method upload gambar
    public void UploadImageToServer() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        if (FixBitmap != null) {
            if (imageType == CAMERA) {
                // Jika gambar diambil dari kamera, gunakan format PNG untuk menghindari kompresi
//                FixBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                FixBitmap = Bitmap.createScaledBitmap(FixBitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true);
                FixBitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, byteArrayOutputStream);

            } else {
                // Jika gambar diambil dari galeri, gunakan format JPEG dengan kualitas 100
                FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            }
            byteArray = byteArrayOutputStream.toByteArray();
            ConvertImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);

            class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                private WeakReference<Context> contextRef;

                AsyncTaskUploadClass(Context context) {
                    this.contextRef = new WeakReference<>(context);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    Context context = contextRef.get();
                    if (context != null) {
                        progressDialog = ProgressDialog.show(context, "Foto sedang di unggah", "Mohon Tunggu....", false, false);
                    } else {
                        // Handle jika context null (mungkin activity/fragment sudah dihancurkan)
                        Log.e("AsyncTaskError", "Context is null in onPreExecute");
                        cancel(true); // Batalkan AsyncTask jika context null
                    }
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);

                    Context context = contextRef.get();
                    if (context != null) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Gambar berhasil diunggah", Toast.LENGTH_LONG).show();
                    } else {
                        // Handle jika context null (mungkin activity/fragment sudah dihancurkan)
                        Log.e("AsyncTaskError", "Context is null in onPostExecute");
                    }
                }

                @Override
                protected String doInBackground(Void... params) {
                    ImageProcessClass imageProcessClass = new ImageProcessClass();
                    HashMap<String, String> HashMapParams = new HashMap<>();
                    SharedPreferences sharedPref = contextRef.get().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    String nik_user = sharedPref.getString("NIK", ""); // Mendapatkan nik_user dari SharedPreferences

                    String deskripsi = edtDes.getText().toString();

                    HashMapParams.put(ImageTag, GetImageNameFromEditText);
                    HashMapParams.put(ImageName, ConvertImage);
                    HashMapParams.put(Deskripsi, deskripsi);
                    HashMapParams.put("nik_user", nik_user);

                    String BASE_URL = RetrofitClient.BASE_URL;

//                    return imageProcessClass.ImageHttpRequest("http://192.168.137.1:8080/siantik/mobile/upGambar.php", HashMapParams);
                    return imageProcessClass.ImageHttpRequest(BASE_URL + "upGambar.php", HashMapParams);
                }
            }

            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass(getContext());
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

    private void showAlreadyReportedWarning() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Anda sudah melapor bulan ini");
        alertDialogBuilder.setMessage("Anda tidak bisa melaporkan lebih dari sekali per bulan.");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    private void checkLaporanStatus() {
        Bundle extras = getActivity().getIntent().getExtras();
        String nik_user = extras.getString("NIK");
//        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String nik_user = sharedPref.getString("NIK", "");

        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        Call<StatusData1> panggilan = layananApi.getStatusData(nik_user);

        panggilan.enqueue(new Callback<StatusData1>() {
            @Override
            public void onResponse(Call<StatusData1> call, Response<StatusData1> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StatusData1 statusData = response.body();
                    String status = statusData.getStatus();

                    if (status.equals("sudah_lapor_positif")) {
                        alert();
                       btnSelectImage.setEnabled(false);
                       imageView.setImageResource(R.drawable.ic_not);
                    } else if (status.equals("belum_lapor")) {

                    } else if(status.equals("error")){

                    } else if (status.equals("sudah_lapor_negatif")) {
                        alert();
                        btnSelectImage.setEnabled(false);
                        imageView.setImageResource(R.drawable.ic_not);
                    } else if (status.equals("sudah_lapor_belum")) {
                        alert();
                        btnSelectImage.setEnabled(false);
                        imageView.setImageResource(R.drawable.ic_not);
                    }
                } else {
                    // Handle respon server tidak berhasil di sini
                    Toast.makeText(getContext(), "Gagal memeriksa status laporan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatusData1> call, Throwable t) {
                // Handle kegagalan koneksi atau panggilan API di sini
                t.printStackTrace();
            }
        });
    }

    public void alert() {
        // Tampilkan peringatan dengan hanya pilihan "Ya"
        new AlertDialog.Builder(getContext())
                .setTitle("Anda sudah lapor")
                .setMessage("Anda sudah melakukan lapor bulan ini")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Panggil method onReturnToBeranda di MainActivity
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).onReturnToBeranda();
                        }

                        // Kembali ke beranda fragment
                        Fragment berandaFragment = new BerandaFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, berandaFragment);
                        transaction.addToBackStack(null); // Jika Anda ingin menambahkannya ke tumpukan kembali
                        transaction.commit();
                    }
                })
                .show();

    }

    public void alertSudah() {
        // Tampilkan peringatan dengan hanya pilihan "Ya"
        new AlertDialog.Builder(getContext())
                .setTitle("Laporan berhasil dikirim")
                .setMessage("Anda sudah melakukan lapor")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Panggil method onReturnToBeranda di MainActivity
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).onReturnToBeranda();
                        }

                        // Kembali ke beranda fragment
                        Fragment berandaFragment = new BerandaFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, berandaFragment);
                        transaction.addToBackStack(null); // Jika Anda ingin menambahkannya ke tumpukan kembali
                        transaction.commit();
                    }
                })
                .show();

    }






}
interface OnReturnToLaporListener {
    void onReturnToLapor();
}
