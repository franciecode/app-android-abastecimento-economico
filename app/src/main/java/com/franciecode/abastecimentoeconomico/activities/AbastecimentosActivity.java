package com.franciecode.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.adapters.AbastecimentosAdapter;
import com.franciecode.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.franciecode.abastecimentoeconomico.widgets.Alerts;

import java.util.List;

public class AbastecimentosActivity extends BaseMenuActivity implements AbastecimentosAdapter.AdapterObserver {
    private RecyclerView recyclerView;
    private AbastecimentosAdapter abastecimentosAdapter;
    private AbastecimentoViewModel abastecimentoViewModel;
    private TextView textViewTotalRegistros;
    private TextView textViewTotaGasto;
    TextView textViewTotalEconomizado;
    private MenuItem search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimentos);

        textViewTotalRegistros = findViewById(R.id.textViewTotalRegistros);
        textViewTotaGasto = findViewById(R.id.textViewTotalGasto);
        textViewTotalEconomizado = findViewById(R.id.textViewTotalEconomizado);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        abastecimentosAdapter = new AbastecimentosAdapter(this, this);
        recyclerView.setAdapter(abastecimentosAdapter);


        abastecimentoViewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        abastecimentoViewModel.setRepository(new AbastecimentoRepository(this));
        abastecimentoViewModel.getAbastecimentoComVeiculos().observe(this, abastecimentoComVeiculos -> abastecimentosAdapter.setAbastecimentos(abastecimentoComVeiculos));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setViewsResumoTotal(List<AbastecimentoComVeiculo> all, List<AbastecimentoComVeiculo> filtrados) {
        if (all.size() > 0) {
            float totalEconomizado = 0, totalGasto = 0;
            List<AbastecimentoComVeiculo> listCalculo = filtrados != null ? filtrados : all;
            for (AbastecimentoComVeiculo abastecimentoComVeiculos : listCalculo) {
                totalGasto += abastecimentoComVeiculos.abastecimento.getTotalPago();
                totalEconomizado += abastecimentoComVeiculos.abastecimento.getValorEconomizado();
            }
            int monstrando = filtrados == null ? all.size() : filtrados.size();
            textViewTotalRegistros.setText(monstrando + "/" + all.size());
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

    @Override
    public void onItemClick(AbastecimentoComVeiculo abastecimento, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        search = menu.findItem(R.id.app_bar_search);
        search.setVisible(true);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtrarAbastecimento(s);
                return false;
            }
        });
        return true;
    }

    private void filtrarAbastecimento(String s) {
        abastecimentosAdapter.getFilter().filter(s);
    }

    @Override
    public boolean onLongItemClick(AbastecimentoComVeiculo abastecimentoComVeiculo, int position) {

        String dataAbastecimento = DateFormat.format(getString(R.string.data_hora), abastecimentoComVeiculo.abastecimento.getDataAbastecimento()).toString();
        Alerts.alertWaring(this, getString(R.string.remover_abastecimento),
                String.format(getString(R.string.confirma_remover_abastecimento), dataAbastecimento)
        ).setPositiveButton(R.string.remover, (dialogInterface, i) -> abastecimentoViewModel.delete(abastecimentoComVeiculo.abastecimento, e -> {
            if (e != null) {
                Alerts.alertError(AbastecimentosActivity.this,
                        getString(R.string.erro_ao_remover_abastecimento),
                        e.getMessage())
                        .setPositiveButton("ok", null)
                        .show();
            } else {
                Toast.makeText(AbastecimentosActivity.this,
                        getString(R.string.abasteciment_removido_com_sucesso),
                        Toast.LENGTH_SHORT).show();
                abastecimentosAdapter.removeItem(position);
            }
        })).setNegativeButton(getString(R.string.cancelar), null)
                .show();
        return false;
    }

    @Override
    public void onChangeListener(List<AbastecimentoComVeiculo> filtrados, List<AbastecimentoComVeiculo> todos) {
        setViewsResumoTotal(todos, filtrados);
    }
}