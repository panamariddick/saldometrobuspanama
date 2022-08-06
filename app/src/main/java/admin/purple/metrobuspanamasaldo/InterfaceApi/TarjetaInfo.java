package admin.purple.metrobuspanamasaldo.InterfaceApi;

import admin.purple.metrobuspanamasaldo.Models.InfoUsuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TarjetaInfo {

    @GET("tarjeta/{tarjeta}")
    Call<InfoUsuario> getInfo(@Path("tarjeta") String tarjetaid);
}
