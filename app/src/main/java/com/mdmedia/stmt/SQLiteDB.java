package com.mdmedia.stmt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arief on 09/10/2015.
 */
public class SQLiteDB extends SQLiteOpenHelper implements JadwalListener {
    public static final String KEY_JAM = "jam";
    public static final String KEY_DAYS = "hari";
    public static final String KEY_DATE = "tanggal";
    public static final String KEY_Ruang = "ruang";
    public static final String KEY_EXAM = "typeExam";
    public static final String KEY_LECTURE = "lecture";
    public static final String KEY_KELAS = "kelas";
    public static final String KEY_KD_MK = "kodemk";
    public static final String KEY_PRODI_ID = "prodiId";
    public static final String KEY_SEMESTER_ID = "id";
    public static final String KEY_SEMESTER_NAME = "semester";
    public static final String KEY_PRODI_NAME = "prodiName";
    public static final String KEY_MK_NAME = "mkname";
    public static final String KEY_TAHUN_AJARAN = "tahunajaran";
    public static final String KEY_SEMESTER = "semester";
    public static final String KEY_FROM_DATE = "fromdate";
    public static final String KEY_TO_DATE = "todate";
    public static final String KEY_TA = "ta";

    public static final String KEY_SMT = "semester";

    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "SQLiteDB.db";

    private static final String DATABASE_TABLE = "samplejadwal";
    private static final String PRODI_TABLE = "prodi";
    private static final String PERIODE_TABLE = "periode";
    private static final String Ujian_TABLE = "jadwalujian";
    private static final String Semester_TABLE = "semester";
    private static final int DATABASE_VERSION = 1;
    private static final String LOGCAT = null;

    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+" ("+KEY_DAYS+" TEXT NOT NULL, "+KEY_MK_NAME+" TEXT NOT NULL,  "+KEY_KD_MK+" TEXT NOT NULL, "+KEY_PRODI_NAME+" TEXT NOT NULL, "+KEY_KELAS+" TEXT NOT NULL, "+KEY_JAM+" TEXT NOT NULL, "+KEY_LECTURE+" TEXT NOT NULL, "+KEY_SMT+" TEXT NOT NULL, "+KEY_TA+" TEXT NOT NULL);";

    private static final String CREATE_UJIAN_TABLE =
            "create table "+Ujian_TABLE+" ("+KEY_DATE+" TEXT NOT NULL, "+KEY_KD_MK+" TEXT NOT NULL, "+KEY_MK_NAME+" TEXT NOT NULL,  "+KEY_PRODI_ID+" TEXT NOT NULL, "+KEY_JAM+" TEXT NOT NULL, "+KEY_LECTURE+" TEXT NOT NULL, "+KEY_SMT+" TEXT NOT NULL, "+KEY_Ruang+" TEXT NOT NULL, "+KEY_EXAM+" TEXT NOT NULL, "+KEY_TA+" TEXT NOT NULL);";
    private static final String PRODI_TABLE_CREATE = "create table "+PRODI_TABLE+" ("+KEY_PRODI_ID+" TEXT PRIMARY KEY, "+KEY_PRODI_NAME+" TEXT NOT NULL)";
    //private static final String SEMESTER_TABLE_CREATE = "create table "+Semester_TABLE+" ("+KEY_SEMESTER_ID+" TEXT PRIMARY KEY, "+KEY_SEMESTER_NAME+" TEXT NOT NULL)";
    private static final String PERIODE_TABLE_CREATE = "create table "+PERIODE_TABLE+" ("+KEY_TAHUN_AJARAN+" TEXT, "+KEY_SEMESTER+" TEXT, "+KEY_FROM_DATE+" DATE, "+KEY_TO_DATE+" DATE)";

