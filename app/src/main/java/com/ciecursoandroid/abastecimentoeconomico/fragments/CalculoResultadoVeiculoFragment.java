package com.ciecursoandroid.abastecimentoeconomico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.activities.BaseCalculoResult;
import com.ciecursoandroid.abastecimentoeconomico.activities.NavigationInActivities;
import com.ciecursoandroid.abastecimentoeconomico.enums.LocalViagem;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.RendimentoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsNumeros;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculoResultadoVeiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculoResultadoVeiculoFragment extends Fragment {

    private static final String LOCAL_VIAGEM = "localViagem";
    private static final String PRECO_GASOLINA = "precoGasolina";
    private static final String PRECO_ALCOOL = "precoAlcool";
    private static final String VEICULO = "veiculo";
    private LocalViagem localViagem;
    private float kmsGasolina, kmsAlcool;
    private Veiculo veiculo;
    BaseCalculoResult baseCalculoResult;
    private float precoGasolina;
    private float precoAlcool;
    private TextView textViewTotalAPagar;
    private TextView textViewValorEconomizado;
    private Abastecimento abastecimento;
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


    public CalculoResultadoVeiculoFragment() {
        // Required empty public constructor
    }

    public static CalculoResultadoVeiculoFragment newInstance(LocalViagem localViagem, Float precoGasolina, Float precoAlcool, Veiculo veiculo) {
        CalculoResultadoVeiculoFragment fragment = new CalculoResultadoVeiculoFragment();
        Bundle args = new Bundle();
        args.putString(LOCAL_VIAGEM, localViagem.name());
        args.putFloat(PRECO_GASOLINA, precoGasolina);
        args.putFloat(PRECO_ALCOOL, precoAlcool);
        args.putParcelable(VEICULO, veiculo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        localViagem = LocalViagem.valueOf(getArguments().getString(LOCAL_VIAGEM));
        precoGasolina = getArguments().getFloat(PRECO_GASOLINA);
        precoAlcool = getArguments().getFloat(PRECO_ALCOOL);
        veiculo = getArguments().getParcelable(VEICULO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calculo_resultado_veiculo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        baseCalculoResult = new BaseCalculoResult(view, getActivity(), precoAlcool, precoGasolina) {
            @Override
            public void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos) {
                exeCalcularAbastecimento(abastecer, precoCombustivel, litrosAbastecidos);
            }
        };
        if (localViagem == LocalViagem.CIDADE) {
            kmsGasolina = veiculo.getKmsLitroCidadeGasolina();
            kmsAlcool = veiculo.getKmsLitroCidadeAlcool();
        } else {
            kmsGasolina = veiculo.getKmsLitroRodoviaGasolina();
            kmsAlcool = veiculo.getKmsLitroRodoviaAlcool();
        }

        textViewTotalAPagar = view.findViewById(R.id.textViewTotalAPagar);
        textViewValorEconomizado = view.findViewById(R.id.textViewValorEconomizado);

        textViewTablePrecoKmGasolina = view.findViewById(R.id.tablePrecoKmGasolina);
        textViewTableTotalKmsGasolina = view.findViewById(R.id.tableTotalKmsGasolina);
        textViewTableTotalPagarGasolina = view.findViewById(R.id.tableTotalPagarGasolina);
        textViewTablePrecoKmAlcool = view.findViewById(R.id.tablePrecoKmAlcool);
        textViewTableTotalKmsAlcool = view.findViewById(R.id.tableTotalKmsAlcool);
        textViewTableTotalPagarAlcool = view.findViewById(R.id.tableTotalPagarAlcool);
        textViewTablePrecoKmDiferenca = view.findViewById(R.id.tablePrecoKmDiferenca);
        textViewTableTotalKmsDiferenca = view.findViewById(R.id.tableTotalKmsDiferenca);
        textViewTableTotalPagarDiferenca = view.findViewById(R.id.tableTotalPagarDiferenca);

        Button btnSalvar = view.findViewById(R.id.btnSalvarAbastecimento);
        btnSalvar.setOnClickListener(v -> salvarAbastecimento(abastecimento));

        Button btnNaoSalvar = view.findViewById(R.id.btnNaoSalvarAbastecimento);
        btnNaoSalvar.setOnClickListener(v -> naoSalvar());

        viewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        viewModel.setRepository(new AbastecimentoRepository(getActivity()));

        baseCalculoResult.calcularCombustivelMaisBarato(precoAlcool, precoGasolina, kmsGasolina, kmsAlcool);
    }

    private void naoSalvar() {
        getActivity().finish();
    }

    private void exeCalcularAbastecimento(TipoCombustivel abastecido, float precoCombustivel, float litrosAbastecidos) {
        if (abastecido == null) return;
        CalculadoraCombustivel.CalculoAbastecimento result = baseCalculoResult.calculadoraCombustivel
                .calcularAbastecimento(litrosAbastecidos,
                        abastecido, precoGasolina, precoAlcool, kmsGasolina, kmsAlcool);

        float totalPagar = abastecido == TipoCombustivel.ALCOOL ? result.getRendimentoAlcool().getCustoTotal() : result.getRendimentoGasolina().getCustoTotal();
        textViewTotalAPagar.setText(UtilsNumeros.formatDinheiro(getActivity(), totalPagar));
        textViewValorEconomizado.setText(UtilsNumeros.formatDinheiro(getActivity(), result.getValorEconomizado()));
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
        abastecimento = new Abastecimento();
        abastecimento.setTipoCalculo(TipoCalculo.VEICULO);
        abastecimento.setVeiculoId(veiculo.getId());
        abastecimento.setPorcentagemEconomia(baseCalculoResult.combustivelMaisBarato.getPorcentagemEconomia());
        abastecimento.setCombustivelRecomendado(baseCalculoResult.combustivelRecomendado);
        abastecimento.setCombustivelAbastecido(abastecido);
        abastecimento.setPrecoAlcool(precoAlcool);
        abastecimento.setPrecoGasolina(precoGasolina);
        abastecimento.setLocalViagem(localViagem);
        abastecimento.setTotalPago(totalPagar);
        abastecimento.setTotalLitrosAbastecidos(baseCalculoResult.litrosAbastecidos);
        abastecimento.setValorEconomizado(valorEconomizado);
        abastecimento.setKmsLitroGasolina(kmsGasolina);
        abastecimento.setKmsLitroAlcool(kmsAlcool);

    }

    public void salvarAbastecimento(Abastecimento abastecimento) {
        if (!validarFormSalvarAbastecimento()) return;
        viewModel.insert(abastecimento, new AbastecimentoRepository.OnInsert() {
            @Override
            public void onComplete(Exception e, Abastecimento abastecimento) {
                if (e != null) {
                    Alerts.alertWaring(getActivity(),
                            getString(R.string.erro_ao_salvar_abastecimento), e.getMessage())
                            .setPositiveButton(R.string.ok, null)
                            .show();
                } else {
                    Alerts.alertSuccess(getActivity(),
                            getString(R.string.sucesso), getString(R.string.abastecimento_salvo_com_sucesso))
                            .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                NavigationInActivities.goAbastecimentos(getActivity());
                                getActivity().finish();
                            }).show();
                }
            }
        });
    }

    protected boolean validarFormSalvarAbastecimento() {
        boolean erro = false;
        if (baseCalculoResult.radioGroupAbastecimento.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Combústível não informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }
        if (baseCalculoResult.litrosAbastecidos == 0) {
            Toast.makeText(getActivity(), "Total de litros não informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }

        return erro == false;
    }

}