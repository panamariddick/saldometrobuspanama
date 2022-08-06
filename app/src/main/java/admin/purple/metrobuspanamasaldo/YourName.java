package admin.purple.metrobuspanamasaldo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;

import admin.purple.metrobuspanamasaldo.Models.InfoUsuario;
import admin.purple.metrobuspanamasaldo.Models.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourName extends AppCompatActivity {

    private AppCompatButton btnSaveName;
    private EditText txtNombre, txtNumTarjeta;
    String nombre = "";
    String numTarjeta = "";
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_name);
        initElements();

        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    loadDataTarjeta();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    private void loadDataTarjeta() {
        nombre = txtNombre.getText().toString();
        numTarjeta = txtNumTarjeta.getText().toString();

        if (nombre.equals("") && numTarjeta.equals("")){
            nombre = "ElPana ;)";
            txtNumTarjeta.setError("Ingresa el numero de tarjeta.");
            txtNumTarjeta.requestFocus();
        }else {
            Call<InfoUsuario> infoUsuarioCall = RetrofitClient.getInfoTarjeta().getInfo(numTarjeta);
            infoUsuarioCall.enqueue(new Callback<InfoUsuario>() {
                @Override
                public void onResponse(Call<InfoUsuario> call, Response<InfoUsuario> response) {

                    if (response.isSuccessful() && response.body() != null ){

                        try {

                            String msg = response.body().getStatus();

                            if (msg.equals("error")){
                                alert.show();
                            }else if (msg.equals("ok")){
                                // TODO: 28/06/2022 si la tarjeta es correcta
                                SharedPreferences.Editor editor = getSharedPreferences("FIRST_OPEN_SHARED_PREF", MODE_PRIVATE).edit();
                                editor.putString("name", nombre);
                                editor.putString("tarjeta", numTarjeta);
                                editor.putBoolean("firstOpen", true);
                                editor.apply();

                                Intent intent = new Intent(YourName.this, Home.class);
                                startActivity(intent);
                                finish();
                            }

                            // TODO: 25/06/2022 quitar el progress bar
                        }catch (Exception e){
                            alert.dismiss();
                            Toast.makeText(YourName.this, "Error " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<InfoUsuario> call, Throwable t) {
                    Log.e("failure", t.getLocalizedMessage());
                    // TODO: 25/06/2022 mensaje de error
                }
            });
        }




    }

    private AlertDialog dialogLoader(){
        ViewGroup viewGroup = findViewById(android.R.id.content);

        Button btnok;

        AlertDialog.Builder builder = new AlertDialog.Builder(YourName.this);
        View view1 = LayoutInflater.from(YourName.this).inflate(R.layout.custon_layout_info_error, viewGroup, false);
        builder.setCancelable(false);
        builder.setView(view1);

        btnok = view1.findViewById(R.id.btnok);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

    private void initElements() {
        btnSaveName = findViewById(R.id.btnSaveName);
        txtNombre = findViewById(R.id.txtNombre);
        txtNumTarjeta = findViewById(R.id.txtNumTarjeta);
        alert = dialogLoader();
    }
}