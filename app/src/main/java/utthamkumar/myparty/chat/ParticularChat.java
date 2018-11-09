package utthamkumar.myparty.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.renderer.scatter.CircleShapeRenderer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import utthamkumar.myparty.DashBoard.Dashboard;
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.session.SessionManager;
import utthamkumar.myparty.utils.Constants;

/**
 * Created by shridhars on 11/1/2017.
 */

public class ParticularChat extends AppCompatActivity {


    private static String API_KEY = "46162222";
    private static String SESSION_ID = "2_MX40NjE2MjIyMn5-MTUzMjg3MzM2ODY4OX5ZUDhSR2ZnWnJwQVIxZ3FJL29ZYnlFblZ-fg";
    private static String TOKEN = "T1==cGFydG5lcl9pZD00NjE2MjIyMiZzaWc9MDU2YTY2NzZmYTI3NDQzZGZjZjM4YzJkMjk4ZDdjODc3ZmY2NDJkZTpzZXNzaW9uX2lkPTJfTVg0ME5qRTJNakl5TW41LU1UVXpNamczTXpNMk9EWTRPWDVaVURoU1IyWm5Xbkp3UVZJeFozRkpMMjlaWW5sRmJsWi1mZyZjcmVhdGVfdGltZT0xNTMyODczMzY4JnJvbGU9cHVibGlzaGVyJm5vbmNlPTE1MzI4NzMzNjguNzMzMjExNzc1Njk2NDQ=";
    private static final String LOG_TAG = VideoCallActivity.class.getSimpleName();
    private static final int RC_SETTINGS_SCREEN_PERM = 123;
    private static final int RC_VIDEO_APP_PERM = 124;

    RecyclerView recycler;
    //  FirebaseDatabase db;
    DatabaseReference dbref;
    ArrayList<ChatMessage> arrayList;
    SessionManager sessionManager;
    Button buttonSend;
    EditText edtMsgBox;
    String name, uid, profile_pic;
    private Toolbar toolbar;

    String fcmkey = "no";

    ImageView back;
    ImageView call;

    ProgressDialog mDialog;
    String combinedEmail="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);



        recycler = (RecyclerView) findViewById(R.id.recyclerChat);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mDialog =Constants.showDialog(this);

