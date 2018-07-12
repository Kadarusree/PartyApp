package sree.myparty.chat;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by shridhars on 11/1/2017.
 */

class ChatMessage {

    public ChatMessage() {
    }



    public ChatMessage(String senderName, String receeverName, String sendUID, String receiverUID, String message) {
        this.senderName = senderName;
        this.receeverName = receeverName;
        this.sendUID = sendUID;
        this.receiverUID = receiverUID;
        this.message = message;
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);


        this.timestampCreated = timestampNow;
        //  System.out.println("----------------"+timestampCreated.get("timestamp"));
    }

    public String senderName;
    public String receeverName;


    public String getSenderName() {
        return senderName;
    }

    public String getReceeverName() {
        return receeverName;
    }

    public String getSendUID() {
        return sendUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public String getMessage() {
        return message;
    }



    public String sendUID;
    public String receiverUID;
    public String message;



    public HashMap<String, Object> getTimestampCreated(){
        return timestampCreated;
    }


    @Exclude
    public String getTimestampCreatedLong(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)timestampCreated.get("timestamp"));
        return formatter.format(calendar.getTime());


        //  return (long)timestampCreated.get("timestamp");
        //  return sfd.format(date);
    }


    HashMap<String, Object> timestampCreated;
}
