package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.adapters.AbastecimentosAdapter;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;

import java.util.List;

public class AbastecimentosActivity extends BaseMenuActivity {


    private RecyclerView recyclerView;
    private AbastecimentosAdapter abastecimentosAdapter;
    private AbastecimentoViewModel viewModel;
    private TextView textViewTotalRegistros;
    private TextView textViewTotaGasto;
    TextView textViewTotalEconomizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimentos);
        getSupportActionBar().setTitle(R.string.abastecimentos);

        textViewTotalRegistros = findViewById(R.id.textViewTotalRegistros);
        textViewTotaGasto = findViewById(R.id.textViewTotalGasto);
        textViewTotalEconomizado = findViewById(R.id.textViewTotalEconomizado);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        abastecimentosAdapter = new AbastecimentosAdapter(this);
        recyclerView.setAdapter(abastecimentosAdapter);


        viewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        viewModel.setRepository(new AbastecimentoRepository(this));
        viewModel.getAll().observe(this, new Observer<List<Abastecimento>>() {
            @Override
            public void onChanged(List<Abastecimento> abastecimentos) {
                abastecimentosAdapter.setAbastecimentos(abastecimentos);
                abastecimentosAdapter.notifyDataSetChanged();
                if (abastecimentos.size() > 0) {
                    float totalEconomizado = 0, totalGasto = 0;
                    for (Abastecimento a : abastecimentos) {
                        totalGasto += a.getTotalPago();
                        totalEconomizado += a.getValorEconomizado();
                    }
                    textViewTotalRegistros.setText(abastecimentos.size() + "/" + abastecimentos.size());
                    textViewTotaGasto.setText(String.format(getString(R.string.valor_dinheiro), totalGasto));
                    textViewTotalEconomizado.setText(String.format(getString(R.string.valor_dinheiro), totalEconomizado));
                    if (totalEconomizado < 0) {
                        textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_red));
                    } else {
                        textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
                    }
                } else {
                    textViewTotalRegistros.setText("0/0");
                    textViewTotaGasto.setText(getString(R.string.dinheiro_vazio));
                    textViewTotalEconomizado.setText(getString(R.string.dinheiro_vazio));
                    textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
                }
            }
        });

    }
}