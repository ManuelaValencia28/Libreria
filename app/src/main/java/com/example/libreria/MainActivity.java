package com.example.libreria;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    Button btnListar, btnregistrar, btnbuscar, btnGesUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verifica si el usuario ya está registrado
        SharedPreferences preferences = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        boolean isRegistered = preferences.getBoolean("isRegistered", false);

        if (!isRegistered) {
            // Redirigir a la actividad de registro si no está registrado
            Intent intent = new Intent(MainActivity.this, registro.class);
            startActivity(intent);
            finish();
        } else {
            // Continuar con la lógica de la actividad principal
            setContentView(R.layout.activity_main);
            init();
        }
    }

    // Inicialización de los botones y asignación de OnClickListener
    private void init() {
        context = getApplicationContext();
        btnregistrar = findViewById(R.id.btnregistrar);
        btnListar = findViewById(R.id.btnlistar);
        btnbuscar = findViewById(R.id.btnbuscar);
        btnGesUser = findViewById(R.id.btnGestionarUser);

        // Asignar OnClickListener
        btnregistrar.setOnClickListener(this);
        btnListar.setOnClickListener(this);
        btnbuscar.setOnClickListener(this);
        btnGesUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnregistrar) {
            Toast.makeText(context, "Registrar", Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, GestionarLibroActivity.class);
            Bundle bolsa = new Bundle();
            bolsa.putInt("idBook", 0);
            i.putExtras(bolsa);

            startActivity(i);
        } else if (view.getId() == R.id.btnlistar) {
            Toast.makeText(context, "Listar", Toast.LENGTH_LONG).show();
            Intent i2 = new Intent(context, ListadoLibrosActivity.class);
            startActivity(i2);
        } else if (view.getId() == R.id.btnbuscar) {
            Toast.makeText(context, "Buscar", Toast.LENGTH_LONG).show();
            Intent i3 = new Intent(context, BuscarLibroActivity.class);
            startActivity(i3);
        } else if (view.getId() == R.id.btnGestionarUser) {
            Toast.makeText(context, "Gestionar usuarios", Toast.LENGTH_LONG).show();
            Intent i4 = new Intent(context, GestionarUsuarioActivity.class);
            startActivity(i4);
        }


    }
}
