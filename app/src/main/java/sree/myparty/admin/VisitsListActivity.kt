package sree.myparty.admin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View

import butterknife.ButterKnife
import butterknife.OnClick
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_visits_list.*
import sree.myparty.MyApplication
import sree.myparty.R
import sree.myparty.beans.VisitPojo
import sree.myparty.utils.ActivityLauncher
import sree.myparty.utils.Constants

class VisitsListActivity : AppCompatActivity() {

    var visitlistAdapter: VisitListAdapter? = null
  //  var list = ArrayList<VisitPojo>()
  var list: MutableList<VisitPojo> = mutableListOf<VisitPojo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visits_list)
        ButterKnife.bind(this)


        visitList.layoutManager = LinearLayoutManager(this@VisitsListActivity)
        visitlistAdapter = VisitListAdapter(this@VisitsListActivity, list)
        visitList.adapter = visitlistAdapter
        visitlistAdapter!!.notifyDataSetChanged()


                MyApplication.getFirebaseDatabase().getReference(Constants.Vists_Table).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        Log.d("shri",dataSnapshot.childrenCount.toString()+"kkk")

                        for (dataSnapshot1 in dataSnapshot.children) {

                        val  pojo:VisitPojo=dataSnapshot1.getValue(VisitPojo::class.java) as VisitPojo
                            list.add(pojo)
                          Log.d("shri",pojo.AreaName+"--------")
                            visitlistAdapter = VisitListAdapter(this@VisitsListActivity, list)
                            visitList.adapter = visitlistAdapter
                          //  visitlistAdapter!!.notifyDataSetChanged()


                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })








    }

     @OnClick(R.id.btn_add_visit)
        fun launchVisits(view: View) {
            ActivityLauncher.launchVisitActivity(this)
        }
}
