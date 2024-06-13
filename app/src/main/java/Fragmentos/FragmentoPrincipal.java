package Fragmentos;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import Utilidades.SliderAdapters;
import Utilidades.SliderItems;
import ec.com.josliblue.cineriesbox.R;
import ec.com.josliblue.cineriesbox.databinding.FragmentoPrincipalBinding;

public class FragmentoPrincipal extends Fragment {
    private FragmentoPrincipalBinding binding;
    // Para el banner --------------------------------------------------------------------
    private Handler sliderHandler = new Handler();

    // Para las peliculas ----------------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentoPrincipalBinding.inflate(inflater, container, false);

        banners();

        return binding.getRoot();
    }

    private void banners() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide3));


        binding.VpFPViewPagerSlider.setAdapter(new SliderAdapters(sliderItems, binding.VpFPViewPagerSlider));
        binding.VpFPViewPagerSlider.setClipToPadding(false);
        // binding.VpFPViewPagerSlider.setClipChildren(false);
        binding.VpFPViewPagerSlider.setOffscreenPageLimit(3);
        binding.VpFPViewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + 0.15f);
            }
        });

        binding.VpFPViewPagerSlider.setPageTransformer(compositePageTransformer);
        binding.VpFPViewPagerSlider.setCurrentItem(1);
        binding.VpFPViewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
            }
        });
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
        // Liberar la referencia del binding
        binding = null;
    }
}
