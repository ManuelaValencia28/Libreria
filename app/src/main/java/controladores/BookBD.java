package controladores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import modelos.Book;

public class BookBD extends SQLiteOpenHelper implements ILibroBD {

    //Es el controlador de la base de datos que extiende SQLiteOpenHelper
    //Se define la estructura de la base de datos, solo con la tabla Book que contiene sus respectivos atributos
    //se implementa los metodos de onCreate - PAra crear la base de datos
    // elemento(int idBook) y elemento(String text) para obtener un libro segun su id o titulo
    Context contexto;
    private List<Book> LibroList = new ArrayList<>();


    public BookBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){

        super(context, name, factory, version);
        this.contexto = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Crear la estructura de la BD
        String sql = "CREATE TABLE Book (" +
                "_idBook INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "text TEXT, " +
                "cost TEXT, " +
                "available TEXT)";

        // Ejecutar la creación de la tabla
        sqLiteDatabase.execSQL(sql);

        // Insertar los datos
        String insert = "INSERT INTO Book VALUES (null, 'POSDATA: Te amo', '80.000', 'Disponible')";
        sqLiteDatabase.execSQL(insert);

        insert = "INSERT INTO Book VALUES (null, 'Bajo la misma estrella', '110.000', 'No disponible')";
        sqLiteDatabase.execSQL(insert);


        //TABLA USUARIO

        String sqlUser =  "CREATE TABLE User (" +
                "_idUser INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "status INTEGER)";
        // Ejecutar la creación de la tabla
        sqLiteDatabase.execSQL(sqlUser);

        String insertUser = "INSERT INTO Book VALUES (01, 'Manuela', 'manuela@gmail.com', 'manu2024', 1)";
        sqLiteDatabase.execSQL(insertUser);

        insertUser = "INSERT INTO Book VALUES (02, 'Sebastian', 'sebastian@gmail.com', 'sebas2024', 1)";
        sqLiteDatabase.execSQL(insertUser);

        //TABLA RENTA LIBRO

        String sqlRent = "CREATE TABLE Rent (" +
                "_idRent INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "_idUser INTEGER, " +
                "_idBook INTEGER, " +
                "date TEXT, " +  // Se usa TEXT para almacenar la fecha en formato 'YYYY-MM-DD'
                "FOREIGN KEY(_idUser) REFERENCES User(_idUser), " +
                "FOREIGN KEY(_idBook) REFERENCES Book(_idBook))";

        sqLiteDatabase.execSQL(sqlRent);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public Book elemento(int idBook) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery( "SELECT * FROM Book WHERE _idBook=" + idBook, null);
        try {

            if (cursor.moveToNext())
                return extraerBook(cursor);
            else
                return null;
        }catch (Exception e){
            Log.d("TAG","ERROR ELEMENTO(idBook) ID BookBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }



    private Book extraerBook(Cursor cursor) {
        Book book = new Book(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        book.setIdbook(cursor.getInt( 0));
        book.setText(cursor.getString( 1));
        book.setCost(cursor.getString( 2));
        book.setAvailable(cursor.getString( 3));

        return book;
    }


    @Override
    public Book elemento(String text) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery( "SELECT * FROM Book WHERE text='" + text + "'", null);
        try {
            if (cursor.moveToNext())
                return extraerBook(cursor);
            else
                return null;
        }catch (Exception e){
            Log.d("TAG","ERROR ELEMENTO(text) BookBD" + e.getMessage());
            throw e;
        }finally {
            if(cursor != null) cursor.close();
        }
    }

    @Override
    public List<Book> Lista() {
        //devuelve el registro de datos encontrados en la BD
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM Book ORDER BY text ASC";

        Cursor cursor = database.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                LibroList.add(
                        new Book( cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3) )
                );
            }while(cursor.moveToNext());
        }
        cursor.close();
        return LibroList;
    }



    @Override
    public void agregar(Book book) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("text", book.getText());
        values.put("cost", book.getCost());
        values.put("available", book.getAvailable());
        database.insert("Book", null, values );

    }

    @Override
    public void actualizar(int idbook, Book book) {

        SQLiteDatabase database = getWritableDatabase();
        String[] parametros = { String.valueOf(idbook)};
        ContentValues values = new ContentValues();

        values.put("text", book.getText());
        values.put("cost", book.getCost());
        values.put("available", book.getAvailable());

        database.update("Book", values, "_idBook=?", parametros );

    }

    @Override
    public void borrar(int idBook) {
        SQLiteDatabase database = getWritableDatabase();
        String[] parametros = { String.valueOf(idBook)};

        database.delete("Book", "_idBook=?", parametros);
    }


}//BookDB
