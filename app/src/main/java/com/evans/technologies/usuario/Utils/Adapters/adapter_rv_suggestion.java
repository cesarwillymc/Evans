package com.evans.technologies.usuario.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Utils.timeCallback.OnclickItemListener;
import com.evans.technologies.usuario.model.sugerenciasLocale;

import java.util.ArrayList;

public class adapter_rv_suggestion extends RecyclerView.Adapter<adapter_rv_suggestion.suggestions> {
    Context context;
    int layoutresources;
    ArrayList<sugerenciasLocale> sugerenciasLocales;
    OnclickItemListener listen;
    String tipe_rv;
    Boolean status_origin;
    public adapter_rv_suggestion(Context context, int layoutresources,String tipe_rv,Boolean status_origin , ArrayList<sugerenciasLocale> sugerenciasLocales, OnclickItemListener listen) {
        this.context = context;
        this.layoutresources = layoutresources;
        this.sugerenciasLocales = sugerenciasLocales;
        this.listen = listen;
        this.tipe_rv = tipe_rv;
        this.status_origin = status_origin;
    }

    @NonNull
    @Override
    public suggestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new suggestions(LayoutInflater.from( context ).inflate( layoutresources,parent,false ));
    }

    @Override
    public void onBindViewHolder(@NonNull suggestions holder, int position) {
        holder.bind(sugerenciasLocales.get(position),listen);
    }


    @Override
    public int getItemCount() {
        if (sugerenciasLocales.size()>0) {
            return sugerenciasLocales.size();
        }
        return 0;
    }
    public class suggestions extends RecyclerView.ViewHolder{
        TextView titulo;
        TextView subTitulo;
        ImageView icon;
        public suggestions(@NonNull View itemView) {
            super(itemView);
            titulo=itemView.findViewById(R.id.dsd_txt_titulo);
            subTitulo=itemView.findViewById(R.id.dsd_txt_direction_aprox);
            icon=itemView.findViewById(R.id.dsd_img_icon);
        }

        public void bind(sugerenciasLocale sugerenciasLocale, OnclickItemListener listen) {
            if (tipe_rv.contains("options")){

            }else {

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.itemClickSuggestion(sugerenciasLocale,getAdapterPosition(),status_origin);
                }
            });
        }
    }
}
