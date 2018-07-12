package sree.myparty.chat;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sree.myparty.R;
import sree.myparty.pojos.UserDetailPojo;
import sree.myparty.session.SessionManager;

class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.USerViewHolder>{

    UserListActicity acticity;
    ArrayList<UserDetailPojo> arrayList;
    SessionManager sessionManager;

    public UserListAdapter(UserListActicity acticity, ArrayList<UserDetailPojo> userDetailPojos, SessionManager sessionManager) {
        this.acticity=acticity;
        this.arrayList=userDetailPojos;
        this.sessionManager=sessionManager;
        notifyDataSetChanged();
    }

    @Override
    public USerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(acticity).inflate(R.layout.userlist_adapter,parent,false);

        return new USerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(USerViewHolder holder, final int position) {

        holder.tv_user.setText(arrayList.get(position).getUser_name());

        holder.tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email1 = arrayList.get(position).getMobile_number().replace(".","").toLowerCase();
                String email2 = sessionManager.getMobile().replace(".","").toLowerCase();
                String combined;

                if (email1.compareToIgnoreCase(email2) > 0) {
                    combined = email1 + "_" + email2;
                } else {
                    combined = email2 + "_" + email1;
                }
                Intent intent = new Intent(acticity, ParticularChat.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key", combined);
                intent.putExtra("name", arrayList.get(position).getUser_name());
                intent.putExtra("uid", arrayList.get(position).getReg_id());
                acticity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class USerViewHolder extends RecyclerView.ViewHolder
    {
            TextView tv_user;

        public USerViewHolder(View itemView) {
            super(itemView);
            tv_user=itemView.findViewById(R.id.tvUser);
        }
    }
}
