package com.ciecursoandroid.abastecimentoeconomico.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.activities.ActivitiesNavigation;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormCalcularVeiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormCalcularVeiculoFragment extends FormCalcularBaseFragment {
    // TODO: Rename and change types of parameters
    private Spinner spinnerVeiculo;
    private VeiculoViewModel veiculoViewModel;
    ArrayAdapter<String> spinnerAdapter;
    private List<Veiculo> listVeiculos;
    private Long newVeiculoId;

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

    public static FormCalcularVeiculoFragment newInstance() {
        FormCalcularVeiculoFragment fragment = new FormCalcularVeiculoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veiculoViewModel = new ViewModelProvider(getActivity()).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(getActivity()));
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
        ActivitiesNavigation.goAddVeiculoForResult(getActivity(), mStartForResult);
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
        veiculoViewModel.getAll().observe(getActivity(), new Observer<List<Veiculo>>() {
            @Override
            public void onChanged(List<Veiculo> veiculos) {
                resetDataSpinnerAdapter();
                Veiculo v = null;
                for (int i = 0; i < veiculos.size(); i++) {
                    v = veiculos.get(i);
                    spinnerAdapter.add(v.getNome());
                    listVeiculos = veiculos;
                    selecionarVeiculoRecemCadastrado(i + 2, v);
                }
            }
        });
    }

    private void selecionarVeiculoRecemCadastrado(int indexSpinner, Veiculo v) {
        if (newVeiculoId != null && v.getId() == newVeiculoId) {
            veiculo = v;
            spinnerVeiculo.setSelection(indexSpinner);
            listener.onChangedFormCalcularFragmentListener(veiculo, null, null);
        }
    }


}