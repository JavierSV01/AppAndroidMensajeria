package com.example.mensajeria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mensajeria.modelos.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ajustes extends AppCompatActivity {


    Usuario usu;
    Button btFoto, btRefrescar;
    final int SELECCIONAR_FOTO = 1;
    String fotoUri;
    EditText txNombre, txTelefono;
    CircleImageView imageView;
    TextView proveedor;


    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("usuarios");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("imagenes");

        fotoUri = getIntent().getExtras().getString("fotoURL");
        usu = new Usuario(
                getIntent().getExtras().getString("nombre"),
                getIntent().getExtras().getString("email"),
                getIntent().getExtras().getString("contraseña"),
                getIntent().getExtras().getString("telefono"),
                getIntent().getExtras().getString("provider"),
                getIntent().getExtras().getString("uid"),
                fotoUri
        );

        txNombre = findViewById(R.id.txNombre);
        txTelefono = findViewById(R.id.txNumero);

        txNombre.setText(usu.nombre);
        txTelefono.setText(usu.telefono);

        btFoto = findViewById(R.id.btFoto);
        btFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECCIONAR_FOTO);
            }
        });

        btRefrescar = findViewById(R.id.btRefrescar);

        btRefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usu.fotoURL = fotoUri;
                if (!txNombre.getText().toString().equals("")){
                    usu.nombre = txNombre.getText().toString();
                }
                if (!txTelefono.getText().toString().equals("")){
                    usu.telefono = txTelefono.getText().toString();
                }

                myRef.child(FirebaseAuth.getInstance().getUid()).setValue(usu);
            }
        });

        CircleImageView imageView = (CircleImageView) findViewById(R.id.fotoPerfil);

        Glide.with(this).load(Uri.parse(usu.fotoURL)).into(imageView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECCIONAR_FOTO && resultCode == RESULT_OK){
            Uri fotoUriAux = data.getData();
            final StorageReference fotoRef = storageRef.child(fotoUriAux.getLastPathSegment());
            Toast.makeText(getApplicationContext(), "Espere a que carga la foto para salvar cambios", Toast.LENGTH_LONG).show();
            fotoRef.putFile(fotoUriAux).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        fotoUri = task.getResult().toString();
                        Toast.makeText(getApplicationContext(), "Foto cargada correctamente", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



}