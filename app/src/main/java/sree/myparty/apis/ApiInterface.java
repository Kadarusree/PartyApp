package sree.myparty.apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sree.myparty.firebase.FirebasePushModel;

public interface ApiInterface {
    @POST("/fcm/send")
    Call<ResponseBody> sendNotification(@Header("Authorization")  String Key,
                                        @Header("Content-Type")  String apiKey,
                                        @Body FirebasePushModel model);
 

}