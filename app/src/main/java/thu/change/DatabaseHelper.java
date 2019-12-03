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
            "  predefined INTEGER" +
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
        db.execSQL("INSERT INTO challenges VALUES (null, ?, ?, ?, ?, ?, ?)",
                new Object[] {
                        c.getName(),
                        c.getMaximum(),
                        c.getWeekly() ? 1 : 0,
                        c.getAverage(),
                        c.getActive() ? 1 : 0,
                        c.getPredefined() ? 1 : 0
        });
        db.close();
    }

    private Challenge Cursor2Challenge(Cursor cursor) {
        return new Challenge(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getInt(2),
                (cursor.getInt(3) == 1),
                cursor.getInt(4),
                (cursor.getInt(5) == 1),
                (cursor.getInt(6) == 1)
        );
    }

    public Challenge getChallenge(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM challengess WHERE id = ?",
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

    public void updateChallenge(Challenge c) { //insert new DB-Entry
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE challenges " +
                        "SET name = ?, " +
                        "    maximum = ?, " +
                        "    weekly = ?, " +
                        "    average = ?, " +
                        "    active = ?, " +
                        "    predefined = ? " +
                        "WHERE id = ?",
                new Object[]{
                        c.getName(),
                        c.getMaximum(),
                        c.getWeekly() ? 1 : 0,
                        c.getAverage(),
                        c.getActive() ? 1 : 0,
                        c.getPredefined() ? 1 : 0,
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
}
