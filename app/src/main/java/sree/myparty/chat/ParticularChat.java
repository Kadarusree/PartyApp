package sree.myparty.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.session.SessionManager;
import sree.myparty.utils.Constants;

/**
 * Created by shridhars on 11/1/2017.
 */

public class ParticularChat extends AppCompatActivity {

    RecyclerView recycler;
  //  FirebaseDatabase db;
    DatabaseReference dbref;
    ArrayList<ChatMessage> arrayList;
    SessionManager sessionManager;
    Button buttonSend;
    EditText edtMsgBox;
    String name, uid;
    private Toolbar toolbar;

    String fcmkey = "no";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlayout);
        recycler = (RecyclerView) findViewById(R.id.recyclerChat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(new ParticularChatMessageAdapter(getApplicationContext(), arrayList));
        String combinedEmail = getIntent().getStringExtra("key");
        name = getIntent().getStringExtra("name");
        name = getCapName(name);
      //  db = FirebaseObjects.getDbObject();
        getSupportActionBar().setTitle(name);
        uid = getIntent().getStringExtra("uid");
     //   dbRef2 = db.getReference("PaymentInfo/" + uid );
        //checkUserIdPayment(uid);
        arrayList = new ArrayList<>();
        dbref =  MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/ChatData/" + combinedEmail);

        sessionManager = new SessionManager(getApplicationContext());
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



}

