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
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String selectedChallenge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionButton addFab =(FloatingActionButton) findViewById(R.id.plusButton);
        final FloatingActionButton firstFab = findViewById(R.id.floatingActionButton3);
        final FloatingActionButton secondFab = findViewById(R.id.floatingActionButton4);
        final LinearLayout firstLayout = findViewById(R.id.firstLayout);
        final LinearLayout secondLayout = findViewById(R.id.secondLayout);
        final TextView firstText = findViewById(R.id.textView);
        final TextView secondText = findViewById(R.id.textView2);
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
                    firstFab.setEnabled(false);
                    secondFab.setEnabled(false);
                    firstText.setEnabled(false);
                    secondText.setEnabled(false);

                }
                else{
                    firstLayout.setVisibility(View.VISIBLE);
                    secondLayout.setVisibility(View.VISIBLE);
                    firstLayout.startAnimation(showLayout);
                    secondLayout.startAnimation(showLayout);
                    addFab.startAnimation(showButton);
                    firstFab.setEnabled(true);
                    secondFab.setEnabled(true);
                    firstText.setEnabled(true);
                    secondText.setEnabled(true);

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
        firstText.setOnClickListener(new View.OnClickListener() {
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
        secondText.setOnClickListener(new View.OnClickListener() {
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
        final TextView firstText = findViewById(R.id.textView);
        final TextView secondText = findViewById(R.id.textView2);
        final FloatingActionButton firstFab = findViewById(R.id.floatingActionButton3);
        final FloatingActionButton secondFab = findViewById(R.id.floatingActionButton4);
        firstFab.setEnabled(false);
        secondFab.setEnabled(false);
        firstText.setEnabled(false);
        secondText.setEnabled(false);
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
        //registriert Contextmenue
        registerForContextMenu(main_ListView);

        //Progress Bar
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Integer progress;
        progressBar.setProgress(50);
        progressBar.setMax(100);
        for(int index = 0; index<challenge_choosen.size(); index++){
            Integer buffer = db.getTodaysChallengeValue(challenge_choosen.get(index).getId());
            Integer target = challenge_choosen.get(index).getMaximum();
            // Check auf Min/Max
            boolean max;
            if(max=true){
                progress = (buffer / (target * challenge_choosen.size())) * 100;
            }
            else{
                //Hier für Minimum
                progress = 1;
            }
            progressBar.setProgress(progressBar.getProgress() + progress);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){

        if(v.getId()== R.id.Main_ListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Auswahl Menue");
            String[] menuItems = getResources().getStringArray(R.array.click_menu);
            for(int i = 0; i < menuItems.length; i++){
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        //View des hinterlegeten Layout
        LinearLayout layout = (LinearLayout)((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).targetView;
        //Textview mit hinterlegeten Id
        TextView textView = (TextView) layout.getChildAt(1);
        //Challenge Id aus Textview Id generieren
        Integer id = textView.getId();
        //Contextmenue Auswahl
        String[] menuItems = getResources().getStringArray(R.array.click_menu);

        switch (menuItems[menuItemIndex]){
            case "Löschen":
                DatabaseHelper db = new DatabaseHelper(this);
                //Challenge als nicht aktiv setzen
                db.setChallengeActive(id, false);
                //Aktualisierug der Startseite
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
                return true;

            case "Zurücksetzen":
                Toast.makeText(this, "Setze zurück Challenge", Toast.LENGTH_LONG).show();
                return true;
            default:
                return true;
        }
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

        LinearLayout layout = (LinearLayout) v.getParent();

        //Textview mit hinterlegeten Id
        TextView textView = (TextView) layout.getChildAt(1);
        //Challenge Id aus Textview Id generieren
        Integer id = textView.getId();

        Intent intentchoose = new Intent(MainActivity.this, EntryActivity.class);
        intentchoose.putExtra("Challenge_id",id);
        startActivity(intentchoose);

    }

    public void onClick_Graphik(View v){
        LinearLayout layout = (LinearLayout) v.getParent();
        Intent intentchoose = new Intent(MainActivity.this, GraphicActivity.class);
        intentchoose.putExtra("Challenge_id",layout.getChildAt(0).getId());

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
            int id = getItem(position).getId();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);

            TextView challenge_text = (TextView) convertView.findViewById(R.id.textView3);



            challenge_text.setText(text);
            challenge_text.setId(id);

            return convertView;

        }
    }

}