    private static final String INSERT_PERIODE = "insert into "+PERIODE_TABLE+" ("+KEY_TAHUN_AJARAN+", "+KEY_SEMESTER+", "+KEY_FROM_DATE+", "+KEY_TO_DATE+") VALUES ('2015', 'ganjil', '2015-09-01', '2016-02-29'), ('2015', 'genap', '2016-03-01', '2016-08-31'), ('2015', 'ganjil pendek', '2016-02-01', '2016-02-29'), ('2015', 'genap pendek', '2016-08-01', '2015-08-31')";
    //private static final String INSERT_SEMESTER_TABLE = "insert into "+Semester_TABLE+" ("+KEY_SEMESTER_ID+", "+KEY_SEMESTER_NAME+") VALUES ('1','ganjil'),('2','genap'),('3','ganjil pendek'), ('4','genap pendek')";
    String DROP_TABLE = "DROP TABLE IF EXISTS "+DATABASE_TABLE;

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(LOGCAT,"Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        db.execSQL(PRODI_TABLE_CREATE);
        db.execSQL(PERIODE_TABLE_CREATE);
        db.execSQL(INSERT_PERIODE);
        db.execSQL(CREATE_UJIAN_TABLE);
        //db.execSQL(SEMESTER_TABLE_CREATE);
        //db.execSQL(INSERT_SEMESTER_TABLE);
        Log.d(LOGCAT, "Database Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void addJadwal(Jadwal jadwal) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_DAYS, jadwal.getTanggal());
            initialValues.put(KEY_JAM, jadwal.getJam());
            initialValues.put(KEY_KELAS, jadwal.getKelas());
            initialValues.put(KEY_LECTURE, jadwal.getLecture());
            initialValues.put(KEY_MK_NAME, jadwal.getMkName());
            initialValues.put(KEY_KD_MK, jadwal.getKodemk());
            initialValues.put(KEY_PRODI_NAME, jadwal.getProdiName());
            initialValues.put(KEY_SMT, jadwal.getSmt());
            initialValues.put(KEY_TA, jadwal.getTahunajaran());
            db.insert(DATABASE_TABLE, null, initialValues);

            ContentValues initialValues2 = new ContentValues();
            initialValues2.put(KEY_PRODI_ID, jadwal.getProdiId());
            initialValues2.put(KEY_PRODI_NAME, jadwal.getProdiName());
            db.insertWithOnConflict(PRODI_TABLE, null, initialValues2, SQLiteDatabase.CONFLICT_REPLACE);

            //Log.d(LOGCAT, "Berhasil Insert "+jadwal.getKodemk());
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }
    @Override
    public void addJadwalUjian(Jadwal jadwal) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues initialValues = new ContentValues();
            db.execSQL("delete from " + Ujian_TABLE + " WHERE " + KEY_DATE + " = '" + jadwal.getTanggal() + "' AND " + KEY_MK_NAME + " = '" + jadwal.getMkName() + "' AND " + KEY_Ruang + " = '" + jadwal.getRuang() + "' AND "
                    + KEY_JAM + " = '" + jadwal.getJam() + "' AND " + KEY_LECTURE + " = '" + jadwal.getLecture() + "' AND " + KEY_SEMESTER + " = '" + jadwal.getSmt() + "' AND " + KEY_EXAM + " = '" + jadwal.getExam() + "'");
            initialValues.put(KEY_DATE, jadwal.getTanggal());
            initialValues.put(KEY_JAM, jadwal.getJam());
            initialValues.put(KEY_Ruang, jadwal.getRuang());
            initialValues.put(KEY_EXAM, jadwal.getExam());
            initialValues.put(KEY_LECTURE, jadwal.getLecture());
            initialValues.put(KEY_MK_NAME, jadwal.getMkName());
            initialValues.put(KEY_KD_MK, jadwal.getKodemk());
            initialValues.put(KEY_PRODI_ID, jadwal.getProdiId());
            initialValues.put(KEY_SMT, jadwal.getSmt());
            initialValues.put(KEY_TA, jadwal.getTahunajaran());
            db.insert(Ujian_TABLE, null, initialValues);

            Log.d(LOGCAT, "Berhasil Insert " + jadwal.getExam());
            /*Log.d(LOGCAT, "delete from " + Ujian_TABLE + " WHERE " + KEY_DATE + " = '" + jadwal.getTanggal() + "' AND " + KEY_MK_NAME + " = '" + jadwal.getMkName() + "' AND " + KEY_Ruang + " = '" + jadwal.getRuang() + "' AND "
                    + KEY_JAM + " = '" + jadwal.getJam() + "' AND " + KEY_LECTURE + " = '" + jadwal.getLecture() + "' AND " + KEY_SEMESTER + " = '" + jadwal.getSmt() + "' AND " + KEY_EXAM + " = '" + jadwal.getExam() + "'");*/
            db.close();
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }

