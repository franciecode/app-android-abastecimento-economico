package com.ciecursoandroid.abastecimentoeconomico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.ciecursoandroid.abastecimentoeconomico.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormCalcularVeiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormCalcularVeiculoFragment extends FormCalcularFragment {
    // TODO: Rename and change types of parameters
    private Spinner spinnerVeiculo;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_veiculo_form_calcular, container, false);
        spinnerVeiculo = root.findViewById(R.id.spinnerVeiculoFormCalcular);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Selecione um veículo...");
        adapter.add("+ NOVO VEÍCULO +");
        spinnerVeiculo.setAdapter(adapter);
        return root;
    }
}