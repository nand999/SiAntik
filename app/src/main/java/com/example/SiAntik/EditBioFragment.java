package com.example.SiAntik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditBioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditBioFragment extends Fragment {

    public EditBioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditBioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditBioFragment newInstance(String param1, String param2) {
        EditBioFragment fragment = new EditBioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_bio, container, false);
        Button btnBatal = (Button) rootView.findViewById(R.id.btnBatal);
        Button btnEdit = (Button) rootView.findViewById(R.id.btnEditUbah);
        EditText edtNama = (EditText) rootView.findViewById(R.id.edtNamaEdit);
        EditText edtRt = (EditText) rootView.findViewById(R.id.edtRtEdit);
        EditText edtNoRumah = (EditText) rootView.findViewById(R.id.edtNoEdit);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String nik = extras.getString("NIK");
            String nama1 = extras.getString("NAMA");
            String rt = extras.getString("RT");
            String no = extras.getString("NO_RUMAH");
            edtNama.setText(nama1);
            edtRt.setText(rt);
            edtNoRumah.setText(no);
        }

        Bundle profileData = getArguments();
        if (profileData != null) {
            String nik = profileData.getString("NIK");
            String nama = profileData.getString("NAMA");
            String rt = profileData.getString("RT");
            String noRumah = profileData.getString("NO_RUMAH");

            edtNama.setText(nama);
            edtRt.setText(rt);
            edtNoRumah.setText(noRumah);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaBaru = edtNama.getText().toString();
                String rtRwBaru = edtRt.getText().toString();
                String noRumahBaru = edtNoRumah.getText().toString();

                Bundle extras = getActivity().getIntent().getExtras();
                String nikUser = extras.getString("NIK");

                if (noRumahBaru.isEmpty() || rtRwBaru.isEmpty() || noRumahBaru.isEmpty() ){
                    Toast.makeText(getContext(), "Isi semua data terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidRtRwFormat(rtRwBaru)) {
                    Toast.makeText(getContext(), "Format RT/RW harus 00/00", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (!isValidNoFormat(noRumahBaru)) {
                    Toast.makeText(getContext(), "Format no rumah harus 00", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (!isValidRwLimit(rtRwBaru)) {
                    Toast.makeText(getContext(), "RW hanya sampai RW 06", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isValidRtForRw(rtRwBaru)) {
                    Toast.makeText(getContext(), "RT tidak sesuai dengan RW", Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    showConfirmationDialog(nikUser,namaBaru , rtRwBaru, noRumahBaru);
                }
            }
        });

        // Menangani tombol "Batal"
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kembali();
            }
        });

        return rootView;
    }

    private void kembali(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ProfilFragment());
        transaction.addToBackStack(null); // Optional: menambahkan transaksi ke back stack
        transaction.commit();
    }

    // Method untuk melakukan pembaruan profil menggunakan Retrofit
//    private void updateProfile(String nikUser, String namaBaru, String rtRwBaru, String noRumahBaru) {
//        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
//
//        Call<UserResponse> call = retrofitEndPoint.updateProfile(nikUser, namaBaru, rtRwBaru, noRumahBaru);
//
//        call.enqueue(new Callback<UserResponse>() {
//            @Override
//            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                if (response.isSuccessful()) {
//                    // Profil berhasil diperbarui, Anda dapat menampilkan pesan sukses
//                    Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
//
////                    MainActivity mainActivity = (MainActivity) getActivity();
////                    if (mainActivity != null) {
////                        mainActivity.updateNamaBeranda(namaBaru);
////                    }
//
//                    // Kirim data profil yang diperbarui kembali ke ProfilFragment
//                    Bundle updatedProfileData = new Bundle();
//                    updatedProfileData.putString("nama", namaBaru);
//                    updatedProfileData.putString("rt", rtRwBaru);
//                    updatedProfileData.putString("no",noRumahBaru);
//
//                    ProfilFragment profilFragment = new ProfilFragment();
//                    profilFragment.setArguments(updatedProfileData);
//
//                    // Ganti fragment kembali ke ProfilFragment
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, profilFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                } else {
//                    // Terjadi kesalahan, Anda dapat menampilkan pesan kesalahan
//                    Toast.makeText(getContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserResponse> call, Throwable t) {
//                // Terjadi kesalahan jaringan, Anda dapat menampilkan pesan kesalahan
//                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void updateProfile(String nikUser, String namaBaru, String rtRwBaru, String noRumahBaru) {
        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        Call<UserResponse> call = retrofitEndPoint.updateProfile(nikUser, namaBaru, rtRwBaru, noRumahBaru);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {

                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        String status = userResponse.getStatus();
                        String message = userResponse.getMessage();

                        if (status != null && status.equals("2")) {
                            // Menampilkan toast jika status = 0
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (status != null && status.equals("3")) {
                            Toast.makeText(getContext(), "Profil berhasil diperbarui, silahkan login ulang", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                        } else if (status != null && status.equals("1")) {
                            Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();

                            Bundle updatedProfileData = new Bundle();
                            updatedProfileData.putString("nama", namaBaru);
                            updatedProfileData.putString("rt", rtRwBaru);
                            updatedProfileData.putString("no", noRumahBaru);

                            ProfilFragment profilFragment = new ProfilFragment();
                            profilFragment.setArguments(updatedProfileData);

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment_container, profilFragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }  else {
                            Toast.makeText(getContext(), "gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Gagal memperbarui profil, tampilkan pesan kesalahan
                    Toast.makeText(getContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Gagal terhubung ke server, tampilkan pesan kesalahan jaringan
                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

//    private void checkIfNameExists(String namaBaru, String nikUser, String rtRwBaru, String noRumahBaru) {
//        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
//        Call<NameCheckResponse> call = retrofitEndPoint.checkIfNameExists(namaBaru);
//
//        call.enqueue(new Callback<NameCheckResponse>() {
//            @Override
//            public void onResponse(Call<NameCheckResponse> call, Response<NameCheckResponse> response) {
//                if (response.isSuccessful()) {
//                    NameCheckResponse nameCheckResponse = response.body();
//                    if (nameCheckResponse != null && nameCheckResponse.isNameExists()) {
//                        // Nama sudah terdaftar, tampilkan Toast
//                        Toast.makeText(getContext(), "Nama sudah terdaftar", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Nama belum terdaftar, tampilkan dialog konfirmasi
//                        showConfirmationDialog(nikUser, namaBaru, rtRwBaru, noRumahBaru);
//                    }
//                } else {
//                    // Kesalahan dalam respons, tampilkan pesan kesalahan
//                    Toast.makeText(getContext(), "Terjadi kesalahan saat memeriksa nama", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NameCheckResponse> call, Throwable t) {
//                // Gagal terhubung ke server, tampilkan pesan kesalahan jaringan
//                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Method untuk menampilkan dialog konfirmasi
    private void showConfirmationDialog(String nikUser, String namaBaru, String rtRwBaru, String noRumahBaru) {
        new AlertDialog.Builder(getContext())
                .setTitle("Ubah profil")
                .setMessage("Apakah Anda yakin ingin mengubah profil?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Lakukan pembaruan profil dengan menggunakan Retrofit
                        updateProfile(nikUser, namaBaru, rtRwBaru, noRumahBaru);

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
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