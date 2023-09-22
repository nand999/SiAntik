package com.example.SiAntik;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BerandaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BerandaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BerandaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BerandaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BerandaFragment newInstance(String param1, String param2) {
        BerandaFragment fragment = new BerandaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        PieChart pieChart = view.findViewById(R.id.pieChart);
        setUpPieChart(pieChart);
        BarChart barChart = view.findViewById(R.id.barChart);
        setUpBarChart(barChart);
        XAxis xAxis = barChart.getXAxis();
        BarChart barChart1 = view.findViewById(R.id.barChart1);
        setUpBarChart(barChart1);

        return view;
    }
    private void setUpPieChart(PieChart pieChart){
        // Inisialisasi data untuk pie chart
        PieDataSet pieDataSet = new PieDataSet(getPieChartData(), " ");
        pieDataSet.setColors(new int[]{Color.RED, Color.BLUE,Color.GRAY});

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        // Konfigurasi lainnya
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(35f);

        pieChart.animateY(1000, Easing.EaseInOutCubic);
    }

    private ArrayList<PieEntry> getPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "Positif"));
        entries.add(new PieEntry(30f, "Negatif"));
        entries.add(new PieEntry(20f, "Belum Terkonfirmasi"));
        return entries;
    }

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
        entries.add(new BarEntry(1f, 20f,"Januari"));
        entries.add(new BarEntry(2f, 50f,"Februari"));
        entries.add(new BarEntry(3f, 40f,"Maret"));
        entries.add(new BarEntry(4f, 20f,"April"));
        entries.add(new BarEntry(5f, 30f,"Mei"));
        entries.add(new BarEntry(6f, 40f,"Juni"));
        entries.add(new BarEntry(7f, 20f,"Juli"));
        entries.add(new BarEntry(8f, 10f,"Agustus"));
        entries.add(new BarEntry(9f, 60f,"oke"));
        entries.add(new BarEntry(10f, 40f,"Oke"));
        return entries;
    }
}