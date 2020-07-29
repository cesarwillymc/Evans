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
import com.evans.technologies.usuario.model.DataCupon;

import java.util.ArrayList;


public class adapter_rv_cupon extends RecyclerView.Adapter<adapter_rv_cupon.suggestions> {
    Context context;
    int layoutresources;
    ArrayList<DataCupon> sugerenciasLocales;
    click listen;
    String tipe;
    public adapter_rv_cupon(Context context, int layoutresource, click listen) {
        this.context = context;
        this.layoutresources = layoutresource;

        this.listen = listen;

    }
    public void getData(ArrayList<DataCupon> datas,String tipe){
        this.tipe=tipe;
        if (sugerenciasLocales!=null)
            sugerenciasLocales.clear();
        this.sugerenciasLocales = datas;
        notifyDataSetChanged();

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
        if (sugerenciasLocales!=null) {
            return sugerenciasLocales.size();
        }
        return 0;
    }
    public class suggestions extends RecyclerView.ViewHolder{
        TextView name;
        TextView digits;
        ImageView icon;
        //Otros de cupones disponibles
        TextView valor;
        TextView saldo;
        TextView nombre;
        TextView expire;
        public suggestions(@NonNull View itemView) {
            super(itemView);
            if (tipe.equals("metodopay")){
                name=itemView.findViewById(R.id.drvmp_txt);
                digits=itemView.findViewById(R.id.drvmp_txt_numTarjeta);
                icon=itemView.findViewById(R.id.drvmp_img);
            }else{
                valor=itemView.findViewById(R.id.drvc_total_cupon);
                saldo=itemView.findViewById(R.id.drvc_saldo_coupon);
                nombre=itemView.findViewById(R.id.drvc_name_cupon);
                expire=itemView.findViewById(R.id.drvc_fecha_coupon);
            }

        }

        public void bind(DataCupon sugerenciasLocale, click listen) {
            if (tipe.equals("metodopay")){
                if (sugerenciasLocale.getTipePay().equals("efectivo")){
                    name.setText("efectivo");
                    icon.setImageDrawable(context.getDrawable(R.drawable.money));
                }else if (sugerenciasLocale.getTipePay().equals("visa")){
                    name.setText("Visa");
                    icon.setImageDrawable(context.getDrawable(R.drawable.money));
                    digits.setText(sugerenciasLocale.getFourNumberT());
                }else if (sugerenciasLocale.getTipePay().equals("mastercard")){
                    name.setText("Mastercard");
                    icon.setImageDrawable(context.getDrawable(R.drawable.money));
                    digits.setText(sugerenciasLocale.getFourNumberT());
                }
            }else{
                valor.setText(sugerenciasLocale.getValue()+" PEN");
                saldo.setText(sugerenciasLocale.getSaldo()+" PEN");
                nombre.setText(sugerenciasLocale.getName());
                if (sugerenciasLocale.getExpire().contains("Venció")){
                    expire.setTextColor(context.getResources().getColor(R.color.rojo));
                }else{
                    expire.setTextColor(context.getResources().getColor(R.color.verde));
                }
                expire.setText(sugerenciasLocale.getExpire());

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listen.itemClick(sugerenciasLocale,getAdapterPosition());
                }
            });
        }
    }
    public interface click {
        void itemClick(DataCupon data, int adapterPosition);

    }
}
