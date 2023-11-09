package com.example.SiAntik;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    private ListView listView1;
    private List<LaporanData> laporanDataList; // Tambahkan variabel untuk menyimpan data laporan

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView1 = rootView.findViewById(R.id.listView1);
        laporanDataList = new ArrayList<>(); // Inisialisasi list data laporan

        Bundle extras = getActivity().getIntent().getExtras();
        String nikUserLogin ="";

        if (extras != null) {
            String nama1 = extras.getString("NAMA");
            nikUserLogin = extras.getString("NIK");
        }

        // Buat Retrofit instance menggunakan RetrofitClient
        Retrofit retrofit = RetrofitClient.getConnection();

        // Buat instance dari RetrofitEndPoint
        RetrofitEndPoint apiService = retrofit.create(RetrofitEndPoint.class);

        // Panggil endpoint untuk mengambil data laporan dengan nik_user tertentu
        Call<List<LaporanData>> call = apiService.getLaporanData(nikUserLogin);

        call.enqueue(new Callback<List<LaporanData>>() {
            @Override
            public void onResponse(Call<List<LaporanData>> call, Response<List<LaporanData>> response) {
                if (response.isSuccessful()) {
                    laporanDataList = response.body(); // Simpan data laporan ke dalam variabel

                    // Update adapter dengan data yang diambil
                    CustomListAdapter adapter = new CustomListAdapter(getContext(), laporanDataList);
                    listView1.setAdapter(adapter);
                } else {
                    // Tangani kesalahan saat mengambil data dari server
                    // Tampilkan pesan kesalahan kepada pengguna menggunakan Toast
                    Toast.makeText(getContext(), "Terjadi kesalahan saat mengambil data.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LaporanData>> call, Throwable t) {
                // Tangani kesalahan jaringan atau koneksi
                // Tampilkan pesan kesalahan kepada pengguna menggunakan Toast
                Toast.makeText(getContext(), "Kesalahan jaringan atau koneksi. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tangani tindakan saat item diklik di sini
                LaporanData selectedLaporan = laporanDataList.get(position);

                // Buat instance dari DetailLaporanFragment
                DetailLaporanFragment detailFragment = new DetailLaporanFragment();

                // Kirim data yang dibutuhkan ke DetailLaporanFragment
                Bundle args = new Bundle();
                args.putString("id_laporan", selectedLaporan.getIdLapor());
                args.putString("nik_user", selectedLaporan.getNikUser());
                args.putString("foto", selectedLaporan.getFoto());
                args.putString("deskripsi", selectedLaporan.getDeskripsi());
                args.putString("tanggal_laporan", selectedLaporan.getTanggalLaporan());
                args.putString("tanggal_pemantauan", selectedLaporan.getTanggalPemantauan());
                args.putString("status", selectedLaporan.getStatuss());
                detailFragment.setArguments(args);

                // Ganti fragment saat item diklik
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, detailFragment);
                transaction.addToBackStack(null); // Ini akan menambahkan transaksi ke back stack
                transaction.commit();
            }
        });

        return rootView;
    }
}

