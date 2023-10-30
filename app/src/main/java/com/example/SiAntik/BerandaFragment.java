package com.example.SiAntik;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaFragment extends Fragment {

    public BerandaFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);
        setUpPieChart(pieChart);

        BarChart barChart = view.findViewById(R.id.barChart);
        setUpBarChart(barChart);

        BarChart barChart1 = view.findViewById(R.id.barChart1);
        getDataAndSetUpMonthlyStatus1BarChart(barChart1);

        TextView nama = view.findViewById(R.id.txt_nama);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            String nama1 = extras.getString("NAMA");
            String nik = extras.getString("NO_RUMAH");
            nama.setText(nik + " " + nama1);
        }
        return view;
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

                    pieDataSet.setSliceSpace(3f);
                    pieDataSet.setSelectionShift(5f);

                    pieDataSet.setColors(new int[]{Color.GRAY, Color.GREEN, Color.RED}); // Warna sesuai dengan kriteria Anda

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter(pieChart));
                    pieData.setValueTextSize(11f);
                    pieData.setValueTextColor(Color.WHITE);

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


//    private void setUpPieChart(PieChart pieChart){
//        // Inisialisasi data untuk pie chart
//        PieDataSet pieDataSet = new PieDataSet(getPieChartData(), " ");
//        pieDataSet.setColors(new int[]{Color.RED, Color.BLUE,Color.GRAY});
//
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//
//        // Konfigurasi lainnya
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setHoleRadius(30f);
//        pieChart.setTransparentCircleRadius(35f);
//
//        pieChart.animateY(1000, Easing.EaseInOutCubic);
//    }

//    private ArrayList<PieEntry> getPieChartData() {
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(40f, "Positif"));
//        entries.add(new PieEntry(30f, "Negatif"));
//        entries.add(new PieEntry(20f, "Belum Terkonfirmasi"));
//        return entries;
//    }

    private void setUpBarChart(BarChart barChart){
        // Inisialisasi data untuk bar chart
        BarDataSet barDataSet = new BarDataSet(getBarChartData(), "Data Set");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        // Konfigurasi lainnya
        barChart.getDescription().setEnabled(false);

        barChart.animateY(1000, Easing.EaseInOutCubic);
    }

    private ArrayList<BarEntry> getBarChartData() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 20f,"January"));
        entries.add(new BarEntry(2f, 50f,"February"));
        entries.add(new BarEntry(3f, 40f,"March"));
        entries.add(new BarEntry(4f, 20f,"April"));
        entries.add(new BarEntry(5f, 30f,"May"));
        entries.add(new BarEntry(6f, 40f,"June"));
        entries.add(new BarEntry(7f, 20f,"July"));
        entries.add(new BarEntry(8f, 10f,"August"));
        entries.add(new BarEntry(9f, 60f,"September"));
        entries.add(new BarEntry(10f, 40f,"October"));
        entries.add(new BarEntry(11f, 40f,"November"));
        entries.add(new BarEntry(12f, 40f,"December"));
        return entries;
    }

    private void getDataAndSetUpMonthlyStatus1BarChart(final BarChart barChart) {
        barChart.getDescription().setEnabled(false);
        final ArrayList<BarEntry> entries = new ArrayList<>();
        final ArrayList<String> xAxisLabels = new ArrayList<>();
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
                            xAxisLabels.add(monthlyStatusCount.getMonth());
                            index++;
                        }
                    }

                    BarDataSet barDataSet = new BarDataSet(entries, "Status = 1 Counts");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);

                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                    barChart.getXAxis().setLabelRotationAngle(45f);
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
    private List<String> getXAxisLabels() {
        List<String> labels = new ArrayList<>();
        // Gantilah ini dengan label-label bulan yang sesuai dengan data Anda
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        labels.add("July");
        labels.add("August");
        labels.add("September");
        labels.add("October");
        labels.add("November");
        labels.add("December");

        return labels;
    }
}