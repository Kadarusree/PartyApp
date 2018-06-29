package sree.myparty.firebase;

import com.rebtel.repackaged.com.google.gson.annotations.Expose;
import com.rebtel.repackaged.com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("key")
@Expose
private String key;

public String getKey() {
return key;
}

public void setKey(String key) {
this.key = key;
}

}