package Vistas;

import static ConAPI.Constantes.API_KEY;
import static ConAPI.Constantes.BASE_URL;
import static ConAPI.Constantes.BASE_URL_IMG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ConAPI.CategoryEachFilmListAdapter;
import ConAPI.FilmItem;
import Main.Main;
import Utilidades.BDFirebase;
import Utilidades.ListaAdapterModal;
import ec.com.josliblue.cineriesbox.R;
import ec.com.josliblue.cineriesbox.databinding.ActividadDetalleFilmBinding;
import ec.com.josliblue.cineriesbox.databinding.ActividadMiDetalleFilmBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalCancelarConfirmarBinding;

public class ActividadMiDetalleFilm extends AppCompatActivity {
    private ActividadMiDetalleFilmBinding binding;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private int idFilm;
    private String titleFilm;
    private RecyclerView.Adapter adapterCategory;
    private String nombreLista;

    private ModalCancelarConfirmarBinding confirmarCancelarBinding;
    private Dialog confirmarCancelarDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActividadMiDetalleFilmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, v.getPaddingBottom());
            return insets;
        });
        idFilm = getIntent().getIntExtra("id", 0);
        titleFilm = getIntent().getStringExtra("title");
        nombreLista = getIntent().getStringExtra("nombreLista");

        //binding.RvADFMiImagenRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.RvADFMiGenreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        confirmarCancelarBinding = ModalCancelarConfirmarBinding.inflate(getLayoutInflater());
        confirmarCancelarDialog = new Dialog(this);
        confirmarCancelarDialog.setContentView(confirmarCancelarBinding.getRoot());
        confirmarCancelarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmarCancelarDialog.setCancelable(false);

        // Convertir dp a píxeles
        int marginInDp = 15;
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f);

        // Ajustar los parámetros del diálogo
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(confirmarCancelarDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        confirmarCancelarDialog.getWindow().setAttributes(layoutParams);

        // Ajustar los márgenes del diálogo de confirmar y cancelar
        confirmarCancelarDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) confirmarCancelarBinding.getRoot().getLayoutParams();
        params.setMargins(marginInPx, 0, marginInPx, 0);
        confirmarCancelarBinding.getRoot().setLayoutParams(params);

        confirmarCancelarBinding.lblMCCTitulo.setText("Eliminar pelicula?");
        confirmarCancelarBinding.lblMCCDescripcion.setText("Seguro de eliminarlo de "+nombreLista);
        binding.IvADFMiEliminar.setOnClickListener(v -> {
            confirmarCancelarDialog.show();
        });

        confirmarCancelarBinding.btnMCCConfirmar.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String path = "USUARIOS/" + uid + "/LISTAS/" + nombreLista;
            BDFirebase.eliminarClave(path, String.valueOf(idFilm), (success, message) -> {
                if (success) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    /*
                    Intent intent = new Intent(ActividadMiDetalleFilm.this, ActividadVerLista.class);
                    intent.putExtra("nombreLista", nombreLista);
                    startActivity(intent);
                    */
                    finish();
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            });
            confirmarCancelarDialog.dismiss();
        });

        confirmarCancelarBinding.btnMCCCancelar.setOnClickListener(v -> {
            confirmarCancelarDialog.dismiss();
        });

        sendRequest();
    }


    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        binding.PbADFMiCarganding.setVisibility(View.VISIBLE);
        binding.scrollMiView2.setVisibility(View.GONE);

        String url = BASE_URL + "movie/" + idFilm + "?api_key=" + API_KEY;

        mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbADFMiCarganding.setVisibility(View.GONE);
            binding.scrollMiView2.setVisibility(View.VISIBLE);

            FilmItem item = gson.fromJson(response, FilmItem.class);
            Glide.with(ActividadMiDetalleFilm.this).load(BASE_URL_IMG + item.getPosterPath()).into(binding.IvADFMiImagenPortada);

            binding.LblADFMiMovieName.setText(item.getTitle());
            binding.LblADFMiMovieStar.setText(String.valueOf(item.getVoteAverage()));
            binding.LblADFMiMovieTime.setText(item.getRuntime() + " min");
            binding.TxtADFMiSummary.setText(item.getOverview());

            if (item.getGenres() != null) {
                List<String> genres = new ArrayList<>();
                for (FilmItem.Genre genre : item.getGenres()) {
                    genres.add(genre.getName());
                }
                adapterCategory = new CategoryEachFilmListAdapter(genres);
                binding.RvADFMiGenreView.setAdapter(adapterCategory);
            }
        }, error -> {
            binding.PbADFMiCarganding.setVisibility(View.GONE);
            Toast.makeText(this, "Error al cargar la película", Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(mStringRequest);
    }

}
