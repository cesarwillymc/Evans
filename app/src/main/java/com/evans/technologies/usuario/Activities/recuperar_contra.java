package com.evans.technologies.usuario.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.fragments.change_password.correo;

import java.util.Objects;

import static com.evans.technologies.usuario.Utils.UtilsKt.getBeforeNavFragment;

public class recuperar_contra extends AppCompatActivity {
    SharedPreferences navFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navFragment= getSharedPreferences("navFragment", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_recuperar_contra);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setFragmentdefault();
    }

    private void setFragmentdefault() {
        try{

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.recuperar_frag, new correo()
            ).commit();
        }catch (Exception E){
            Log.e("error",E.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (getBeforeNavFragment(navFragment)==null){
                Intent intent = new Intent(this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.recuperar_frag,
                        Objects.requireNonNull(getBeforeNavFragment(navFragment))
                ).commit();
            }
        }
        return super.onOptionsItemSelected(item);

    }


}
