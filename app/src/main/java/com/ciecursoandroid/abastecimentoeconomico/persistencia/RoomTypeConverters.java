package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.room.TypeConverter;

import com.ciecursoandroid.abastecimentoeconomico.enums.LocalViagem;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;

import java.sql.Timestamp;

public class RoomTypeConverters {
    @TypeConverter
    public static TipoCalculo fromTipoCalculo(String tipo) {
        return TipoCalculo.valueOf(tipo);
    }

    @TypeConverter
    public static String toTipoCalculo(TipoCalculo tipo) {
        return tipo.name();
    }

    @TypeConverter
    public static LocalViagem fromLocalViagem(String local) {
        if (local.equals(""))
            return null;
        return LocalViagem.valueOf(local);
    }

    @TypeConverter
    public static String toLocalViagem(LocalViagem local) {
        if (local == null)
            return "";
        return local.name();
    }

    @TypeConverter
    public static TipoCombustivel fromTipoCombustivel(String combustivel) {
        return TipoCombustivel.valueOf(combustivel);
    }

    @TypeConverter
    public static String toTipoCombustivel(TipoCombustivel combustivel) {
        return combustivel.name();
    }

    @TypeConverter
    public static Timestamp fromTimestamp(long timestamp) {
        return new Timestamp(timestamp);
    }

    @TypeConverter
    public static long toTimestamp(Timestamp timestamp) {
        return timestamp.getTime();
    }
}
