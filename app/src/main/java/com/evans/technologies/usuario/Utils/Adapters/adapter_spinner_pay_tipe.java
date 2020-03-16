package com.evans.technologies.usuario.Utils.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.evans.technologies.usuario.R;

public class adapter_spinner_pay_tipe extends BaseAdapter {
    Context context;
    int images[];
    String[] fruit;
    LayoutInflater inflter;
    int color;
    public adapter_spinner_pay_tipe(Context applicationContext, int[] flags, String[] fruit, int color) {
        this.context = applicationContext;
        this.images = flags;
        this.fruit = fruit;
        this.color=color;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.dialog_spinner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(images[i]);
        names.setText(fruit[i]);
       // names.setTextColor(context.getColor(color));
        return view;
    }
}