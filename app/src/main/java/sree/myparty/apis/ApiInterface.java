package sree.myparty.apis;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sree.myparty.firebase.FirebasePushModel;

public interface ApiInterface {
    @Headers({
            "Authorization: key=AAAA4uqgDOE:APA91bEsiXQF6zCwtUMbEZBTt57KXmztAxHnVJt2t_cpXFRKlzSse1xSOnI6uG5GVUJuKAUNV9cC8oku8jGvbupNMwQ-N5q_KJ4mJU8fpLT_0OjOnSQs50uZ5bKnzuugh_-5vk1lz_a0clvCfVBuAmWeA9UEaFBDZg",
            "Content-Type: application/json"
    })
    @POST("/fcm/send")
    Call<ResponseBody> sendNotification(@Header("Authorization")  String Key,
                                        @Header("Content-Type")  String apiKey,
                                        @Body JSONObject model);
 

}