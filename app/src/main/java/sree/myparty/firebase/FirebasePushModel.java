package sree.myparty.firebase;

import com.rebtel.repackaged.com.google.gson.annotations.Expose;
import com.rebtel.repackaged.com.google.gson.annotations.SerializedName;

import java.util.List;

public class FirebasePushModel {

@SerializedName("data")
@Expose
private Data data;
@SerializedName("registration_ids")
@Expose
private List<String> registrationIds = null;

public Data getData() {
return data;
}

public void setData(Data data) {
this.data = data;
}

public List<String> getRegistrationIds() {
return registrationIds;
}

public void setRegistrationIds(List<String> registrationIds) {
this.registrationIds = registrationIds;
}

}