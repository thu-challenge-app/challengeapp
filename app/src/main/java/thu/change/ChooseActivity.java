package thu.change;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import thu.change.DatabaseHelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.LinkedList;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {

    ListView mListView;
    List<Challenge> challenges;
    Context c = (Context) this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        DatabaseHelper db = new DatabaseHelper(this);
        challenges = db.getAllChallenges();
        ChallengeListAdapter adapter;
        EditText search_filter = (EditText) findViewById(R.id.search_filter);
        mListView = (ListView) findViewById(R.id.ListView);



        adapter = new ChallengeListAdapter(this,R.layout.adapter_view_choose_layout,challenges);
        mListView.setAdapter(adapter);

        search_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Challenge> filter = new LinkedList<Challenge>();
                DatabaseHelper db = new DatabaseHelper(c);
                challenges = db.getAllChallenges();

                for(int i = 0;i<challenges.size();i++){
                    if(challenges.get(i).getName().toUpperCase().contains(s.toString().toUpperCase())){
                        filter.add(challenges.get(i));
                    }
                }

                if(filter.isEmpty()){
                    Toast.makeText(c, "Challenge nicht vorhanden", Toast.LENGTH_LONG).show();
                }

                ((ChallengeListAdapter)mListView.getAdapter()).update(filter);



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_choose, menu);
        return true;
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

    private class ChallengeListAdapter extends ArrayAdapter<Challenge> {

        private Context mContext;
        private int mResource;
        private List<Challenge> current_challenges;

        public ChallengeListAdapter(Context context, int resource, List<Challenge> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
            this.current_challenges = objects;


        }

        public void update(List<Challenge> challenges){
            current_challenges = new LinkedList<>();
            current_challenges.addAll(challenges);
            notifyDataSetChanged();

        }



        @Override
        public int getCount() {
            return current_challenges.size();
        }

        @Nullable
        @Override
        public Challenge getItem(int position) {
            return current_challenges.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

            String text = getItem(position).getName();
            Boolean check = getItem(position).getActive();
            int id = getItem(position).getId();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);



            TextView challenge_text = (TextView) convertView.findViewById(R.id.textView);
            CheckBox challenge_checkbox = (CheckBox) convertView.findViewById(R.id.checkBox);
            ImageView challenge_delete = (ImageView) convertView.findViewById(R.id.imageView_delete);

            challenge_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getItem(position).setActive(buttonView.isChecked());
                    DatabaseHelper db = new DatabaseHelper(mContext);
                    db.setChallengeActive(buttonView.getId(), buttonView.isChecked());
                }
            });

            challenge_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout layout = (LinearLayout)v.getParent();
                    DatabaseHelper db = new DatabaseHelper(mContext);
                    CheckBox challenge_checkbox = (CheckBox) layout.getChildAt(0);
                    Challenge c = db.getChallenge(challenge_checkbox.getId());
                    db.deleteChallenge(c);
                    List<Challenge> challenges_list = db.getAllChallenges();
                    update(challenges_list);


                }
            });
            challenge_text.setText(text);
            challenge_checkbox.setChecked(getItem(position).getActive());
            challenge_checkbox.setId(id);

            return convertView;

        }
    }

}

