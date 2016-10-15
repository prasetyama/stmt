package com.mdmedia.stmt;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

	private ProgressDialog pDialog;
	RequestQueue requestQueue;
	String spinnervalue2, hari, tahunajaran, smt,smt2, semesterdropdown, prodijadwal, prodijadwal2;
	private static String url = "http://118.97.136.133/ws/api_jadwal.php?";
	Spinner spinner;
	ListView listView;
	JadwalAdapter adapter2;
	SQLiteDB handler;
	String settanggal = (String) DateFormat.format("yyyy-MM-dd", new Date());
	private static String TAG = UjianActivity.class.getSimpleName();
	private static final String LOGCAT = null;
	//String settanggal = "2016-05-05";
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
	private MenuItem mSpinnerItem1 = null;
	private MenuItem mSpinnerItem2 = null;
	static final int DATE_DIALOG_ID = 1111;
	private int year;
	private int month;
	private int day;
	String[] jadwalhari = new String[] {
			"Senin",
			"Selasa",
			"Rabu",
			"Kamis",
			"Jum`at",
			"Sabtu",
			"Minggu"
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		handler = new SQLiteDB(this);
		requestQueue = Volley.newRequestQueue(this);
		spinner = (Spinner) findViewById(R.id.spinnersemester);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		String dayOfTheWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());

		if(dayOfTheWeek.equals("Sunday")){
			hari = "Minggu";
		}
		else if(dayOfTheWeek.equals("Monday")){
			hari = "Senin";
		}
		else if (dayOfTheWeek.equals("Tuesday")){
			hari = "Selasa";
		}
		else if (dayOfTheWeek.equals("Wednesday")){
			hari = "Rabu";
		}
		else if (dayOfTheWeek.equals("Thursday")){
			hari = "Kamis";
		}
		else if (dayOfTheWeek.equals("Friday")){
			hari = "Jum`at";
		}
		else if (dayOfTheWeek.equals("Saturday")){
			hari = "Sabtu";
		}

		List<Jadwal> periode = handler.getPeriode(settanggal);
		for (Jadwal cn : periode) {
			tahunajaran = cn.getTahunajaran();
			smt2 = cn.getSmt2();

			if (smt2.equals("ganjil")) {
				smt = "1";
			} else if (smt2.equals("genap")) {
				smt = "2";
			} else if (smt2.equals("ganjil pendek")) {
				smt = "3";
			} else if (smt2.equals("genap pendek")) {
				smt = "4";
			}
		}
		final List<String> semester = handler.getPeriodedropdown(settanggal);

		final ArrayAdapter<String> dataAdaptersemester = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, semester);

		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {

				semesterdropdown = dataAdaptersemester.getItem(itemPosition);

				if (semesterdropdown.equals("ganjil")) {
					smt = "1";
				} else if (semesterdropdown.equals("genap")) {
					smt = "2";
				} else if (semesterdropdown.equals("ganjil pendek")) {
					smt = "3";
				} else if (semesterdropdown.equals("genap pendek")) {
					smt = "4";
				}

				listView.setAdapter(null);

				return true;
			}
		};

		/** Setting dropdown items and item navigation listener for the actionbar */
		getActionBar().setListNavigationCallbacks(dataAdaptersemester, navigationListener);

		listView.setAdapter(adapter2);

		if(handler.getJadwalSmtCount(smt, tahunajaran)== 0){
			makeJsonArrayRequest();
		}
		else {

				ArrayList<Jadwal> jadwalList = handler.getAllJadwal(hari, prodijadwal, smt, tahunajaran);
				adapter2 = new JadwalAdapter(MainActivity.this, jadwalList);
				listView.setAdapter(adapter2);

		}

	}
	private void makeJsonArrayRequest() {

		showpDialog();
		Log.d(LOGCAT, "API " + url + "p=" + tahunajaran + "&s=" + smt);
		JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url + "p=" + tahunajaran + "&s=" + smt, null,
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

								ArrayList<Jadwal> jadwalList = handler.getAllJadwal(hari, prodijadwal, smt, tahunajaran);
								adapter2 = new JadwalAdapter(MainActivity.this, jadwalList);
								listView.setAdapter(adapter2);


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
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
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
				String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", calendar);

				if(dayOfTheWeek.equals("Sunday")){
					hari = "Minggu";
				}
				else if(dayOfTheWeek.equals("Monday")){
					hari = "Senin";
				}
				else if (dayOfTheWeek.equals("Tuesday")){
					hari = "Selasa";
				}
				else if (dayOfTheWeek.equals("Wednesday")){
					hari = "Rabu";
				}
				else if (dayOfTheWeek.equals("Thursday")){
					hari = "Kamis";
				}
				else if (dayOfTheWeek.equals("Friday")){
					hari = "Jum`at";
				}
				else if (dayOfTheWeek.equals("Saturday")){
					hari = "Sabtu";
				}

				final List<String> semester = handler.getPeriodedropdown(settanggal);

				final ArrayAdapter<String> dataAdaptersemester = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, semester);

				getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {

						semesterdropdown = dataAdaptersemester.getItem(itemPosition);

						if (semesterdropdown.equals("ganjil")) {
							smt = "1";
						} else if (semesterdropdown.equals("genap")) {
							smt = "2";
						} else if (semesterdropdown.equals("ganjil pendek")) {
							smt = "3";
						} else if (semesterdropdown.equals("genap pendek")) {
							smt = "4";
						}

						listView.setAdapter(null);
						ActionBar actionBar = getActionBar();
						actionBar.setDisplayShowCustomEnabled(true);
						actionBar.setCustomView(R.layout.tanggal_actionbar);
						TextView tanggal_text = (TextView) actionBar.getCustomView().findViewById(R.id.tanggaltext);
						tanggal_text.setText(settanggal);

						if(handler.getJadwalSmtCount(smt, tahunajaran)== 0){
							makeJsonArrayRequest();
						}
						else {
							if (handler.getSMTJadwalCount(hari, prodijadwal, smt, tahunajaran) != 0) {

								ArrayList<Jadwal> jadwalList = handler.getAllJadwal(hari, prodijadwal, smt, tahunajaran);
								adapter2 = new JadwalAdapter(MainActivity.this, jadwalList);
								listView.setAdapter(adapter2);
							} else {
								Toast.makeText(getApplicationContext(),
										"Tidak Ada Jadwal", Toast.LENGTH_SHORT).show();

							}
						}

						return true;
					}
				};

				/** Setting dropdown items and item navigation listener for the actionbar */
				getActionBar().setListNavigationCallbacks(dataAdaptersemester, navigationListener);


				listView.setAdapter(null);

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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		mSpinnerItem1 = menu.findItem(R.id.prodijadwal);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.tanggal_actionbar);
		TextView tanggal_text = (TextView) actionBar.getCustomView().findViewById(R.id.tanggaltext);
		tanggal_text.setText(settanggal);
		View view2 = mSpinnerItem1.getActionView();

		if (view2 instanceof Spinner) {
			final Spinner spinner = (Spinner) view2;
			List<String> lables = handler.getAllProdi();
			final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, lables);

			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
			spinner.setAdapter(spinnerArrayAdapter);


			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
					// TODO Auto-generated method stub

					prodijadwal = spinnerArrayAdapter.getItem(arg2);

					listView.setAdapter(null);

					if(handler.getJadwalSmtCount(smt, tahunajaran)== 0){
						makeJsonArrayRequest();
					}
					else {
						if (handler.getSMTJadwalCount(hari, prodijadwal, smt, tahunajaran) != 0) {

							ArrayList<Jadwal> jadwalList = handler.getAllJadwal(hari, prodijadwal, smt, tahunajaran);
							adapter2 = new JadwalAdapter(MainActivity.this, jadwalList);
							listView.setAdapter(adapter2);
						} else {
							Toast.makeText(getApplicationContext(),
									"Tidak Ada Jadwal", Toast.LENGTH_SHORT).show();

						}
					}

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
			case R.id.pesan:
				Intent Intent= new Intent(MainActivity.this,FormEmail.class);
				Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(Intent);
				return true;
			case R.id.tanggal:
				showDialog(DATE_DIALOG_ID);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

}