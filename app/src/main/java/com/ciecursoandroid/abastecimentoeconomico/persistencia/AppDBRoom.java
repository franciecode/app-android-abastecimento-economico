package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.models.VeiculoTipCalculoKMsLitro;
import com.ciecursoandroid.abastecimentoeconomico.models.VeiculoTipoCalculoBasico;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;

@Database(entities = {Veiculo.class, Abastecimento.class}, version = 7, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDBRoom extends RoomDatabase {
    public static final String DATABASE_NAME = "appDatabase";
    public static AppDBRoom INSTANCE;

    public abstract AbastecimentoDao abastecimentoDao();

    public static Migration migration_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(@NonNull @NotNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIEW AbastecimentoRelatorioGrafico " +
                    "AS SELECT sum(totalPago) as somaTotalGasto, " +
                    "sum(valorEconomizado) as somaValorEconomizado, " +
                    "");
        }
    };

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
        Veiculo[] veiculos = new Veiculo[]{
                new VeiculoTipoCalculoBasico(),
                new VeiculoTipCalculoKMsLitro()
        };
        appDatabase.veiculoDao().insertAll(veiculos);
    }
}
