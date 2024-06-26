package Main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragmentos.FragmentoBuscar;
import Fragmentos.FragmentoPerfil;
import Fragmentos.FragmentoPrincipal;
import ec.com.josliblue.cineriesbox.R;

public class ActividadPantallaPrincipal extends AppCompatActivity {
    private BottomNavigationView menuNavegacionApp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.actividad_pantalla_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, v.getPaddingBottom());
            return insets;
        });

        //Puse la aplicacion predeterminada en modo oscuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //aqui pongo a la barra de navegacion inferior del color de mi barra de navegacion de mi app
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_main_color));
        //este de aca abajo es para poner la barra de navegacion de un color puesto por mi
        //window.setStatusBarColor(getResources().getColor(R.color.main_color));

        this.menuNavegacionApp = findViewById(R.id.EspacioMenu);

        menuNavegacionApp.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.navPrincipal) {
                    cargarFragmento(new FragmentoPrincipal(), false);
                } else if (itemId == R.id.navBuscar) {
                    cargarFragmento(new FragmentoBuscar(), false);
                } else if (itemId == R.id.navPerfil) {
                    cargarFragmento(new FragmentoPerfil(), false);
                }
                return true;
            }
        });
        cargarFragmento(new FragmentoPrincipal(), true);
    }

    private void cargarFragmento(Fragment fragmento, boolean isAppInitialized) {
        FragmentManager fragmentoManager = getSupportFragmentManager();
        FragmentTransaction fragmentoTransaction = fragmentoManager.beginTransaction();

        if (isAppInitialized) {
            fragmentoTransaction.add(R.id.EspacioApp, fragmento);
        } else {
            fragmentoTransaction.replace(R.id.EspacioApp, fragmento);
        }
        fragmentoTransaction.commit();
    }
}