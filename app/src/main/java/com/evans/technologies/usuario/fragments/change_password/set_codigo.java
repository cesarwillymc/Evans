package com.evans.technologies.usuario.fragments.change_password;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evans.technologies.usuario.R;
import com.evans.technologies.usuario.Retrofit.RetrofitClient;
import com.evans.technologies.usuario.fragments.Fragment_perfil_user;
import com.evans.technologies.usuario.model.ResponsesApi.LoginResponse;
import com.evans.technologies.usuario.model.user;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.evans.technologies.usuario.Utils.UtilsKt.getIDrecuperar;
import static com.evans.technologies.usuario.Utils.UtilsKt.getUserId_Prefs;
import static com.evans.technologies.usuario.Utils.UtilsKt.setAccountActivate;
import static com.evans.technologies.usuario.Utils.UtilsKt.setNavFragment;
import static com.evans.technologies.usuario.Utils.UtilsKt.settokenrecuperar;
import static com.evans.technologies.usuario.Utils.UtilsKt.toastLong;
import static org.jetbrains.anko.ToastsKt.toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class set_codigo extends Fragment {

    View view;
    SharedPreferences navFragment;
    SharedPreferences prefs;
    @BindView(R.id.fsc_btn_next)
    Button next;
    @BindView(R.id.fsc_edtxt_codigo)
    EditText codigo;
    @BindView(R.id.progressBar_codigo)
    ProgressBar progressbar;
    @BindView(R.id.fsc_validar_tarde)
    TextView validartarde;
    String code;
    Boolean validar;
    Boolean actividad;
    String id;
    public set_codigo(boolean validar_account,boolean activity) {
        // Required empty public constructor
        validar=validar_account;
        this.actividad=activity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navFragment= getContext().getSharedPreferences("navFragment", Context.MODE_PRIVATE);
        prefs = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        view= inflater.inflate(R.layout.fragment_set_codigo, container, false);
        ButterKnife.bind(this,view);
        if (validar&&actividad){
            validartarde.setVisibility(View.VISIBLE);
            id= getIDrecuperar(navFragment);
        }else{
            id= getUserId_Prefs(prefs);
        }
        validartarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actividad){
                    getActivity().finish();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=codigo.getText().toString().trim();
                if (comprobarcampos()){
                    progressbar.setVisibility(View.VISIBLE);
                    if (validar){
                        validarAccount();
                    }else{
                        cambiarContraseña();
                    }
                }
            }


        });
        return view;
    }

    private void validarAccount() {
        Log.e("codigo_setva","\n"+
                id+"\n"+
                code+"\n");

        Call<LoginResponse> sendAccount= RetrofitClient.getInstance().getApi().validarCode_validate(id.trim(),code.trim());
        sendAccount.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body()!=null){
                    Log.e("codigo_set","tiene cuerpo");

                }
                try {
                    Log.e("codigo_set",response.code()+"\n"+
                            id+"\n"+
                            code+"\n");
                }catch (Exception e){

                }

                if (response.isSuccessful()){
                    progressbar.setVisibility(View.GONE);
                    toastLong(getActivity(),"Su cuenta se valido correctamente");
                    if (actividad){
                        getActivity().finish();
                    }else{
                        setAccountActivate(prefs,true);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.main_layout_change_fragment,
                                new Fragment_perfil_user()
                        ).commit();
                    }

                }else{
                    progressbar.setVisibility(View.GONE);
                    toast(getActivity(),"El codigo no es valido");

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                Log.e("codigo_setva","\n"+
                        t.getMessage());
            }
        });
    }

    private void cambiarContraseña() {
        Log.e("codigo_setcc","\n"+
                id+"\n"+
                code+"\n");
        Call<user> sendCorreo= RetrofitClient.getInstance().getApi().sendCodigo_recuperar(getIDrecuperar(navFragment),code);
        sendCorreo.enqueue(new Callback<user>() {
            @Override
            public void onResponse(Call<user> call, Response<user> response) {
                if (response.body()!=null){
                    Log.e("codigo_set","tiene cuerpo");
                }
                try {
                    Log.e("codigo_set",response.code()+"\n"+
                            getIDrecuperar(navFragment)+"\n"+
                            code+"\n");
                }catch (Exception e){

                }

                if (response.isSuccessful()){
                    progressbar.setVisibility(View.GONE);
                    setNavFragment(navFragment,new set_codigo(true,false).toString());
                    settokenrecuperar(navFragment,code);
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.recuperar_frag,
                            new changepassword()
                    ).commit();

                }else{
                    progressbar.setVisibility(View.GONE);
                    toast(getActivity(),"El codigo no es valido");

                }
            }

            @Override
            public void onFailure(Call<user> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private boolean comprobarcampos() {
        if (code.isEmpty()){
            codigo.setError("El campo esta vacio");
            return false;
        }
        if (code.length()!=5){
            codigo.setError("Codigo invalido el codigo es mayor de 5 digitos");
            return false;
        }

        return true;
    }

}
