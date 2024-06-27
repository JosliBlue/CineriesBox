package Vistas;

import static ConAPI.Constantes.*;

import android.os.Bundle;
import android.view.View;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ConAPI.CategoryEachFilmListAdapter;
import ConAPI.FilmItem;
import Utilidades.BDFirebase;
import Utilidades.FirebaseCallBack;
import Utilidades.ListaAdapterModal;
import ec.com.josliblue.cineriesbox.R;
import ec.com.josliblue.cineriesbox.databinding.ActividadDetalleFilmBinding;

public class ActividadDetalleFilm extends AppCompatActivity {
    private ActividadDetalleFilmBinding binding;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private int idFilm;
    private String titleFilm;
    private RecyclerView.Adapter adapterCategory;
    private BottomSheetDialog bottomSheetDialog;
    private ListaAdapterModal listaAdapter;
    private List<String> listaItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActividadDetalleFilmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, v.getPaddingBottom());
            return insets;
        });

        idFilm = getIntent().getIntExtra("id", 0);
        titleFilm = getIntent().getStringExtra("title");
        binding.RvADFImagenRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.RvADFGenreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.IvADFBackButton.setOnClickListener(v -> finish());
        binding.IvADFFavorito.setOnClickListener(v -> bottomSheetDialog.show());
        sendRequest();

        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.modal_listas_inferior, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.Rv_MLI_lista);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        listaItems = new ArrayList<>();
        listaAdapter = new ListaAdapterModal(this, listaItems, position -> {
            String nombreLista = listaItems.get(position);
            String uid = BDFirebase.getUsuarioActual().getUid();

            String path = "USUARIOS/" + uid + "/LISTAS/" + nombreLista;
            Map<String, Object> data = new HashMap<>();
            data.put(String.valueOf(idFilm), titleFilm);

            BDFirebase.buscarClaveEnDocumento(path, idFilm, (exists, message) -> {
                if (exists) {
                    Toast.makeText(this, "Ya agregado a la lista", Toast.LENGTH_SHORT).show();
                } else {
                    BDFirebase.actualizarDocumento(path, data, (success, updateMessage) -> {
                        if (success) {
                            Toast.makeText(this, updateMessage + nombreLista, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error al actualizar campo: " + updateMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            bottomSheetDialog.dismiss();
        });
        recyclerView.setAdapter(listaAdapter);

        intentarMostrarListas();
    }



    private void intentarMostrarListas() {
        listaItems.clear();
        String uid = BDFirebase.getUsuarioActual().getUid();
        String path = "USUARIOS/" + uid + "/LISTAS";

        BDFirebase.obtenerDocumentos(path, (success, result) -> {
            if (success && result != null) {
                listaItems.addAll(result);
                listaAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error al cargar las listas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        binding.PbADFCarganding.setVisibility(View.VISIBLE);
        binding.scrollView2.setVisibility(View.GONE);

        String url = BASE_URL + "movie/" + idFilm + "?api_key=" + API_KEY;

        mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbADFCarganding.setVisibility(View.GONE);
            binding.scrollView2.setVisibility(View.VISIBLE);

            FilmItem item = gson.fromJson(response, FilmItem.class);
            Glide.with(ActividadDetalleFilm.this).load(BASE_URL_IMG + item.getPosterPath()).into(binding.IvADFImagenPortada);

            binding.LblADFMovieName.setText(item.getTitle());
            binding.LblADFMovieStar.setText(String.valueOf(item.getVoteAverage()));
            binding.LblADFMovieTime.setText(item.getRuntime() + " min");
            binding.TxtADFSummary.setText(item.getOverview());

            if (item.getGenres() != null) {
                List<String> genres = new ArrayList<>();
                for (FilmItem.Genre genre : item.getGenres()) {
                    genres.add(genre.getName());
                }
                adapterCategory = new CategoryEachFilmListAdapter(genres);
                binding.RvADFGenreView.setAdapter(adapterCategory);
            }
        }, error -> {
            binding.PbADFCarganding.setVisibility(View.GONE);
            Toast.makeText(this, "Error al cargar la película", Toast.LENGTH_SHORT).show();
        });

        mRequestQueue.add(mStringRequest);
    }

}
