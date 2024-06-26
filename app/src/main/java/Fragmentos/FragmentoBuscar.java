package Fragmentos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
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
import ConAPI.FilmListAdapter;
import ConAPI.ListFilm;
import ec.com.josliblue.cineriesbox.databinding.FragmentoBuscarBinding;

public class FragmentoBuscar extends Fragment {
    private FragmentoBuscarBinding binding;
    private FilmListAdapter adapter;
    private ListFilm listFilm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentoBuscarBinding.inflate(inflater, container, false);
        setupRecyclerView();

        binding.SvFBBuscarpelicula.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No necesitas manejar la búsqueda al dar enter si quieres búsqueda dinámica
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    listFilm.getData().clear();
                    adapter.notifyDataSetChanged();
                } else {
                    searchMovies(newText);
                }
                return true;
            }
        });
        return binding.getRoot();
    }

    private void setupRecyclerView() {
        listFilm = new ListFilm(new ArrayList<>());
        adapter = new FilmListAdapter(listFilm);

        binding.RvFPResultados.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        binding.RvFPResultados.setAdapter(adapter);
    }

    private void searchMovies(String query) {
        String url = Constantes.BASE_URL + "search/movie?query=" + query + "&api_key=" + Constantes.API_KEY;

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            ListFilm listFilm = gson.fromJson(response, ListFilm.class);
            adapter = new FilmListAdapter(listFilm);
            binding.RvFPResultados.setAdapter(adapter);
        }, error -> {
            // Handle error
        });
        requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
