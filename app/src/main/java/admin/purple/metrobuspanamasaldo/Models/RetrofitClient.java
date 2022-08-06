package admin.purple.metrobuspanamasaldo.Models;

import java.util.concurrent.TimeUnit;

import admin.purple.metrobuspanamasaldo.InterfaceApi.TarjetaInfo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://saldometrobus.yizack.com/api/0/";
    private static Retrofit retrofit;


    public static Retrofit getRetrofitClient(){
        HttpLoggingInterceptor httpLogginInterceptor = new HttpLoggingInterceptor();
        httpLogginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLogginInterceptor)
                .build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static TarjetaInfo getInfoTarjeta(){

        TarjetaInfo tarjetaInfo = getRetrofitClient().create(TarjetaInfo.class);
        return  tarjetaInfo;

    }

}
