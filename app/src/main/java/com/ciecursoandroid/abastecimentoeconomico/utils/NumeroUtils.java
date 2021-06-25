package com.ciecursoandroid.abastecimentoeconomico.utils;

import android.content.Context;

import com.ciecursoandroid.abastecimentoeconomico.R;

import java.util.Formatter;

public class NumeroUtils {
    public static String formatDinheiro(Context c, Float valor) {
        Formatter f = new Formatter();
        return f.format(c.getString(R.string.valor_dinheiro), valor).toString();
    }
}