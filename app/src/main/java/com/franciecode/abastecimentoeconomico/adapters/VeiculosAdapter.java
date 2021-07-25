package com.franciecode.abastecimentoeconomico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.models.Veiculo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VeiculosAdapter extends RecyclerView.Adapter<VeiculosAdapter.ViewHolder> {

    private static final String TAG = VeiculosAdapter.class.getSimpleName();
    private List<Veiculo> veiculos;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final OnItemClickListener listener;

    public VeiculosAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.veiculo_item_list, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (getItemCount() > 0) {
            holder.setData(veiculos.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(veiculos.get(position), position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return listener.onLongClick(veiculos.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return veiculos == null ? 0 : veiculos.size();
    }

    public void removeItem(int position) {
        veiculos.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconVeiculo;
        TextView nomeVeiculo;
        TextView kmsAlcoolCidade;
        TextView kmsAlcoolRodovia;
        TextView kmsGasolinaCidade;
        TextView kmsGasolinaRodovia;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            iconVeiculo = itemView.findViewById(R.id.imageIconVeiculo);
            nomeVeiculo = itemView.findViewById(R.id.textViewNomeVeiculo);
            kmsAlcoolCidade = itemView.findViewById(R.id.textViewKmsAlcoolCidade);
            kmsAlcoolRodovia = itemView.findViewById(R.id.textViewKmsAlcoolRodovia);
            kmsGasolinaCidade = itemView.findViewById(R.id.textViewKmsGasolinaCidade);
            kmsGasolinaRodovia = itemView.findViewById(R.id.textViewKmsGasolinaRodovia);
        }

        public void setData(Veiculo veiculo) {
            if (veiculo.getTipo().equals(Veiculo.TIPO_VEICULO_CARRO)) {
                iconVeiculo.setImageResource(R.drawable.ic_baseline_directions_car_24);
            } else {
                iconVeiculo.setImageResource(R.drawable.ic_motorcycle_solid);
            }
            nomeVeiculo.setText(veiculo.getNome());
            kmsAlcoolCidade.setText(veiculo.getKmsLitroCidadeAlcool() + "");
            kmsAlcoolRodovia.setText(veiculo.getKmsLitroRodoviaAlcool() + "");
            kmsGasolinaCidade.setText(veiculo.getKmsLitroCidadeGasolina() + "");
            kmsGasolinaRodovia.setText(veiculo.getKmsLitroRodoviaGasolina() + "");

        }
    }

    public interface OnItemClickListener {
        void onClick(Veiculo veiculo, int position);

        boolean onLongClick(Veiculo veiculo, int position);
    }
}
