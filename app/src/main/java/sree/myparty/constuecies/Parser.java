package sree.myparty.constuecies;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by srikanthk on 8/2/2018.
 */

public class Parser {


    public Parser(Activity mActivity) {
        this.mActivity = mActivity;
    }

    Activity mActivity;

    public Parser() {
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mActivity.getAssets().open("statesData");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public Country getConst() {

        String data = loadJSONFromAsset();
        ArrayList<State> mStates = new ArrayList<>();

        try {
            JSONObject mJsonObject = new JSONObject(data);
            JSONObject mStatesObject = mJsonObject.getJSONObject("STATES");
            JSONArray mStatesArray = mStatesObject.getJSONArray("STATE");
            mStatesArray.length();
            State mState;
            for (int i = 0; i < mStatesArray.length(); i++) {
                mState = new State();
                JSONObject mStateObject = mStatesArray.getJSONObject(i);
                mState.setName(mStateObject.getString("STATENAME"));
                JSONArray pcArray = mStateObject.getJSONArray("PC");
                PC mPC = null;
                ArrayList<PC> mPclist = new ArrayList<>();
                for (int j = 0; j < pcArray.length(); j++) {
                    mPC = new PC();
                    JSONObject pCObject = pcArray.getJSONObject(j);
                    mPC.setpCName(pCObject.getString("PCNAME"));
                    mPC.setDistrict(pCObject.getString("DISTRICT"));
                    JSONObject mACsObject = pCObject.getJSONObject("ACS");
                    JSONArray mAcArray = mACsObject.getJSONArray("ACNAME");
                    ArrayList<String> acNames = new ArrayList<>();
                    for (int k = 0; k < mAcArray.length(); k++) {
                        acNames.add(mAcArray.getString(k));
                    }
                    mPC.setAssemblies(acNames);
                    mPclist.add(mPC);
                }
                mState.setPcs(mPclist);
                mStates.add(mState);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  new Country(mStates);
    }
}
