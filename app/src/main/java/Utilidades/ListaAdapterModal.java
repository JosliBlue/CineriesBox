package Utilidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.com.josliblue.cineriesbox.R;

public class ListaAdapterModal extends RecyclerView.Adapter<ListaAdapterModal.ListaViewHolder> {

    private List<String> listaItems;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ListaAdapterModal(Context context, List<String> listaItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listaItems = listaItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.componente_lista_seleccion, parent, false);
        return new ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        String item = listaItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder {

        TextView textViewListaId;

        public ListaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewListaId = itemView.findViewById(R.id.Lbl_CLS_NombreLista);

            // Manejar clics en el item para informar al listener
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

        public void bind(String item, int position) {
            textViewListaId.setText(item);
            // Aquí puedes realizar cualquier otra configuración del item según sea necesario
        }
    }
}
