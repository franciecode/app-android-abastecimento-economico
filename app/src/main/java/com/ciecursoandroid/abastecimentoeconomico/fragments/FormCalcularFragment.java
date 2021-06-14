package com.ciecursoandroid.abastecimentoeconomico.fragments;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

public abstract class FormCalcularFragment extends Fragment {

    // TODO: Rename and change types of parameters
    protected Listener listener;
    protected Veiculo veiculo;
    protected Float kmsLitroGasolina = 0.0f;
    protected Float kmsLitroAlcool = 0.0f;

    public FormCalcularFragment() {
        // Required empty public constructor
    }

    public interface Listener {
        void onChangedFormCalcularFragment(Veiculo veiculo, Float kmsLitroGasolina, Float KmsLitroAlcool);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListener(Listener listener) {
        Toast.makeText(getActivity(), "setListener", Toast.LENGTH_SHORT).show();
        this.listener = listener;
        listener.onChangedFormCalcularFragment(veiculo, kmsLitroGasolina, kmsLitroAlcool);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Listener myListener = (Listener) context;
        myListener.onChangedFormCalcularFragment(veiculo, kmsLitroGasolina, kmsLitroAlcool);
    }
}