package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

@Database(entities = {Veiculo.class, Abastecimento.class}, version = 2, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDBRoom extends RoomDatabase {
    public static AppDBRoom INSTANCE;

    public abstract AbastecimentoDao abastecimentoDao();

    public static AppDBRoom getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDBRoom.class) {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context, AppDBRoom.class, "appDatabase")
                            .fallbackToDestructiveMigration()
                            .build();
            }
        }
        return INSTANCE;
    }
}