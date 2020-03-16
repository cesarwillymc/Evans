package com.evans.technologies.usuario.Activities.cupon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Utils.Adapters.adapter_rv_cupon;
import com.evans.technologies.usuario.model.DataCupon;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagosActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.ap_rv_metodos_pay)
    RecyclerView metodos_pay;
    @BindView(R.id.ap_txt_add_pagos)
    TextView add_pay;
    @BindView(R.id.ap_txt_add_promo)
    TextView add_promo;
    @BindView(R.id.ap_ll_cupones)
    LinearLayout cupones;
    @BindView(R.id.ap_imagebutton_back)
    ImageButton back;
    private RecyclerView.Adapter adapterRview;
    ArrayList<DataCupon> tipepays= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        ButterKnife.bind(this);
        DataCupon efectivo= new DataCupon("Cesar Willy","efectivo","Luis rivarola","Puno","Puno");
        tipepays.add(efectivo);
        metodos_pay.setLayoutManager( new LinearLayoutManager(PagosActivity.this) );

        adapterRview = new adapter_rv_cupon(this, R.layout.dialog_rv_metodos_pago, "metodopay", tipepays, new adapter_rv_cupon.click() {
            @Override
            public void itemClick(DataCupon data, int adapterPosition) {

            }
        });
        add_pay.setOnClickListener(this);
        add_promo.setOnClickListener(this);
        cupones.setOnClickListener(this);
        metodos_pay.setAdapter(adapterRview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ap_txt_add_pagos:
                break;
            case R.id.ap_txt_add_promo:
                startActivity(new Intent(PagosActivity.this,AddCodePromocional.class));
                break;
            case R.id.ap_ll_cupones:
                startActivity(new Intent(PagosActivity.this,Cupones.class));
                break;
        }
    }
}