    @Override
    public ArrayList<Jadwal> getAllJadwal(String days, String spinnervalue, String smt, String ta) {
        //public ArrayList<Jadwal> getAllJadwal(String spinnervalue, String settanggal) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Jadwal> jadwalList = null;
        try{

            jadwalList = new ArrayList<Jadwal>();

            //String QUERY = "SELECT * FROM "+DATABASE_TABLE+ " WHERE "+KEY_PRODI_ID+ " = "+spinnervalue+ " AND "+KEY_TANGGAL+ " = '"+settanggal+"'";
            String QUERY = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_DAYS+ " = '"+days+ "' AND "+KEY_PRODI_NAME+" = '"+spinnervalue+"' AND "+KEY_TA+" = '"+ta+"' AND "+KEY_SMT+" = '"+smt+"'";
            Log.d(LOGCAT, "Query select jadwal " + QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Jadwal jadwal = new Jadwal();
                    jadwal.setTanggal(cursor.getString(0));
                    jadwal.setMKName(cursor.getString(1));
                    jadwal.setKodemk(cursor.getString(2));
                    jadwal.setKelas(cursor.getString(4));
                    jadwal.setJam(cursor.getString(5));
                    jadwal.setLecture(cursor.getString(6));
                    jadwal.setSmt(cursor.getString(7));
                    jadwalList.add(jadwal);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return jadwalList;
    }
    @Override
    public ArrayList<Jadwal> getAllJadwalUjian(String tanggal, String spinnervalue, String smt, String ta, String exam) {
        //public ArrayList<Jadwal> getAllJadwal(String spinnervalue, String settanggal) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Jadwal> jadwalList = null;
        try{

            jadwalList = new ArrayList<Jadwal>();

            //String QUERY = "SELECT * FROM "+DATABASE_TABLE+ " WHERE "+KEY_PRODI_ID+ " = "+spinnervalue+ " AND "+KEY_TANGGAL+ " = '"+settanggal+"'";
            String QUERY = "SELECT * FROM "+Ujian_TABLE+" WHERE "+KEY_DATE+" = '"+tanggal+"' AND "+KEY_PRODI_ID+" = '"+spinnervalue+"' AND "+KEY_TA+" = '"+ta+"' AND "+KEY_SMT+" = '"+smt+"' AND "+KEY_EXAM+" = '"+exam+"' ORDER BY `"+KEY_DATE+"` ASC ";
            Log.d(LOGCAT, "Query select jadwal " + QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {
                    Jadwal jadwal = new Jadwal();
                    jadwal.setTanggal(cursor.getString(0));
                    jadwal.setMKName(cursor.getString(2));
                    jadwal.setJam(cursor.getString(4));
                    jadwal.setLecture(cursor.getString(5));
                    jadwal.setSmt(cursor.getString(6));
                    jadwal.setRuang(cursor.getString(7));
                    jadwal.setExam(cursor.getString(8));
                    jadwalList.add(jadwal);
                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return jadwalList;
    }
    public List<String> getAllProdi(){
        List<String> labels = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + PRODI_TABLE + " ORDER BY `"+KEY_PRODI_ID+"` ASC ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return labels;
    }

    public List<String> getPeriodedropdown(String date){
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + PERIODE_TABLE + " WHERE "+KEY_FROM_DATE+ " <= '"+date+"' AND "+KEY_TO_DATE+" >= '"+date+"'";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return labels;
    }
    public List<Jadwal> getProdiId(String prodi){
        List<Jadwal> prodiid = new ArrayList<Jadwal>();

        String selectQuery = "SELECT  * FROM " + PRODI_TABLE +" WHERE "+KEY_PRODI_NAME+" = '"+prodi+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Jadwal prodiList = new Jadwal();
                prodiList.setProdiId(cursor.getString(0));
                prodiid.add(prodiList);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return prodiid;
    }
    public List<Jadwal> getPeriode(String date){
        List<Jadwal> periode = new ArrayList<Jadwal>();

        String selectQuery = "SELECT  * FROM " + PERIODE_TABLE + " WHERE "+KEY_FROM_DATE+ " <= '"+date+"' AND "+KEY_TO_DATE+" >= '"+date+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Jadwal periodelist = new Jadwal();
                periodelist.setTahunajaran(cursor.getString(0));
                periodelist.setSmt2(cursor.getString(1));
                periode.add(periodelist);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return periode;
    }
    public List<Jadwal> getapiprodi(){
        List<Jadwal> periode = new ArrayList<Jadwal>();

        String selectQuery = "SELECT  * FROM " + PRODI_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Jadwal prodiIdList = new Jadwal();
                prodiIdList.setProdiId(cursor.getString(0));
                periode.add(prodiIdList);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return periode;
    }

    public int getJadwalCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+DATABASE_TABLE;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }
    public int getJadwalSmtCount(String smt, String ta) {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_TA+" = '"+ta+"' AND "+KEY_SMT+" = '"+smt+"'";
            Log.d(LOGCAT, "Query " + QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }
    public int getSMTJadwalCount(String days, String spinnervalue, String smt, String ta) {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_DAYS+ " = '"+days+ "' AND "+KEY_TA+" = '"+ta+"' AND "+KEY_SMT+" = '"+smt+"' AND "+KEY_PRODI_NAME+" = '"+spinnervalue+"'";
            Log.d(LOGCAT, "Query " + QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }
    public int getJadwalSmtUjianCount(String tanggal, String smt, String ta, String prodiId, String exam) {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+Ujian_TABLE+" WHERE "+ KEY_DATE+ " = '"+tanggal+ "' AND "+KEY_TA+" = '"+ta+"' AND "+KEY_SMT+" = '"+smt+"' AND "+KEY_PRODI_ID+" = '"+prodiId+"' AND "+KEY_EXAM+" = '"+exam+"'";
            Log.d(LOGCAT, "Query Jadwal Ujian " + QUERY);
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }

}
