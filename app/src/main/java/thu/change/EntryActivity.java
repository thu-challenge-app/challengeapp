package thu.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class EntryActivity extends AppCompatActivity {

private Challenge ch;
private int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        TextView tchallenge = (TextView) findViewById(R.id.textView_chosenChallenge);
        TextView tunit = (TextView) findViewById(R.id.textView_unit);

        DatabaseHelper db = new DatabaseHelper(this);

        Intent myIntent = getIntent();
        int id=myIntent.getIntExtra("Challenge_id",0);
        ch = db.getChallenge(id);
        String ch_name = ch.getName();
        int ch_skala = ch.getMaximum();
        tchallenge.setText(ch.getName());
        tunit.setText(ch.getUnit());

        if (ch_skala==2){
            findViewById(R.id.radioGroup_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.radioGroup_5).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_unit).setVisibility(View.INVISIBLE);}
        else if (ch_skala ==3){
            findViewById(R.id.radioGroup_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.radioGroup_5).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_unit).setVisibility(View.INVISIBLE);}
        else if (ch_skala==5) {
            findViewById(R.id.radioGroup_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.radioGroup_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_unit).setVisibility(View.INVISIBLE);}
        else{
                findViewById(R.id.radioGroup_2).setVisibility(View.INVISIBLE);
                findViewById(R.id.radioGroup_3).setVisibility(View.INVISIBLE);
                findViewById(R.id.radioGroup_5).setVisibility(View.INVISIBLE);}


    }
    public void onClick_rate5(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        //int progress;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_51:
                if (checked)
                    progress = 0;
                break;
            case R.id.radioButton_52:
                if (checked)
                    progress = 1;
                break;
            case R.id.radioButton_53:
                if (checked)
                    progress = 2;
                break;
            case R.id.radioButton_54:
                if (checked)
                    progress = 3;
                break;
            case R.id.radioButton_55:
                if (checked)
                    progress = 4;
                break;
        }
    }

    public void onClick_rate2(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        //int progress;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_2no:
                if (checked)
                    progress = 0;
                break;
            case R.id.radioButton_2yes:
                if (checked)
                    progress = 1;
                break;
        }
    }

    public void onClick_rate3(View view){
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        //int progress;
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton_3no:
                if (checked)
                    progress = 0;
                break;
            case R.id.radioButton_3some:
                if (checked)
                    progress = 1;
                break;
            case R.id.radioButton_3yes:
                if (checked)
                    progress = 2;
                break;
        }
    }

    public void onClick_update(View view){
        EditText tprogress = (EditText) findViewById(R.id.editText_progress);
        if (tprogress.getText().toString().isEmpty()){
            progress = 0;
        } else {
            progress = Integer.parseInt(tprogress.getText().toString());
        }

        Intent intentstart = new Intent(this, MainActivity.class);
        startActivity(intentstart);
    }

}
