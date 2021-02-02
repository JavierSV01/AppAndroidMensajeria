package com.example.mensajeria;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mensajeria.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorContactos extends RecyclerView.Adapter {

    private ArrayList<Usuario> listaUsuarios;
    Context contexto;
    Usuario usuActual;
    public AdaptadorContactos(ArrayList<Usuario> listaUsuarios, Usuario usuActual, Context contexto){
        this.listaUsuarios = listaUsuarios;
        this.contexto = contexto;
        this.usuActual = usuActual;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_trabajo = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacto, parent, false);
        AdaptadorContactos.AdaptadorTrabajoViewHolder aavh = new  AdaptadorContactos.AdaptadorTrabajoViewHolder(item_trabajo);
        return aavh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Usuario usu = listaUsuarios.get(position);
        AdaptadorContactos.AdaptadorTrabajoViewHolder holderAux = new AdaptadorContactos.AdaptadorTrabajoViewHolder(holder.itemView);

        final Intent intent = new Intent(contexto, Chat.class);

        if (usu.nombre.equals("")){
            holderAux.txNombre.setText(usu.email);
            intent.putExtra("nombreRecibe", usu.email);
        }else{
            holderAux.txNombre.setText(usu.nombre);
            intent.putExtra("nombreRecibe", usu.nombre);
        }



        if (usu != null){
            intent.putExtra("telefonoContacto", usu.telefono);
        }else{
            intent.putExtra("telefonoContacto", "");
        }

        if (!usu.uid.equals("")){
            intent.putExtra("fotoUrl", usu.fotoURL);
        }else{
            intent.putExtra("fotoUrl", "");
        }



        holderAux.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("uidEnviado", usu.uid);


                if (!usuActual.nombre.equals("")){
                    intent.putExtra("remitente", usuActual.nombre);
                }else{
                    intent.putExtra("remitente", usuActual.email);
                }




                contexto.startActivity(intent);
            }
        });
        System.out.println(usu.fotoURL);
        if (usu.fotoURL != null){
            Glide
                    .with(holderAux.fotoPerfil.getContext())
                    .load(Uri.parse(usu.fotoURL))
                    .into(holderAux.fotoPerfil);
        }
        holderAux.txTelefono.setText(usu.telefono);


    }


    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public class AdaptadorTrabajoViewHolder extends RecyclerView.ViewHolder {

        private TextView txNombre, txTelefono;
        private CircleImageView fotoPerfil;
        private ConstraintLayout view;

        public AdaptadorTrabajoViewHolder(@NonNull View itemView) {
            super(itemView);
            txNombre = itemView.findViewById(R.id.txNombre);
            txTelefono = itemView.findViewById(R.id.txTelefono);
            fotoPerfil = itemView.findViewById(R.id.fotoPerfil);
            view = itemView.findViewById(R.id.layaoutItemContactos);
        }

    }

}
