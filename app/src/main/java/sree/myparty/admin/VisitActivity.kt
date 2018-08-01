package sree.myparty.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_visit.*
import sree.myparty.MyApplication
import sree.myparty.R
import sree.myparty.R.id.btn_infl_save
import sree.myparty.beans.VisitPojo
import sree.myparty.utils.Constants
import sree.myparty.utils.DailogUtill
import java.text.SimpleDateFormat
import java.util.*

class VisitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)
        edt_visitdate.setInputType(InputType.TYPE_NULL);
        edt_visitdate.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showDateTimePicker()
            }
        })

        edt_visitdate.setOnClickListener(View.OnClickListener { showDateTimePicker() })
        //    database.getReference( Constants.DB_PATH+"/Visits").setValue();
        btn_infl_save.setOnClickListener({

            var areaName: String = edt_area_name.text.trim().toString();
            var vistDate: String = edt_visitdate.text.trim().toString();
            var boothNumber: String = infl_booth_num.text.trim().toString();
            var purposeOfVisit: String = edt_purposeVisit.text.trim().toString();


            if (dataValidation(areaName, vistDate, boothNumber, purposeOfVisit)) {

                val pojo = VisitPojo(areaName, vistDate, boothNumber, purposeOfVisit)


                var database: FirebaseDatabase = MyApplication.getFirebaseDatabase();
                database.getReference(Constants.Vists_Table).push().setValue(pojo).addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful) {
                        //Registration OK
                        edt_area_name.setText("")
                        edt_visitdate.setText("")
                        infl_booth_num.setText("")
                        edt_purposeVisit.setText("")
                        DailogUtill.showDialog("Vister Added",supportFragmentManager,this@VisitActivity)

                    } else {
                        //Registration error
                        DailogUtill.showDialog("Vister Not Added",supportFragmentManager,this@VisitActivity)

                    }
                }


            }


        })


    }


    fun dataValidation(areaName: String, visitDate: String, boothNumber: String, purpose: String): Boolean {
        // var flag : Boolean =false;
        var flag = true;

        if (areaName.isEmpty()) {
            edt_area_name.setError("required area name")
            flag = false;
        }
        if (visitDate.isEmpty()) {
            edt_visitdate.setError("required visit date")
            flag = false;
        }
        if (boothNumber.isEmpty()) {
            infl_booth_num.setError("required booth number")
            flag = false;
        }
        if (purpose.isEmpty()) {
            edt_purposeVisit.setError("required purpose feild")
            flag = false;
        }

        return flag;


    }


    fun showDateTimePicker() {
        val currentDate = Calendar.getInstance()
        val date = Calendar.getInstance()
        DatePickerDialog(this@VisitActivity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            date.set(year, monthOfYear, dayOfMonth)
          /*  TimePickerDialog(this@VisitActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                date.set(Calendar.MINUTE, minute)
                Log.v("TAG", "The choosen one " + date.time)*/
                val format1 = SimpleDateFormat("dd-MM-yyyy")
                edt_visitdate.setText(format1.format(date.time))
                //  meetingDateTime.setEnabled(false);
           /* }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show()*/
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show()
    }


}