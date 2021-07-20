package com.franciecode.abastecimentoeconomico.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.franciecode.abastecimentoeconomico.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormCalcularKmsLitroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormCalcularKmsLitroFragment extends FormCalcularBaseFragment implements TextWatcher {

    private EditText editTextKmsLitroGasolina;
    private EditText editTextKmsLitroAlcool;

    public FormCalcularKmsLitroFragment() {
        // Required empty public constructor
    }

    public static FormCalcularKmsLitroFragment newInstance(String param1, String param2) {
        FormCalcularKmsLitroFragment fragment = new FormCalcularKmsLitroFragment();
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
        View root = inflater.inflate(R.layout.fragment_kms_litro_from_calcular, container, false);
        editTextKmsLitroGasolina = root.findViewById(R.id.editTextKmsLitroGasolina);
        editTextKmsLitroAlcool = root.findViewById(R.id.editTextKmsLitroAlcool);
        editTextKmsLitroGasolina.addTextChangedListener(this);
        editTextKmsLitroAlcool.addTextChangedListener(this);
        return root;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        Float kmsLitroGasolina = Float.valueOf(TextUtils.isEmpty(editTextKmsLitroGasolina.getText())
                ? "0.0" : editTextKmsLitroGasolina.getText().toString());
        Float kmsLitroAlcool = Float.valueOf(TextUtils.isEmpty(editTextKmsLitroAlcool.getText())
                ? "0.0" : editTextKmsLitroAlcool.getText().toString());
        listener.onChangedFormCalcularFragmentListener(null, kmsLitroGasolina, kmsLitroAlcool);
    }
}