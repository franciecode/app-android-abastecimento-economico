package com.ciecursoandroid.abastecimentoeconomico.persistencia.databaseViews;

import androidx.room.DatabaseView;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;

@DatabaseView("select " +
        "sum(totalPago) as somaTotalPago, " +
        "sum(valorEconomizado) as somaTotalEconomizado, " +
        "strftime(\"%m\", 1626186005830 /1000, 'unixepoch') as mes, " +
        "strftime(\"%Y\", 1626186005830 /1000, 'unixepoch') as ano, " +
        "tipoCalculo " +
        "from table_abastecimento where deleted = 0")
public class AbastecimentoRelatorioGraficoView {
    public Float somaTotalPago;
    public Float somaTotalEconomizado;
    public String mes;
    public String ano;
    public TipoCalculo tipoCalculo;

}
