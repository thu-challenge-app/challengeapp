package thu.change;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
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
        final LinearLayout combinedLayout = findViewById(R.id.combinedLayout);
        final LinearLayout firstLayout = findViewById(R.id.firstLayout);
        final LinearLayout secondLayout = findViewById(R.id.secondLayout);
        final TextView firstText = findViewById(R.id.textView);
        final TextView secondText = findViewById(R.id.textView2);
        final Animation showButton = AnimationUtils.loadAnimation(this, R.anim.show_button);
        final Animation hideButton = AnimationUtils.loadAnimation(this, R.anim.hide_button);
        final Animation showLayout = AnimationUtils.loadAnimation(this, R.anim.show_layout);
        final Animation hideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);

       myAlarm();

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstLayout.getVisibility() == View.VISIBLE && secondLayout.getVisibility() == View.VISIBLE){
                    firstLayout.setVisibility(View.GONE);
                    secondLayout.setVisibility(View.GONE);
                    combinedLayout.setVisibility(View.GONE);
                    firstLayout.startAnimation(hideLayout);
                    secondLayout.startAnimation(hideLayout);
                    combinedLayout.startAnimation(hideLayout);
                    addFab.startAnimation(hideButton);
                    firstFab.setEnabled(false);
                    secondFab.setEnabled(false);
                    firstText.setEnabled(false);
                    secondText.setEnabled(false);

                }
                else{
                    combinedLayout.setVisibility(View.VISIBLE);
                    firstLayout.setVisibility(View.VISIBLE);
                    secondLayout.setVisibility(View.VISIBLE);
                    combinedLayout.startAnimation(showLayout);
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
                hide_FloatingButtons();
                Intent intentcreate = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intentcreate);
            }
        });
        secondFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_FloatingButtons();
                Intent intentchoose = new Intent(MainActivity.this, ChooseActivity.class);
                startActivity(intentchoose);
            }
        });
        firstText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_FloatingButtons();
                Intent intentchoose = new Intent(MainActivity.this, ChooseActivity.class);
                startActivity(intentchoose);
            }
        });
        secondText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_FloatingButtons();
                Intent intentchoose = new Intent(MainActivity.this, ChooseActivity.class);
                startActivity(intentchoose);
            }
        });
    }

    public void hide_FloatingButtons(){
        final LinearLayout firstLayout = findViewById(R.id.firstLayout);
        final LinearLayout secondLayout = findViewById(R.id.secondLayout);
        final Animation hideButton = AnimationUtils.loadAnimation(this, R.anim.hide_button);
        final Animation hideLayout = AnimationUtils.loadAnimation(this, R.anim.hide_layout);
        final FloatingActionButton addFab =(FloatingActionButton) findViewById(R.id.plusButton);

        firstLayout.setVisibility(View.GONE);
        secondLayout.setVisibility(View.GONE);
        firstLayout.startAnimation(hideLayout);
        secondLayout.startAnimation(hideLayout);
        addFab.startAnimation(hideButton);

    }
    @Override
    protected void onPause(){
        super.onPause();
        hide_FloatingButtons();
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
        updateProgressBar();

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
            menu.setHeaderTitle("Auswahlmenü");
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
        DatabaseHelper db = new DatabaseHelper(this);
        //View des hinterlegeten Layout
        LinearLayout pre_layout = (LinearLayout)((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).targetView;
        LinearLayout layout = (LinearLayout)pre_layout.getChildAt(0);
        //Textview mit hinterlegeten Id
        TextView textView = (TextView) layout.getChildAt(1);
        //Challenge Id aus Textview Id generieren
        Integer id = textView.getId();
        //Contextmenue Auswahl
        String[] menuItems = getResources().getStringArray(R.array.click_menu);
        switch (menuItems[menuItemIndex]){
            case "Löschen":
                //Challenge als nicht aktiv setzen
                db.setChallengeActive(id, false);
                updateProgressBar();
                updateMainView();
                return true;

            case "Zurücksetzen":
                db.resetChallenge(id);
                updateProgressBar();
                updateMainView();
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.auswahl:
                hide_FloatingButtons();
                Intent intentauswahl = new Intent(this, ChooseActivity.class);
                startActivity(intentauswahl);
                return true;
            case R.id.erstellen:
                hide_FloatingButtons();
                Intent intenterstellen = new Intent(this, CreateActivity.class);
                startActivity(intenterstellen);
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
        hide_FloatingButtons();
        Intent intentchoose = new Intent(MainActivity.this, EntryActivity.class);
        intentchoose.putExtra("Challenge_id",id);
        startActivity(intentchoose);

    }

    public void onClick_Graphik(View v){
        LinearLayout layout = (LinearLayout) v.getParent();

        //Textview mit hinterlegeten Id
        TextView textView = (TextView) layout.getChildAt(1);
        //Challenge Id aus Textview Id generieren
        Integer id = textView.getId();
        hide_FloatingButtons();
        Intent intentchoose = new Intent(MainActivity.this, GraphicActivity.class);
        intentchoose.putExtra("Challenge_id", id);
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
            Button edit_button = (Button)convertView.findViewById(R.id.button);
            TextView weekly_or_daily = (TextView) convertView.findViewById(R.id.weekly_or_daily);

            // Set the "checked edit icon" if todays value is set
            DatabaseHelper db = new DatabaseHelper(MainActivity.this);
            if (db.isTodaysChallengeValueSet(id))
                edit_button.setForeground(AppCompatResources.getDrawable(MainActivity.this, R.drawable.edit_icon_check));

            if(db.getChallenge(id).getWeekly()){
                weekly_or_daily.setText("wöchentlich");
            }
            else{
                weekly_or_daily.setText("täglich");
            }
            challenge_text.setText(text);
            challenge_text.setId(id);

            return convertView;

        }
    }

    public void  updateMainView(){
        ListView main_ListView = (ListView)findViewById(R.id.Main_ListView);
        DatabaseHelper db = new DatabaseHelper(this);
        //Aktualisierug der Startseite
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

    public void updateProgressBar() {

        DatabaseHelper db = new DatabaseHelper(this);
        List<Challenge> challenges = db.getAllChallenges();
        List<Challenge> challenge_progress = new LinkedList<Challenge>();
        for (int i = 0; i < challenges.size(); i++) {
            //Challenges: Daily + Active
            if (challenges.get(i).getActive() && !challenges.get(i).getWeekly()) {
                challenge_progress.add(challenges.get(i));
            }
        }
        //Progress Bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        float progress;
        progressBar.setProgress(0);
        progressBar.setMax(100);
        for (int index = 0; index < challenge_progress.size(); index++) {
            float value = (float)db.getTodaysChallengeValue(challenge_progress.get(index).getId());
            Integer target = challenge_progress.get(index).getMaximum();
            //Check auf heute eingetragen
            boolean today = db.isTodaysChallengeValueSet(challenge_progress.get(index));
            // Check auf Min/Max
            boolean above = challenge_progress.get(index).getAbove();
            if (above == true && value < target) {
                progress = ((value / target)*100) / challenge_progress.size();
            }
            else if(above == true && value >= target){
                progress = 100/(float)challenge_progress.size();
            }
            else {
                if(value <= target && today == true){
                    progress = 100/(float)challenge_progress.size();
                }
                else if(value > target && value < 2*target && today == true) {
                    progress = (100-((value - target)/target)*100)/challenge_progress.size();
                }
                else{
                    progress = (float)0;
                }
            }
            int int_progress = (int) Math.floor(progress);
            progressBar.setProgress(progressBar.getProgress() + int_progress);
        }
    }

    public void myAlarm() {
        DatabaseHelper db = new DatabaseHelper(this);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        // time comparison
        if(calendar.getTimeInMillis()<System.currentTimeMillis()){calendar.add(Calendar.DAY_OF_YEAR,1);}



        // sending Intent
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        List<Challenge> challenges = db.getAllChallenges();
        boolean notification_is_needed = false;
        for(int i = 0; i<challenges.size();i++){
            if (!db.isTodaysChallengeValueSet(challenges.get(i))){
                notification_is_needed = true;
                break;
            }
        }

        if (notification_is_needed) {
            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

}
