package com.example.SiAntik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    TextView txtNik,txtNama,txtRt,txtNo;


    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        requireActivity().onBackPressedDispatcher.addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // Tampilkan dialog konfirmasi saat tombol "Kembali" ditekan
//                tampilkanDialogKonfirmasiKeluar();
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profil, container, false);
        Button btnEdit = (Button) rootview.findViewById(R.id.btnEdit);
        Button btnKeluar = (Button) rootview.findViewById(R.id.btnKeluar);
        txtNik = (TextView) rootview.findViewById(R.id.txtNikProfil);
        txtNama = (TextView) rootview.findViewById(R.id.txtNamaProfil);
        txtRt = (TextView) rootview.findViewById(R.id.txtRtProfil);
        txtNo = (TextView) rootview.findViewById(R.id.txtNoProfil);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String nik = extras.getString("NIK");
            String nama1 = extras.getString("NAMA");
            String rt = extras.getString("RT");
            String no = extras.getString("NO_RUMAH");
            txtNik.setText(nik);
            txtNama.setText(nama1);
            txtRt.setText(rt);
            txtNo.setText(no);
        }
        Bundle profileData = getArguments();
        if (profileData != null) {
            // Perbarui tampilan profil berdasarkan data yang diterima dari EditBioFragment
            String nama = profileData.getString("nama");
            String rt = profileData.getString("rt");
            txtNama.setText(nama);
            txtRt.setText(rt);
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pindahEdit();
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampilkanDialogKonfirmasiKeluar();
            }
        });

        return rootview;
    }

    private void pindahEdit() {
        String nik = txtNik.getText().toString();
        String nama = txtNama.getText().toString();
        String rt = txtRt.getText().toString();
        String noRumah = txtNo.getText().toString();
        // Membuat Bundle untuk data profil
        Bundle profileData = new Bundle();
        profileData.putString("NIK", nik);
        profileData.putString("NAMA", nama);
        profileData.putString("RT", rt);
        profileData.putString("NO_RUMAH", noRumah);

        // Membuat instance EditBioFragment dan mengirimkan data profil
        EditBioFragment editBioFragment = new EditBioFragment();
        editBioFragment.setArguments(profileData);

        // Ganti fragment ke EditBioFragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, editBioFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void keluar() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Menghapus semua data
        editor.apply();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void tampilkanDialogKonfirmasiKeluar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Konfirmasi Keluar");
        builder.setMessage("Anda yakin ingin keluar?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keluar();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void keluarDanHapusProfilFragment() {
        tampilkanDialogKonfirmasiKeluar();
    }

}