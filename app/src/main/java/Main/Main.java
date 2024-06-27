package Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseUser;

import Vistas.ActividadLogin;
import Vistas.ActividadPantallaPrincipal;
import Utilidades.BDFirebase;
import ec.com.josliblue.cineriesbox.R;

public class Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Puse la aplicacion predeterminada en modo oscuro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        //aqui pongo a la barra de navegacion inferior del color de mi barra de navegacion de mi app
        getWindow().setNavigationBarColor(getResources().getColor(R.color.dark_main_color));
        //este de aca abajo es para poner la barra de navegacion de un color puesto por mi
        //window.setStatusBarColor(getResources().getColor(R.color.main_color));

        // Lanzar LoginActivity
        FirebaseUser currentUser = BDFirebase.getUsuarioActual();
        if (currentUser != null) {
            // Usuario ya ha iniciado sesión, redirigir a ActividadPantallaPrincipal
            Intent intent = new Intent(Main.this, ActividadPantallaPrincipal.class);
            startActivity(intent);
            finish();
        } else {
            // Usuario no ha iniciado sesión, permanecer en ActividadMain o redirigir a ActividadLogin
            Intent intent = new Intent(Main.this, ActividadLogin.class);
            startActivity(intent);
            finish();
        }
    }
}