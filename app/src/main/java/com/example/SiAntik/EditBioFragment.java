package com.example.SiAntik;

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

                // Lakukan pembaruan profil dengan menggunakan Retrofit
                updateProfile(nikUser, namaBaru, rtRwBaru, noRumahBaru);
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
    private void updateProfile(String nikUser, String namaBaru, String rtRwBaru, String noRumahBaru) {
        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        Call<UserResponse> call = retrofitEndPoint.updateProfile(nikUser, namaBaru, rtRwBaru, noRumahBaru);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Profil berhasil diperbarui, Anda dapat menampilkan pesan sukses
                    Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();

//                    MainActivity mainActivity = (MainActivity) getActivity();
//                    if (mainActivity != null) {
//                        mainActivity.updateNamaBeranda(namaBaru);
//                    }

                    // Kirim data profil yang diperbarui kembali ke ProfilFragment
                    Bundle updatedProfileData = new Bundle();
                    updatedProfileData.putString("nama", namaBaru);
                    updatedProfileData.putString("rt", rtRwBaru);
                    updatedProfileData.putString("no",noRumahBaru);

                    ProfilFragment profilFragment = new ProfilFragment();
                    profilFragment.setArguments(updatedProfileData);

                    // Ganti fragment kembali ke ProfilFragment
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, profilFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    // Terjadi kesalahan, Anda dapat menampilkan pesan kesalahan
                    Toast.makeText(getContext(), "Gagal memperbarui profil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Terjadi kesalahan jaringan, Anda dapat menampilkan pesan kesalahan
                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}