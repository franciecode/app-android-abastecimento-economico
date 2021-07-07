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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsEnums;

import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AbastecimentosAdapter extends RecyclerView.Adapter<AbastecimentosAdapter.VH> implements Filterable {

    private Map<Abastecimento, ShowAbastecimento> abastecimentosAguardandoCarregarVeiculos = new HashMap<>();
    LayoutInflater inflater;
    List<Abastecimento> abastecimentos = new LinkedList<>();
    List<Abastecimento> abastecimentosAll;
    List<Abastecimento> abastecimentosFiltrados = new LinkedList<>();
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

    public void setAbastecimentos(List<Abastecimento> list) {
        this.abastecimentosAll = list;
        carregarVeiculosDosAbastecimentos();
        if (filtered == false) {
            this.abastecimentos.clear();
            this.abastecimentos.addAll(abastecimentosAll);
            notifyDataSetChanged();
        }
        listener.onChangeListener(this.abastecimentos, abastecimentosAll);
    }

    private void carregarVeiculosDosAbastecimentos() {
        for (Abastecimento abastecimento : abastecimentosAll) {
            if (abastecimento.getTipoCalculo() == TipoCalculo.VEICULO) {
                veiculoViewModel.getById(abastecimento.getVeiculoId())
                        .observe((LifecycleOwner) context, veiculo -> {
                            abastecimento.setVeiculo(veiculo);

                            ShowAbastecimento showAbastecimento =
                                    abastecimentosAguardandoCarregarVeiculos.get(abastecimento);
                            if (showAbastecimento != null)
                                showAbastecimento.setDescricaoVeiculo(veiculo);
                        });
            }
        }
    }


    public void removeItem(int position) {
        if (filtered == true) {
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
            Abastecimento abastecimento = abastecimentos.get(position);
            holder.setData(abastecimento, position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(abastecimento, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return listener.onLongItemClick(abastecimento, position);
                }
            });
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
                        for (Abastecimento a : abastecimentosAll) {
                            String strAbastecimento = new ShowAbastecimento(a).toString().toLowerCase().trim();
                            boolean contains = true;
                            int i = 0;
                            while (i < words.length && contains == true) {
                                contains = strAbastecimento.contains(words[i]);
                                i++;
                            }
                            if (contains == true)
                                abastecimentosFiltrados.add(a);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filterResults.values = abastecimentosFiltrados;
                int a = 0;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                abastecimentos.clear();
                if (filterResults.values != null)
                    abastecimentos.addAll((List<Abastecimento>) filterResults.values);
                notifyDataSetChanged();
                listener.onChangeListener(abastecimentos, abastecimentosAll);
            }
        };
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

        public void setData(Abastecimento abastecimento, int position) {
            ShowAbastecimento showAbastecimento = new ShowAbastecimento(abastecimento);
            showAbastecimento.setViewHolder(this);
            tipoCalculo.setText(showAbastecimento.tipoAbastecimento);
            dataAbastecimento.setText(showAbastecimento.dataAbastecimento);
            descricaoAbastecimento.setText(showAbastecimento.descricao);
            detalhesAbastecimento.setText(showAbastecimento.detalhes);
        }
    }

    public interface AdapterObserver {
        void onItemClick(Abastecimento abastecimento, int position);

        boolean onLongItemClick(Abastecimento abastecimento, int position);

        void onChangeListener(List<Abastecimento> listed, List<Abastecimento> all);
    }

    private class ShowAbastecimento {
        String tipoAbastecimento;
        String dataAbastecimento;
        String detalhes;
        String descricao;
        VH viewHolder;

        public ShowAbastecimento(Abastecimento abastecimento) {
            tipoAbastecimento = UtilsEnums.getTipoCalculo(context, abastecimento.getTipoCalculo());
            dataAbastecimento = DateFormat.format(context.getString(R.string.data_hora),
                    abastecimento.getDataAbastecimento()).toString();
            detalhes = String.format(context.getResources().getString(R.string.detalhe_resumo_abastecimento),
                    abastecimento.getCombustivelAbastecido() == TipoCombustivel.ALCOOL ? context.getString(R.string.alcool) : context.getString(R.string.gasolina),
                    abastecimento.getTotalLitrosAbastecidos(),
                    abastecimento.getTotalPago(),
                    abastecimento.getValorEconomizado());
            descricao = "";
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
                    if (abastecimento.getVeiculo() == null) {
                        abastecimentosAguardandoCarregarVeiculos.put(abastecimento, this);
                        descricao = "";
                    } else {
                        setDescricaoVeiculo(abastecimento.getVeiculo());
                    }
                    break;
            }
        }

        public void setDescricaoVeiculo(Veiculo veiculo) {
            descricao = veiculo.getNome();
            if (this.viewHolder != null)
                viewHolder.descricaoAbastecimento.setText(descricao);
        }

        public void setViewHolder(VH viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(tipoAbastecimento);
            sb.append(" ");
            sb.append(dataAbastecimento);
            sb.append(" ");
            sb.append(descricao);
            sb.append(" ");
            sb.append(detalhes);
            return sb.toString();
        }

    }

}
