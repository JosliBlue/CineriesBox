package Vistas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import ConAPI.Constantes;
import ConAPI.Datum;
import ConAPI.ListFilm;
import ConAPI.MyFilmListAdapter;
import Utilidades.BDFirebase;
import ec.com.josliblue.cineriesbox.databinding.ActividadVerListaBinding;
public class ActividadVerLista extends AppCompatActivity {
    private ActividadVerListaBinding binding;
    private MyFilmListAdapter adapter;
    private List<Datum> moviesList = new ArrayList<>();
    private String nombreLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActividadVerListaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });

        iniciarVentana();
    }

    public void iniciarVentana() {
        nombreLista = getIntent().getStringExtra("nombreLista");
        binding.LblAVLTitulo.setText(nombreLista);

        adapter = new MyFilmListAdapter(new ListFilm(moviesList), nombreLista);  // Paso de nombreLista
        binding.RvAVLLista.setLayoutManager(new GridLayoutManager(this, 3));
        binding.RvAVLLista.setAdapter(adapter);

        String uid = BDFirebase.getUsuarioActual().getUid();
        String path = "USUARIOS/" + uid + "/LISTAS/" + nombreLista;
        BDFirebase.obtenerDocumento(path, (success, data) -> {
            if (success && data != null) {
                for (String key : data.keySet()) {
                    Datum movie = new Datum();
                    movie.setId(Integer.valueOf(key));
                    moviesList.add(movie);
                }
                fetchMovieDetails(moviesList);
            } else {
                Toast.makeText(this, "Error al obtener el documento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMovieDetails(List<Datum> movies) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        for (Datum movie : movies) {
            String url = Constantes.BASE_URL + "movie/" + movie.getId() + "?api_key=" + Constantes.API_KEY;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Datum detailedMovie = gson.fromJson(response, Datum.class);
                        movie.setTitle(detailedMovie.getTitle());
                        movie.setPosterPath(detailedMovie.getPosterPath());
                        adapter.notifyDataSetChanged();
                    },
                    error -> {
                        Toast.makeText(this, "Error al obtener detalles de la película", Toast.LENGTH_SHORT).show();
                    });
            requestQueue.add(request);
        }
    }
}
