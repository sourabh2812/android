package kickstarter.payu.com.payukickstarter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kickstarter.payu.com.adapter.KickStartAdapter;
import kickstarter.payu.com.constants.AppConstants;
import kickstarter.payu.com.domain.KickStartBean;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {

    ArrayList<KickStartBean> dataList = null;

    KickStartAdapter adapter = null;
    // UI references
    private ListView listView = null;
    private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.dataListView);

        EditText searchFilter = (EditText) findViewById(R.id.txtFilter);

        new KickStartTask().execute();

        // Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                view.setBackgroundColor(Color.LTGRAY);

                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                intent.putExtra(AppConstants.URL_PARAM, AppConstants.PREPEND_URL + dataList.get(itemIndex).getUrl());

                startActivityForResult(intent, 100);
            }
        });


        searchFilter.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                MainActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private class KickStartTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataList = new ArrayList<>();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Please Wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            BufferedReader reader;
            StringBuffer buffer;
            String res = null;

            try {
                URL url = new URL(AppConstants.MAIN_SERVICE_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(AppConstants.READ_TIMEOUT);
                con.setConnectTimeout(AppConstants.CONNECT_TIMEOUT);
                con.setRequestMethod(AppConstants.REQUEST_TYPE);
                con.setRequestProperty(AppConstants.CONTENT_TYPE, AppConstants.MIME_TYPE);
                int status = con.getResponseCode();
                InputStream inputStream;
                if (status == HttpURLConnection.HTTP_OK) {
                    inputStream = con.getInputStream();
                } else {
                    inputStream = con.getErrorStream();
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                buffer = new StringBuffer();

                String line = reader.readLine();

                while (line != null) {
                    buffer.append(line);
                    line = reader.readLine();
                }

                res = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {

                if (s != null) {

                    JSONArray jsonArray = new JSONArray(s);

                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);

                        KickStartBean bean = new KickStartBean();

                        bean.setSno(jsonObject.getInt("s.no"));
                        bean.setAmtPledged(jsonObject.getInt("amt.pledged"));
                        bean.setBlurb(jsonObject.getString("blurb"));
                        bean.setBy(jsonObject.getString("by"));
                        bean.setCountry(jsonObject.getString("country"));
                        bean.setCurrency(jsonObject.getString("currency"));
                        bean.setEndTime(jsonObject.getString("end.time").split(AppConstants.T_REGEX)[0]);
                        bean.setLocation(jsonObject.getString("location"));
                        bean.setPercentageFunded(jsonObject.getInt("percentage.funded"));
                        bean.setNumBackers(jsonObject.getString("num.backers"));
                        bean.setState(jsonObject.getString("state"));
                        bean.setTitle(jsonObject.getString("title"));
                        bean.setType(jsonObject.getString("type"));
                        bean.setUrl(jsonObject.getString("url"));

                        dataList.add(bean);
                    }
                }

                if (dataList.size() > 0) {
                    dialog.dismiss();
                    adapter = new KickStartAdapter(getApplicationContext(), R.layout.kickstart_view, dataList);

                    listView.setAdapter(adapter);

                } else {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "No Data to display", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}