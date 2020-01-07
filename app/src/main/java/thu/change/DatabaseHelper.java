package thu.change;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        Challenge C1 = new Challenge(0,"Fleischkonsum reduzieren",5,false,0,false, "mal", true);
        Challenge C2 = new Challenge(0,"Weniger Autofahren",100,false,38,false,"km", true);
        Challenge C3 = new Challenge(0,"Keine Plastiktüten nutzen",2,false,0,false,"Stück", true);
        _addChallenge(C1,db);
        _addChallenge(C2,db);
        _addChallenge(C3,db);
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

    public void deleteChallenge(Challenge c){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete challenge itself
        db.execSQL("DELETE FROM challenges WHERE id = ?",
                new Object[]{c.getId()});
        // Delete challenge logs
        db.execSQL("DELETE FROM challengelog WHERE challengeid = ?",
                new Object[]{c.getId()});
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

    public void setTodaysChallengeValue(int id, int value) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM challengelog WHERE challengeid = ? AND time = " + SQL_CURRENT_DATE_FOR_CHALLENGE,
                new String[]{String.valueOf(id), String.valueOf(id)});

        // If the entry exists, get its ID and update the entry
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int logid = cursor.getInt(0);
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
}
