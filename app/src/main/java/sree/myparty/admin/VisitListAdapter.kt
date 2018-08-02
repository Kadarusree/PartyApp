package sree.myparty.admin

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.chatadapterlaout.view.*
import kotlinx.android.synthetic.main.visit_list_adapter.view.*
import sree.myparty.R
import sree.myparty.beans.VisitPojo

class VisitListAdapter : RecyclerView.Adapter<VisitListAdapter.VisitViewHolder> {

    var visitsListActivitym: VisitsListActivity? = null
    var listDatam: ArrayList<VisitPojo>? = null

    constructor(visitsListActivity: VisitsListActivity, listData: ArrayList<VisitPojo>) {
        this.visitsListActivitym = visitsListActivity;
        this.listDatam = listData;
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: VisitViewHolder, position: Int) {

         holder.tvname!!.setText(listDatam!!.get(position).AreaName)
        holder.visitdate!!.setText(listDatam!!.get(position).VisitDate)
        holder.tvPurpose!!.setText(listDatam!!.get(position).Purpose)
        holder.tvBoothNumber!!.setText(listDatam!!.get(position).BoothNumber)

    }
    // holder.tvname.setText(listData.get(position).AreaName.toString())

    override fun getItemCount(): Int {

        return listDatam!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitViewHolder {

        var inflater = LayoutInflater.from(visitsListActivitym)
        var view = inflater.inflate(R.layout.visit_list_adapter, parent, false)
        return VisitViewHolder(view)
    }


    class VisitViewHolder : RecyclerView.ViewHolder {
        var tvname: TextView?
        var visitdate: TextView?
        var tvBoothNumber: TextView?
        var tvPurpose: TextView?

        constructor(view: View) : super(view) {
            tvname = view.tvName
            visitdate = view.tvVisitdate
            tvBoothNumber = view.tvBoothNumber
            tvPurpose = view.tvPurpose
        }

    }
}