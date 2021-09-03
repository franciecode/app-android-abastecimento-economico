package com.franciecode.abastecimentoeconomico.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.models.VeiculoTipCalculoKMsLitro;
import com.franciecode.abastecimentoeconomico.models.VeiculoTipoCalculoBasico;
import com.franciecode.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Veiculo.class, Abastecimento.class},
        views = {AbastecimentoRelatorioGraficoView.class},
        version = 3, exportSchema = false)
@TypeConverters({RoomTypeConverters.class})
public abstract class AppDBRoom extends RoomDatabase {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("DROP VIEW `AbastecimentoRelatorioGraficoView`");
            database.execSQL("CREATE VIEW `AbastecimentoRelatorioGraficoView` AS " +
                    "select " +
                    "totalPago as somaTotalPago, " +
                    "valorEconomizado as somaTotalEconomizado, " +
                    "strftime(\"%m\", dataAbastecimento /1000, 'unixepoch') as mes, " +
                    "strftime(\"%Y\", dataAbastecimento /1000, 'unixepoch') as ano, " +
                    "tipoCalculo, veiculoId, deleted " +
                    "from table_abastecimento");
        }
    };
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("UPDATE table_veiculo " +
                    "SET tipo = '" + Veiculo.TIPO_VEICULO_CARRO + "'\n" +
                    "WHERE tipo IS NULL ");
        }
    };
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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
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

        CreateVeiculos createVeiculos = new CreateVeiculos();
        createVeiculos
                .addCarro("FIAT Mobi 1.0\u00AD6V Drive ", 13.8f, 9.7f, 16.4f, 11.5f)
                .addCarro("PEUGEOT 208", 13.9f, 9.6f, 15.5f, 10.7f);
        List<Veiculo> listVeiculos = createVeiculos.build();

        Veiculo[] veiculos = new Veiculo[listVeiculos.size() + 2];
        veiculos[0] = new VeiculoTipoCalculoBasico();
        veiculos[1] = new VeiculoTipCalculoKMsLitro();
        for (int i = 0; i < listVeiculos.size(); i++) {
            veiculos[i + 2] = listVeiculos.get(i);
        }
        appDatabase.veiculoDao().insertAll(veiculos);
    }

    public static class CreateVeiculos {
        List<Veiculo> veiculos = new LinkedList<>();

        public CreateVeiculos addCarro(String nome, float kmsGasloinaCidade, float kmsAlcoolCidade, float kmsGasolinaRodovia, float kmsAlcoolRodovia) {
            Veiculo v = new Veiculo();
            v.setNome(nome);
            v.setTipo(Veiculo.TIPO_VEICULO_CARRO);
            v.setKmsLitroCidadeGasolina(kmsGasloinaCidade);
            v.setKmsLitroCidadeAlcool(kmsAlcoolCidade);
            v.setKmsLitroRodoviaGasolina(kmsGasolinaRodovia);
            v.setKmsLitroRodoviaAlcool(kmsAlcoolRodovia);
            this.veiculos.add(v);
            return this;
        }

        public List<Veiculo> build() {
            return this.veiculos;
        }
    }
}
