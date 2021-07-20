package com.franciecode.abastecimentoeconomico.utils;

import android.content.Context;

import com.franciecode.abastecimentoeconomico.R;

import java.util.Formatter;

public class UtilsNumeros {
    public static String formatDinheiro(Context c, Float valor) {
        Formatter f = new Formatter();
        return f.format(c.getString(R.string.valor_dinheiro), valor).toString();
    }
}
