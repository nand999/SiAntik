package com.example.SiAntik;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporanFragment extends Fragment {

    private String bulanSekarang;
    private String idLaporan;

    public DetailLaporanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_laporan, container, false);

        Button kembali = rootView.findViewById(R.id.btnKembaliLapor);

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new HistoryFragment());
                transaction.addToBackStack(null); // Optional: menambahkan transaksi ke back stack
                transaction.commit();
            }
        });

        Button hapus = rootView.findViewById(R.id.btnHapusLapor);

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cekBulanSekarang()){
                    hapusLaporan(idLaporan);
                }
            }
        });

        // Mendapatkan data yang dikirim dari HistoryFragment
        Bundle args = getArguments();

        if (args != null) {
            idLaporan = args.getString("id_laporan");
            String nikUser = args.getString("nik_user");
            String foto = args.getString("foto");
            String deskripsi = args.getString("deskripsi");
            String tanggalLaporan = args.getString("tanggal_laporan");
            String tanggalPemantauan = args.getString("tanggal_pemantauan");
            String status = args.getString("status");

            bulanSekarang = tanggalLaporan.substring(5,7);

            // Menemukan tampilan berdasarkan ID
            TextView txtId = rootView.findViewById(R.id.id_lap);
            TextView txtLap = rootView.findViewById(R.id.tgl_lap);
            TextView txtPem = rootView.findViewById(R.id.tgl_pem);
            TextView txtDes = rootView.findViewById(R.id.des);
            TextView txtStat = rootView.findViewById(R.id.stat);
            ImageView fotoImageView = rootView.findViewById(R.id.foto);


            // Mengisi tampilan dengan data yang sesuai
            txtId.setText("ID Laporan: " + idLaporan);
            txtLap.setText("Tanggal Laporan: " + tanggalLaporan);
            txtPem.setText("Tanggal Pemantauan: " + tanggalPemantauan);
            txtDes.setText("Deskripsi: " + deskripsi);
            txtStat.setText("Status: " + status);




            // ...
            if (TextUtils.isEmpty(deskripsi)) {
                txtDes.setText("Deskripsi: Tidak ada deskripsi");
            } if (TextUtils.isEmpty(tanggalPemantauan)) {
                txtPem.setText("Tanggal Pemantauan: Laporan belum dikonfirmasi");
            } if ("0".equals(status)) {
                txtStat.setText("Status: negatif jentik");
            } if ("1".equals(status)) {
                txtStat.setText("Status: positif jentik");
            } if (TextUtils.isEmpty(status)) {
                txtStat.setText("Status: belum dikonfirmasi");
            }


            String urlFoto = "http://172.17.202.21:8080/siantik/mobile/" + foto;


            // Menampilkan gambar (foto) menggunakan Picasso
            Picasso.get().load(urlFoto).into(fotoImageView);
        }

        return rootView;
    }

    public boolean cekBulanSekarang(){
        Calendar calendar = Calendar.getInstance();

        // Mendapatkan bulan saat ini dalam bentuk angka (0-11)
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        if (!bulanSekarang.equals(String.valueOf(currentMonth))){
            alertSudah();
            return false;
        } else{
            return true;
        }
    }

    public void alertSudah() {
        // Tampilkan peringatan dengan hanya pilihan "Ya"
        new AlertDialog.Builder(getContext())
                .setTitle("Gagal Menghapus")
                .setMessage("Tidak dapat menghapus laporan di buan sebelumnya")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      return;
                    }
                })
                .show();

    }

    private void hapusLaporan (String id_laporan) {
        RetrofitEndPoint retrofitEndPoint = RetrofitClient.getConnection().create(RetrofitEndPoint.class);

        Call<LaporanResponse> call = retrofitEndPoint.hapusLaporan(id_laporan);

        call.enqueue(new Callback<LaporanResponse>() {
            @Override
            public void onResponse(Call<LaporanResponse> call, Response<LaporanResponse> response) {
                if (response.isSuccessful()) {
                    // Profil berhasil diperbarui, Anda dapat menampilkan pesan sukses
//                    Toast.makeText(getContext(), "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(getContext())
                            .setTitle("Laporan berhasil dihapus")
                            .setMessage("Silahkan kirim kembali laporan anda bulan ini")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Panggil method onReturnToBeranda di MainActivity
                                    if (getActivity() instanceof MainActivity) {
                                        ((MainActivity) getActivity()).onReturnToBeranda();
                                    }

                                    // Kembali ke lapor fragment
                                    Fragment laporFragment = new BerandaFragment();
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment_container, laporFragment);
                                    transaction.addToBackStack(null); // Jika Anda ingin menambahkannya ke tumpukan kembali
                                    transaction.commit();
                                }
                            })
                            .show();

                } else {
                    // Terjadi kesalahan, Anda dapat menampilkan pesan kesalahan
                    Toast.makeText(getContext(), "Gagal menghapus laporan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LaporanResponse> call, Throwable t) {
                // Terjadi kesalahan jaringan, Anda dapat menampilkan pesan kesalahan
                Toast.makeText(getContext(), "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
