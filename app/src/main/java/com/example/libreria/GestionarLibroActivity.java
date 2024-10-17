package com.example.libreria;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Objects;

import controladores.BookBD;
import modelos.Book;

public class GestionarLibroActivity extends AppCompatActivity implements View.OnClickListener {

    //En este apartado se permite gestionar (agregar, actualizar y eliminar) libros.
    //Si un libro ya existe(basado en idBook), se llenar los campos con los datos del libro y se desactivan
    // los botones guardar. Si no existe, los botones actualizar y borrar estan desactivados.

    Context context;
    EditText txttext, txtprecio, txtavailable, rentEmail;
    Button btnGuardar, btnActualizar, btnBorrar, btnRent, btnRentarLibro, btnDevolver;  // Declara los botones
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
        btnRent = findViewById(R.id.btnRentarBook);
        btnRentarLibro = findViewById(R.id.btnRentar);
        rentEmail = findViewById(R.id.rentEmail);
        btnDevolver = findViewById(R.id.btnDevolver);

        // Asigna el OnClickListener a cada botón
        btnGuardar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnRent.setOnClickListener(this);

        // Configura el botón Devolver
        btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolver();
            }
        });

        // Recibe los datos del Intent
        Intent i = getIntent();
        Bundle bolsa = i.getExtras();
        idBook = bolsa.getInt("idBook");

        if (idBook != 0) {
            txttext.setText(bolsa.getString("text"));
            txtprecio.setText(bolsa.getString("cost"));
            String availableStatus = bolsa.getString("available");
            txtavailable.setText(availableStatus);

            if (availableStatus.equals("No disponible")) {
                btnDevolver.setVisibility(View.VISIBLE);  // Muestra el botón si el libro no está disponible
                btnRent.setVisibility(View.GONE);         // Oculta el botón de rentar si ya está alquilado

                // Desactiva el botón de borrar si el libro está en "No disponible"
                btnBorrar.setEnabled(false);
            } else {
                btnDevolver.setVisibility(View.GONE);     // Oculta el botón si el libro está disponible
                btnRent.setVisibility(View.VISIBLE);      // Muestra el botón de rentar si está disponible

                // Activa el botón de borrar si el libro está disponible
                btnBorrar.setEnabled(true);
            }

            btnGuardar.setEnabled(false);
        } else {
            btnActualizar.setEnabled(false);
            btnBorrar.setEnabled(false);
            btnRent.setEnabled(false);
            btnDevolver.setEnabled(false);  // Desactiva el botón si es un libro nuevo
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
        book.setAvailable(a); // Mantiene el estado actual del campo disponible.

        return book;
    }


    //para almacenar o actualizar los datos de un libro en la BD

    private void guardar() {
        bookBD = new BookBD(context, "BookBD.db", null, 2);
        Book book = llenarDatosLibro();
        if (idBook == 0) {
            bookBD.agregar(book);
            Toast.makeText(context, "GUARDADO NUEVO.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            bookBD.actualizar(idBook, book);
            btnActualizar.setEnabled(false);
            btnBorrar.setEnabled(false);
            Toast.makeText(context, "ACTUALIZADO EXITOSO.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    //elimina un libro
    private void Borrar() {
        bookBD = new BookBD(context, "BookBD.db", null, 2);
        if (idBook == 0) {
            Toast.makeText(context, "NO ES POSIBLE BORRAR.", Toast.LENGTH_LONG).show();
        } else {
            bookBD.borrar(idBook);
            limpiarcampos();
            btnBorrar.setEnabled(true);
            btnGuardar.setEnabled(false);
            btnActualizar.setEnabled(false);
            Toast.makeText(context, "BORRADO EXITOSO DEL REGISTRO.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void rentar() {
        bookBD = new BookBD(context, "BookBD.db", null, 2);

        rentEmail.setVisibility(View.VISIBLE);
        btnRentarLibro.setVisibility(View.VISIBLE);
        btnRent.setEnabled(false);

        btnRentarLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = rentEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(context, "Por favor ingresa un correo válido.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Verificar si el usuario existe y si está sancionado
                String estadoUsuario = bookBD.BuscarUserforRent(email);

                if (estadoUsuario == null) {
                    // El usuario no está registrado
                    Toast.makeText(context, "El usuario no está registrado en el sistema.", Toast.LENGTH_LONG).show();
                } else if (estadoUsuario.equals("0")) {
                    // Usuario sancionado
                    Toast.makeText(context, "No puedes rentar libros en este momento, tienes sanciones pendientes.", Toast.LENGTH_LONG).show();
                } else {
                    // Si el usuario está habilitado para rentar
                    if (idBook == 0) {
                        Toast.makeText(context, "NO SE PUEDE RENTAR UN LIBRO INEXISTENTE.", Toast.LENGTH_LONG).show();
                    } else {
                        // Actualiza el estado del libro
                        Book book = llenarDatosLibro();
                        book.setAvailable("No disponible");
                        bookBD.actualizar(idBook, book);

                        txtavailable.setText("No disponible");
                        btnActualizar.setEnabled(false);
                        btnBorrar.setEnabled(false);
                        btnRent.setEnabled(false);

                        Toast.makeText(context, "Libro rentado exitosamente.", Toast.LENGTH_LONG).show();
                        finish(); // Cierra la actividad
                    }
                }
            }
        });

        btnDevolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                devolver();
            }
        });

    }


    private void devolver() {
        bookBD = new BookBD(context, "BookBD.db", null, 2);

        if (idBook == 0) {
            Toast.makeText(context, "NO SE PUEDE DEVOLVER UN LIBRO INEXISTENTE.", Toast.LENGTH_LONG).show();
        } else {
            // Actualiza el estado del libro a "Disponible"
            Book book = llenarDatosLibro();
            book.setAvailable("Disponible");
            bookBD.actualizar(idBook, book);

            txtavailable.setText("Disponible");
            btnActualizar.setEnabled(true);
            btnBorrar.setEnabled(true);
            btnRent.setEnabled(true);
            btnDevolver.setEnabled(false);

            Toast.makeText(context, "Libro devuelto exitosamente.", Toast.LENGTH_LONG).show();
            finish(); // Cierra la actividad
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
        } else if (id == R.id.btnRentarBook) {
            rentar();   // Llama a la función rentar
        }
    }


}
