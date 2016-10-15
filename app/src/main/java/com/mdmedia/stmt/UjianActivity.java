package com.mdmedia.stmt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.text.format.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mdmedia.stmt.AppController;

public class UjianActivity extends Activity {


    String tahunajaran, prodiujian, semester, prodiujian2, semesterdropdown, tanggal;
    ListView listView;
    UjianAdapter adapter2;
    SQLiteDB handler;
    String settanggal = (String) DateFormat.format("yyyy-MM-dd", new Date());
    String[] typeExam = new String[] {
            "UTS",
            "UAS",
    };
    private static String TAG = UjianActivity.class.getSimpleName();
    RequestQueue requestQueue;
    private int year, day, month;
    static final int DATE_DIALOG_ID = 1111;
    private ProgressDialog pDialog;

    String api_ujian = "http://118.97.136.133/ws/jadwalujian/";
    //private static String api_ujian = "http://stmt.wallmagz.com/jadwal_ujian.php";

    // JSON Node names
    private static final String TAG_JADWAL = "jadwal";
    private static final String TAG_FROM = "sch_from";
    private static final String TAG_TO = "sch_to";
    private static final String TAG_LECTURE = "sch_lecturer";
    private static final String TAG_PRODI_ID = "prodi_id";
    private static final String TAG_PRODI_NAME = "prodi_name";
    private static final String TAG_MK_NAME = "mk_name";
    private static final String TAG_KDMK = "sch_id";
    private static final String TAG_SMT = "sch_smt";

    private static final String TAG_DATE = "sch_date";
    private static final String TAG_RUANG = "sch_ruang";
    private static final String TAG_TYPE_EXAM = "type";

