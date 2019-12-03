package thu.change;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {
    private EditText tChallenge,tMaximum,tDurchschnitt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        tChallenge=(EditText)findViewById(R.id.editText_Challenge);
        tMaximum=(EditText)findViewById(R.id.editText_Maximum);
        tDurchschnitt=(EditText)findViewById(R.id.editText_Durchschnitt);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.auswahl:
                Intent intentauswahl = new Intent(this, ChooseActivity.class);
                startActivity(intentauswahl);
                return true;
            case R.id.editieren:
                Toast.makeText(this, "EditierenActivity", Toast.LENGTH_LONG).show();
                return true;
            case R.id.start:
                Intent intentstart = new Intent(this, MainActivity.class);
                startActivity(intentstart);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //AB HIER
    public void onClick_Wert (View view){
        RadioButton Skala_Wert = (RadioButton) findViewById(R.id.radioButton_Wert);
        if (Skala_Wert.isChecked()){
            tMaximum.setVisibility(View.VISIBLE);
            tDurchschnitt.setVisibility(View.VISIBLE);
            findViewById(R.id.textView_Maximalwert).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_Durchschnitt).setVisibility(View.VISIBLE);
            return;
        } else
        {
            tMaximum.setVisibility(View.INVISIBLE);
            tDurchschnitt.setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_Maximalwert).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_Durchschnitt).setVisibility(View.INVISIBLE);
        }
    }
    public void onClick_Speichern (View view) {
        RadioButton Skala_2 = (RadioButton) findViewById(R.id.radioButton_janein);
        RadioButton Skala_3 = (RadioButton) findViewById(R.id.radioButton_1bis3);
        RadioButton Skala_5 = (RadioButton) findViewById(R.id.radioButton_Skala);
        RadioButton Skala_Wert = (RadioButton) findViewById(R.id.radioButton_Wert);
        Switch sTagWoche = (Switch) findViewById(R.id.switch_TagWoche);

        int DB_Maximum;
        String DB_Challenge="";
        int DB_Durchschnitt = 0;
        int DB_Skala = 2;
        int DB_TagWoche = 1;

        if (view.getId() == R.id.button_Speichern) {


            if (tChallenge.getText().toString().trim().isEmpty()||tChallenge.equals("")) {
                Toast.makeText(this, "Bitte geben Sie einen gültigen Namen ein", Toast.LENGTH_LONG).show();
                return;
            } else {
                DB_Challenge = tChallenge.getText().toString();

                if (tDurchschnitt.getText().toString().isEmpty()) {
                    DB_Durchschnitt = 0;
                } else {
                    DB_Durchschnitt = Integer.parseInt(tDurchschnitt.getText().toString());
                }

                if (tMaximum.getText().toString().isEmpty()) {
                    DB_Maximum = 0;
                } else {
                    DB_Maximum = Integer.parseInt(tMaximum.getText().toString());
                }
                if (tDurchschnitt.getText().toString().isEmpty()) {
                    DB_Durchschnitt = 0;
                }
                if (sTagWoche.isChecked()) {
                    DB_TagWoche = 7;
                } else {
                    DB_TagWoche = 1;
                }
                if (Skala_2.isChecked()) {
                    DB_Skala = 2;
                } else if (Skala_3.isChecked()) {
                    DB_Skala = 3;
                } else if (Skala_5.isChecked()) {
                    DB_Skala = 5;
                } else if (Skala_Wert.isChecked()) {
                    DB_Skala = DB_Maximum;
                    if (DB_Maximum == 0) {
                        DB_Skala = 100;
                    }
                }
            }


            String skala = String.valueOf(DB_Skala);
            String durchschnitt = String.valueOf((DB_Durchschnitt));
            Toast.makeText(this, "Challenge: " + DB_Challenge + " Durchschnitt: " + durchschnitt + " Skala: " + skala + " Doku: " + DB_TagWoche, Toast.LENGTH_LONG).show();
            tChallenge.setText("");
            tDurchschnitt.setText("");
            tMaximum.setText("");
        }


    }



}
