package com.ciecursoandroid.abastecimentoeconomico.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsEnums;

import java.util.Formatter;
import java.util.List;

public class AbastecimentosAdapter extends RecyclerView.Adapter<AbastecimentosAdapter.VH> {

    LayoutInflater inflater;
    List<Abastecimento> abastecimentos;
    Context context;
    VeiculoViewModel veiculoViewModel;

    public AbastecimentosAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.veiculoViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(VeiculoViewModel.class);
        this.veiculoViewModel.setRespository(new VeiculoRespository(context));
    }

    public void setAbastecimentos(List<Abastecimento> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.abatecimento_item_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbastecimentosAdapter.VH holder, int position) {
        if (abastecimentos != null && abastecimentos.size() > 0) {
            Abastecimento abastecimento = abastecimentos.get(position);
            if (abastecimento.getTipoCalculo() == TipoCalculo.VEICULO) {

                veiculoViewModel.getById(abastecimento.getVeiculoId())
                        .observe((LifecycleOwner) context, veiculo -> {
                            abastecimento.setVeiculo(veiculo);
                            holder.setData(abastecimento);
                        });
            } else {
                holder.setData(abastecimento);
            }
        }
    }

    @Override
    public int getItemCount() {
        return abastecimentos == null ? 0 : abastecimentos.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        private TextView tipoCalculo;
        private TextView dataAbastecimento;
        private TextView descricaoAbastecimento;
        private TextView detalhesAbastecimento;

        public VH(@NonNull View itemView) {
            super(itemView);
            tipoCalculo = itemView.findViewById(R.id.tipoCalculo);
            dataAbastecimento = itemView.findViewById(R.id.dataAbastecimento);
            descricaoAbastecimento = itemView.findViewById(R.id.descricaoAbastecimento);
            detalhesAbastecimento = itemView.findViewById(R.id.detalhesAbastecimento);
        }

        public void setData(Abastecimento abastecimento) {
            tipoCalculo.setText(UtilsEnums.getTipoCalculo(context, abastecimento.getTipoCalculo()));
            String dt = DateFormat.format(context.getString(R.string.data_hora),
                    abastecimento.getDataAbastecimento()).toString();
            dataAbastecimento.setText(dt);
            String descricao = "";
            switch (abastecimento.getTipoCalculo()) {
                case BASICO:
                    descricao = new Formatter().format(
                            context.getResources().getString(R.string.alcool_sobre_gasolina),
                            (abastecimento.getKmsLitroAlcool() / abastecimento.getKmsLitroGasolina() * 100),
                            context.getResources().getString(R.string.porcent_sobre_gasolina)
                    ).toString();
                    break;
                case KMS_LITRO:
                    descricao = new Formatter().format(
                            context.getResources().getString(R.string.descricao_abastecimento_por_kmsLitro),
                            abastecimento.getKmsLitroAlcool(),
                            abastecimento.getKmsLitroGasolina()).toString();
                    break;
                case VEICULO:
                    descricao = abastecimento.getVeiculo().getNome();
                    break;
            }
            descricaoAbastecimento.setText(descricao);
            String detalhes = String.format(context.getResources().getString(R.string.detalhe_resumo_abastecimento),
                    abastecimento.getCombustivelAbastecido() == TipoCombustivel.ALCOOL ? context.getString(R.string.alcool) : context.getString(R.string.gasolina),
                    abastecimento.getTotalLitrosAbastecidos(),
                    abastecimento.getTotalPago(),
                    abastecimento.getValorEconomizado());
            detalhesAbastecimento.setText(detalhes);
        }
    }
}
