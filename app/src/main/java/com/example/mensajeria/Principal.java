package com.example.mensajeria;

import android.content.Intent;
import android.os.Bundle;

import com.example.mensajeria.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Principal extends AppCompatActivity {

    public ArrayList<Usuario> listaUsuarios = new ArrayList();

    FirebaseDatabase database;
    DatabaseReference myRef;
    Usuario usuActual = null;
    Intent intentAjustes;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvContactos;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");

        intentAjustes = new Intent(this, Ajustes.class);

        lecturaBDContactos();

    }

    @Override
    protected void onStart() {
        super.onStart();

        lanzarRV();

    }

    public void lanzarRV(){

        try {
            rvContactos = findViewById(R.id.recyclerViewContactos);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvContactos.setLayoutManager(layoutManager);
            ArrayList<Usuario> listaUsuariosAux = new ArrayList();
            for (int i = 0; i < listaUsuarios.size() ; i++) {
                if (!listaUsuarios.get(i).uid.equals(FirebaseAuth.getInstance().getUid())){
                    listaUsuariosAux.add(listaUsuarios.get(i));
                }
            }
            adaptador = new AdaptadorContactos(listaUsuariosAux, usuActual, this);
            rvContactos.setAdapter(adaptador);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ajustes) {
            this.startActivity(intentAjustes);
            return true;
        }

        if (id == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            finish();

        }

        return false;

    }

    public void lecturaBDContactos() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaUsuarios.clear();

                for (DataSnapshot xUsuarios : dataSnapshot.getChildren()) {
                    Usuario u = xUsuarios.getValue(Usuario.class);
                    listaUsuarios.add(u);
                }

                for (int i = 0; i < listaUsuarios.size(); i++) {
                    if (listaUsuarios.get(i).uid.equals(FirebaseAuth.getInstance().getUid())){
                        usuActual = listaUsuarios.get(i);
                        intentAjustes.putExtra("nombre", usuActual.nombre);
                        intentAjustes.putExtra("email", usuActual.email);
                        intentAjustes.putExtra("contraseña", usuActual.contraseña);
                        intentAjustes.putExtra("telefono", usuActual.telefono);
                        intentAjustes.putExtra("providers", usuActual.providers);
                        intentAjustes.putExtra("uid", usuActual.uid);
                        if (usuActual.fotoURL != null){
                            intentAjustes.putExtra("fotoURL", usuActual.fotoURL);
                        }else{
                            intentAjustes.putExtra("fotoURL", "");
                        }



                    }
                }

                lanzarRV();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        myRef.addValueEventListener(postListener);

    }



}