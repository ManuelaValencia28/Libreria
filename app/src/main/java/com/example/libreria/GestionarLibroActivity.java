package com.example.libreria;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import controladores.BookBD;
import modelos.Book;

public class GestionarLibroActivity extends AppCompatActivity implements View.OnClickListener {

    //En este apartado se permite gestionar (agregar, actualizar y eliminar) libros.
    //Si un libro ya existe(basado en idBook), se llenar los campos con los datos del libro y se desactivan
    // los botones guardar. Si no existe, los botones actualizar y borrar estan desactivados.

    Context context;
    EditText txttext, txtprecio, txtavailable;
    Button btnGuardar, btnActualizar, btnBorrar;  // Declara los botones
    int idBook;

    BookBD bookBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_libro);

        init();  // Llama al método init
    }

    private void init() {
        context = getApplicationContext();
        txttext = findViewById(R.id.ges_txttext);
        txtprecio = findViewById(R.id.ges_cost);
        txtavailable = findViewById(R.id.ges_available);

        // Inicializa los botones
        btnGuardar = findViewById(R.id.ges_btnguardar);
        btnActualizar = findViewById(R.id.ges_btnactualizar);
        btnBorrar = findViewById(R.id.ges_btnborrar);

        // Asigna el OnClickListener a cada botón
        btnGuardar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);

        // Recibe los datos del Intent
        Intent i = getIntent();
        Bundle bolsa = i.getExtras();
        idBook = bolsa.getInt("idBook");

        if (idBook != 0) {
            txttext.setText(bolsa.getString("text"));
            txtprecio.setText(bolsa.getString("cost"));
            txtavailable.setText(bolsa.getString("available"));
            btnGuardar.setEnabled(false);
        }else{
            btnActualizar.setEnabled(false);
            btnBorrar.setEnabled(false);
        }
    }

    private void limpiarcampos() {
        idBook = 0;
        txttext.setText("");
        txtprecio.setText("");
        txtavailable.setText("");
    }

    //Crea una objeto Book con los datos ingresados por el usuario
    private Book llenarDatosLibro() {
        Book book = new Book();
        String t = txttext.getText().toString();
        String p = txtprecio.getText().toString();
        String a = txtavailable.getText().toString();

        book.setIdbook(idBook);
        book.setText(t);
        book.setCost(p);
        book.setAvailable(a);

        return book;
    }

    //para almacenar o actualizar los datos de un libro en la BD

    private void guardar() {
        bookBD = new BookBD(context, "BookBD.db", null, 1);
        Book book = llenarDatosLibro();
        if (idBook == 0) {
            bookBD.agregar(book);
            Toast.makeText(context, "GUARDADO NUEVO.", Toast.LENGTH_LONG).show();
        } else {
            bookBD.actualizar(idBook, book);
            btnActualizar.setEnabled(false);
            btnBorrar.setEnabled(false);
            Toast.makeText(context, "ACTUALIZADO EXITOSO.", Toast.LENGTH_LONG).show();
        }
    }

    //elimina un libro
    private void Borrar() {
        bookBD = new BookBD(context, "BookBD.db", null, 1);
        if (idBook == 0) {
            Toast.makeText(context, "NO ES POSIBLE BORRAR.", Toast.LENGTH_LONG).show();
        } else {
            bookBD.borrar(idBook);
            limpiarcampos();
            btnBorrar.setEnabled(true);
            btnGuardar.setEnabled(false);
            btnActualizar.setEnabled(false);
            Toast.makeText(context, "BORRADO EXITOSO DEL REGISTRO.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();  // Almacena el ID del botón clicado

        if (id == R.id.ges_btnactualizar) {
            guardar();  // Llama a la función guardar()
        } else if (id == R.id.ges_btnguardar) {
            guardar();  // También llama a la función guardar()
        } else if (id == R.id.ges_btnborrar) {
            Borrar();   // Llama a la función borrar()
        }
    }

}
