package com.mdmedia.stmt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arief on 29/09/2015.
 */
public class FormEmail extends Activity implements OnClickListener{
    String emailText, messageText, nameText, nimText, spinnervalue, valid_email;
    Uri URI = null;
    Button send;
    EditText txtname, txtnim, txtemail, txtpesan, txtemailto;
    private ProgressDialog pDialog;

    private static String url_save_saran = "http://stmt.wallmagz.com/kotaksaran/";

    private static final String TAG_SUCCESS = "success";

    JSONParser jsonParser = new JSONParser();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_email);

        txtemailto = (EditText)findViewById(R.id.editTextTo);
        txtname = (EditText)findViewById(R.id.editTextName);
        txtnim = (EditText)findViewById(R.id.editTextNim);
        txtemail = (EditText)findViewById(R.id.editTextemail);
        txtpesan = (EditText)findViewById(R.id.editTextMessage);
        send = (Button)findViewById(R.id.buttonSend);
        final Spinner spinner = (Spinner) findViewById(R.id.kategori);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.kategori, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {
                spinnervalue = spinner.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        send.setOnClickListener(this);
        txtemail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                Is_Valid_Email(txtemail); // pass your EditText Obj here.
            }

            public void Is_Valid_Email(EditText edt) {
                if (edt.getText().toString() == null) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else if (isEmailValid(edt.getText().toString()) == false) {
                    edt.setError("Invalid Email Address");
                    valid_email = null;
                } else {
                    valid_email = edt.getText().toString();
                }
            }

            boolean isEmailValid(CharSequence email) {
                return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches();
            } // end of TextWatcher (email)
        });
    }


    public void onClick(View view) {
        nameText = txtname.getText().toString();
        nimText = txtnim.getText().toString();
        emailText = txtemail.getText().toString();
        messageText = txtpesan.getText().toString();
            if( txtname.getText().toString().trim().equals(""))
               {
                   txtname.setError( "Isi nama terlebih dahulu" );
                    //You can Toast a message here that the Username is Empty
                }
                else if(valid_email == null)
                {
                    txtemail.setError( "Masukan email valid anda" );
                }
                else if(txtpesan.getText().toString().trim().equals(""))
                {
                    txtpesan.setError( "Masukan pesan yang ingin disampaikan" );
                }
                else {
                    new SaveSaran().execute();
                }
        }
    class SaveSaran extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FormEmail.this);
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", nameText));
            params.add(new BasicNameValuePair("nim", nimText));
            params.add(new BasicNameValuePair("email", emailText));
            params.add(new BasicNameValuePair("subject", spinnervalue));
            params.add(new BasicNameValuePair("pesan", messageText));

            JSONObject json = jsonParser.makeHttpRequest(url_save_saran,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), FormEmail.class);
                    startActivity(i);

                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Saran Anda Telah Dikirim, Terima Kasih Atas Kerjasamanya ", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.jadwalujianmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.jadwal:
                Intent Intent= new Intent(FormEmail.this,MainActivity.class);
                Intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
