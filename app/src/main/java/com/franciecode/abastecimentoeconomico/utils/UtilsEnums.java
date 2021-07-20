package com.franciecode.abastecimentoeconomico.utils;

import android.content.Context;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;

public class UtilsEnums {
    public static String getTipoCalculo(Context context, TipoCalculo tipoCalculo) {
        switch (tipoCalculo) {
            case BASICO:
                return context.getString(R.string.basico);
            case KMS_LITRO:
                return context.getString(R.string.mks_por_litro);
            case VEICULO:
                return context.getString(R.string.veiculo);
        }
        return "";
    }
}
