package com.example.mensajeria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mensajeria.modelos.Mensajes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    Button btSend;
    String mensaje, uidEnvia, nombreRecibe, uidRecibe, nombreEnvia;

    public ArrayList<Mensajes> listaMensajes = new ArrayList();
    EditText txMensaje;

    TextView txNombre, txTelefono;
    CircleImageView fotoContacto;

    private RecyclerView.Adapter adaptador;
    private RecyclerView rvMensajes;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        btSend = findViewById(R.id.btSend);
        txMensaje = findViewById(R.id.txMensaje);
        mensaje = txMensaje.getText().toString();
        uidEnvia = FirebaseAuth.getInstance().getUid();
        uidRecibe = getIntent().getExtras().getString("uidEnviado");
        nombreEnvia = getIntent().getExtras().getString("remitente");

        txNombre = findViewById(R.id.nombreContacto);
        txTelefono = findViewById(R.id.telefonoContacto);
        fotoContacto = findViewById(R.id.ftContacto);
        nombreRecibe = getIntent().getExtras().getString("nombreRecibe");

        String urlFotoContacto = getIntent().getExtras().getString("fotoUrl");

        if (urlFotoContacto != null){
            Glide
                    .with(this)
                    .load(Uri.parse(urlFotoContacto))
                    .into(fotoContacto);
        }

        txNombre.setText(nombreRecibe);
        txTelefono.setText(getIntent().getStringExtra("telefonoContacto"));

        enviarMensaje();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("mensajes");
        lecturaBD();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lecturaBD();
    }

    public void enviarMensaje(){
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensaje = txMensaje.getText().toString();
                if (!mensaje.equals("")){
                    Mensajes m = new Mensajes(mensaje, uidRecibe, uidEnvia, nombreEnvia);
                    String key = myRef.push().getKey();
                    myRef.child(key).setValue(m);
                    System.out.println("La key es:" + key);
                    txMensaje.setText("");
                }else{
                    Toast.makeText(getApplicationContext(),"Introduce el mensaje", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void lanzarRV(){
        try {
            rvMensajes = findViewById(R.id.recyclerViewMensajes);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvMensajes.setLayoutManager(layoutManager);
            ArrayList<Mensajes> listaMensajesAux = new ArrayList();


            for (int i = 0; i < listaMensajes.size(); i++) {
                if ( (listaMensajes.get(i).uidRecibe.equals(uidRecibe) && listaMensajes.get(i).uidEnvia.equals(uidEnvia) ) ||
                        (listaMensajes.get(i).uidRecibe.equals(uidEnvia) && listaMensajes.get(i).uidEnvia.equals(uidRecibe) )) {
                    listaMensajesAux.add(listaMensajes.get(i));
                }
            }

            adaptador = new ApatadorMensajes(listaMensajesAux, this, FirebaseAuth.getInstance().getUid());
            rvMensajes.scrollToPosition(adaptador.getItemCount() - 1);
            rvMensajes.setAdapter(adaptador);


        } catch (Exception e) {
            System.out.println("Error rv: " + e.getMessage());
        }
    }

    public void lecturaBD() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listaMensajes.clear();

                for (DataSnapshot xMensajes : dataSnapshot.getChildren()) {
                    Mensajes m = xMensajes.getValue(Mensajes.class);
                    listaMensajes.add(m);
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