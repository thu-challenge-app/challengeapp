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

                }
                else{
                    firstLayout.setVisibility(View.VISIBLE);
                    secondLayout.setVisibility(View.VISIBLE);
                    firstLayout.startAnimation(showLayout);
                    secondLayout.startAnimation(showLayout);
                    addFab.startAnimation(showButton);
                    firstFab.setEnabled(true);
                    secondFab.setEnabled(true);

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
        //registriert Contextmenue
        registerForContextMenu(main_ListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_start, menu);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        CoordinatorLayout layout = (CoordinatorLayout) v.getParent();
        ListView challengeList = (ListView) layout.getChildAt(0);
        //TextView hello = (TextView) challengeList.getChildAt(0);
        //TextView challengeTextview = (TextView) challengeList.getChildAt(1);
        if(v.getId()== R.id.Main_ListView){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //selectedChallenge = ((TextView) info.targetView).getText().toString();

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
        int selectedChallengeInt = info.position;
        long id = info.id;
        //ListView listview = (ListView)info.targetView.getParent();
        LinearLayout layout = (LinearLayout)((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).targetView;
        TextView textView = (TextView) layout.getChildAt(1);
        String s = textView.getText().toString();
        String buffer = String.format("Lösche %s", id);
        String[] menuItems = getResources().getStringArray(R.array.click_menu);


        switch (menuItems[menuItemIndex]){
            case "Löschen":
                Toast.makeText(this, buffer, Toast.LENGTH_LONG).show();
                return true;
            case "Zurücksetzen":
                Toast.makeText(this, "Setze zurück Challenge", Toast.LENGTH_LONG).show();
                return true;
            default:
                return true;
        }
        //if(menuItems[menuItemIndex].equals("Löschen")){
          //  Toast.makeText(this, "Lösche Challenge", Toast.LENGTH_LONG).show();
        //}
        //else{
          //  Toast.makeText(this, "Setze Challenge zurück", Toast.LENGTH_LONG).show();
        //}


        //return true;
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
