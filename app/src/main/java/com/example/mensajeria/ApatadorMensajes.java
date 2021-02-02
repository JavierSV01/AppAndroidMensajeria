package com.example.mensajeria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mensajeria.modelos.Mensajes;
import com.example.mensajeria.modelos.Usuario;

import java.util.ArrayList;

public class ApatadorMensajes extends RecyclerView.Adapter {
    private ArrayList<Mensajes> listaMensajes;
    public String uidMio;
    Context contexto;

    public ApatadorMensajes(ArrayList<Mensajes> listaMensajes, Context contexto, String uidMio) {
        this.listaMensajes = listaMensajes;
        this.contexto = contexto;
        this.uidMio = uidMio;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_contacto = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mensaje, parent, false);
        ApatadorMensajes.AdaptadorMensajesViewHolder aavh = new ApatadorMensajes.AdaptadorMensajesViewHolder(item_contacto);
        return aavh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Mensajes men = listaMensajes.get(position);
        ApatadorMensajes.AdaptadorMensajesViewHolder holderAux = new ApatadorMensajes.AdaptadorMensajesViewHolder(holder.itemView);

        holderAux.txMensaje.setText(men.mensaje);

        holderAux.txRemitente.setText(men.remitente);


    }


    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }

    public class AdaptadorMensajesViewHolder extends RecyclerView.ViewHolder {

        private TextView txMensaje, txRemitente;

        public AdaptadorMensajesViewHolder(@NonNull View itemView) {
            super(itemView);
            txMensaje = itemView.findViewById(R.id.mensaje);
            txRemitente = itemView.findViewById(R.id.remitente);
        }

    }
}
