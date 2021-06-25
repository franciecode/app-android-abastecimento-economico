package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;

import com.ciecursoandroid.abastecimentoeconomico.R;

public class AbastecimentosActivity extends BaseMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimentos);
        getSupportActionBar().setTitle(R.string.abastecimentos);
    }
}