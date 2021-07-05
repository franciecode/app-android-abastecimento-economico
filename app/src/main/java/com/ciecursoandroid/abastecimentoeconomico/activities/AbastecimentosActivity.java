package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.adapters.AbastecimentosAdapter;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

import java.util.List;

public class AbastecimentosActivity extends BaseMenuActivity implements AbastecimentosAdapter.OnItemClickListener {


    private RecyclerView recyclerView;
    private AbastecimentosAdapter abastecimentosAdapter;
    private AbastecimentoViewModel abastecimentoViewModel;
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
        abastecimentosAdapter = new AbastecimentosAdapter(this, this);
        recyclerView.setAdapter(abastecimentosAdapter);


        abastecimentoViewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        abastecimentoViewModel.setRepository(new AbastecimentoRepository(this));
        abastecimentoViewModel.getAll().observe(this, new Observer<List<Abastecimento>>() {
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

    @Override
    public void onItemClick(Abastecimento abastecimento, int position) {

    }

    @Override
    public boolean onLongItemClick(Abastecimento abastecimento, int position) {
        String dataAbastecimento = DateFormat.format(getString(R.string.data_hora), abastecimento.getDataAbastecimento()).toString();
        Alerts.alertWaring(this, "Remover Abastecimento!",
                "Você confirmar remover o abastecimento feito em " + dataAbastecimento
        ).setPositiveButton(R.string.remover, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abastecimentoViewModel.delete(abastecimento, new VeiculoRespository.OnDeleteListener() {
                    @Override
                    public void onComplete(Exception e) {
                        if (e != null) {
                            Alerts.alertError(AbastecimentosActivity.this,
                                    getString(R.string.erro_ao_remover_abastecimento),
                                    e.getMessage())
                                    .setPositiveButton("ok", null)
                                    .show();
                        } else {
                            Toast.makeText(AbastecimentosActivity.this, "Abastecimento removido com sucesso!", Toast.LENGTH_SHORT).show();
                            abastecimentosAdapter.removeItem(position);
                        }
                    }
                });
            }
        }).setNegativeButton(getString(R.string.cancelar), null)
                .show();
        return false;
    }
}