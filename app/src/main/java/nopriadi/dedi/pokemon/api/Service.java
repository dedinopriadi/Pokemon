package nopriadi.dedi.pokemon.api;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface Service {

    @GET("pokemon")
    Call<ResponseBody> getPokemon(@QueryMap HashMap<String, String> params);

    @GET("pokemon/{id}/")
    Call<ResponseBody> getDetailPokemon(@Path("id") String groupId);


}
