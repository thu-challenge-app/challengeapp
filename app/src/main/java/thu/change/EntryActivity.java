package thu.change;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class EntryActivity extends AppCompatActivity {
private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        TextView challenge = (TextView) findViewById(R.id.textView_Challengee);
        DatabaseHelper db = new DatabaseHelper(this);
        Intent myIntent = getIntent();
        id=myIntent.getIntExtra("Challenge_id",0);
        int idtest = 1;
        Challenge ch = db.getChallenge(idtest);
        String ch_name = ch.getName();
        int ch_skala = ch.getMaximum();
        //Toast.makeText(this, ch_skala, Toast.LENGTH_LONG).show();
        challenge.setText(ch.getName());

        if (ch_skala==2){
            findViewById(R.id.RadioGroup_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.RadioGroup_5).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);}
        else if (ch_skala ==3){
            findViewById(R.id.RadioGroup_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.RadioGroup_5).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);}
        else if (ch_skala==5) {
            findViewById(R.id.RadioGroup_2).setVisibility(View.INVISIBLE);
            findViewById(R.id.RadioGroup_3).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_progress).setVisibility(View.INVISIBLE);
            findViewById(R.id.editText_progress).setVisibility(View.INVISIBLE);
        }
        else{
                findViewById(R.id.RadioGroup_2).setVisibility(View.INVISIBLE);
                findViewById(R.id.RadioGroup_3).setVisibility(View.INVISIBLE);
                findViewById(R.id.RadioGroup_5).setVisibility(View.INVISIBLE);
            }


    }




    public void onClick_update(View view){
        Intent intentstart = new Intent(this, MainActivity.class);
        startActivity(intentstart);
    }

}
