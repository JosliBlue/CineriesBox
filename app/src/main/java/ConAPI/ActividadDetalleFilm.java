package ConAPI;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

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
    }

    private void sendRequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        binding.PbADFCarganding.setVisibility(View.VISIBLE);
        binding.scrollView2.setVisibility(View.GONE);


        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/" + idFilm, response -> {
            Gson gson = new Gson();
            binding.PbADFCarganding.setVisibility(View.GONE);
            binding.scrollView2.setVisibility(View.VISIBLE);

            FilmItem item = gson.fromJson(response, FilmItem.class);
            Glide.with(ActividadDetalleFilm.this).load(item.getPoster()).into(binding.IvADFImagenPortada);

            binding.LblADFMovieName.setText(item.getTitle());
            binding.LblADFMovieStar.setText(item.getImdbRating());
            binding.LblADFMovieTime.setText(item.getRuntime());
            binding.TxtADFSummary.setText(item.getPlot());
            binding.LblADFActores.setText(item.getActors());
            if (item.getImages() != null) {
                adapterActorList = new ActorsListAdapter(item.getImages());
                binding.RvADFImagenRecycler.setAdapter(adapterActorList);
            }
            if (item.getGenres() != null) {
                adapterCategory = new CategoryEachFilmListAdapter(item.getGenres());
                binding.RvADFGenreView.setAdapter(adapterCategory);
            }
        }, error -> binding.PbADFCarganding.setVisibility(View.GONE));
        mRequestQueue.add(mStringRequest);
    }
}