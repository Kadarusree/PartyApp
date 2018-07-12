package sree.myparty.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sree.myparty.R;
import sree.myparty.session.SessionManager;

/**
 * Created by shridhars on 11/1/2017.
 */

class ParticularChatMessageAdapter  extends RecyclerView.Adapter<ParticularChatMessageAdapter.ChatMessageViewHolder> {
    public ParticularChatMessageAdapter(Context applicationContext, ArrayList<ChatMessage> arrayList) {
        context=applicationContext;
        this.arrayList=arrayList;
        sessionManager=new SessionManager(context);
    }

    View view;
    Context context;
    ArrayList<ChatMessage> arrayList;
    SessionManager sessionManager;

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.chatmessage, parent, false);
        return new ChatMessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {

        if(sessionManager.getRegID().equalsIgnoreCase(arrayList.get(position).getSendUID())) {
            setMargins(holder.linchat,holder.tvMessage,holder.datetime,120, 0, 0, 0, Gravity.RIGHT|Gravity.CENTER);
            holder.linchat.setBackgroundResource(R.drawable.chat_me);
        }
        else {
            setMargins(holder.linchat,holder.tvMessage,holder.datetime, 0, 0, 120, 0, Gravity.LEFT|Gravity.CENTER);
            holder.linchat.setBackgroundResource(R.drawable.chat_frd);
        }

        holder.tvMessage.setText(arrayList.get(position).getMessage());
        holder.datetime.setText(arrayList.get(position).getTimestampCreatedLong()+"");

    }

    private void setMargins(View view, TextView textView, TextView tvdatetime, int left, int top, int right, int bottom, int gravity) {




        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,gravity);

        Params.setMargins(left, top, right, bottom);
        textView.setGravity(gravity);
        textView.setLayoutParams(Params);
        tvdatetime.setGravity(gravity);
        tvdatetime.setLayoutParams(Params);
        view.setLayoutParams( Params);


    }
    @Override
    public int getItemCount() {
        if(arrayList!=null)
        {
            return arrayList.size();
        }
        return 0;
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage,datetime;
        LinearLayout linchat;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            linchat=(LinearLayout)itemView.findViewById(R.id.linchat);
        }
    }
}
