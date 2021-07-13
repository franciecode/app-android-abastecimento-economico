package com.ciecursoandroid.abastecimentoeconomico.persistencia.databaseViews;

import androidx.room.DatabaseView;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;

@DatabaseView("select " +
        "totalPago as somaTotalPago, " +
        "valorEconomizado as somaTotalEconomizado, " +
        "strftime(\"%m\", 1626186005830 /1000, 'unixepoch') as mes, " +
        "strftime(\"%Y\", 1626186005830 /1000, 'unixepoch') as ano, " +
        "tipoCalculo, veiculoId, deleted " +
        "from table_abastecimento")
public class AbastecimentoRelatorioGraficoView {
    public Float somaTotalPago;
    public Float somaTotalEconomizado;
    public String mes;
    public String ano;
    public Long veiculoId;
    public TipoCalculo tipoCalculo;
    public boolean deleted;

}
