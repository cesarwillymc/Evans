package com.evans.technologies.usuario.Activities.cupon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.getUserId_Prefs;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;

public class AddCodePromocional extends AppCompatActivity {
    @BindView(R.id.aacp_imagebutton_back)
    ImageButton back;
    @BindView(R.id.aacp_edtxt_codep)
    EditText code;
    @BindView(R.id.aacp_btn_add)
    Button add;
    SharedPreferences prefs;
    String dato;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            View focusedView = getCurrentFocus();

            if (focusedView != null) {
                inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                        InputMethodManager.SHOW_FORCED);
            }
        }catch (Exception e){
            Log.e("error",e.getMessage());
        }
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_add_code_promocional);

        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dato=code.getText().toString().trim();
                if(ComprobarCode()){
                    funcionAumentarCode();
                }
            }
        });

    }

    private void funcionAumentarCode() {
        Log.e("Agregarc",getUserId_Prefs(prefs)+dato);
        Call code= RetrofitClient.getInstance().getApi().setCuponUser(getUserId_Prefs(prefs),dato);
        code.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("Agregarc",""+response.code());
                if (response.isSuccessful()){
                    toastLong(AddCodePromocional.this,"Se agrego el cúpon correctamente");
                    startActivity(new Intent(AddCodePromocional.this,PagosActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }else{
                    toastLong(AddCodePromocional.this,"Hubo problemas al agregar el cúpon");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                toastLong(AddCodePromocional.this,"Intentalo mas tarde");
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private boolean ComprobarCode() {
        if (code.getText().toString().trim().isEmpty()){
            code.setError("El campo no puede estar vacio");
            return false;
        }
        if (code.getText().toString().trim().length()<6){
            code.setError("Codigo incorrecto");
            return false;
        }
        return  true;
    }
}
