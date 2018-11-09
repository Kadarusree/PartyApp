package utthamkumar.myparty.utils;

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
import utthamkumar.myparty.MyApplication;
import utthamkumar.myparty.R;
import utthamkumar.myparty.constuecies.Booth;
import utthamkumar.myparty.constuecies.Booths;
import utthamkumar.myparty.constuecies.Country;
import utthamkumar.myparty.constuecies.PC;
import utthamkumar.myparty.constuecies.Parser;
import utthamkumar.myparty.constuecies.State;
import utthamkumar.myparty.session.SessionManager;

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

    @BindView(R.id.spin_booth)
    Spinner spn_booth;


    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    ProgressDialog mPdialog;


    ArrayList<String> states;
    ArrayList<String> list_pc;
    ArrayList<String> list_ac;

    SessionManager mSessionManager;

    Country mCountry;

    public SelectPC(Country mCountry) {
        this.mCountry = mCountry;
    }


    String mState_name, mPc_name, mAc_name;


    ProgressDialog mProgressDialog;
    ArrayList<Booth> mBoothsList;
    ArrayList<String> boothNames;

    ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_pc, container, false);
        ButterKnife.bind(this, view);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        mFirebaseDatabase = MyApplication.getFirebaseDatabase();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mSessionManager = new SessionManager(getActivity());


        list_ac = new ArrayList<>();
        states = new ArrayList<>();
        list_pc = new ArrayList<>();
        boothNames = new ArrayList<>();
        mBoothsList = new ArrayList<>();

        states.clear();
        for (int i = 0; i < mCountry.getStates().size(); i++) {
            states.add(mCountry.getStates().get(i).getName());
        }
        /////////////////////

        spin_states.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, states));
        spin_states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int statePosition, long id) {
                State mState = mCountry.getStates().get(statePosition);
                mState_name = mState.getName();
                mSessionManager.setState(mState_name);
                Constants.STATE = mState_name;

                list_pc.clear();
                for (int i = 0; i < mState.getPcs().size(); i++) {
                    list_pc.add(mState.getPcs().get(i).getpCName());
                }
                spin_pc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list_pc));
                spin_pc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int pCPosition, long id) {
                        list_ac.clear();
                        PC mPC = mCountry.getStates().get(statePosition).getPcs().get(pCPosition);
                        mPc_name = mPC.getpCName();
                        Constants.PARLIMENT_CONST = mPc_name;
                        mSessionManager.setPC(mPc_name);
                        for (int k = 0; k < mPC.getAssemblies().size(); k++) {
                            list_ac.add(mPC.getAssemblies().get(k));
                        }
                        spn_ac.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list_ac));
                        spn_ac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                mAc_name = list_ac.get(pCPosition);
                                Constants.ASSEMBLY_CONST = mAc_name;
                                Constants.DB_PATH = mState_name + "/" + mPc_name + "/" + list_ac.get(position);
                                mSessionManager.setAC(list_ac.get(position));
                                mSessionManager.setDB_PATH(Constants.DB_PATH);

                                getBooths(list_ac.get(position));

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


    public void getPCs(final String state) {
        mPdialog.show();
        mDatabaseReference.child(state).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPdialog.dismiss();
                list_pc.clear();
                String key = "";
                for (DataSnapshot mIndi : dataSnapshot.getChildren()) {
                    key = mIndi.getKey();
                    list_pc.add(key);
                }

                if (list_pc.size() > 0) {
                    spin_pc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list_pc));
                    spin_pc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            getACs(state + "/" + list_pc.get(position));
                            mSessionManager.setPC(list_pc.get(position));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getACs(final String state_pc) {
        mPdialog.show();
        mDatabaseReference.child(state_pc).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPdialog.dismiss();
                list_ac.clear();
                String key = "";
                for (DataSnapshot mIndi : dataSnapshot.getChildren()) {
                    key = mIndi.getKey();
                    list_ac.add(key);
                }

                if (list_ac.size() > 0) {
                    spn_ac.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, list_ac));
                    spn_ac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            Constants.DB_PATH = state_pc + "/" + list_ac.get(position);
                            mSessionManager.setAC(list_ac.get(position));
                            mSessionManager.setDB_PATH(state_pc + "/" + list_ac.get(position));
                            getBooths(list_ac.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @OnClick(R.id.btn_ok)
    public void OnButtonClick(View v) {
        if (boothNames.size() > 0) {
            getDialog().dismiss();
        } else {
            Constants.showToast("This Constituency is not supported", getActivity());
        }

    }


    public void getBooths(final String ac) {
        mProgressDialog = Constants.showDialog(getActivity());
        mProgressDialog.show();
        boothNames.clear();
        mBoothsList.clear();
          /*Booths Booths = mParser.getBooths("Khairatabad");
        DatabaseReference reference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH).child("Booths");

        reference.setValue(Booths);*/

        MyApplication.getFirebaseDatabase()
                .getReference(Constants.DB_PATH + "/Booths/mBooths")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mProgressDialog.dismiss();
                        if (dataSnapshot.getChildrenCount() > 0) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Booth mBooth = snapshot.getValue(Booth.class);
                                mBoothsList.add(mBooth);
                                boothNames.add(mBooth.getBoothNumber() + "-" + mBooth.getName());
                            }
                            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, boothNames);
                            spn_booth.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                            spn_booth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Constants.BOOTH_NUMBER = mBoothsList.get(spn_booth.getSelectedItemPosition()).getBoothNumber();
                                    mSessionManager.setBoothNumber(Constants.BOOTH_NUMBER);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    mProgressDialog.dismiss();
                                }
                            });
                        } else {
                            mProgressDialog.dismiss();
                            Parser mParser = new Parser(getActivity());
                            Booths Booths = mParser.getBooths(ac);

                            if (Booths != null && Booths.getmBooths().size() > 0) {
                                DatabaseReference reference = MyApplication.getFirebaseDatabase().getReference(Constants.DB_PATH).child("Booths");
                                reference.setValue(Booths);
                            } else {
                                Constants.showToast("Booths Not Available", getActivity());
                                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, boothNames);
                                spn_booth.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mProgressDialog.dismiss();
                    }
                });
    }


}
