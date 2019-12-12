package thu.change;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionButton addFab =(FloatingActionButton) findViewById(R.id.plusButton);
        FloatingActionButton firstFab = findViewById(R.id.floatingActionButton3);
        FloatingActionButton secondFab = findViewById(R.id.floatingActionButton4);
        final LinearLayout firstLayout = findViewById(R.id.firstLayout);
        final LinearLayout secondLayout = findViewById(R.id.secondLayout);
        final Animation showButton = AnimationUtils.loadAnimation(this, R.anim.show_button);
        final Animation hideButton = AnimationUtils.loadAnimation(this, R.anim.hide_button);
        final Animation showLayout = AnimationUtils.loadAnimation(this, R.anim.show_layout);
        final Animation hideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);


        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstLayout.getVisibility() == View.VISIBLE && secondLayout.getVisibility() == View.VISIBLE){
                    firstLayout.setVisibility(View.GONE);
                    secondLayout.setVisibility(View.GONE);
                    firstLayout.startAnimation(hideLayout);
                    secondLayout.startAnimation(hideLayout);
                    addFab.startAnimation(hideButton);

                }
                else{
                    firstLayout.setVisibility(View.VISIBLE);
                    secondLayout.setVisibility(View.VISIBLE);
                    firstLayout.startAnimation(showLayout);
                    secondLayout.startAnimation(showLayout);
                    addFab.startAnimation(showButton);

                }
            }
        });

        firstFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.GONE);
                firstLayout.startAnimation(hideLayout);
                secondLayout.startAnimation(hideLayout);
                addFab.startAnimation(hideButton);
                Intent intentcreate = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intentcreate);
            }
        });
        secondFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.GONE);
                firstLayout.startAnimation(hideLayout);
                secondLayout.startAnimation(hideLayout);
                addFab.startAnimation(hideButton);
                Intent intentchoose = new Intent(MainActivity.this, ChooseActivity.class);
                startActivity(intentchoose);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseHelper db = new DatabaseHelper(this);

        ListView main_ListView = (ListView)findViewById(R.id.Main_ListView);
        List<Challenge> challenges = db.getAllChallenges();
        List<Challenge> challenge_choosen = new LinkedList<Challenge>();
        for(int i = 0; i<challenges.size();i++){
            if(challenges.get(i).getActive()){
                challenge_choosen.add(challenges.get(i));
            }
        }

        ChallengeActiveListAdapter adapter = new ChallengeActiveListAdapter(this,R.layout.adapter_view_main_challenge_layout,challenge_choosen);
        main_ListView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.auswahl:
                Intent intentauswahl = new Intent(this, ChooseActivity.class);
                startActivity(intentauswahl);
                return true;
            case R.id.erstellen:
                Intent intenterstellen = new Intent(this, CreateActivity.class);
                startActivity(intenterstellen);
                return true;
            case R.id.editieren:
                Toast.makeText(this, "EditierenActivity", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onClick_Eintragen(View v){
        Intent intentchoose = new Intent(MainActivity.this, EntryActivity.class);
        startActivity(intentchoose);
    }

    public void onClick_Graphik(View v){
        Intent intentchoose = new Intent(MainActivity.this, GraphicActivity.class);
        startActivity(intentchoose);
    }

    private class ChallengeActiveListAdapter extends ArrayAdapter<Challenge> {

        private Context mContext;
        private int mResource;

        public ChallengeActiveListAdapter(Context context, int resource, List<Challenge> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mResource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            String text = getItem(position).getName();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView challenge_text = (TextView) convertView.findViewById(R.id.textView3);


            challenge_text.setText(text);

            return convertView;

        }
    }

}