        call = myToolbar.findViewById(R.id.call);
        back = myToolbar.findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSessionConnectionData();
            }
        });


        String fontPath = "fonts/oswald_regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(new ParticularChatMessageAdapter(getApplicationContext(), arrayList));
         combinedEmail = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        name = getCapName(name);
        fcmkey = getIntent().getStringExtra("fcm");
        profile_pic = getIntent().getStringExtra("profile_pic");
        //  db = FirebaseObjects.getDbObject();
        TextView chatTitle = myToolbar.findViewById(R.id.chat_title);
        chatTitle.setText(name);
        chatTitle.setTypeface(tf);
        CircleImageView chatImage = myToolbar.findViewById(R.id.chat_image);

        loadImage(this, chatImage, profile_pic);
        uid = getIntent().getStringExtra("uid");
        MyApplication.LastChatUSer=uid;
        //   dbRef2 = db.getReference("PaymentInfo/" + uid );
        //checkUserIdPayment(uid);
        arrayList = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());
        dbref = MyApplication.getFirebaseDatabase().getReference(sessionManager.getDB_PATH() + "/ChatData/" + combinedEmail);


        buttonSend = (Button) findViewById(R.id.buttonSend);
        edtMsgBox = (EditText) findViewById(R.id.edtMsgBox);
        //getFcmOfReceiver();
        edtMsgBox.getText().toString().trim();
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!edtMsgBox.getText().toString().equalsIgnoreCase("")) {
                    String key = dbref.push().getKey();

                    ChatMessage chatMessage = new ChatMessage(sessionManager.getName(), name, sessionManager.getRegID(), uid, edtMsgBox.getText().toString().trim());
                    dbref.child(key).setValue(chatMessage);

                    sendNotification();
                    edtMsgBox.setText("");
                }


            }
        });


        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {
                    arrayList.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        arrayList.add(dataSnapshot1.getValue(ChatMessage.class));

                    }
                    ParticularChatMessageAdapter adapter = new ParticularChatMessageAdapter(getApplicationContext(), arrayList);
                    recycler.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public String getCapName(String name) {
        return new StringBuilder()
                .append(name.substring(0, 1).toUpperCase())
                .append(name.substring(1)).toString();
    }

    public void sendNotification() {

        JSONObject jsonObjec = null;
        String bodydata = edtMsgBox.getText().toString().trim();
        try {


            ArrayList<String> al = new ArrayList<>();
            al.add(fcmkey);
            // al.add("c9IPlDrmaug:APA91bFyh_FjY_c3Dr88kpDx12pW5w_SidlybInldngZ20vCedLnVbkOXikAm_hF_DMblkRhKH9Jcr0x_hDMMBPtFtb4DgQ7wLOFMRTMrq83Um2W3NbKmBBzIlONPnawGYlbNvCNUcwy_grVd9CWWPSu6Kb4rSZ7Pg");
            jsonObjec = new JSONObject();
//Log.d("shri",sessionManager.getFirebaseKey());
            JSONArray jsonArray = new JSONArray(al);
            jsonObjec.put("registration_ids", jsonArray);
            JSONObject jsonObjec2 = new JSONObject();
            jsonObjec2.put("message", bodydata);
            jsonObjec2.put("username", sessionManager.getName());
            jsonObjec2.put("purpose", "Chat");

            //usefull to open particular chat
            jsonObjec2.put("key",combinedEmail);
            jsonObjec2.put("uid",sessionManager.getRegID());
            jsonObjec2.put("name",sessionManager.getName());
            jsonObjec2.put("fcm",sessionManager.getFirebaseKey());
            jsonObjec2.put("profile_pic",sessionManager.getProfilePic());


            jsonObjec.put("data", jsonObjec2);

            jsonObjec.put("time_to_live", 172800);
            jsonObjec.put("priority", "HIGH");


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }


        Log.d("Shri", jsonObjec.toString());

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObjec.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Constants.FCM_SERVER_KEY)
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });

    }

    public static void loadImage(final Activity context, ImageView imageView, String url) {
        if (context == null || context.isDestroyed()) return;

        //placeHolderUrl=R.drawable.ic_user;
        //errorImageUrl=R.drawable.ic_error;
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .placeholder(R.drawable.avatar) //this would be your default image (like default profile or logo etc). it would be loaded at initial time and it will replace with your loaded image once glide successfully load image using url.
                .error(R.drawable.ic_warning)//in case of any glide exception or not able to download then this image will be appear . if you won't mention this error() then nothing to worry placeHolder image would be remain as it is.
                .diskCacheStrategy(DiskCacheStrategy.ALL) //using to load into cache then second time it will load fast.
                .animate(R.anim.fade_in) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .fitCenter()//this method help to fit image into center of your ImageView
                .into(imageView); //pass imageView reference to appear the image.
    }


    public void fetchSessionConnectionData() {
        mDialog.show();
        RequestQueue reqQueue = Volley.newRequestQueue(this);
        reqQueue.add(new JsonObjectRequest(com.android.volley.Request.Method.GET,
                "https://voter360.herokuapp.com/session",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    API_KEY = response.getString("apiKey");
                    SESSION_ID = response.getString("sessionId");
                    TOKEN = response.getString("token");

                    Log.i(LOG_TAG, "API_KEY: " + API_KEY);
                    Log.i(LOG_TAG, "SESSION_ID: " + SESSION_ID);
                    Log.i(LOG_TAG, "TOKEN: " + TOKEN);

                    sendCallNotification(API_KEY, SESSION_ID, TOKEN);
                    mDialog.dismiss();

                    Intent i = new Intent(getApplicationContext(), VideoCallActivity.class);
                    i.putExtra("TOKENS",API_KEY + "@#@" + SESSION_ID + "@#@" + TOKEN);
                    startActivity(i);


                    Toast.makeText(getApplicationContext(),"Call Sent",Toast.LENGTH_LONG).show();

                } catch (JSONException error) {
                    Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Log.e(LOG_TAG, "Web Service error: " + error.getMessage());
            }
        }));
    }


    ///////////Send Call/////

    public void sendCallNotification(String apiKey, String sessionId, String TOKEN) {



        JSONObject jsonObjec = null;
        String bodydata = apiKey + "@#@" + sessionId + "@#@" + TOKEN;
        try {


            ArrayList<String> al = new ArrayList<>();
            al.add(fcmkey);
            jsonObjec = new JSONObject();
            JSONArray jsonArray = new JSONArray(al);
            jsonObjec.put("registration_ids", jsonArray);
            JSONObject jsonObjec2 = new JSONObject();
            jsonObjec2.put("message", bodydata);
            jsonObjec2.put("purpose", "Incoming Video Call");
            jsonObjec.put("data", jsonObjec2);

            jsonObjec.put("time_to_live", 172800);
            jsonObjec.put("priority", "HIGH");


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_SHORT).show();
        }


        Log.d("Shri", jsonObjec.toString());

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObjec.toString());
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", Constants.FCM_SERVER_KEY)
                .url("https://fcm.googleapis.com/fcm/send")
                .post(body)
                .build();
        okhttp3.Call call = client.newCall(request);
        mDialog.dismiss();
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.status=2;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.status=0;
        MyApplication.LastChatUSer="";
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        if(isTaskRoot())
        {
            Intent intent=new Intent(getApplicationContext(), UserListActicity.class);
            startActivity(intent);
            finish();
        }
        else {
          finish();
        }

    }
}

