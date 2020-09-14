package bsl.co.ke.fundsmanagementapi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Creating sqlite database and tables>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            db.execSQL(DataBaseAdapter.CREATE_USER_TABLE);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_PERSONAL_DEPOSITS);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_BUSINESS_DEPOSITS);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_PERSONAL_WITHDRAWALS);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_BUSINESS_WITHDRAWALS);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_PERSONAL_BANK_TRANSSFER);
            db.execSQL(DataBaseAdapter.CREATE_TABLE_BUSINESS_BANK_TRANSSFERS);
        } catch (Exception er) {
            Log.e("Error", "exception");
        }
    }

    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
        System.out.println("Database being upgraded");
        // Log the version upgrade.
        //Drop User Table if exist_db = databaseHelper.getReadableDatabase();
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_USER);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_PERSONAL_DEPOSITS);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_BUSINESS_DEPOSITS);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_PERSONAL_WITHDRAWALS);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_BUSINESS_WITHDRAWALS);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_PERSONAL_BANK_TRANSSFER);
        _db.execSQL("DROP TABLE IF EXISTS " + DataBaseAdapter.TABLE_BUSINESS_BANK_TRANSSFERS);
        // Create tables again
        onCreate(_db);
    }
}