package thu.change;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import thu.change.DatabaseHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        DatabaseHelper db = new DatabaseHelper(this);
        Challenge C1 = new Challenge(0,"Fleischkonsum reduzieren",5,false,0,false,true);
        Challenge C2 = new Challenge(0,"Weniger Autofahren",100,false,38,false,true);
        Challenge C3 = new Challenge(0,"Keine Plastiktüten nutzen",2,false,0,false,true);
        db.addChallenge(C1);
        db.addChallenge(C2);
        db.addChallenge(C3);
        ListView mListView = (ListView) findViewById(R.id.ListView);
        List<Challenge> challenges = db.getAllChallenges();


        ChallengeListAdapter adapter = new ChallengeListAdapter(this,R.layout.adapter_view_choose_layout,challenges);
        mListView.setAdapter(adapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choose, menu);
        return true;
    }

    public void onClick_Auswahl(View v){

        LinearLayout layout = (LinearLayout)v.getParent();
        DatabaseHelper db = new DatabaseHelper(this);
        List<Challenge> challenges = db.getAllChallenges();
        CheckBox challenge_checkbox = (CheckBox) layout.getChildAt(0);
        TextView challenge_information = (TextView) layout.getChildAt(1);


        for(int i = 0; i< challenges.size();i++){
            if(challenge_information.getText().toString().contains(challenges.get(i).getName())){
                Challenge challenge = new Challenge();
                challenge = challenges.get(i);
                if(challenge_checkbox.isChecked()) {
                    challenge.setActive(true);
                    db.updateChallenge(challenge);
                }
                else{
                    challenge.setActive(false);
                    db.updateChallenge(challenge);
                }
            }
            else{
                /* do nothing */
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.erstellen:
                Intent intenterstellen = new Intent(this, CreateActivity.class);
                startActivity(intenterstellen);
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

    private class ChallengeListAdapter extends ArrayAdapter<Challenge>{

        private Context mContext;
        private int mResource;

        public ChallengeListAdapter(Context context, int resource, List<Challenge> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String text = getItem(position).getName();
            Boolean check = getItem(position).getActive();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView challenge_text = (TextView) convertView.findViewById(R.id.textView);
            CheckBox challenge_checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);

            challenge_text.setText(text);
            challenge_checkbox.setChecked(check);

            return convertView;

        }
    }

}

