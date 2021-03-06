package sree.myparty.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import sree.myparty.R;
import sree.myparty.pojos.VoterPojo;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx) {
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.map_info_window, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView details_tv = view.findViewById(R.id.details);
        TextView hotel_tv = view.findViewById(R.id.hotels);
        TextView food_tv = view.findViewById(R.id.food);
        TextView transport_tv = view.findViewById(R.id.transport);
        TextView fatherName = view.findViewById(R.id.father_name);
        TextView age = view.findViewById(R.id.age);
        TextView caste = view.findViewById(R.id.caste);
        TextView addedby = view.findViewById(R.id.addedby);
        TextView call = view.findViewById(R.id.cilckTomakeCall);
        TextView nextVote = view.findViewById(R.id.nextVote);



        name_tv.setText(marker.getTitle());

        final VoterPojo infoWindowData = (VoterPojo) marker.getTag();

        if (infoWindowData!=null){
            details_tv.setText("ADDRESS : " + infoWindowData.getAddress());
            age.setText("AGE : " + infoWindowData.getAge());
            caste.setText("CASTE : " + infoWindowData.getCatageory() + " - " + infoWindowData.getCaste());
            fatherName.setText("FATHER NAME : " + infoWindowData.getVoterFatherName());
            hotel_tv.setText("VOTER ID : " + infoWindowData.getVoterID());
            food_tv.setText("BOOTH NUMBER : " + infoWindowData.getBoothNumber());
            transport_tv.setText("PHONE  NO : " + infoWindowData.getMobileNumber());
            addedby.setText("ADDED BY : " + infoWindowData.getAdded_by());
            nextVote.setText("Vote : "+infoWindowData.getNextVote());
            if (infoWindowData.getNextVote().equalsIgnoreCase("Congress")){
                transport_tv.setTextColor(Color.GREEN);
            }
            else {
                transport_tv.setTextColor(Color.RED);
            }
        }

        return view;
    }


    private void dialContactPhone(final String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }
}