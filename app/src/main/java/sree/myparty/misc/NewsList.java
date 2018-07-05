package sree.myparty.misc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.beans.NewsPojo;
import sree.myparty.utils.Constants;

public class NewsList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH+"/News").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot indi : dataSnapshot.getChildren()){
                             NewsPojo mNewsItem = indi.getValue(NewsPojo.class);
                         }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
