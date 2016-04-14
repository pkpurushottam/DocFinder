package com.example.root.docfinder;



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    ImageView image;
    ListView listView;
    private Spinner catagory;

    class Users {
        String id;
        String name;
        String icon_path;

        Users(String id, String name, String icon_path) {
            this.id = id;
            this.name = name;
            this.icon_path = icon_path;
        }
    }
    private ArrayList<Users> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // image = (ImageView) findViewById(R.id.image);
       // String ur = "http://172.32.10.70/Dsearch/v1/uploadedFile/images (4).jpg";

     //   Glide
       //         .with(getBaseContext())
         //       .load(ur)
           //     .into(image);

        addItemsOncatagory();
        addListenerOnSpinnerItemSelection();

        Button search_btn=(Button)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_val=(EditText)findViewById(R.id.search_val);
                String search_value=search_val.getText().toString();
                //System.out.println(search_value);
                listView=(ListView)findViewById(R.id.listView);
                new LoadAllUsers().execute("http://172.32.10.70/Dsearch/v1/search",search_value);
            }
        });
    }


    // add items into spinner dynamically
    public void addItemsOncatagory() {

        catagory = (Spinner) findViewById(R.id.catagory);
        List<String> list = new ArrayList<String>();
        list.add("catagory");
        list.add("all");
        list.add("pdf formats");
        list.add("document files");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catagory.setAdapter(dataAdapter);
    }


    public void addListenerOnSpinnerItemSelection() {
        catagory = (Spinner) findViewById(R.id.catagory);
      // catagory.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }



    /*


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }   */






    public class LoadAllUsers extends AsyncTask<String, String, String> {
        ProgressDialog loading;

        protected String doInBackground(String... args) {

            try {
                URL url=new URL(args[0]);
                String search_value=args[1];
                //System.out.println("Search:"+search_value);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                JSONObject search_object=new JSONObject();
                search_object.put("search_key",search_value);
                Writer writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(search_object)); // json data
                System.out.println("search1"+String.valueOf(search_object));
                writer.close();


                int responseCode = connection.getResponseCode();
                System.out.println(responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer response=new StringBuffer();
                    String inputLine;
                    while ((inputLine=br.readLine())!=null){
                        response.append(inputLine);
                    }
                    br.close();
                    System.out.println("response"+response.toString());
                    return response.toString();
                }else{
                    return "Errorrrrr";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this,"Please Wait",null,true,true);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String result) {
            loading.dismiss();
            // dismiss the dialog after getting all users
            super.onPostExecute(result);
            if(result!=null){
                users=new ArrayList<>();
                System.out.println(result);
                try {
                    JSONObject jsonRootObject = new JSONObject(result);

//                    int code=jsonRootObject.getInt("code");
//                    System.out.println("jijfs efhdsf dfdf dfhdsif"+code);

                    if(true){
                // System.out.println("jijfs efhdsf dfdf dfhdsif");
//                        JSONObject jsonResponseObject =jsonRootObject.getJSONObject("response");
//                        JSONArray jsonArray = jsonResponseObject.optJSONArray("user_list");
                        JSONArray jsonArray = jsonRootObject.optJSONArray("files");

                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            // System.out.println("User" + i + ": " + jsonObject.getString("user_name"));
                            users.add(new Users(jsonObject.getString("id"),jsonObject.getString("name"),jsonObject.getString("icon_path")));
                        }
                        CustomAdapter customAdapter=new CustomAdapter(MainActivity.this, users);
                        listView.setAdapter(customAdapter);
                    }else{
                        String message=jsonRootObject.getString("message");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
            }
        }
    }
}