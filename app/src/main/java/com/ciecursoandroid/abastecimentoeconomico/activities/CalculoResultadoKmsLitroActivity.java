package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abaste;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.RendimentoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsNumeros;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

public class CalculoResultadoKmsLitroActivity extends CalculoResultadoBaseActivity {
    private float kmsGasolina, kmsAlcool;
    private TextView textViewTotalAPagar;
    private TextView textViewValorEconomizado;
    private Abaste abastecimento;
    private AbastecimentoViewModel viewModel;
    private TextView textViewTablePrecoKmGasolina;
    private TextView textViewTableTotalKmsGasolina;
    private TextView textViewTableTotalPagarGasolina;
    private TextView textViewTablePrecoKmAlcool;
    private TextView textViewTableTotalKmsAlcool;
    private TextView textViewTableTotalPagarAlcool;
    private TextView textViewTablePrecoKmDiferenca;
    private TextView textViewTableTotalKmsDiferenca;
    private TextView textViewTableTotalPagarDiferenca;
    private Veiculo veiculo;
    Button btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_kms_litro);
        setFields();

        abastecimento = new Abaste();

        kmsGasolina = getIntent().getFloatExtra("kmsGasolina", 0);
        kmsAlcool = getIntent().getFloatExtra("kmAlcool", 0);

        textViewTotalAPagar = findViewById(R.id.textViewTotalAPagar);
        textViewValorEconomizado = findViewById(R.id.textViewValorEconomizado);

        textViewTablePrecoKmGasolina = findViewById(R.id.tablePrecoKmGasolina);
        textViewTableTotalKmsGasolina = findViewById(R.id.tableTotalKmsGasolina);
        textViewTableTotalPagarGasolina = findViewById(R.id.tableTotalPagarGasolina);
        textViewTablePrecoKmAlcool = findViewById(R.id.tablePrecoKmAlcool);
        textViewTableTotalKmsAlcool = findViewById(R.id.tableTotalKmsAlcool);
        textViewTableTotalPagarAlcool = findViewById(R.id.tableTotalPagarAlcool);
        textViewTablePrecoKmDiferenca = findViewById(R.id.tablePrecoKmDiferenca);
        textViewTableTotalKmsDiferenca = findViewById(R.id.tableTotalKmsDiferenca);
        textViewTableTotalPagarDiferenca = findViewById(R.id.tableTotalPagarDiferenca);

        btnSalvar = findViewById(R.id.btnSalvarAbastecimento);
        btnSalvar.setEnabled(false);
        btnSalvar.setOnClickListener(v -> salvarAbastecimento(abastecimento));

        Button btnNaoSalvar = findViewById(R.id.btnNaoSalvarAbastecimento);

        viewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        viewModel.setRepository(new AbastecimentoRepository(this));

        radioGroupAbastecimento.setEnabled(false);
        editTextLitros.setEnabled(false);

        VeiculoViewModel veiculoViewModel = new ViewModelProvider(this).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(this));
        veiculoViewModel.getByTipo(Veiculo.TIPO_VEICULO_KMSLITRO).observe(this, new Observer<Veiculo>() {
            @Override
            public void onChanged(Veiculo v) {
                veiculo = v;
                btnSalvar.setEnabled(true);
                abastecimento.setVeiculoId(v.getId());
                radioGroupAbastecimento.setEnabled(true);
                editTextLitros.setEnabled(true);
            }
        });

        calcularCombustivelMaisBarato(precoAlcool, precoGAsolina, kmsGasolina, kmsAlcool);


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

        setTableValues(result);
        setAbastecimento(abastecido, totalPagar, result.getValorEconomizado());
    }

    private void setTableValues(CalculadoraCombustivel.CalculoAbastecimento result) {
        RendimentoCombustivel rGasolina = result.getRendimentoGasolina();
        RendimentoCombustivel rAlcool = result.getRendimentoAlcool();

        textViewTablePrecoKmGasolina.setText(String.format(getString(R.string.valor_dinheiro), rGasolina.getPrecoKm()));
        textViewTableTotalKmsGasolina.setText(String.format("%.2f", rGasolina.getTotalKms()));
        textViewTableTotalPagarGasolina.setText(String.format(getString(R.string.valor_dinheiro), rGasolina.getCustoTotal()));

        textViewTablePrecoKmAlcool.setText(String.format(getString(R.string.valor_dinheiro), rAlcool.getPrecoKm()));
        textViewTableTotalKmsAlcool.setText(String.format("%.2f", rAlcool.getTotalKms()));
        textViewTableTotalPagarAlcool.setText(String.format(getString(R.string.valor_dinheiro), rAlcool.getCustoTotal()));

        textViewTablePrecoKmDiferenca.setText(String.format(getString(R.string.valor_dinheiro), Math.abs(rAlcool.getPrecoKm() - rGasolina.getPrecoKm())));
        textViewTableTotalKmsDiferenca.setText(String.format("%.2f", Math.abs(rAlcool.getTotalKms() - rGasolina.getTotalKms())));
        textViewTableTotalPagarDiferenca.setText(String.format(getString(R.string.valor_dinheiro), Math.abs(rAlcool.getCustoTotal() - rGasolina.getCustoTotal())));

    }

    private void setAbastecimento(TipoCombustivel abastecido, float totalPagar, float valorEconomizado) {
        abastecimento.setTipoCalculo(TipoCalculo.KMS_LITRO);
        abastecimento.setPorcentagemEconomia(combustivelMaisBarato.getPorcentagemEconomia());
        abastecimento.setCombustivelRecomendado(combustivelRecomendado);
        abastecimento.setCombustivelAbastecido(abastecido);
        abastecimento.setPrecoAlcool(precoAlcool);
        abastecimento.setPrecoGasolina(precoGAsolina);
        abastecimento.setTotalPago(totalPagar);
        abastecimento.setTotalLitrosAbastecidos(litrosAbastecidos);
        abastecimento.setValorEconomizado(valorEconomizado);
        abastecimento.setKmsLitroGasolina(kmsGasolina);
        abastecimento.setKmsLitroAlcool(kmsAlcool);
    }

    @Override
    public void salvarAbastecimento(Abaste abastecimento) {
        if (!validarFormSalvarAbastecimento()) return;
        viewModel.insert(abastecimento, new AbastecimentoRepository.OnInsert() {
            @Override
            public void onComplete(Exception e, Abaste abastecimento) {
                if (e != null) {
                    Alerts.alertWaring(CalculoResultadoKmsLitroActivity.this,
                            getString(R.string.erro_ao_salvar_abastecimento), e.getMessage())
                            .setPositiveButton(R.string.ok, null)
                            .show();
                } else {
                    Alerts.alertSuccess(CalculoResultadoKmsLitroActivity.this,
                            getString(R.string.sucesso), getString(R.string.abastecimento_salvo_com_sucesso))
                            .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                NavigationInActivities.goAbastecimentos(getApplicationContext());
                                finish();
                            }).show();
                }
            }
        });
    }
}