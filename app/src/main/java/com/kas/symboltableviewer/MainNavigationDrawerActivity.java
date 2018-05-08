package com.kas.symboltableviewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainNavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   
   DataBaseHelper dataBaseHelper;
   SQLiteDatabase db;
   
   ArrayAdapter<String> userAdapter;
   private List<String> arrTblNames = new ArrayList<>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.i("___MainA", "onCreate");
      
      setContentView(R.layout.activity_main_navigation_drawer);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
         }
      });*/

      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
      drawer.addDrawerListener(toggle);
      toggle.syncState();

      NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      navigationView.setNavigationItemSelectedListener(this);
   
   
      Log.d("___", "before DBHelper create");
      dataBaseHelper = new DataBaseHelper(this);
      dataBaseHelper.createDataBase();
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      
      ListView plcListView = findViewById(R.id.mainListView);
      
      db = dataBaseHelper.getReadableDatabase();
      arrTblNames = getTablesNames();
      
      
      try {
         userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrTblNames);
      } catch (Exception e) {
         Log.e("___Cursor", "" + e.getMessage());
      }
      plcListView.setAdapter(userAdapter);
      plcListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), SymbolResultActivity.class);//ResultActivity.class
            String pressedPosition = null;
            
            pressedPosition = userAdapter.getItem(position);
            
            intent.putExtra("SELECTED_PLC", pressedPosition);
            Log.i("___pressed", "" + pressedPosition);
            
            startActivity(intent);
            //setDefaultKeyMode(Activity.MODE_PRIVATE);
         }
      });
   }
   
   private List<String> getTablesNames() {
      ArrayList arrTableNames = new ArrayList<String>();
      Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
      
      if (c.moveToFirst()) {
         while (!c.isAfterLast()) {
            String plcName = c.getString(c.getColumnIndex("name"));
            Log.d("___", plcName);
            if (plcName.contains("table")) {
               arrTableNames.add(plcName.replace("table_", "PLC "));
            }
            c.moveToNext();
         }
      }
      return arrTableNames;
   }

   @Override
   public void onBackPressed() {
      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      if (drawer.isDrawerOpen(GravityCompat.START)) {
         drawer.closeDrawer(GravityCompat.START);
      } else {
         super.onBackPressed();
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main_navigation_drawer, menu);
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
   }

   @SuppressWarnings("StatementWithEmptyBody")
   @Override
   public boolean onNavigationItemSelected(MenuItem item) {
      // Handle navigation view item clicks here.
      int id = item.getItemId();

      if (id == R.id.nav_camera) {
         // Handle the camera action
      } else if (id == R.id.nav_gallery) {

      } else if (id == R.id.nav_slideshow) {

      } else if (id == R.id.nav_manage) {

      } else if (id == R.id.nav_share) {

      } else if (id == R.id.nav_send) {

      }

      DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
      drawer.closeDrawer(GravityCompat.START);
      return true;
   }
   
   public static SharedPreferences getSharedPrefs(Context context) {
      return PreferenceManager.getDefaultSharedPreferences(context);
   }
   
}
