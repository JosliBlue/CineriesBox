package Fragmentos;

import static ConAPI.Constantes.*;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import ConAPI.CategoryListAdapter;
import ConAPI.FilmListAdapter;
import ConAPI.GenresItem;
import ConAPI.ListFilm;
import Utilidades.SliderAdapters;
import Utilidades.SliderItems;
import ec.com.josliblue.cineriesbox.R;
import ec.com.josliblue.cineriesbox.databinding.FragmentoPrincipalBinding;

public class FragmentoPrincipal extends Fragment {
    private FragmentoPrincipalBinding binding;
    private Handler sliderHandler = new Handler();
    private RequestQueue mRequestQueue;

    private RecyclerView.Adapter adapterBestHistory, adapterBestMovies, adapterUpComing, adapterCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentoPrincipalBinding.inflate(inflater, container, false);

        banners();
        initializeRecyclerViews();
        initializeRequestQueue();

        sendRequestBestHistory();
        sendRequestPopular();
        sendRequestUpComming();
        sendRequestCategory();

        return binding.getRoot();
    }

    private void banners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        binding.VpFPViewPagerSlider.setAdapter(new SliderAdapters(sliderItems, binding.VpFPViewPagerSlider));
        binding.VpFPViewPagerSlider.setClipToPadding(false);
        binding.VpFPViewPagerSlider.setOffscreenPageLimit(3);
        binding.VpFPViewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + 0.15f * r);
            }
        });

        binding.VpFPViewPagerSlider.setPageTransformer(compositePageTransformer);
        binding.VpFPViewPagerSlider.setCurrentItem(1);
        binding.VpFPViewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private void initializeRecyclerViews() {
        binding.RvFPVista1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.RvFPVista2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.RvFPVista3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        binding.RvFPVista4.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initializeRequestQueue() {
        mRequestQueue = Volley.newRequestQueue(getActivity());
    }

    private void sendRequestBestHistory() {
        binding.PbFP1.setVisibility(View.VISIBLE);
        String url = BASE_URL + "movie/top_rated"+"?api_key=" + API_KEY;
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbFP1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterBestHistory = new FilmListAdapter(items);
            binding.RvFPVista1.setAdapter(adapterBestHistory);
        }, error -> {
            binding.PbFP1.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private void sendRequestPopular() {
        binding.PbFP4.setVisibility(View.VISIBLE);
        String url = BASE_URL + "movie/popular"+"?api_key=" + API_KEY;
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbFP4.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterBestMovies = new FilmListAdapter(items);
            binding.RvFPVista4.setAdapter(adapterBestMovies);
        }, error -> {
            binding.PbFP4.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }


    private void sendRequestCategory() {
        binding.PbFP2.setVisibility(View.VISIBLE);
        String url = BASE_URL + "genre/movie/list"+"?api_key=" + API_KEY;
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbFP2.setVisibility(View.GONE);

            // Deserializar la respuesta JSON
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            JsonArray genresArray = jsonResponse.getAsJsonArray("genres");

            // Convertir el array de géneros en una lista de GenresItem
            ArrayList<GenresItem> catList = gson.fromJson(genresArray, new TypeToken<ArrayList<GenresItem>>() {
            }.getType());

            adapterCategory = new CategoryListAdapter(catList);
            binding.RvFPVista2.setAdapter(adapterCategory);
        }, error -> {
            binding.PbFP2.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }


    private void sendRequestUpComming() {
        binding.PbFP3.setVisibility(View.VISIBLE);
        String url = BASE_URL + "movie/upcoming"+"?api_key=" + API_KEY;
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            Gson gson = new Gson();
            binding.PbFP3.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            adapterUpComing = new FilmListAdapter(items);
            binding.RvFPVista3.setAdapter(adapterUpComing);
        }, error -> {
            binding.PbFP3.setVisibility(View.GONE);
            Log.i("UiLover", "onErrorResponse: " + error.toString());
        });
        mRequestQueue.add(mStringRequest);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            binding.VpFPViewPagerSlider.setCurrentItem(binding.VpFPViewPagerSlider.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
