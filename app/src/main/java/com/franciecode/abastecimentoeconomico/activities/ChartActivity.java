package com.franciecode.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.franciecode.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.franciecode.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements Observer<List<AbastecimentoRelatorioGraficoView>> {
    private final float CHART_GROUP_SPACE = 0.06f;
    private final float CHART_BAR_SPACE = 0.02f; // x2 dataset
    private final float CHART_BAR_WIDTH = 0.45f; // x2 dataset
    private final String TAG = ChartActivity.class.getSimpleName();
    private final long TODOS_VEICULO = -1;
    private BarChart chart;
    private TextView textViewTotalGasto;
    private TextView textViewTotalEconomizado;
    private ArrayList<BarEntry> entryGastos = new ArrayList<BarEntry>(12);
    private ArrayList<BarEntry> entryEconomias = new ArrayList<BarEntry>(12);
    private AbastecimentoViewModel abastecimentoViewModel;
    private ImageView imageViewAnoAnterior;
    private ImageView imageViewAnoProximo;
    private TextView textViewAno;
    private int ano;
    private TipoCalculo tipoCalculo = TipoCalculo.TODOS;
    private long veiculoId = TODOS_VEICULO;
    private Spinner spinnerFiltrarGrafico;
    private Spinner spinnerGraficoVeiculos;
    private VeiculoViewModel veiculoViewModel;
    private ArrayAdapter spinnerVeiculosAdapter;
    private List<Veiculo> veiculos = new ArrayList<>();
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {

            }
        });

        adicionarAnuncio(null);


        ano = Calendar.getInstance().get(Calendar.YEAR);

        textViewTotalGasto = findViewById(R.id.textViewTotalGastoChart);
        textViewTotalEconomizado = findViewById(R.id.textViewTotalEconomizadoChart);
        imageViewAnoAnterior = findViewById(R.id.imageViewAnoAnterior);
        imageViewAnoProximo = findViewById(R.id.imageViewAnoProximo);
        textViewAno = findViewById(R.id.textViewAno);
        spinnerFiltrarGrafico = findViewById(R.id.spinnerFiltrarGrafico);
        spinnerGraficoVeiculos = findViewById(R.id.spinnerGraficoVeiculos);


        abastecimentoViewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        abastecimentoViewModel.setRepository(new AbastecimentoRepository(this));

        veiculoViewModel = new ViewModelProvider(this).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(this));

        // CHART
        // -------------------------------------------------------
        chart = findViewById(R.id.chart);
        XAxis xAxis = chart.getXAxis();
        String[] meses = getResources().getStringArray(R.array.meses_chart);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(meses));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);

        veiculoViewModel.getAll().observe(this, new Observer<List<Veiculo>>() {
            @Override
            public void onChanged(List<Veiculo> list) {
                veiculos = list;
                setDataSpinnerViculos(veiculos);
            }
        });


        imageViewAnoAnterior.setOnClickListener(view -> carregarDados(--ano, tipoCalculo, veiculoId));
        imageViewAnoProximo.setOnClickListener(view -> carregarDados(++ano, tipoCalculo, veiculoId));

        spinnerFiltrarGrafico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        carregarDados(ano, TipoCalculo.TODOS, TODOS_VEICULO);
                        break;
                    case 1:
                        carregarDados(ano, TipoCalculo.VEICULO, TODOS_VEICULO);
                        break;
                    case 2:
                        carregarDados(ano, TipoCalculo.KMS_LITRO, TODOS_VEICULO);
                        break;
                    case 3:
                        carregarDados(ano, TipoCalculo.BASICO, TODOS_VEICULO);
                        break;
                    default:
                        carregarDados(ano, TipoCalculo.VEICULO, 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerGraficoVeiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    carregarDados(ano, TipoCalculo.VEICULO, TODOS_VEICULO);
                else
                    carregarDados(ano, TipoCalculo.VEICULO, veiculos.get(position - 1).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void adicionarAnuncio(AdListener listener) {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (listener != null) {
            mAdView.setAdListener(listener);
        }
    }

    private void setDataSpinnerViculos(List<Veiculo> veiculos) {
        spinnerVeiculosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnerGraficoVeiculos.setAdapter(spinnerVeiculosAdapter);
        spinnerVeiculosAdapter.clear();
        spinnerVeiculosAdapter.add(getString(R.string.todos_os_veiculos));
        if (veiculos == null) return;
        for (int i = 0; i < veiculos.size(); i++) {
            spinnerVeiculosAdapter.add(veiculos.get(i).getNome());
        }

    }


    private void carregarDados(int ano, TipoCalculo tipoCalculo, long veiculoId) {
        this.ano = ano;
        this.tipoCalculo = tipoCalculo;
        this.veiculoId = veiculoId;
        String strAno = String.valueOf(ano);
        textViewAno.setText(strAno);
        int showVeiculos = View.GONE;
        switch (tipoCalculo) {
            case TODOS:
                abastecimentoViewModel.getRelatorioGraficoAnual(strAno)
                        .observe(this, this);
                break;
            case BASICO:
            case KMS_LITRO:
                abastecimentoViewModel.getRelatorioGraficoAnualPorTipoCalculo(strAno, tipoCalculo)
                        .observe(this, this);
                break;
            case VEICULO:
                showVeiculos = View.VISIBLE;
                if (veiculoId == TODOS_VEICULO) {
                    abastecimentoViewModel.getRelatorioGraficoAnualPorTipoCalculo(strAno, tipoCalculo)
                            .observe(this, this);
                } else {
                    abastecimentoViewModel.getRelatorioGraficoAnualPorVeiculo(strAno, veiculoId)
                            .observe(this, this);
                }

        }
        spinnerGraficoVeiculos.setVisibility(showVeiculos);
    }


    private void setDataChart(List<AbastecimentoRelatorioGraficoView> relatorios) {
        entryGastos.clear();
        entryEconomias.clear();
        float totalGasto = 0f;
        float totalEconomizado = 0f;
        int relatorioIndex = 0;
        // fill the lists
        for (int i = 0; i <= 11; i++) {
            float gasto = 0f;
            float economizado = 0f;
            int mes = i + 1;
            if (relatorios != null
                    && relatorioIndex < relatorios.size()
                    && Integer.valueOf(relatorios.get(relatorioIndex).mes) == mes) {
                gasto = relatorios.get(relatorioIndex).somaTotalPago;
                economizado = relatorios.get(relatorioIndex).somaTotalEconomizado;
                relatorioIndex++;
            }
            entryGastos.add(i, new BarEntry(i, gasto));
            entryEconomias.add(i, new BarEntry(i, economizado));
            totalGasto += gasto;
            totalEconomizado += economizado;
        }
        BarDataSet set1 = new BarDataSet(entryGastos, getString(R.string.gastos));
        set1.setColor(getResources().getColor(R.color.color_chart_gastos));
        BarDataSet set2 = new BarDataSet(entryEconomias, getString(R.string.economias));
        set2.setColor(getResources().getColor(R.color.color_chart_economia));

        BarData barData = new BarData(set1, set2);
        barData.setBarWidth(CHART_BAR_WIDTH); // set the width of each bar
        chart.setData(barData);
        chart.groupBars(-0.5f, CHART_GROUP_SPACE, CHART_BAR_SPACE); // perform the "explicit" grouping
        chart.animateY(500);
        chart.invalidate(); // refresh

        textViewTotalGasto.setText(String.format(getString(R.string.total_gasto_chart), totalGasto));
        textViewTotalEconomizado.setText(String.format(getString(R.string.total_economizado_chart), totalEconomizado));

    }

    @Override
    public void onChanged(List<AbastecimentoRelatorioGraficoView> list) {
        setDataChart(list);
    }
}