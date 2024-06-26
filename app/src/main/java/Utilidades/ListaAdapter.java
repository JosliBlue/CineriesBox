package Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.com.josliblue.cineriesbox.R;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder> {

    private List<String> listaItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public ListaAdapter(Context context, List<String> listaItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listaItems = listaItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ListaViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        String item = listaItems.get(position);
        holder.tvListName.setText(item);
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public static class ListaViewHolder extends RecyclerView.ViewHolder {
        TextView tvListName;
        ImageView ivEdit, ivDelete;

        public ListaViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvListName = itemView.findViewById(R.id.Lbl_IL_NombreLista);
            ivEdit = itemView.findViewById(R.id.Btn_IL_Editar);
            ivDelete = itemView.findViewById(R.id.Btn_IL_Borrar);

            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onEditClick(position);
                        }
                    }
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
