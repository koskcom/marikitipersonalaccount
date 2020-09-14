package bsl.co.ke.fundsmanagementapi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bsl.co.ke.fundsmanagementapi.model.BSend;
import bsl.co.ke.fundsmanagementapi.model.BTransfer;
import bsl.co.ke.fundsmanagementapi.model.BWithdrawal;
import bsl.co.ke.fundsmanagementapi.model.DePo;
import bsl.co.ke.fundsmanagementapi.model.PTransfer;
import bsl.co.ke.fundsmanagementapi.model.PWithdrawal;
import bsl.co.ke.fundsmanagementapi.model.Trader;
import bsl.co.ke.fundsmanagementapi.model.User;
import bsl.co.ke.fundsmanagementapi.pojo.BSendPojo;
import bsl.co.ke.fundsmanagementapi.pojo.DManager;

public class DataBaseAdapter {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    //the user table
    // TODO: Create public field for each column in your table.
    // SQL Statement to create a new database.
    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Database open/upgrade helper
    private static DatabaseHelper dbHelper;
    String ok = "OK";
    // Context of the application using the database.
    private Context context = null;

    public DataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //local brand Stocked

    // Method to openthe Database
    public DataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;

    }

    // Method to close the Database
    public void close() {

        db.close();
    }

    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    // User table name
    // Database Version

    // Database Name
    private static final String DATABASE_NAME = "MarikitFundsManager.db";

    public static final String TABLE_USER = "User";
    public static final String TABLE_TRADERS = "Traders";
    public static final String TABLE_PERSONAL_DEPOSITS = "Deposits";
    public static final String TABLE_BUSINESS_DEPOSITS = "B/s_deposits";
    public static final String TABLE_PERSONAL_WITHDRAWALS = "personal_withdrawals";
    public static final String TABLE_BUSINESS_WITHDRAWALS = "business_withdrawals";
    public static final String TABLE_PERSONAL_BANK_TRANSSFER = "personal_bank_transfer";
    public static final String TABLE_BUSINESS_BANK_TRANSSFERS = "business_bank_transfer";

    // User Table Columns names
    private static final String COLUMN_TRANSACTION_ID = "transaction_id";
    private static final String COLUMN_ID = "Id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "trader_name";
    private static final String COLUMN_USER_NUMBER = "trader_number";
    private static final String COLUMN_USER_PIN = "user_Pin";
    private static final String COLUMN_SS_FUNCTION = "function";
    private static final String COLUMN_SS_TYPE_ACCOUNT = "type_of_account";
    private static final String COLUMN_PHONE_NO = "phone_number";
    private static final String COLUMN_RECIPIENT = "recipient";
    private static final String COLUMN_PAYMENT_MODE = "mode_of_payment";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_MARIKITI_PIN = "m_pin";
    private static final String COLUMN_RECIPIENT_NUMBER = "Recipient_number";
    private static final String COLUMN_BANK_NAME = "bank_name";
    private static final String COLUMN_BANK_ACOUNT_NUMBER = "Bank_account_no";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TRADER_ID = "user_id";
    private static final String COLUMN_TRADER_USER_NAME = "trader_name";
    private static final String COLUMN_TRADER_NUMBER = "trader_number";
    private static final String COLUMN_DEPOSITS_ID = "type_deposit";
    private static final String COLUMN_DEPOSITS_TYPE_ID = "type_deposit_ID";
    private static final String COLUMN_AGENT_NUMBER = "agent_no";
    private static final String COLUMN_DEPOSIT_AMOUNT = "dep_amount";
    private static final String COLUMN_DEBIT = "debit";
    // create table sql query
    public static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_NUMBER + " TEXT UNIQUE ,"
            + COLUMN_PHONE_NO + " TEXT,"
            + COLUMN_USER_PIN + " TEXT,"
            + COLUMN_SS_FUNCTION + " TEXT,"
            + COLUMN_SS_TYPE_ACCOUNT + " TEXT,"
            + COLUMN_RECIPIENT + " TEXT,"
            + COLUMN_PAYMENT_MODE + " TEXT,"
            + COLUMN_AMOUNT + " TEXT" + ")";
    // create table sql query


    // create table sql query
    public static final String CREATE_TABLE_PERSONAL_DEPOSITS = "CREATE TABLE " + TABLE_PERSONAL_DEPOSITS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DEPOSITS_TYPE_ID + " TEXT,"
            + COLUMN_AGENT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DEBIT + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_BUSINESS_DEPOSITS = "CREATE TABLE " + TABLE_BUSINESS_DEPOSITS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DEPOSITS_TYPE_ID + " TEXT,"
            + COLUMN_RECIPIENT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DEBIT + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_PERSONAL_WITHDRAWALS = "CREATE TABLE " + TABLE_PERSONAL_WITHDRAWALS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_ID + " TEXT,"
            + COLUMN_TRADER_NUMBER + " TEXT,"
            + COLUMN_AGENT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_BUSINESS_WITHDRAWALS = "CREATE TABLE " + TABLE_BUSINESS_WITHDRAWALS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_ID + " TEXT,"
            + COLUMN_TRADER_NUMBER + " TEXT,"
            + COLUMN_AGENT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_PERSONAL_BANK_TRANSSFER = "CREATE TABLE " + TABLE_PERSONAL_BANK_TRANSSFER + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_ID + " TEXT,"
            + COLUMN_BANK_NAME + " TEXT,"
            + COLUMN_BANK_ACOUNT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_TABLE_BUSINESS_BANK_TRANSSFERS = "CREATE TABLE " + TABLE_BUSINESS_BANK_TRANSSFERS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TRANSACTION_ID + " TEXT,"
            + COLUMN_BANK_NAME + " TEXT,"
            + COLUMN_BANK_ACOUNT_NUMBER + " TEXT,"
            + COLUMN_AMOUNT + " TEXT,"
            + COLUMN_MARIKITI_PIN + " TEXT,"
            + COLUMN_DATE + " TEXT" + ")";

    public static final String CREATE_USER_TABLE_TRADERS = "CREATE TABLE " + TABLE_TRADERS + "("
            + COLUMN_TRADER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TRADER_USER_NAME + " TEXT,"
            + COLUMN_TRADER_NUMBER + " TEXT" + ")";

    public void addUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_NUMBER, user.getIdno());
        values.put(COLUMN_PHONE_NO, user.getPhoneNumber());
        values.put(COLUMN_USER_PIN, user.getPassword());
        values.put(COLUMN_SS_FUNCTION, user.getSelectionfunction());
        values.put(COLUMN_SS_TYPE_ACCOUNT, user.getTypeofaccount());
        values.put(COLUMN_RECIPIENT, user.getRecipient());
        values.put(COLUMN_PAYMENT_MODE, user.getPaymentmode());
        values.put(COLUMN_AMOUNT, user.getAmount());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public void insertpersonaldeposits(DePo dePo) {
        // db.beginTransaction();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEPOSITS_TYPE_ID, dePo.getAgentDeposittype());
        values.put(COLUMN_AGENT_NUMBER, dePo.getAgentNumber());
        values.put(COLUMN_AMOUNT, dePo.getAmount());
        values.put(COLUMN_DEBIT, "Debit");
        // Inserting Row
        long depositsid = db.insertWithOnConflict(TABLE_PERSONAL_DEPOSITS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("depositsid: " + depositsid);
        Log.e("Contact Entered", "DATABASE");
        // db.setTransactionSuccessful();
        // db.endTransaction();
        db.close();
    }

    public void insertpersonalsents(BSend bSend) {
        // db.beginTransaction();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEPOSITS_TYPE_ID, bSend.getPayment_type());
        values.put(COLUMN_RECIPIENT_NUMBER, bSend.getRecipient_number());
        values.put(COLUMN_AMOUNT, bSend.getAmount());
        values.put(COLUMN_MARIKITI_PIN, bSend.getMarikiti_pin());
        values.put(COLUMN_DEBIT, "Debit");
        // Inserting Row
        long depositsid = db.insertWithOnConflict(TABLE_PERSONAL_DEPOSITS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("depositsid: " + depositsid);
        Log.e("Contact Entered", "DATABASE");
        // db.setTransactionSuccessful();
        // db.endTransaction();
        db.close();
    }

    public List<BSendPojo> getPSendList() {

        List<BSendPojo> bSendList = new ArrayList<BSendPojo>();
        BSendPojo td = new BSendPojo();

        String selectQuery = "SELECT  * FROM " + TABLE_PERSONAL_DEPOSITS;

        //System.out.println(selectQuery.toString());
        System.out.println("Picking data from the db<<<<<<<<<<<<<<<<<--------->>>>>>>>>>>>");

        db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (c.moveToFirst()) {
                do {
                    td = new BSendPojo();

                    td.setLocalID((c.getString((c.getColumnIndex(COLUMN_ID)))));
                    td.setPayment_type((c.getString((c.getColumnIndex(COLUMN_DEPOSITS_TYPE_ID)))));
                    td.setRecipient_number((c.getString((c.getColumnIndex(COLUMN_RECIPIENT_NUMBER)))));
                    td.setAmount((c.getString((c.getColumnIndex(COLUMN_AMOUNT)))));
                    td.setMarikiti_pin(c.getString((c.getColumnIndex(COLUMN_MARIKITI_PIN))));
                    td.setStatus(c.getString((c.getColumnIndex(COLUMN_DEBIT))));

                } while (c.moveToNext());
            }
        } finally {
            db.close();
            if (c != null)
                c.close();
            // closeDB();

            // RIGHT: ensure resource is always recovered
        }
        System.out.println("picking list---->>>>>>>" + bSendList.size());
        return bSendList;
    }

    public void insertbusinessdeposits(BSend bSend) {
        // db.beginTransaction();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEPOSITS_TYPE_ID, bSend.getPayment_type());
        values.put(COLUMN_RECIPIENT_NUMBER, bSend.getRecipient_number());
        values.put(COLUMN_AMOUNT, bSend.getAmount());
        values.put(COLUMN_MARIKITI_PIN, bSend.getMarikiti_pin());
        // Inserting Row
        long depositsid = db.insertWithOnConflict(TABLE_BUSINESS_DEPOSITS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("depositsid: " + depositsid);
        Log.e("Contact Entered", "DATABASE");
        // db.setTransactionSuccessful();
        // db.endTransaction();
        db.close();
    }


    public void insertperosonalwithdrawals(PWithdrawal pWithdrawal) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_ID, pWithdrawal.getTrans_id());
        values.put(COLUMN_TRADER_NUMBER, pWithdrawal.getTrader_number());
        values.put(COLUMN_AGENT_NUMBER, pWithdrawal.getAgent_number());
        values.put(COLUMN_AMOUNT, pWithdrawal.getAmount());
        values.put(COLUMN_MARIKITI_PIN, pWithdrawal.getMarikiti_pin());
        values.put(COLUMN_DATE, pWithdrawal.getDate());
        // Inserting Row
        long wid = db.insertWithOnConflict(TABLE_PERSONAL_WITHDRAWALS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("wid: " + wid);
        // db.insert(TABLE_PERSONAL_WITHDRAWALS, null, values);
        db.close();
    }

    public void insertbusinesswithdrawals(BWithdrawal bWithdrawal) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_ID, bWithdrawal.getTrans_id());
        values.put(COLUMN_TRADER_NUMBER, bWithdrawal.getTrader_number());
        values.put(COLUMN_AGENT_NUMBER, bWithdrawal.getAgent_number());
        values.put(COLUMN_AMOUNT, bWithdrawal.getAmount());
        values.put(COLUMN_MARIKITI_PIN, bWithdrawal.getMarikiti_pin());
        values.put(COLUMN_DATE, bWithdrawal.getDate());
        // Inserting Row
        long bswid = db.insertWithOnConflict(TABLE_BUSINESS_WITHDRAWALS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("bswid: " + bswid);
        // db.insert(TABLE_BUSINESS_WITHDRAWALS, null, values);
        db.close();
    }

    public void insertpersonaltransfer(PTransfer PTransfer) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_ID, PTransfer.getTrans_id());
        values.put(COLUMN_BANK_NAME, PTransfer.getBank_number());
        values.put(COLUMN_BANK_ACOUNT_NUMBER, PTransfer.getBank_ac_number());
        values.put(COLUMN_AMOUNT, PTransfer.getAmount());
        values.put(COLUMN_MARIKITI_PIN, PTransfer.getMarikiti_pin());
        values.put(COLUMN_DATE, PTransfer.getDate());
        // Inserting Row
        long transferid = db.insertWithOnConflict(TABLE_PERSONAL_BANK_TRANSSFER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("transferid: " + transferid);

        // db.insert(TABLE_PERSONAL_BANK_TRANSSFER, null, values);
        db.close();
    }

    public void insertBusinesstransfer(BTransfer bTransfer) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_ID, bTransfer.getTrans_id());
        values.put(COLUMN_BANK_NAME, bTransfer.getBank_number());
        values.put(COLUMN_BANK_ACOUNT_NUMBER, bTransfer.getBank_ac_number());
        values.put(COLUMN_AMOUNT, bTransfer.getAmount());
        values.put(COLUMN_MARIKITI_PIN, bTransfer.getMarikiti_pin());
        values.put(COLUMN_DATE, bTransfer.getDate());
        // Inserting Row
        long bstransferid = db.insertWithOnConflict(TABLE_BUSINESS_BANK_TRANSSFERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        //   db.insert(TABLE_PERSONAL_DEPOSITS, null, values);
        System.out.println("bstransferid: " + bstransferid);
        //  db.insert(TABLE_BUSINESS_BANK_TRANSSFERS, null, values);
        db.close();
    }

    public void addtrader(Trader trader) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TRADER_USER_NAME, trader.getName());
        values.put(COLUMN_TRADER_NUMBER, trader.getNumber());

        // Inserting Row
        db.insert(TABLE_TRADERS, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_NUMBER,
                COLUMN_USER_NAME,
                COLUMN_USER_PIN
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        db = dbHelper.getWritableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,id_number,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setIdno(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NUMBER)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PIN)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public List<DManager> getAlltransactions() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_DEPOSITS_ID,
                COLUMN_DEPOSITS_TYPE_ID,
                COLUMN_AGENT_NUMBER,
                COLUMN_AMOUNT
        };

        // sorting orders
        String sortOrder =
                COLUMN_DEPOSITS_ID + " ASC";
        List<DManager> depoList = new ArrayList<DManager>();

        db = dbHelper.getWritableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,id_number,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_PERSONAL_DEPOSITS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DManager depo = new DManager();
                depo.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_DEPOSITS_ID))));

                depo.setAmount(cursor.getString(cursor.getColumnIndex(COLUMN_AMOUNT)));
                // Adding user record to list
                depoList.add(depo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return depoList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_NUMBER, user.getIdno());
        values.put(COLUMN_PHONE_NO, user.getPhoneNumber());
        values.put(COLUMN_USER_PIN, user.getPassword());
        values.put(COLUMN_SS_FUNCTION, user.getSelectionfunction());
        values.put(COLUMN_SS_TYPE_ACCOUNT, user.getTypeofaccount());
        values.put(COLUMN_RECIPIENT, user.getRecipient());
        values.put(COLUMN_PAYMENT_MODE, user.getPaymentmode());
        values.put(COLUMN_AMOUNT, user.getAmount());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        db = dbHelper.getWritableDatabase();

        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String phonenumber) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        db = dbHelper.getWritableDatabase();

        // selection criteria
        String selection = COLUMN_PHONE_NO + " = ?";

        // selection argument
        String[] selectionArgs = {phonenumber};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE id_number = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        db = dbHelper.getWritableDatabase();

        // selection criteria
        String selection = COLUMN_PHONE_NO + " = ?" + " AND " + COLUMN_USER_PIN + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE id_number = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean checkpin(String pin) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        db = dbHelper.getWritableDatabase();

        // selection criteria
        String selection = COLUMN_PHONE_NO + " = ?";

        // selection arguments
        String[] selectionArgs = {pin};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE id_number = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


}
