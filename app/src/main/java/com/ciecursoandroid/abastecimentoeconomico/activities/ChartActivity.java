package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class ChartActivity extends BaseMenuActivity {

    private BarChart chart;
    private TextView textViewTotalGasto;
    private TextView textViewTotalEconomizado;
    private ArrayList<BarEntry> entryGastos = new ArrayList<BarEntry>(12);
    private ArrayList<BarEntry> entryEconomias = new ArrayList<BarEntry>(12);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chart = findViewById(R.id.chart);
        textViewTotalGasto = findViewById(R.id.textViewTotalGastoChart);
        textViewTotalEconomizado = findViewById(R.id.textViewTotalEconomizadoChart);


        // CHART
        // -------------------------------------------------------
        Float[] gastos = {350.40f, 350.40f, 450.40f, 550.40f, 750.40f, 250.40f, 450.40f, 0f, 0f, 0f, 0f, 1200.80f};
        Float[] economias = {-18.40f, 24.40f, 39.40f, 41.40f, 67.40f, 12.40f, 3.40f, 0f, 0f, 0f, 0f, 125.7f};

        setAndShowChartData(gastos, economias);

        XAxis xAxis = chart.getXAxis();
        String[] meses = getResources().getStringArray(R.array.meses_chart);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(meses));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);


    }

    private void setAndShowChartData(Float[] gastos, Float[] economias) {
        float totalGasto = 0f;
        float totalEconomizado = 0f;
        // fill the lists
        for (int i = 0; i < 12; i++) {
            float gasto = gastos[i] == null ? 0 : gastos[i];
            float economizado = economias[i] == null ? 0 : economias[i];
            entryGastos.add(i, new BarEntry(i, gasto));
            entryEconomias.add(i, new BarEntry(i, economizado));
            totalGasto += gasto;
            totalEconomizado += economizado;
        }
        BarDataSet set1 = new BarDataSet(entryGastos, getString(R.string.gastos));
        set1.setColor(getResources().getColor(R.color.color_chart_gastos));
        BarDataSet set2 = new BarDataSet(entryEconomias, getString(R.string.economias));
        set2.setColor(getResources().getColor(R.color.color_chart_economia));

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset

        BarData barData = new BarData(set1, set2);
        barData.setBarWidth(barWidth); // set the width of each bar
        chart.setData(barData);
        chart.groupBars(-0.5f, groupSpace, barSpace); // perform the "explicit" grouping
        chart.animateY(1000);
        chart.invalidate(); // refresh

        textViewTotalGasto.setText(String.format(getString(R.string.total_gasto_chart), totalGasto));
        textViewTotalEconomizado.setText(String.format(getString(R.string.total_economizado_chart), totalEconomizado));

    }
}