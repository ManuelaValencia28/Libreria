package com.example.libreria;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import controladores.BookBD;
import modelos.User;

public class GestionarUsuarioActivity extends AppCompatActivity {

    EditText Id, Username, Email, password;
    Button Delete, Update, Search;

    BookBD bookBD = new BookBD(this, "BookBD.db", null, 2);

    User tblUser  = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_usuario);

        Id = findViewById(R.id.IdUser);
        Username= findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        Delete  =findViewById(R.id.btnEliminarUsuario);
        Update = findViewById(R.id.btnActualizarUsuario);
        Search = findViewById(R.id.btnbuscar);



        //eventos

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!Id.getText().toString().isEmpty()){
                    if(searchID(Id.getText().toString()).size()>0){

                        Username.setText(tblUser.getName());
                        Email.setText(tblUser.getEmail());
                        password.setText(tblUser.getPassword());


                    }
                    else {
                        Toast.makeText(getApplicationContext(), "EL usuario no se esncuntra registrado en la base de datos", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Campo de busqueda vacio", Toast.LENGTH_LONG).show();
                }
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mIdUser = Id.getText().toString();
                String mUsername = Username.getText().toString();
                String mEmail = Email.getText().toString();
                String mPassword = password.getText().toString();

                if(checkData(mIdUser,mUsername,mEmail,mPassword)){
                    if (searchID(mIdUser).size()==0){
                        int id= Integer.parseInt(mIdUser);
                        bookBD.actualizarUsuario(id,mEmail,mPassword);
                        Toast.makeText(getApplicationContext(), "Datos actualizados con exito", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Faltan campos por llenar", Toast.LENGTH_LONG).show();
                }
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mIdUser = Id.getText().toString();
                if(!mIdUser.isEmpty()){
                    if (searchID(mIdUser).size()>0){
                        new AlertDialog.Builder(GestionarUsuarioActivity.this).setTitle("Eliminar usuario ")
                                .setMessage("¿Estas seguro que deseas borrar este usuario?")
                                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //eliminar producto
                                        int id = Integer.parseInt(mIdUser);
                                        bookBD.eliminarUsuario(id);
                                        Toast.makeText(getApplicationContext(), "Usuario borrado con exito", Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setNegativeButton("Cancelar",null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }

            }
        });

    }


    private ArrayList<User> searchID(String mIdUser) {
        Integer idUser = Integer.parseInt(mIdUser);
        // Crear el objeto de tipo ArrayList que será el valor que retorne
        ArrayList<User> arrUser = new ArrayList<User>();
        // Crear un objeto de la clase SQLiteDatabase
        SQLiteDatabase userRead = bookBD.getReadableDatabase();
        String query = "Select username, email, password from User where _idUser = '"+idUser+"'";
        // Generar una tabla cursor para almacenar los datos del query
        Cursor cUser = userRead.rawQuery(query,null);
        // Chequear como quedo la tabla cursor
        if (cUser.moveToFirst()){
            tblUser.setIdUser(idUser);
            tblUser.setName(cUser.getString(0));
            tblUser.setEmail(cUser.getString(1));
            tblUser.setPassword(cUser.getString(2));
            arrUser.add(tblUser);
        }
        return arrUser;
    }


    private boolean checkData(String mIdUser, String mUsername, String mEmail, String mPassword) {
        return !mIdUser.isEmpty() && !mUsername.isEmpty() && !mEmail.isEmpty() && !mPassword.isEmpty();
    }

}