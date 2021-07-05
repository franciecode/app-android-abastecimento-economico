package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsNumeros;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

public class CalculoResultadoBasicoActivity extends CalculoResultadoBaseActivity {

    float kmsGasolina = 10;
    float kmsAlcool = 7;
    TextView textViewTotalAPagar;
    TextView textViewValorEconomizado;
    Abastecimento abastecimento;
    AbastecimentoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_basico);

        setFields();
        textViewTotalAPagar = findViewById(R.id.textViewTotalAPagar);
        textViewValorEconomizado = findViewById(R.id.textViewValorEconomizado);
        textViewTotalAPagar.setText(UtilsNumeros.formatDinheiro(this, 0f));
        textViewValorEconomizado.setText(UtilsNumeros.formatDinheiro(this, 0f));
        Button btnSalvar = findViewById(R.id.btnSalvar);
        Button btnNaoSalvar = findViewById(R.id.btnNaoSalvar);
        calcularCombustivelMaisBarato(precoAlcool, precoGAsolina, 10, 7);

        viewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        viewModel.setRepository(new AbastecimentoRepository(this));

        btnSalvar.setOnClickListener(v -> salvarAbastecimento(abastecimento));
        btnNaoSalvar.setOnClickListener(v -> finish());
    }

    @Override
    public void calcularAbastecimento(TipoCombustivel abastecido, float precoCombustivel, float litrosAbastecidos) {
        if (abastecido == null) return;
        CalculadoraCombustivel.CalculoAbastecimento result = calculadoraCombustivel
                .calcularAbastecimento(litrosAbastecidos,
                        abastecido, precoGAsolina, precoAlcool, kmsGasolina, kmsAlcool);

        float totalPagar = abastecido == TipoCombustivel.ALCOOL ? result.getRendimentoAlcool().getCustoTotal() : result.getRendimentoGasolina().getCustoTotal();
        textViewTotalAPagar.setText(UtilsNumeros.formatDinheiro(this, totalPagar));
        textViewValorEconomizado.setText(UtilsNumeros.formatDinheiro(this, result.getValorEconomizado()));
        if (result.getValorEconomizado() < 0) {
            textViewValorEconomizado.setTextColor(getResources().getColor(R.color.color_text_red));
        } else {
            textViewValorEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
        }

        abastecimento = new Abastecimento();
        abastecimento.setPorcentagemEconomia(combustivelMaisBarato.getPorcentagemEconomia());
        abastecimento.setTipoCalculo(TipoCalculo.BASICO);
        abastecimento.setCombustivelRecomendado(combustivelRecomendado);
        abastecimento.setCombustivelAbastecido(abastecido);
        abastecimento.setPrecoAlcool(precoAlcool);
        abastecimento.setPrecoGasolina(precoGAsolina);
        abastecimento.setTotalPago(totalPagar);
        abastecimento.setTotalLitrosAbastecidos(litrosAbastecidos);
        abastecimento.setValorEconomizado(result.getValorEconomizado());
        abastecimento.setKmsLitroGasolina(kmsGasolina);
        abastecimento.setKmsLitroAlcool(kmsAlcool);
    }

    @Override
    public void salvarAbastecimento(Abastecimento abastecimento) {
        if (!validarFormSalvarAbastecimento()) return;
        viewModel.insert(abastecimento, (e, abastecimento1) -> {
            if (e != null)
                Alerts.alertWaring(this, "Erro ao salvar", e.getMessage())
                        .setPositiveButton("ok", null).show();
            else
                Alerts.alertSuccess(this, "Sucesso", "Abastecimento salvo com sucesso")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavigationInActivities.goAbastecimentos(CalculoResultadoBasicoActivity.this);
                                finish();
                            }
                        }).show();
        });
    }
}