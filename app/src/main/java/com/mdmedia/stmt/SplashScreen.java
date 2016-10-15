package com.mdmedia.stmt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by arief on 07/10/2015.
 */
public class SplashScreen extends Activity {
    SQLiteDB handler;
    String tahunajaran, smt,  kodesemester;
    String settanggal = (String) DateFormat.format("yyyy-MM-dd", new Date());
    //String settanggal = "2016-05-05";
    private ProgressDialog pDialog;
    ArrayList<Jadwal> jadwalLists;
    RequestQueue requestQueue;
    static final int tampil_error=1;
    private static String url = "http://118.97.136.133/ws/api_jadwal.php?";
    private static String TAG = UjianActivity.class.getSimpleName();
    private static final String LOGCAT = null;

    // JSON Node names
    private static final String TAG_JADWAL = "jadwal";
    private static final String TAG_DAYS = "sch_days";
    private static final String TAG_FROM = "sch_from";
    private static final String TAG_TO = "sch_to";
    private static final String TAG_LECTURE = "sch_lecturer";
    private static final String TAG_CLASS = "sch_KodeKelas";
    private static final String TAG_PRODI_ID = "prodi_id";
    private static final String TAG_PRODI_NAME = "prodi_name";
    private static final String TAG_MK_NAME = "mk_name";
    private static final String TAG_KDMK = "sch_id";
    private static final String TAG_SMT = "sch_smt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        handler = new SQLiteDB(this);
        requestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);



        List<Jadwal> periode = handler.getPeriode(settanggal);
        for (Jadwal cn : periode) {
            tahunajaran = cn.getTahunajaran();
            smt = cn.getSmt2();

            if (smt.equals("ganjil")) {
                kodesemester = "1";
            } else if (smt.equals("genap")) {
                kodesemester = "2";
            } else if (smt.equals("ganjil pendek")) {
                kodesemester = "3";
            } else if (smt.equals("genap pendek")) {
                kodesemester = "4";
            }
        }

        if (handler.getJadwalCount() == 0) {
            makeJsonArrayRequest();

        } else {
            Thread timerThread = new Thread() {
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                }
            };
            timerThread.start();
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case tampil_error:
                AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                errorDialog.setTitle("Koneksi Gagal");
                errorDialog.setMessage("Silahkan nyalakan koneksi internet anda");
                errorDialog.setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent exit = new Intent(Intent.ACTION_MAIN);
                                exit.addCategory(Intent.CATEGORY_HOME);
                                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            }
                        });

                AlertDialog errorAlert = errorDialog.create();
                return errorAlert;
            default:
                break;
        }
        return dialog;
    }
    private boolean cek_status(Context cek) {
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void makeJsonArrayRequest() {

        showpDialog();
        Log.d("query: ", "> " + url + "p=" + tahunajaran + "&s=" + kodesemester);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url+"p="+tahunajaran+"&s="+kodesemester, null,
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
                                String days = jadwal.getString(TAG_DAYS);
                                String kdmk = jadwal.getString(TAG_KDMK);
                                String lecture = jadwal.getString(TAG_LECTURE);
                                String kelas = jadwal.getString(TAG_CLASS);
                                String prodi_id = jadwal.getString(TAG_PRODI_ID);
                                String prodiName = jadwal.getString(TAG_PRODI_NAME);
                                String mkname = jadwal.getString(TAG_MK_NAME);
                                String smt = jadwal.getString(TAG_SMT);
                                String ta = tahunajaran;

                                Jadwal jadwal2 = new Jadwal();
                                jadwal2.setKodemk(kdmk);
                                jadwal2.setTanggal(days);
                                jadwal2.setMKName(mkname);
                                jadwal2.setProdiId(prodi_id);
                                jadwal2.setProdiName(prodiName);
                                jadwal2.setKelas(kelas);
                                jadwal2.setJam(jam);
                                jadwal2.setLecture(lecture);
                                jadwal2.setSmt(smt);
                                jadwal2.setTahunajaran(ta);
                                handler.addJadwal(jadwal2);// Inserting into DB

                            }
                            hidepDialog();
                            Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(i);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                /*Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();*/
                System.out.println(error.getMessage());
                hidepDialog();
                Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });

        // Adding request to request queue
        requestQueue.add(jor);
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
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
