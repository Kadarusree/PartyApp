package sree.myparty.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sree.myparty.R;
import sree.myparty.pojos.UserDetailPojo;

class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.USerViewHolder>{

    UserListActicity acticity;
    ArrayList<UserDetailPojo> userDetailPojos;

    public UserListAdapter(UserListActicity acticity, ArrayList<UserDetailPojo> userDetailPojos) {
        this.acticity=acticity;
        this.userDetailPojos=userDetailPojos;
        notifyDataSetChanged();
    }

    @Override
    public USerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(acticity).inflate(R.layout.userlist_adapter,parent,false);

        return new USerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(USerViewHolder holder, int position) {

        holder.tv_user.setText(userDetailPojos.get(position).getUser_name());

    }

    @Override
    public int getItemCount() {
        return userDetailPojos.size();
    }

    class USerViewHolder extends RecyclerView.ViewHolder
    {
            TextView tv_user;

        public USerViewHolder(View itemView) {
            super(itemView);
            tv_user=itemView.findViewById(R.id.tv_user);
        }
    }
}
