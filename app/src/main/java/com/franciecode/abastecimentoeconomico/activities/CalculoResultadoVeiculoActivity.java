package com.franciecode.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.adapters.AbastecimentoViculoViewPager2Adapter;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CalculoResultadoVeiculoActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Veiculo veiculo;
    Float precoGasolina, precoAlcool;
    TextView textViewDetalhesVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_veiculo);

        tabLayout = findViewById(R.id.tabLayoutCalculoAbastecimentoVeiculo);
        viewPager2 = findViewById(R.id.viewPager2CalculoAbastecimentoVeiculo);
        viewPager2.setAdapter(new AbastecimentoViculoViewPager2Adapter(this));
        textViewDetalhesVeiculo = findViewById(R.id.textViewDetalheVeiculo);

        veiculo = getIntent().getParcelableExtra("veiculo");
        precoGasolina = getIntent().getFloatExtra("precoGasolina", 0);
        precoAlcool = getIntent().getFloatExtra("precoAlcool", 0);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText(getString(R.string.cidade));
            } else {
                tab.setText(getString(R.string.rodovia));
            }
        });
        tabLayoutMediator.attach();

        textViewDetalhesVeiculo.setText(String.format(getString(R.string.detalhe_veiculo_calculo_veiculo),
                veiculo.getNome(),
                veiculo.getKmsLitroCidadeAlcool(),
                veiculo.getKmsLitroRodoviaAlcool(),
                veiculo.getKmsLitroCidadeGasolina(),
                veiculo.getKmsLitroRodoviaGasolina()));

    }

    public Float getPrecoGasolina() {
        return precoGasolina;
    }

    public Float getPreoAlcool() {
        return precoAlcool;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPreferencias appPreferencias = new AppPreferencias(this);
        appPreferencias.setVeiculoIdDoUltimoAbastecimentoPorVeiclo(veiculo.getId());
        appPreferencias.commit();
    }
}