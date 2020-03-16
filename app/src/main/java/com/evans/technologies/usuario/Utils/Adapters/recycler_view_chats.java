package com.evans.technologies.usuario.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.model.chats;

import java.util.ArrayList;

public class recycler_view_chats extends RecyclerView.Adapter<recycler_view_chats.ProductViewHolder> {
private Context mcontex;
private  int layoutResources;
private OnItemClickListener Listen;
private ArrayList<chats> chatTipe;

public recycler_view_chats(Context mcontex, int layoutResources, ArrayList<chats> chatTipe, OnItemClickListener Listen) {
        this.mcontex = mcontex;
        this.layoutResources = layoutResources;
        this.Listen = Listen;
        this.chatTipe=chatTipe;
        }

@NonNull
@Override
public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( mcontex ).inflate( layoutResources,parent,false );
        return new ProductViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(chatTipe.get(position),Listen);

        }

@Override
public int getItemCount() {
    if(chatTipe.size()>0){
        return chatTipe.size();
    }
    return 0;
}

public class ProductViewHolder extends RecyclerView.ViewHolder  {
    TextView mPrecio,mNombre,mStarts;
    ImageView mImage;
    RatingBar ratingBar;
    public ProductViewHolder(@NonNull View itemView) {
        super( itemView );
       /* mNombre= itemView.findViewById(R.id.drccv_text_view_username);

        mImage= itemView.findViewById(R.id.drccv_image_view_profile);*/
    }


    @SuppressLint("SetTextI18n")
    public void bind(final chats data,final OnItemClickListener listen) {
        mNombre.setText(data.getNombre() );
        change();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listen.OnClickListener(data,getAdapterPosition());
            }
        });
    }
    public void change(){
        Bitmap bitmap = BitmapFactory.decodeResource(mcontex.getResources(), R.drawable.logo1);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(mcontex.getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        mImage.setBackgroundColor(Color.TRANSPARENT);
        mImage.setImageDrawable(roundedBitmapDrawable);
    }
}
public interface OnItemClickListener{
    void OnClickListener(chats android, int position);
}
/**
 * getPlatilloPosicion: Obtiene la posicion del platillo seleccionado
 * strNombrePlatillo: nombre del platillo seleccionado
 * */

}
