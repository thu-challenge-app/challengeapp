package thu.change;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ChallengeDB";
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "CREATE TABLE challenges (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "  name TEXT, " +
            "  maximum INTEGER, " +
            "  weekly INTEGER, " +
            "  average INTEGER, " +
            "  active INTEGER, " +
            "  unit TEXT, " +
            "  above INTEGER " +
            ");"
        );
        db.execSQL(
            "CREATE TABLE challengelog (" +
            "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "  challengeid INTEGER, " +
            "  time INTEGER, " +
            "  progress INTEGER" +
            ");"
        );
        Challenge C1 = new Challenge(0,"Fleischkonsum reduzieren",50,false,90,true, "Gramm", true);
        Challenge C2 = new Challenge(0,"Weniger Autofahren",20,false,38,true,"km", false);
        Challenge C3 = new Challenge(0,"Keine Plastiktüten nutzen",1,false,0,true,"Stück", false);
        Challenge C4 = new Challenge(0,"Keine Lebensmittel wegwerfen",4,false,1,true,"", false);
        Challenge C5 = new Challenge(0,"Kalt duschen",4,true,0,true,"", false);
        Challenge C6 = new Challenge(0,"Biolebensmittel kaufen",4,false,1,false,"", false);
        Challenge C7 = new Challenge(0,"Keine Kleidung kaufen",2,true,1,false,"", false);
        Challenge C8 = new Challenge(0,"Regional einkaufen",2,false,0,false,"", false);
        Challenge C9 = new Challenge(0,"Werbemails kündigen",4,false,0,false,"", false);
        Challenge C10 = new Challenge(0,"Handynutzung reduzieren",2,false,2,false,"Stunden", false);
        Challenge C11 = new Challenge(0,"Regional einkaufen",4,true,0,false,"", false);
        //Challenge C13 = new Challenge(0,"",2,false,0,true,"", false);
        // Palmölfreie Woche
        // Müll aufsammeln in der Öffentlichkeit
        // Mit Freunden Regional kochen


        _addChallenge(C1,db);
        _addChallenge(C2,db);
        _addChallenge(C3,db);
        _addChallenge(C4,db);
        _addChallenge(C5,db);
        _addChallenge(C6,db);
        _addChallenge(C7,db);
        _addChallenge(C8,db);
        _addChallenge(C9,db);
        _addChallenge(C10,db);
        _addChallenge(C11,db);

        // These are for debug! Comment/Delete before go-live!
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-01 00:00:00'), 4)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-02 00:00:00'), 3)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-03 00:00:00'), 2)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-04 00:00:00'), 0)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-05 00:00:00'), 1)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-06 00:00:00'), 2)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-07 00:00:00'), 3)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-08 00:00:00'), 0)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-09 00:00:00'), 4)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-10 00:00:00'), 5)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-11 00:00:00'), 2)");
        db.execSQL("INSERT INTO challengelog VALUES (null, 1, strftime('%s','2020-01-12 00:00:00'), 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS challenges");
        db.execSQL("DROP TABLE IF EXISTS challengelog");
        // create fresh tables
        this.onCreate(db);
    }
    /**
     * CRUD operations (create "add", read "get", update, delete) challenge +
     * get all challenges + delete all challenges
     */
    public void addChallenge(Challenge c){
        SQLiteDatabase db = this.getWritableDatabase();
        _addChallenge(c,db);
        db.close();

    }
    private void _addChallenge(Challenge c,SQLiteDatabase db){
        db.execSQL("INSERT INTO challenges VALUES (null, ?, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        c.getName(),
                        c.getMaximum(),
                        c.getWeekly() ? 1 : 0,
                        c.getAverage(),
                        c.getActive() ? 1 : 0,
                        c.getUnit(),
                        c.getAbove() ? 1 : 0
                });
    }
    private Challenge Cursor2Challenge(Cursor cursor) {
        return new Challenge(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                (cursor.getInt(3) == 1),
                cursor.getInt(4),
                (cursor.getInt(5) == 1),
                cursor.getString(6),
                (cursor.getInt(7) == 1)
        );
    }

    public Challenge getChallenge(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM challenges WHERE id = ?",
                new String[]{String.valueOf(id)});

        // if we got results get the first one
        if (cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Challenge c = Cursor2Challenge(cursor);
        db.close();
        return c;
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> cs = new LinkedList<Challenge>();
        //build the query
        String query = "SELECT  * FROM challenges";
        // get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        // go over each row, build challenge and add it to list
        if (cursor.moveToFirst()) {
            do {
                cs.add(Cursor2Challenge(cursor));
            } while (cursor.moveToNext());
        }
        // return challenges
        return cs;
    }

    public void setChallengeActive(int id, boolean active) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE challenges " +
                        "SET active = ? " +
                        "WHERE id = ?",
                new Object[]{
                        active ? 1 : 0,
                        id
                }
        );
        db.close();
    }

    public void updateChallenge(Challenge c) { //insert new DB-Entry
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE challenges " +
                        "SET name = ?, " +
                        "    maximum = ?, " +
                        "    weekly = ?, " +
                        "    average = ?, " +
                        "    active = ?, " +
                        "    unit = ?, " +
                        "    above = ? " +
                        "WHERE id = ?",
                new Object[]{
                        c.getName(),
                        c.getMaximum(),
                        c.getWeekly() ? 1 : 0,
                        c.getAverage(),
                        c.getActive() ? 1 : 0,
                        c.getUnit(),
                        c.getAbove() ? 1 : 0,
                        c.getId()
                }
        );
        db.close();
    }

    // Methods for resetting challenge progress
    public void resetChallenge(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete challenge logs
        db.execSQL("DELETE FROM challengelog WHERE challengeid = ?",
                new Object[]{id});
    }
    public void resetChallenge(Challenge c) {
        resetChallenge(c.getId());
    }

    // Methods for deleting challenge (including progress)
    public void deleteChallenge(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete challenge itself
        db.execSQL("DELETE FROM challenges WHERE id = ?",
                new Object[]{id});
        // Delete (reset) challenge log for the deleted challenge
        resetChallenge(id);
    }
    public void deleteChallenge(Challenge c) {
        deleteChallenge(c.getId());
    }

    // Block of SQL code to define the "current" timestamp for this day
    // for the given challenge ID.
    // Daily challenges are normalized to the start of the current day
    // Weekly challenges are normalized to the start of the wednesday of this week
    private final String SQL_CURRENT_DATE_FOR_CHALLENGE =
      "CASE WHEN (SELECT weekly FROM challenges WHERE id = ?) == 1 THEN " +
        "strftime('%s', 'now', 'start of day', 'weekday 0', '-4 days') " +
      "ELSE " +
        "strftime('%s', 'now', 'start of day') " +
      "END ";

    public int getTodaysChallengeValue(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT progress FROM challengelog WHERE challengeid = ? AND time = " + SQL_CURRENT_DATE_FOR_CHALLENGE,
                new String[]{String.valueOf(id), String.valueOf(id)});

        // if we got results get the first one
        if (cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return 0;

        int progress = cursor.getInt(0);

        db.close();
        return progress;
    }
    public int getTodaysChallengeValue(Challenge c) {
        return getTodaysChallengeValue(c.getId());
    }

    // Internal helper. Returns the log id (for table challengelog) for the given challenge id
    private int getTodaysChallengeValueId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM challengelog WHERE challengeid = ? AND time = " + SQL_CURRENT_DATE_FOR_CHALLENGE,
                new String[]{String.valueOf(id), String.valueOf(id)});

        // If the entry exists, return its ID
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        // Else return "0"
        // Note: The ID always starts at "1", so "0" means no entry logged for today!
        return 0;
    }

    // Returns true if the given challenge id has a value set for today
    public boolean isTodaysChallengeValueSet(int id) {
        return (getTodaysChallengeValueId(id) != 0);
    }
    public boolean isTodaysChallengeValueSet(Challenge c) {
        return isTodaysChallengeValueSet(c.getId());
    }

    // Sets todays challenge value for given challenge id
    public void setTodaysChallengeValue(int id, int value) {
        SQLiteDatabase db = this.getReadableDatabase();
        int logid = getTodaysChallengeValueId(id);

        // If the entry exists
        if (logid > 0) {
            db.execSQL("UPDATE challengelog " +
                            "SET progress = ? " +
                            "WHERE id = ?",
                new Object[]{
                        value,
                        logid
                }
            );
        }
        // Otherwise, create the entry
        else {
            db.execSQL("INSERT INTO challengelog " +
                            "VALUES(null, " +
                            "       ?, " +
                            SQL_CURRENT_DATE_FOR_CHALLENGE + ", " +
                            "       ?)",
                    new Object[]{
                            id,
                            id,
                            value
                    }
            );
        }

        db.close();
    }

    public ArrayList<Entry> getChallengeValueListBetween(long starttime, long endtime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT time, progress FROM challengelog WHERE time >= ? AND time <= ? ORDER BY time",
                new String[]{String.valueOf(starttime), String.valueOf(endtime)});

        ArrayList<Entry> values = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                values.add(new Entry(cursor.getInt(0), cursor.getInt(1)));
            } while (cursor.moveToNext());
        }

        return values;
    }
}
