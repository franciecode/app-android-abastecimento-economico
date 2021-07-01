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
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.activities.ActivitiesNavigation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormCalcularVeiculoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormCalcularVeiculoFragment extends FormCalcularBaseFragment {
    // TODO: Rename and change types of parameters
    private Spinner spinnerVeiculo;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        long veiculoId = intent.getLongExtra("veiculoId", -1);
                        selectSpinnerVeiculo(veiculoId);
                    }
                }
            });

    private void selectSpinnerVeiculo(long veiculoId) {
        Toast.makeText(getActivity(), "select veiculo id: " + veiculoId, Toast.LENGTH_SHORT).show();
    }


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
        spinnerVeiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    spinnerVeiculo.setSelection(0);
                    ActivitiesNavigation.goAddVeiculoForResult(getActivity(), mStartForResult);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return root;
    }

}