package pjm.tlcn.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pjm.tlcn.R;

public class Edit_profile extends AppCompatActivity {
private Button btn_edit_profile_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Create Variable
        btn_edit_profile_cancel = (Button) findViewById(R.id.btn_edit_profile_cancel);

        //Start Set Onclick btn_edit_profile_cancel
        btn_edit_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //End Set Onclick btn_edit_profile_cancel
    }
}
