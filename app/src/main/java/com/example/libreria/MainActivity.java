package com.example.libreria;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //*Actua como la plantilla principal de la app, estan los botones para registrar
    //*listar y buscar los libros
    //*implemente OnclickListener para manejar los clicks de los botones
    //* Dependiendo del boton que se presiona, se muestra un mensaje y se redirige al usuario a la actividad
    // correspendiente (Gestionar el libro, Listar los libros, o buscar un libro)

    Context context;
    Button btnListar, btnregistrar, btnbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    // Inicialización de los botones y asignación de OnClickListener
    private void init() {
        context = getApplicationContext();
        btnregistrar = findViewById(R.id.btnregistrar);
        btnListar = findViewById(R.id.btnlistar);
        btnbuscar = findViewById(R.id.btnbuscar);

        // Asignar OnClickListener
        btnregistrar.setOnClickListener(this);
        btnListar.setOnClickListener(this);
        btnbuscar.setOnClickListener(this);
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
        }
    }
}
