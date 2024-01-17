package com.example.SiAntik;

import static com.example.SiAntik.RetrofitClient.BASE_URL;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BerandaFragment extends Fragment {

    private boolean isLaporFragmentDisabled = false;

    private ImageView imageView;
    private Handler handler;
    TextView statusView;
    private TextView txtSelamat;
    private static final long REFRESH_INTERVAL = 10000;

    public BerandaFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (imageView != null && !isLaporFragmentDisabled) {
            checkLaporanStatus();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (imageView != null) {
            checkLaporanStatus();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);
        setUpPieChart(pieChart);

//        BarChart barChart = view.findViewById(R.id.barChart);
//        setUpBarChart(barChart);

        BarChart barChart1 = view.findViewById(R.id.barChart1);
        getDataAndSetUpMonthlyStatus1BarChart(barChart1);

        TextView nama = view.findViewById(R.id.txt_nama);
        imageView = view.findViewById(R.id.imageBeranda);
        statusView = view.findViewById(R.id.edtStatusBeranda);


        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String nama1 = extras.getString("NAMA");
            nama.setText(nama1);
        }

        checkLaporanStatus();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Refresh the charts
                setUpPieChart(pieChart);
                getDataAndSetUpMonthlyStatus1BarChart(barChart1);
                checkLaporanStatus();

                // Schedule the next refresh
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);

        txtSelamat = view.findViewById(R.id.txt_selamat);

        // Mendapatkan waktu saat ini
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Mengatur salam berdasarkan waktu
        if (hour >= 3 && hour < 10) {
            txtSelamat.setText("Selamat Pagi!");
        } else if (hour >= 10 && hour < 15) {
            txtSelamat.setText("Selamat Siang!");
        } else if (hour >= 15 && hour < 19) {
            txtSelamat.setText("Selamat Sore!");
        } else {
            txtSelamat.setText("Selamat Malam!");
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove the callback to stop the periodic task when the fragment is destroyed
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }


    public void updateNama(String namaBaru) {
        TextView txtNamaBeranda = getView().findViewById(R.id.txt_nama);
        txtNamaBeranda.setText(namaBaru);
    }


    private void setUpPieChart(PieChart pieChart) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        // Mengambil data dari server
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
        Call<StatusData> panggilan = layananApi.getStatusData();

        panggilan.enqueue(new Callback<StatusData>() {
            @Override
            public void onResponse(Call<StatusData> call, Response<StatusData> response) {
                if (response.isSuccessful()) {
                    StatusData statusData = response.body();
                    int countNull = statusData.getCountNull();
                    int countZero = statusData.getCountZero();
                    int countOne = statusData.getCountOne();

                    // Buat entri Pie Chart
                    ArrayList<PieEntry> pieEntries = new ArrayList<>();
                    pieEntries.add(new PieEntry(countNull, "Belum Terkonfirmasi"));
                    pieEntries.add(new PieEntry(countZero, "Negatif Jentik"));
                    pieEntries.add(new PieEntry(countOne, "Positif Jentik"));

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");

                    // Tambahkan parameter sliceSpace untuk menyembunyikan teks label pada bagian pie
                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(5f);

                    pieDataSet.setColors(new int[]{Color.GRAY, Color.GREEN, Color.RED}); // Warna sesuai dengan kriteria Anda

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter(pieChart));

                    // Atur ukuran teks label pada bagian pie menjadi 0f untuk menyembunyikannya
                    pieData.setValueTextSize(0f);
                    pieData.setValueTextColor(Color.WHITE);

                    // Menyembunyikan label pada pie chart (tetapi legenda akan tetap ada)
                    pieChart.setDrawEntryLabels(false);

                    pieChart.setData(pieData);
                    pieChart.highlightValues(null);
                    pieChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<StatusData> call, Throwable t) {
                // Tangani kegagalan komunikasi (misalnya, masalah jaringan)
                t.printStackTrace();
            }
        });

        // Konfigurasi lainnya
        pieChart.setHoleRadius(52f);
        pieChart.setTransparentCircleRadius(57f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.animateY(1000);
    }

    private void getDataAndSetUpMonthlyStatus1BarChart(final BarChart barChart) {
        barChart.getDescription().setEnabled(false);
        final ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> xAxisLabels = new ArrayList();

        // Ambil data bulan dan jumlahnya dari server
        RetrofitEndPoint layananApi = RetrofitClient.getConnection().create(RetrofitEndPoint.class);
        Call<List<MonthlyStatusCount>> panggilan = layananApi.getMonthlyStatusCount();

        panggilan.enqueue(new Callback<List<MonthlyStatusCount>>() {
            @Override
            public void onResponse(Call<List<MonthlyStatusCount>> call, Response<List<MonthlyStatusCount>> response) {
                if (response.isSuccessful()) {
                    List<MonthlyStatusCount> monthlyStatusCounts = response.body();
                    int index = 0;

                    for (MonthlyStatusCount monthlyStatusCount : monthlyStatusCounts) {
                        if (monthlyStatusCount.getMonth() != null && !monthlyStatusCount.getMonth().isEmpty()) {
                            entries.add(new BarEntry(index, monthlyStatusCount.getCount()));
                            xAxisLabels.add(getMonthName(monthlyStatusCount.getMonth())); // Ambil nama bulan
                            index++;
                        }
                    }

                    BarDataSet barDataSet = new BarDataSet(entries, "Jumlah kasus positif jentik");
                    barDataSet.setColors(Color.RED);
                    barDataSet.setValueTextSize(14f);

                    BarData barData = new BarData(barDataSet);


                    // Mengatur formatter untuk sumbu Y agar angka bilangan bulat
//                    barData.setValueFormatter(new DefaultValueFormatter(0));

                    barData.setValueFormatter(new IntegerValueFormatter());
                    barChart.setData(barData);
//                    barChart.getAxisLeft().setValueFormatter(new IntegerValueFormatter());

                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setTextSize(12f);
                    yAxis.setGranularity(1f); // Ini penting agar angka di sumbu Y hanya menampilkan bilangan bulat
                    yAxis.setAxisMinimum(0f);
                    yAxis.setValueFormatter(new IntegerValueFormatter());

                    // Konfigurasi sumbu X
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(xAxisLabels.size());
                    xAxis.setDrawLabels(true); // Menonaktifkan rotasi label
                    xAxis.setDrawGridLines(false); // Optional: Menonaktifkan garis grid jika tidak dibutuhkan

                    barChart.getDescription().setText("Monthly Status = 1 Counts");
                    barChart.getDescription().setTextSize(12f);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.animateY(1000, Easing.EaseInOutCubic);
                    barChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<MonthlyStatusCount>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void tampilkanPesanKegagalan() {
        // Tampilkan pesan kesalahan kepada pengguna jika diperlukan
        Toast.makeText(getContext(), "Gagal mengambil data bulan per bulan", Toast.LENGTH_SHORT).show();
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
                        // Tampilkan gambar "anda sudah lapor"
                        imageView.setImageResource(R.drawable.ic_yes);
                        statusView.setText("Anda sudah lapor bulan ini, status: Positif jentik");
                        disableLaporFragment();
                    } else if (status.equals("belum_lapor")) {
                        // Tampilkan gambar "anda belum lapor bulan ini"
                        imageView.setImageResource(R.drawable.ic_not);
                        statusView.setText("Anda belum lapor bulan ini, segera lakukan lapor");
                    } else if(status.equals("error")){
                        imageView.setImageResource(R.drawable.ic_not);
                        statusView.setText("Tidak dapat mengambil data");
                    } else if (status.equals("sudah_lapor_negatif")) {
                        imageView.setImageResource(R.drawable.ic_seru);
                        statusView.setText("Anda sudah lapor bulan ini, status: Negatif jentik");
                        disableLaporFragment();
                    } else if (status.equals("sudah_lapor_belum")) {
                        imageView.setImageResource(R.drawable.ic_play);
                        statusView.setText("Anda sudah lapor bulan ini, status: Belum dikonfirmasi");
                        disableLaporFragment();
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

    private String getMonthName(String month) {
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int monthIndex = Integer.parseInt(month) - 1;
        if (monthIndex >= 0 && monthIndex < monthNames.length) {
            return monthNames[monthIndex];
        }
        return month;
    }



    private void disableLaporFragment() {
        // Nonaktifkan Fragment Lapor setelah transaksi Fragment selesai
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    Fragment laporFragment = fragmentManager.findFragmentByTag("LaporFragment");

                    if (laporFragment != null) {
                        // Set status di LaporFragment
                        Bundle args = new Bundle();
                        args.putString("status", "sudah lapor");
                        laporFragment.setArguments(args);

                        // Tampilkan pesan peringatan
                        Toast.makeText(getContext(), "Anda sudah melaporkan, akses ke Fragment Lapor dinonaktifkan.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



}

class IntegerValueFormatter extends ValueFormatter {
    @Override
    public String getBarLabel(BarEntry barEntry) {
        return String.valueOf((int) barEntry.getY()); // Mengonversi nilai ke bilangan bulat
    }
    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return String.valueOf((int) value);
    }
}

 interface OnReturnToBerandaListener {
    void onReturnToBeranda();
}
