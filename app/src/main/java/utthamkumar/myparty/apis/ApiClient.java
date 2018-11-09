package utthamkumar.myparty.apis;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utthamkumar.myparty.utils.Constants;

public class ApiClient {
 
    private static Retrofit retrofit = null;
 
 
    public static Retrofit getFirebaseClient() {
        if (retrofit==null) {
            /*retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.FIREBASE_PUSH_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();*/
        }
        return retrofit;
    }
}