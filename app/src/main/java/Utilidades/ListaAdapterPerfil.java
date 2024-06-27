package Utilidades;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Vistas.ActividadVerLista;
import ec.com.josliblue.cineriesbox.R;

public class ListaAdapterPerfil extends RecyclerView.Adapter<ListaAdapterPerfil.ListaPerfilViewHolder> {
    private List<String> listaItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public ListaAdapterPerfil(Context context, List<String> listaItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listaItems = listaItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaPerfilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.componente_item_list, parent, false);
        return new ListaPerfilViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPerfilViewHolder holder, int position) {
        String item = listaItems.get(position);
        holder.tvListName.setText(item);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActividadVerLista.class);
            intent.putExtra("nombreLista", item);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public static class ListaPerfilViewHolder extends RecyclerView.ViewHolder {
        TextView tvListName;
        ImageView ivDelete;

        public ListaPerfilViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.Lbl_CLI_NombreLista);
            ivDelete = itemView.findViewById(R.id.Btn_CLI_Borrar);

            ivDelete.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onDeleteClick(position);
                    }
                }
            });
        }
    }
}
