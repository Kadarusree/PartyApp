package utthamkumar.myparty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import butterknife.BindView;

public class StateDetailSelection_Activity extends AppCompatActivity {

    @BindView(R.id.spn_state)
    Spinner spn_state;

    @BindView(R.id.spn_pc)
    Spinner spn_pc;

    @BindView(R.id.spn_as)
    Spinner spn_as;

    boolean flag_state, flag_pc, flag_as;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_detail_selection_);


        spn_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag_state) {

                } else {
                    flag_state = true;
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        spn_pc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag_pc) {

                } else {
                    flag_pc = true;
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        spn_as.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag_as) {

                } else {
                    flag_as = true;
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


    }
}
