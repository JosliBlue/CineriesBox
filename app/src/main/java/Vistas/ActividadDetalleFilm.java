package Vistas;

import static ConAPI.Constantes.*;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import ConAPI.CategoryEachFilmListAdapter;
import ConAPI.Constantes;
import ConAPI.FilmItem;
import ec.com.josliblue.cineriesbox.databinding.ActividadDetalleFilmBinding;

public class ActividadDetalleFilm extends AppCompatActivity {
    private ActividadDetalleFilmBinding binding;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private int idFilm;
    private RecyclerView.Adapter adapterActorList, adapterCategory;

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
        initView();
        sendRequest();
    }

    private void initView() {
        binding.RvADFImagenRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.RvADFGenreView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.IvADFBackButton.setOnClickListener(v -> finish());
        binding.IvADFFavorito.setOnClickListener(v -> {

        });
    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        binding.PbADFCarganding.setVisibility(View.VISIBLE);
        binding.scrollView2.setVisibility(View.GONE);

        String url = BASE_URL+"movie/" + idFilm + "?api_key=" + API_KEY;

        mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbADFCarganding.setVisibility(View.GONE);
            binding.scrollView2.setVisibility(View.VISIBLE);

            FilmItem item = gson.fromJson(response, FilmItem.class);
            Glide.with(ActividadDetalleFilm.this).load(BASE_URL_IMG + item.getPosterPath()).into(binding.IvADFImagenPortada);

            binding.LblADFMovieName.setText(item.getTitle());
            binding.LblADFMovieStar.setText(String.valueOf(item.getVoteAverage()));
            binding.LblADFMovieTime.setText(String.valueOf(item.getRuntime()) + " min");
            binding.TxtADFSummary.setText(item.getOverview());

            // Handle genres
            if (item.getGenres() != null) {
                List<String> genres = new ArrayList<>();
                for (FilmItem.Genre genre : item.getGenres()) {
                    genres.add(genre.getName());
                }
                adapterCategory = new CategoryEachFilmListAdapter(genres);
                binding.RvADFGenreView.setAdapter(adapterCategory);
            }
        }, error -> binding.PbADFCarganding.setVisibility(View.GONE));

        mRequestQueue.add(mStringRequest);
    }
}