    private MenuItem mSpinnerItem1 = null;
    private MenuItem mSpinnerItem2 = null;
    private MenuItem mSpinnerItem3 = null;
    String exam = "UTS";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ujian);
        listView = (ListView) findViewById(R.id.list);
        handler = new SQLiteDB(this);
        List<Jadwal> periode = handler.getPeriode(settanggal);
        for (Jadwal cn : periode) {
            tahunajaran = cn.getTahunajaran();
        }

        listView.setAdapter(adapter2);

        requestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        final List<String> semester2 = handler.getPeriodedropdown(settanggal);

        final ArrayAdapter<String> dataAdaptersemester = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, semester2);

        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                semesterdropdown = dataAdaptersemester.getItem(itemPosition);

                if (semesterdropdown.equals("ganjil")) {
                    semester = "1";
                } else if (semesterdropdown.equals("genap")) {
                    semester = "2";
                } else if (semesterdropdown.equals("ganjil pendek")) {
                    semester = "3";
                } else if (semesterdropdown.equals("genap pendek")) {
                    semester = "4";
                }

                listView.setAdapter(null);

                return true;
            }
        };

        /** Setting dropdown items and item navigation listener for the actionbar */
        getActionBar().setListNavigationCallbacks(dataAdaptersemester, navigationListener);

        listView.setAdapter(adapter2);
            makeJsonArrayRequest();

    }
    private void makeJsonArrayRequest() {

        showpDialog();
        Log.d("query: ", "> " + api_ujian + "t/" + exam + "/y/" + tahunajaran + "/c/" + semester + "/d/" + prodiujian2);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, api_ujian+"t/"+exam+"/y/"+tahunajaran+"/c/"+semester+"/d/"+prodiujian2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            //jsonResponse = "";
                            JSONArray ja = response.getJSONArray(TAG_JADWAL);

                            for (int i = 0; i < ja.length(); i++) {

                                JSONObject jadwal = ja.getJSONObject(i);

                                String jam = jadwal.getString(TAG_FROM) + " - " + jadwal.getString(TAG_TO);
                                String date = jadwal.getString(TAG_DATE);
                                String ruang = jadwal.getString(TAG_RUANG);
                                String exam = jadwal.getString(TAG_TYPE_EXAM);
                                String lecture = jadwal.getString(TAG_LECTURE);
                                String prodi_id = jadwal.getString(TAG_PRODI_ID);
                                String mkname = jadwal.getString(TAG_MK_NAME);
                                String kdmk = jadwal.getString(TAG_KDMK);
                                String smt = jadwal.getString(TAG_SMT);
                                String ta = tahunajaran;

                                Jadwal jadwal2 = new Jadwal();
                                jadwal2.setTanggal(date);
                                jadwal2.setMKName(mkname);
                                jadwal2.setKodemk(kdmk);
                                jadwal2.setProdiId(prodi_id);
                                jadwal2.setRuang(ruang);
                                jadwal2.setExam(exam);
                                jadwal2.setJam(jam);
                                jadwal2.setLecture(lecture);
                                jadwal2.setSmt(smt);
                                jadwal2.setTahunajaran(ta);
                                handler.addJadwalUjian(jadwal2);// Inserting into DB

                            }

                            if(handler.getJadwalSmtUjianCount(settanggal, semester, tahunajaran, prodiujian2,exam)== 0){
                                Toast.makeText(getApplicationContext(),
                                        "Tidak Ada Jadwal", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ArrayList<Jadwal> jadwalList = handler.getAllJadwalUjian(settanggal, prodiujian2, semester, tahunajaran, exam);
                                adapter2 = new UjianAdapter(UjianActivity.this,jadwalList);

                                listView.setAdapter(adapter2);
                            }

                            //listView.setAdapter(adapter2);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Tidak Ada Jadwal", Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                System.out.println(error.getMessage());
                hidepDialog();
            }
        });

        // Adding request to request queue
        requestQueue.add(jor);
    }
    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            if(!settanggal.equals(format.format(calendar.getTime()))) {
                settanggal = format.format(calendar.getTime());
                final List<String> semester2 = handler.getPeriodedropdown(settanggal);

                final ArrayAdapter<String> dataAdaptersemester = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, semester2);

                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

                        semesterdropdown = dataAdaptersemester.getItem(itemPosition);

                        if (semesterdropdown.equals("ganjil")) {
                            semester = "1";
                        } else if (semesterdropdown.equals("genap")) {
                            semester = "2";
                        } else if (semesterdropdown.equals("ganjil pendek")) {
                            semester = "3";
                        } else if (semesterdropdown.equals("genap pendek")) {
                            semester = "4";
                        }

                        listView.setAdapter(null);
                        if(handler.getJadwalSmtUjianCount(settanggal, semester, tahunajaran, prodiujian2,exam)== 0){
                            makeJsonArrayRequest();
                        }
                        else {
                            if(handler.getJadwalSmtUjianCount(settanggal, semester, tahunajaran, prodiujian2,exam)== 0){
                                Toast.makeText(getApplicationContext(),
                                        "Tidak Ada Jadwal", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ArrayList<Jadwal> jadwalList = handler.getAllJadwalUjian(settanggal, prodiujian2, semester, tahunajaran, exam);
                                adapter2 = new UjianAdapter(UjianActivity.this,jadwalList);

                                listView.setAdapter(adapter2);
                            }
                        }
                        ActionBar actionBar = getActionBar();
                        actionBar.setDisplayShowCustomEnabled(true);
                        actionBar.setCustomView(R.layout.tanggal_actionbar_ujian);
                        TextView tanggal_text = (TextView) actionBar.getCustomView().findViewById(R.id.tanggaltext2);
                        tanggal_text.setText(settanggal);

                        return true;
                    }
                };

                /** Setting dropdown items and item navigation listener for the actionbar */
                getActionBar().setListNavigationCallbacks(dataAdaptersemester, navigationListener);

                listView.setAdapter(adapter2);

            }
        }
    };
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(this, datePickerListener,
                        year, month, day);

            default:
                break;
        }
        return dialog;
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.jadwalujianmenu, menu);
        mSpinnerItem1 = menu.findItem( R.id.typeExam);
        mSpinnerItem2 = menu.findItem(R.id.prodiUjian);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.tanggal_actionbar_ujian);
        TextView tanggal_text = (TextView) actionBar.getCustomView().findViewById(R.id.tanggaltext2);
        tanggal_text.setText(settanggal);
        //mSpinnerItem3 = menu.findItem(R.id.smtujian);
        View view1 = mSpinnerItem1.getActionView();
        View view2 = mSpinnerItem2.getActionView();
        //View view3 = mSpinnerItem3.getActionView();
        if (view1 instanceof Spinner)
        {
            final Spinner spinner = (Spinner) view1;
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, typeExam);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner.setAdapter(spinnerArrayAdapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    exam = typeExam[arg2];
                    listView.setAdapter(null);
                    makeJsonArrayRequest();
                    /*if(handler.getJadwalSmtUjianCount(semester, tahunajaran, prodiujian2,exam)== 0){
                        makeJsonArrayRequest();
                    }
                    else {
                        ArrayList<Jadwal> jadwalList = handler.getAllJadwalUjian(prodiujian2, semester, tahunajaran, exam);
                        adapter2 = new UjianAdapter(UjianActivity.this,jadwalList);

                        listView.setAdapter(adapter2);
                    }*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }
        if (view2 instanceof Spinner) {
            final Spinner spinner = (Spinner) view2;
            List<String> lables = handler.getAllProdi();
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lables);
            /*final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, prodiId);*/
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner.setAdapter(spinnerArrayAdapter);


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub

                    prodiujian = spinnerArrayAdapter.getItem(arg2);
                    List<Jadwal> prodiId = handler.getProdiId(prodiujian);
                    for (Jadwal cn : prodiId) {
                        prodiujian2 = cn.getProdiId();
                    }
                    listView.setAdapter(null);
                    makeJsonArrayRequest();
                    /*if(handler.getJadwalSmtUjianCount(semester, tahunajaran, prodiujian2, exam)== 0){
                        makeJsonArrayRequest();
                    }
                    else {
                        ArrayList<Jadwal> jadwalList = handler.getAllJadwalUjian(prodiujian2, semester, tahunajaran, exam);
                        adapter2 = new UjianAdapter(UjianActivity.this,jadwalList);

                        listView.setAdapter(adapter2);
                    }*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });

        }

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.tanggal:
                showDialog(DATE_DIALOG_ID);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}