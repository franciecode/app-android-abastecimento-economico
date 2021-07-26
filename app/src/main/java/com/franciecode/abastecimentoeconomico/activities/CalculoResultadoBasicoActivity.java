package com.franciecode.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.enums.TipoCombustivel;
import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.franciecode.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.franciecode.abastecimentoeconomico.utils.UtilsNumeros;
import com.franciecode.abastecimentoeconomico.widgets.Alerts;

public class CalculoResultadoBasicoActivity extends CalculoResultadoBaseActivity {

    float kmsGasolina;
    float kmsAlcool;
    TextView textViewTotalAPagar;
    TextView textViewValorEconomizado;
    Abastecimento abastecimento;
    AbastecimentoViewModel abastecimentoViewModel;
    VeiculoViewModel veiculoViewModel;
    Button btnSalvar;
    Button btnNaoSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_basico);

        abastecimento = new Abastecimento();

        setBaseFields();

        textViewTotalAPagar = findViewById(R.id.textViewTotalAPagar);
        textViewValorEconomizado = findViewById(R.id.textViewValorEconomizado);
        textViewTotalAPagar.setText(UtilsNumeros.formatDinheiro(this, 0f));
        textViewValorEconomizado.setText(UtilsNumeros.formatDinheiro(this, 0f));
        btnSalvar = findViewById(R.id.btnSalvar);
        btnNaoSalvar = findViewById(R.id.btnNaoSalvar);

        calcularCombustivelMaisBarato(precoAlcool, precoGAsolina, 10, 7);

        abastecimentoViewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        abastecimentoViewModel.setRepository(new AbastecimentoRepository(this));

        veiculoViewModel = new ViewModelProvider(this).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(this));

        btnSalvar.setEnabled(false);
        btnSalvar.setOnClickListener(v -> salvarAbastecimento(abastecimento));
        btnNaoSalvar.setOnClickListener(v -> finish());

        radioGroupAbastecimento.setEnabled(false);
        editTextLitros.setEnabled(false);

        veiculoViewModel.getByTipo(Veiculo.TIPO_VEICULO_BASICO).observe(this,
                veiculo -> {
                    btnSalvar.setEnabled(true);
                    abastecimento.setVeiculoId(veiculo.getId());
                    kmsGasolina = veiculo.getKmsLitroCidadeGasolina();
                    kmsAlcool = veiculo.getKmsLitroCidadeAlcool();
                    radioGroupAbastecimento.setEnabled(true);
                    editTextLitros.setEnabled(true);
                    setDetalheVeiculo(String.format(
                            getString(R.string.detalhe_veiculo_docalculo_basico),
                            100 * veiculo.getKmsLitroCidadeAlcool() / veiculo.getKmsLitroCidadeGasolina(),
                            "%"));
                }
        );
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
        abastecimentoViewModel.insert(abastecimento, (e, abastecimento1) -> {
            if (e != null)
                Alerts.alertWaring(this, getString(R.string.erro_ao_salvar), e.getMessage())
                        .setPositiveButton(R.string.ok, null).show();
            else

                Alerts.alertSuccess(CalculoResultadoBasicoActivity.this, getString(R.string.sucesso), getString(R.string.abastecimento_salvo_com_sucesso))
                        .setCancelable(false)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            NavigationInActivities.goAbastecimentos(CalculoResultadoBasicoActivity.this);
                            finish();
                        }).show();


        });
    }
}