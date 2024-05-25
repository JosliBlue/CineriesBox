package ec.com.josliblue.cineriesbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    private EditText correo, clave;
    private Button btn_ingresar;
    private TextView lbl_registarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.initView();
    }

    private void initView() {
        this.correo = findViewById(R.id.txt_Correo);
        this.clave = findViewById(R.id.txt_Clave);
        this.btn_ingresar = findViewById(R.id.btn_Registrarme);

        this.lbl_registarme = findViewById(R.id.lbl_Registro);

        this.btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (correo.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Login.this, "Correo invalido", Toast.LENGTH_SHORT).show();
                    return;
                } else if (clave.getText().toString().trim().isEmpty()) {
                    Toast.makeText(Login.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        this.lbl_registarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
                finish();
            }
        });
    }
}