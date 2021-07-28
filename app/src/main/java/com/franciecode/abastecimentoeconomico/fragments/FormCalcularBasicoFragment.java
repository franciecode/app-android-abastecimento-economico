package com.franciecode.abastecimentoeconomico.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.franciecode.abastecimentoeconomico.R;

public class FormCalcularBasicoFragment extends FormCalcularBaseFragment {

    public FormCalcularBasicoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_calcular_basico, container, false);
    }


}