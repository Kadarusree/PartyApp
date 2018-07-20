package sree.myparty.utils;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sree.myparty.MyApplication;
import sree.myparty.R;
import sree.myparty.session.SessionManager;

/**
 * Created by srikanthk on 7/13/2018.
 */

public class SelectPC extends DialogFragment {

    @BindView(R.id.spin_states)
    Spinner spin_states;

    @BindView(R.id.spin_pc)
    Spinner spin_pc;

    @BindView(R.id.spin_ac)
    Spinner spn_ac;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    ProgressDialog mPdialog;


    ArrayList<String> states;
    ArrayList<String> list_pc;
    ArrayList<String> list_ac;

    SessionManager mSessionManager ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_pc, container, false);
        ButterKnife.bind(this, view);

        mFirebaseDatabase = MyApplication.getFirebaseDatabase();
        mDatabaseReference = mFirebaseDatabase.getReference();

        list_ac = new ArrayList<>();
        states = new ArrayList<>();
        list_pc = new ArrayList<>();
        mPdialog = Constants.showDialog(getActivity());
        mPdialog.show();
        mSessionManager = new SessionManager(getActivity());
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPdialog.dismiss();
                states.clear();
                String key = "";
               for (DataSnapshot mIndi : dataSnapshot.getChildren()){
                   key=   mIndi.getKey();
                   states.add(key);
               }

                if (states.size()>0){
                    spin_states.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,states));
                    spin_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getPCs(states.get(position));
                            mSessionManager.setState(states.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }


    public void getPCs(final String state){
        mPdialog.show();
        mDatabaseReference.child(state).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPdialog.dismiss();
                list_pc.clear();
                String key = "";
                for (DataSnapshot mIndi : dataSnapshot.getChildren()){
                    key=   mIndi.getKey();
                    list_pc.add(key);
                }

                if (list_pc.size()>0){
                    spin_pc.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,list_pc));
                    spin_pc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getACs(state+"/"+list_pc.get(position));
                            mSessionManager.setPC(list_pc.get(position));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getACs(final String state_pc){
        mPdialog.show();
        mDatabaseReference.child(state_pc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPdialog.dismiss();
                list_ac.clear();
                String key = "";
                for (DataSnapshot mIndi : dataSnapshot.getChildren()){
                    key=   mIndi.getKey();
                    list_ac.add(key);
                }

                if (list_ac.size()>0){
                    spn_ac.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,list_ac));
                    spn_ac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Constants.DB_PATH = state_pc+"/"+list_ac.get(position);
                            mSessionManager.setAC(list_ac.get(position));
                            mSessionManager.setDB_PATH(state_pc+"/"+list_ac.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.btn_ok)
    public void OnButtonClick(View v){

       getDialog().dismiss();

    }
}