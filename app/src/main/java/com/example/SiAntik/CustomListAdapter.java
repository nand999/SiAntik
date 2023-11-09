package com.example.SiAntik;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private Context context;
    private List<LaporanData> laporanDataList;

    public CustomListAdapter(Context context, List<LaporanData> laporanDataList) {
        this.context = context;
        this.laporanDataList = laporanDataList;
    }

    @Override
    public int getCount() {
        return laporanDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return laporanDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        // Isi data ke elemen-elemen tampilan dalam list_item.xml
        TextView nikUserTextView = convertView.findViewById(R.id.nikUserTextView);
        TextView tanggalLaporanTextView = convertView.findViewById(R.id.tanggalLaporanTextView);
        TextView statusTextView = convertView.findViewById(R.id.statusTextView);
        TextView idTextView = convertView.findViewById(R.id.idTextView);
        TextView tglPem = convertView.findViewById(R.id.pemTextView);
        TextView des = convertView.findViewById(R.id.deskripsiTextView);
        TextView url = convertView.findViewById(R.id.urlTextView);

        LaporanData laporan = laporanDataList.get(position);

        idTextView.setText("ID laporan: " + laporan.getIdLapor());
        nikUserTextView.setText("NIK User: " + laporan.getNikUser());
        tanggalLaporanTextView.setText("Tanggal Laporan: " + laporan.getTanggalLaporan());
        url.setText("foto" + laporan.getFoto());

        if (TextUtils.isEmpty(laporan.getDeskripsi())) {
            des.setText("Deskripsi: Tidak ada deskripsi");
        }
        if (TextUtils.isEmpty(laporan.getTanggalPemantauan())) {
            tglPem.setText("Tanggal Pemantauan: Laporan belum dikonfirmasi");
        }


            if (laporan.getStatuss() != null) {
                String status = laporan.getStatuss();

                if (status.equals("1")) {
                    statusTextView.setText("Status: Positif jentik");
                } else if (status.equals("0")) {
                    statusTextView.setText("Status: Negatif jentik");
                } else {
                    statusTextView.setText("Status: Belum Terkonfirmasi");
                }
            } else {
                statusTextView.setText("Status : Belum terkonfirmasi");
            }

            return convertView;
        }
    }



