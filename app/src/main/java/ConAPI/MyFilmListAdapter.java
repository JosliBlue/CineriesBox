package ConAPI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import Vistas.ActividadMiDetalleFilm;
import Vistas.ActividadVerLista;
import ec.com.josliblue.cineriesbox.R;

public class MyFilmListAdapter extends RecyclerView.Adapter<MyFilmListAdapter.ViewHolder>{
    ListFilm items;
    Context context;
    String nombreLista;

    public MyFilmListAdapter(ListFilm items, String nombreLista) {  // Constructor modificado
        this.items = items;
        this.nombreLista = nombreLista;
    }

    @NonNull
    @Override
    public MyFilmListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.componente_viewholder_film, parent, false);
        return new MyFilmListAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Datum movie = items.getData().get(position);
        holder.titleTxt.setText(movie.getTitle());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop(), new RoundedCorners(30));

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .apply(requestOptions)
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ActividadMiDetalleFilm.class);
            intent.putExtra("id", movie.getId());
            intent.putExtra("title",movie.getTitle());
            intent.putExtra("nombreLista", nombreLista);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return items.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.Lbl_CVF_title);
            pic = itemView.findViewById(R.id.Iv_CVF_portadaFilm);
        }
    }
}
