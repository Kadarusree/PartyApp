package sree.myparty.admin

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.chatadapterlaout.view.*
import sree.myparty.R
import sree.myparty.beans.VisitPojo

class VisitListAdapter: RecyclerView.Adapter<VisitListAdapter.VisitViewHolder> {


    var visitsListActivity:VisitsListActivity?=null
    var listData:MutableList<VisitPojo>

    constructor(visitsListActivity:VisitsListActivity,listData:MutableList<VisitPojo> )
    {
        this.visitsListActivity=visitsListActivity;
        this.listData=listData;
    }


    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {
        try {
            holder.tvname!!.text = listData.get(position).AreaName.toString()
            Log.d("shri","==="+listData.get(position).AreaName.toString())
        }catch (e:KotlinNullPointerException)
        {

        }

       // holder.tvname.setText(listData.get(position).AreaName.toString())

    }

    override fun getItemCount(): Int {
        if(listData.size>0) {
       //     Log.d("shri", "" + listData!!.get(0).BoothNumber)
        }
      return  listData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitViewHolder {

        var inflater = LayoutInflater.from(visitsListActivity)
        var view = inflater.inflate(R.layout.visit_list_adapter,parent,false)
        return  VisitViewHolder(view)
    }


    class VisitViewHolder:RecyclerView.ViewHolder
    {
        var tvname:TextView?=null
        constructor(view:View):super(view)
        {
            tvname=view.tvname
        }

    }
}