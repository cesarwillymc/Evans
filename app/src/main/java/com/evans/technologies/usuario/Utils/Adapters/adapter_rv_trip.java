package com.evans.technologies.usuario.Utils.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.model.DataCupon;
import com.evans.technologies.usuario.model.chats;
import com.evans.technologies.usuario.model.trip;

import java.util.ArrayList;

public class adapter_rv_trip extends RecyclerView.Adapter<adapter_rv_trip.mismensajes> {

    Context context;
    int layoutResources;
    ArrayList<trip> viaje;
    View user;
    private OnItemClickListener Listen;

    public adapter_rv_trip(Context context, int layoutResources, OnItemClickListener Listen) {
        this.context = context;
        this.layoutResources = layoutResources;
        this.Listen = Listen;
    }

    @NonNull
    @Override
    public mismensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = LayoutInflater.from(context).inflate(layoutResources, parent, false);
        return new mismensajes(user);

    }
    public void setData(ArrayList<trip> viaje){
        this.viaje=viaje;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull mismensajes holder, int position) {
        holder.bind(viaje.get(position));
    }


    @Override
    public int getItemCount() {
        if (viaje == null) {
            return 0;
        }
        return viaje.size();
    }

    public class mismensajes extends RecyclerView.ViewHolder {

        TextView origen;
        TextView fecha;
        TextView hora;
        TextView estado;
        TextView vermas;
        TextView destino;

        public mismensajes(@NonNull View itemView) {
            super(itemView);
            origen = itemView.findViewById(R.id.dhv_txt_origen);
            fecha = itemView.findViewById(R.id.dhv_txt_fecha);
            hora = itemView.findViewById(R.id.dhv_txt_hora);
            estado = itemView.findViewById(R.id.dhv_txt_estado);
            vermas = itemView.findViewById(R.id.dhv_txt_detalle);
            destino = itemView.findViewById(R.id.dhv_txt_destino);
            //

        }

        public void bind(trip mismensajes) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listen.OnClickListener(mismensajes,getAdapterPosition());
                }
            });
        }


    }
    public interface OnItemClickListener{
        void OnClickListener(trip vaije, int position);
    }
}

