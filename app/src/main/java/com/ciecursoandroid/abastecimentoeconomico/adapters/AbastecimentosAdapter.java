package com.ciecursoandroid.abastecimentoeconomico.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.LocalViagem;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsEnums;

import org.jetbrains.annotations.NotNull;

import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

public class AbastecimentosAdapter extends RecyclerView.Adapter<AbastecimentosAdapter.VH> implements Filterable {

    LayoutInflater inflater;
    List<AbastecimentoComVeiculo> abastecimentos = new LinkedList<>();
    List<AbastecimentoComVeiculo> abastecimentosAll;
    List<AbastecimentoComVeiculo> abastecimentosFiltrados = new LinkedList<>();
    Context context;
    VeiculoViewModel veiculoViewModel;
    AdapterObserver listener;
    private boolean filtered;


    public AbastecimentosAdapter(Context context, AdapterObserver listener) {
        this.listener = listener;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.veiculoViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(VeiculoViewModel.class);
        this.veiculoViewModel.setRespository(new VeiculoRespository(context));
    }

    public void setAbastecimentos(List<AbastecimentoComVeiculo> list) {
        this.abastecimentosAll = list;
        if (!filtered) {
            this.abastecimentos.clear();
            this.abastecimentos.addAll(abastecimentosAll);
            notifyDataSetChanged();
        }
        listener.onChangeListener(this.abastecimentos, abastecimentosAll);
    }

    public void removeItem(int position) {
        if (filtered) {
            abastecimentos.remove(position);
            notifyItemRemoved(position);
        }
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
            AbastecimentoComVeiculo abastecimentoComVeiculo = abastecimentos.get(position);
            holder.setData(abastecimentoComVeiculo);
            holder.itemView.setOnClickListener(view -> listener.onItemClick(abastecimentoComVeiculo, position));
            holder.itemView.setOnLongClickListener(view -> listener.onLongItemClick(abastecimentoComVeiculo, position));
        }
    }

    @Override
    public int getItemCount() {
        return abastecimentos == null ? 0 : abastecimentos.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                abastecimentosFiltrados.clear();
                FilterResults filterResults = new FilterResults();
                try {
                    if (charSequence == null || charSequence.length() == 0) {
                        filtered = false;
                        abastecimentosFiltrados.addAll(abastecimentosAll);
                    } else {
                        filtered = true;
                        String search = charSequence.toString().toLowerCase().trim();
                        String[] words = search.split(" ");
                        for (AbastecimentoComVeiculo a : abastecimentosAll) {
                            String strAbastecimento = new ShowAbastecimento(a).toString().toLowerCase().trim();
                            boolean contains = true;
                            int i = 0;
                            while (i < words.length && contains) {
                                contains = strAbastecimento.contains(words[i]);
                                i++;
                            }
                            if (contains)
                                abastecimentosFiltrados.add(a);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filterResults.values = abastecimentosFiltrados;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                abastecimentos.clear();
                if (filterResults.values != null)
                    abastecimentos.addAll((List<AbastecimentoComVeiculo>) filterResults.values);
                notifyDataSetChanged();
                listener.onChangeListener(abastecimentos, abastecimentosAll);
            }
        };
    }


    public class VH extends RecyclerView.ViewHolder {
        private final TextView detalhesAbastecimento;
        private final TextView tipoCalculo;
        private final TextView dataAbastecimento;
        private final TextView descricaoAbastecimento;

        public VH(@NonNull View itemView) {
            super(itemView);
            tipoCalculo = itemView.findViewById(R.id.tipoCalculo);
            dataAbastecimento = itemView.findViewById(R.id.dataAbastecimento);
            descricaoAbastecimento = itemView.findViewById(R.id.descricaoAbastecimento);
            detalhesAbastecimento = itemView.findViewById(R.id.detalhesAbastecimento);
        }

        public void setData(AbastecimentoComVeiculo abastecimentoComVeiculo) {
            ShowAbastecimento showAbastecimento = new ShowAbastecimento(abastecimentoComVeiculo);
            showAbastecimento.setViewHolder(this);
            tipoCalculo.setText(showAbastecimento.tipoAbastecimento);
            dataAbastecimento.setText(showAbastecimento.dataAbastecimento);
            descricaoAbastecimento.setText(showAbastecimento.descricao);
            detalhesAbastecimento.setText(showAbastecimento.detalhes);
        }
    }

    public interface AdapterObserver {
        void onItemClick(AbastecimentoComVeiculo abastecimento, int position);

        boolean onLongItemClick(AbastecimentoComVeiculo abastecimento, int position);

        void onChangeListener(List<AbastecimentoComVeiculo> listed, List<AbastecimentoComVeiculo> all);
    }

    private class ShowAbastecimento {
        String tipoAbastecimento;
        String dataAbastecimento;
        String detalhes;
        String descricao;
        String localViagem = "";
        VH viewHolder;

        public ShowAbastecimento(AbastecimentoComVeiculo abastecimentoComVeiculo) {
            tipoAbastecimento = UtilsEnums.getTipoCalculo(context, abastecimentoComVeiculo.abastecimento.getTipoCalculo());
            dataAbastecimento = DateFormat.format(context.getString(R.string.data_hora),
                    abastecimentoComVeiculo.abastecimento.getDataAbastecimento()).toString();
            detalhes = String.format(context.getResources().getString(R.string.detalhe_resumo_abastecimento),
                    abastecimentoComVeiculo.abastecimento.getCombustivelAbastecido() == TipoCombustivel.ALCOOL ? context.getString(R.string.alcool) : context.getString(R.string.gasolina),
                    abastecimentoComVeiculo.abastecimento.getTotalLitrosAbastecidos(),
                    abastecimentoComVeiculo.abastecimento.getTotalPago(),
                    abastecimentoComVeiculo.abastecimento.getValorEconomizado());
            descricao = "";
            switch (abastecimentoComVeiculo.abastecimento.getTipoCalculo()) {
                case BASICO:
                    descricao = new Formatter().format(
                            context.getResources().getString(R.string.alcool_sobre_gasolina),
                            (abastecimentoComVeiculo.abastecimento.getKmsLitroAlcool() / abastecimentoComVeiculo.abastecimento.getKmsLitroGasolina() * 100),
                            context.getResources().getString(R.string.porcent_sobre_gasolina)
                    ).toString();
                    break;
                case KMS_LITRO:
                    descricao = new Formatter().format(
                            context.getResources().getString(R.string.descricao_abastecimento_por_kmsLitro),
                            abastecimentoComVeiculo.abastecimento.getKmsLitroAlcool(),
                            abastecimentoComVeiculo.abastecimento.getKmsLitroGasolina()).toString();
                    break;
                case VEICULO:
                    localViagem = abastecimentoComVeiculo.abastecimento.getLocalViagem() == LocalViagem.CIDADE ?
                            context.getString(R.string.cidade) : context.getString(R.string.rodovia);
                    setDescricaoVeiculo(abastecimentoComVeiculo.veiculo);
                    break;
            }
        }

        public void setDescricaoVeiculo(Veiculo veiculo) {
            descricao = veiculo.getNome() + "/" + localViagem;
            if (this.viewHolder != null)
                viewHolder.descricaoAbastecimento.setText(descricao);
        }

        public void setViewHolder(VH viewHolder) {
            this.viewHolder = viewHolder;
        }

        @NotNull
        @Override
        public String toString() {
            String sb = tipoAbastecimento +
                    " " +
                    dataAbastecimento +
                    " " +
                    descricao +
                    " " +
                    detalhes;
            return sb;
        }

    }


}
