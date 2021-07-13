package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.models.VeiculoTipCalculoKMsLitro;
import com.ciecursoandroid.abastecimentoeconomico.models.VeiculoTipoCalculoBasico;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;

@Database(entities = {Veiculo.class, Abastecimento.class},
        views = {AbastecimentoRelatorioGraficoView.class},
        version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDBRoom extends RoomDatabase {
    public static final String DATABASE_NAME = "appDatabase";
    public static AppDBRoom INSTANCE;

    public abstract AbastecimentoDao abastecimentoDao();

    public abstract VeiculoDao veiculoDao();

    public static AppDBRoom getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDBRoom.class) {
                if (INSTANCE == null)
                    INSTANCE = buildDatabase(context);
            }
        }
        return INSTANCE;
    }

    public static AppDBRoom buildDatabase(final Context context) {
        return Room.databaseBuilder(context, AppDBRoom.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull @NotNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                prePopulateVeiculos(getInstance(context));
                            }
                        });
                    }
                })
                .build();
    }

    private static void prePopulateVeiculos(AppDBRoom appDatabase) {
        Veiculo v1 = new Veiculo();
        v1.setTipo(Veiculo.TIPO_VEICULO_CARRO);
        v1.setNome("FIAT Mobi 1.0\u00AD6V Drive ");
        v1.setKmsLitroCidadeAlcool(9.7f);
        v1.setKmsLitroRodoviaAlcool(11.5f);
        v1.setKmsLitroCidadeGasolina(13.8f);
        v1.setKmsLitroRodoviaGasolina(16.4f);
        Veiculo v2 = new Veiculo();
        v2.setTipo(Veiculo.TIPO_VEICULO_CARRO);
        v2.setNome("PEUGEOT 208");
        v2.setKmsLitroCidadeAlcool(9.6f);
        v2.setKmsLitroRodoviaAlcool(10.7f);
        v2.setKmsLitroCidadeGasolina(13.9f);
        v2.setKmsLitroRodoviaGasolina(15.5f);


        Veiculo[] veiculos = new Veiculo[]{
                new VeiculoTipoCalculoBasico(),
                new VeiculoTipCalculoKMsLitro(),
                v1, v2
        };
        appDatabase.veiculoDao().insertAll(veiculos);
    }
}
