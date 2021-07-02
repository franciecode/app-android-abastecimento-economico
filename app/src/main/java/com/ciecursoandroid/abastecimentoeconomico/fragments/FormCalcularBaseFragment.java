package com.ciecursoandroid.abastecimentoeconomico.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

public abstract class FormCalcularBaseFragment extends Fragment {

    // TODO: Rename and change types of parameters
    protected Listener listener;
    protected Veiculo veiculo;
    protected Float kmsLitroGasolina = 0.0f;
    protected Float kmsLitroAlcool = 0.0f;

    public FormCalcularBaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setListener((Listener) context);
        listener.onChangedFormCalcularFragmentListener(veiculo, kmsLitroGasolina, kmsLitroAlcool);
    }

    public interface Listener {
        void onChangedFormCalcularFragmentListener(Veiculo veiculo, Float kmsLitroGasolina, Float KmsLitroAlcool);
    }
}