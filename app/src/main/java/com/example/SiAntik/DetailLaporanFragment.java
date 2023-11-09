package com.example.SiAntik;

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
import com.squareup.picasso.Picasso;

public class DetailLaporanFragment extends Fragment {

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

        // Mendapatkan data yang dikirim dari HistoryFragment
        Bundle args = getArguments();

        if (args != null) {
            String idLaporan = args.getString("id_laporan");
            String nikUser = args.getString("nik_user");
            String foto = args.getString("foto");
            String deskripsi = args.getString("deskripsi");
            String tanggalLaporan = args.getString("tanggal_laporan");
            String tanggalPemantauan = args.getString("tanggal_pemantauan");
            String status = args.getString("status");

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


            String urlFoto = "http://172.16.106.16:8080/test_siantik/mobile/" + foto;


            // Menampilkan gambar (foto) menggunakan Picasso
            Picasso.get().load(urlFoto).into(fotoImageView);
        }

        return rootView;
    }
}
