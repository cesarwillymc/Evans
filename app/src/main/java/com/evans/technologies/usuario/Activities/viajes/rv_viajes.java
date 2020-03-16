package com.evans.technologies.usuario.Activities.viajes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon;
import com.evans.technologies.usuario.model.DataCupon;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class rv_viajes extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.arv_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.arv_viajes)
    RecyclerView recyclerView;
    ArrayList<DataCupon> cupones= new ArrayList<>();
    private RecyclerView.Adapter adapterRview;
    @BindView(R.id.arv_imagebutton_back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_viajes);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager( new LinearLayoutManager(rv_viajes.this) );
        adapterRview = new adapter_rv_cupon(this, R.layout.dialog_rv_cupon,"cupon",cupones, new adapter_rv_cupon.click() {
            @Override
            public void itemClick(DataCupon data, int adapterPosition) {

            }
        });
        recyclerView.setAdapter(adapterRview);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                // getCuponesData();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
