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
    private EditText tChallenge,tMaximum,tDurchschnitt,tEinheit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        tChallenge=(EditText)findViewById(R.id.editText_Challenge);
        tMaximum=(EditText)findViewById(R.id.editText_Maximum);
        tDurchschnitt=(EditText)findViewById(R.id.editText_Durchschnitt);
        tEinheit=(EditText)findViewById(R.id.editText_Einheit);
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

    //sets visibility of additional value items
    public void onClick_Wert (View view){
        RadioButton Skala_Wert = (RadioButton) findViewById(R.id.radioButton_Wert);
        if (Skala_Wert.isChecked()){
            tMaximum.setVisibility(View.VISIBLE);
            tDurchschnitt.setVisibility(View.VISIBLE);
            tEinheit.setVisibility(View.VISIBLE);
            findViewById(R.id.textView_Maximalwert).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_Durchschnitt).setVisibility(View.VISIBLE);
            findViewById(R.id.switch_maxmin).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_abovemax).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_belowmax).setVisibility(View.VISIBLE);
            return;
        } else {
            tMaximum.setVisibility(View.INVISIBLE);
            tDurchschnitt.setVisibility(View.INVISIBLE);
            tEinheit.setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_Maximalwert).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_Durchschnitt).setVisibility(View.INVISIBLE);
            findViewById(R.id.switch_maxmin).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_abovemax).setVisibility(View.INVISIBLE);
            findViewById(R.id.textView_belowmax).setVisibility(View.INVISIBLE);
        }
    }
    public void onClick_Speichern (View view) {
        RadioButton Skala_2 = (RadioButton) findViewById(R.id.radioButton_janein);
        RadioButton Skala_3 = (RadioButton) findViewById(R.id.radioButton_1bis3);
        RadioButton Skala_5 = (RadioButton) findViewById(R.id.radioButton_Skala);
        RadioButton Skala_Wert = (RadioButton) findViewById(R.id.radioButton_Wert);
        Switch sTagWoche = (Switch) findViewById(R.id.switch_TagWoche);
        Switch sbelow_above = (Switch) findViewById(R.id.switch_maxmin);

        if (view.getId() == R.id.button_Speichern) {
            // No further processing if no text is given
            if (tChallenge.getText().toString().trim().isEmpty() || tChallenge.equals("")) {
                Toast.makeText(this, "Bitte geben Sie einen gültigen Namen ein", Toast.LENGTH_LONG).show();
                return;
            }
            if ((tEinheit.getText().toString().trim().isEmpty() || tEinheit.equals(""))&&(Skala_Wert.isChecked())) {
                Toast.makeText(this, "Bitte geben Sie eine Einheit ein", Toast.LENGTH_LONG).show();
                return;
            }

            // Create challenge instance
            Challenge c = new Challenge();

            // Store challenge name
            c.setName(tChallenge.getText().toString());

            // Store unit
            c.setUnit(tEinheit.getText().toString());

            // accomplished if below max value
            boolean above = sbelow_above.isChecked();

            // Store average if text box is filled
            if (!tDurchschnitt.getText().toString().isEmpty()) {
                c.setAverage(Integer.parseInt(tDurchschnitt.getText().toString()));
            }

            c.setWeekly(sTagWoche.isChecked());
            c.setAbove(true);

            if (Skala_2.isChecked()) {
                c.setMaximum(1);
            } else if (Skala_3.isChecked()) {
                c.setMaximum(2);
            } else if (Skala_5.isChecked()) {
                c.setMaximum(4);
            } else if (Skala_Wert.isChecked()) {
                // If text box is not empty, use its value. Use 100 otherwise
                if (!tMaximum.getText().toString().isEmpty()) {
                    c.setMaximum(Integer.parseInt(tMaximum.getText().toString()));
                } else {
                    c.setMaximum(100);
                }
                c.setAbove(above);
            }

            // Save new challenge to database
            DatabaseHelper db = new DatabaseHelper(this);
            db.addChallenge(c);

            String skala = String.valueOf(c.getMaximum());
            String durchschnitt = String.valueOf(c.getAverage());
            String uebermax = String.valueOf(above);
            Toast.makeText(this, "Challenge erfolgreich erstellt", Toast.LENGTH_LONG).show();
            tChallenge.setText("");
            tDurchschnitt.setText("");
            tMaximum.setText("");
            tEinheit.setText("");
            sbelow_above.setChecked(true);
        }
    }
}

