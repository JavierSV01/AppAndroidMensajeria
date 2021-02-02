package com.example.mensajeria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.example.mensajeria.modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PantallaCarga extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;

    public ArrayList<Usuario> listaUsuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");

        lecturaBD();
        try {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //intent.putParcelableArrayListExtra("listaUsuarios", listaUsuarios);
            Thread.sleep(4000);
            startActivity(intent);
        }catch (Exception e){
            System.out.println("Error pantalla de carga");
        }


    }

    public void lecturaBD() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaUsuarios.clear();

                for (DataSnapshot xUsuarios : dataSnapshot.getChildren()) {
                    Usuario u = xUsuarios.getValue(Usuario.class);
                    listaUsuarios.add(u);
                    System.out.println("Usuariio pantalla carga: " + u.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(postListener);


    }
}