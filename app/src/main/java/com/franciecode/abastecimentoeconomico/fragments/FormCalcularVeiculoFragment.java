package com.franciecode.abastecimentoeconomico.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.activities.NavigationInActivities;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;
import com.franciecode.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;

import java.util.List;

public class FormCalcularVeiculoFragment extends FormCalcularBaseFragment {
    // TODO: Rename and change types of parameters
    private Spinner spinnerVeiculo;
    private VeiculoViewModel veiculoViewModel;
    ArrayAdapter<String> spinnerAdapter;
    private List<Veiculo> listVeiculos;
    private Long newVeiculoId;
    private Long veiculoIdUltimoAbastecimento;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        newVeiculoId = intent.getLongExtra("veiculoId", -1);
                    }
                }
            });

    public FormCalcularVeiculoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veiculoViewModel = new ViewModelProvider(getActivity()).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(getActivity()));

        AppPreferencias pref = new AppPreferencias(getActivity());
        veiculoIdUltimoAbastecimento = pref.getVeiculoIdDoUltimoAbastecimentoPorVeiclo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_veiculo_form_calcular, container, false);
        spinnerVeiculo = root.findViewById(R.id.spinnerVeiculoFormCalcular);
        setDataSpinner();

        spinnerVeiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i <= 1)
                    veiculo = null;
                if (i == 1) {
                    inserirVeiculo();
                } else if (i >= 2) {
                    veiculo = listVeiculos.get(i - 2);
                }
                listener.onChangedFormCalcularFragmentListener(veiculo, 0f, 0f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

    private void inserirVeiculo() {
        spinnerVeiculo.setSelection(0);
        NavigationInActivities.goAddVeiculoForResult(getActivity(), mStartForResult);
    }

    private void setDataSpinner() {
        spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        spinnerVeiculo.setAdapter(spinnerAdapter);
        resetDataSpinnerAdapter();
        carregarVeiculos();

    }

    private void resetDataSpinnerAdapter() {
        spinnerAdapter.clear();
        spinnerAdapter.add("Selecione um veículo...");
        spinnerAdapter.add("+ NOVO VEÍCULO +");
    }

    private void carregarVeiculos() {
        veiculoViewModel.getAll().observe(getActivity(), veiculos -> {
            resetDataSpinnerAdapter();
            Veiculo v;
            for (int i = 0; i < veiculos.size(); i++) {
                v = veiculos.get(i);
                spinnerAdapter.add(v.getNome());
                listVeiculos = veiculos;
                if ((newVeiculoId != null && v.getId() == newVeiculoId) ||
                        (veiculoIdUltimoAbastecimento != -1 && v.getId() == veiculoIdUltimoAbastecimento))
                    autoSelecionarVeiculo(i + 2, v);
            }
        });
    }

    private void autoSelecionarVeiculo(int indexSpinner, Veiculo v) {
        veiculo = v;
        spinnerVeiculo.setSelection(indexSpinner);
        listener.onChangedFormCalcularFragmentListener(veiculo, null, null);
    }


}