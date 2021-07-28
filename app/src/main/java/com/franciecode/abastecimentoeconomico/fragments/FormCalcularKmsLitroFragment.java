package com.franciecode.abastecimentoeconomico.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.franciecode.abastecimentoeconomico.R;

import org.jetbrains.annotations.NotNull;


public class FormCalcularKmsLitroFragment extends FormCalcularBaseFragment implements TextWatcher {
    private EditText editTextKmsLitroGasolina;
    private EditText editTextKmsLitroAlcool;

    public FormCalcularKmsLitroFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            kmsLitroGasolina = savedInstanceState.getFloat("kmsLitroGasolina", 0f);
            kmsLitroAlcool = savedInstanceState.getFloat("kmsLitroAlcool", 0f);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_kms_litro_from_calcular, container, false);
        editTextKmsLitroGasolina = root.findViewById(R.id.editTextKmsLitroGasolina);
        editTextKmsLitroAlcool = root.findViewById(R.id.editTextKmsLitroAlcool);
        editTextKmsLitroGasolina.addTextChangedListener(this);
        editTextKmsLitroAlcool.addTextChangedListener(this);
        if (kmsLitroGasolina > 0)
            editTextKmsLitroGasolina.setText(String.valueOf(kmsLitroGasolina));
        if (kmsLitroAlcool > 0)
            editTextKmsLitroGasolina.setText(String.valueOf(kmsLitroAlcool));
        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("kmsLitroGasolina", kmsLitroGasolina);
        outState.putFloat("kmsLitroAlcool", kmsLitroAlcool);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        kmsLitroGasolina = Float.valueOf(TextUtils.isEmpty(editTextKmsLitroGasolina.getText())
                ? "0" : editTextKmsLitroGasolina.getText().toString());
        kmsLitroAlcool = Float.valueOf(TextUtils.isEmpty(editTextKmsLitroAlcool.getText())
                ? "0" : editTextKmsLitroAlcool.getText().toString());
        listener.onChangedFormCalcularFragmentListener(null, kmsLitroGasolina, kmsLitroAlcool);

    }
}