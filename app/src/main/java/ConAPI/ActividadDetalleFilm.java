package ConAPI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ec.com.josliblue.cineriesbox.R;
import ec.com.josliblue.cineriesbox.databinding.ActividadDetalleFilmBinding;
import ec.com.josliblue.cineriesbox.databinding.ActividadPantallaPrincipalBinding;

public class ActividadDetalleFilm extends AppCompatActivity {
    private ActividadDetalleFilmBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActividadDetalleFilmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });
    }
}