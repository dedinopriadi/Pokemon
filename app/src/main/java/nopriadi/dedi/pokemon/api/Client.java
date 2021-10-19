package nopriadi.dedi.pokemon.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import nopriadi.dedi.pokemon.utils.Constanta;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit;
    public interface CallBackRequest{
        void successCode(String message, JSONObject rd);
        void unsuccessResponse(String message);
        void parsingError(String message);
        void otherError(String message);
        void failure(String message);
    }


    public static nopriadi.dedi.pokemon.api.Service initialize(String url){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return  retrofit.create(nopriadi.dedi.pokemon.api.Service.class);
    }

    public static void request(Call<ResponseBody> result, final CallBackRequest callBack){

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if(response.isSuccessful()){
                        JSONObject obj1 = new JSONObject(response.body().string());

                        callBack.successCode(Constanta.Message.INQUERY_SUCCESS, obj1);

                    }else {
                        callBack.unsuccessResponse(Constanta.Message.UNSUCCESS_RESPONSE);
                    }
                } catch (JSONException e) {
                    callBack.parsingError(Constanta.Message.ERROR_PARSING);
                }catch (Exception e){
                    e.printStackTrace();
                    callBack.otherError(Constanta.Message.ERROR_RESPONSE);
//                    callBack.otherError(e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.failure(Constanta.Message.FAILURE);
            }
        });
    }

}
