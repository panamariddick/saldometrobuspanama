package admin.purple.metrobuspanamasaldo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;

import admin.purple.metrobuspanamasaldo.Models.InfoUsuario;
import admin.purple.metrobuspanamasaldo.Models.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {

    String name = "";
    String numtarjeta = "";
    TextView tvUserName, tvSaldo, tvTarjeta, tvEstado, tvTipo;
    ImageView imgReload;
    CardView cardIdSahre, cardIdRate, cardIdAbout;
    AlertDialog alert;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initElements();
        alert = dialogLoader();

        SharedPreferences prefs = getSharedPreferences("FIRST_OPEN_SHARED_PREF", MODE_PRIVATE);
        name = prefs.getString("name", "El Pana ;)");
        numtarjeta = prefs.getString("tarjeta", "No id card defined");

        loadLocalData();
        loadDataTarjeta();

        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 25/06/2022 Ejecutar la recarga de los datos
                loadDataTarjeta();
            }
        });

        cardIdSahre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 25/06/2022 accion compartir app
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "http://play.google.com/store/apps/details?id=" + getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mantente al dia sobre tu saldo del Metro y Metro bus con esta app!");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartir via"));
            }
        });

        cardIdAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 25/06/2022 accion acerca de app
                Intent intent = new Intent(Home.this, About.class);
                startActivity(intent);
            }
        });

        cardIdRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 25/06/2022 accion calificar app
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e){
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAd();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-9167516523739930/4214247376", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.i("AD FAILED", loadAdError.getMessage());
                mInterstitialAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                Log.i("CARGA AD","onAdLoaded");

                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }
                });

            }
        });
    }

    private void loadDataTarjeta() {
        alert.show();
        // TODO: 25/06/2022 agregar loader progress o lo que sea
        Call<InfoUsuario> infoUsuarioCall = RetrofitClient.getInfoTarjeta().getInfo(numtarjeta);
        infoUsuarioCall.enqueue(new Callback<InfoUsuario>() {
            @Override
            public void onResponse(Call<InfoUsuario> call, Response<InfoUsuario> response) {

                if (response.isSuccessful() && response.body() != null ){

                    try {
                            alert.dismiss();
                            tvSaldo.setText("B/." + response.body().getTarjeta().getSaldo());
                            tvEstado.setText(response.body().getTarjeta().getEstado());
                            tvTipo.setText(response.body().getTarjeta().getTipo());

                        // TODO: 25/06/2022 quitar el progress bar
                    }catch (Exception e){
                        alert.dismiss();
                        Toast.makeText(Home.this, "Error " + e, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        showInterstitial();
    }

    public void showInterstitial(){
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadLocalData() {
        tvUserName.setText(name);
        tvTarjeta.setText(numtarjeta);
    }

    private AlertDialog dialogLoader(){
        ViewGroup viewGroup = findViewById(android.R.id.content);

        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        View view1 = LayoutInflater.from(Home.this).inflate(R.layout.custon_layout_info, viewGroup, false);
        builder.setCancelable(false);
        builder.setView(view1);

        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return alertDialog;
    }

    private void initElements() {

        tvUserName = findViewById(R.id.tvUserName);
        tvSaldo = findViewById(R.id.tvSaldo);
        imgReload = findViewById(R.id.imgReload);
        cardIdSahre  = findViewById(R.id.cardIdSahre);
        cardIdRate  = findViewById(R.id.cardIdRate);
        cardIdAbout  = findViewById(R.id.cardIdAbout);
        tvTarjeta = findViewById(R.id.tvtarjeta);
        tvEstado = findViewById(R.id.estado);
        tvTipo = findViewById(R.id.tipo);
    }

}