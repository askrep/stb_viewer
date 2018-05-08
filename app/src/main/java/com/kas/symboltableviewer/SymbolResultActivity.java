package com.kas.symboltableviewer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SymbolResultActivity extends AppCompatActivity {
   
   DataBaseHelper databaseHelper;
   SQLiteDatabase myDB;
   Cursor userCursor;
   SimpleCursorAdapter userAdapter;
   private String selectedPlcTable;
   private String plcTitle;
   
   ListView plcListView;
   private TextView userFilter;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_symbol_result);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);
/*
      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
         }
      });*/
      
      plcListView = findViewById(R.id.plcListView);
      userFilter = findViewById(R.id.userFilter);
      
      Bundle extras = getIntent().getExtras();
      plcTitle = extras.getString("SELECTED_PLC");
      selectedPlcTable = plcTitle.replace("PLC ", "table_");
      
      toolbar.setTitle(plcTitle.toString());
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      
      databaseHelper = new DataBaseHelper(this);
      myDB = databaseHelper.getWritableDatabase();
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      
      
      try {
         
         userCursor = databaseHelper.mySelectAllFromTable(myDB, selectedPlcTable);
         
         // определяем, какие столбцы из курсора будут выводиться в ListView
         String[] headers = new String[]{DataBaseHelper.COLUMN_ADDRESS, DataBaseHelper.COLUMN_TAG, DataBaseHelper.COLUMN_COMMENT};
         // Log.d("___", "res 3, headers.length=" + headers.length);
         // создаем адаптер, передаем в него курсор
         
         userAdapter = new SimpleCursorAdapter(this, R.layout.two_line_three_text_list_item, userCursor, headers, new int[]{R.id.address, R.id.tag, R.id.comment}, 0);
         //Log.d("___", "res 3, userAdapter - " + userAdapter.isEmpty());
      } catch (Exception e) {
         Log.e("___ ALM RA onResume", e.getMessage());
      }
      
      plcListView.setAdapter(userAdapter);
      
      
      filteringListView();
   }
   
   
   private void filteringListView() {
      if (userFilter.getText().toString().isEmpty()) {
         
         userFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               userAdapter.getFilter().filter(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) { }
         });
         
         
         //filtering provider
         userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
               if (constraint == null || constraint.length() == 0) {
                  return myDB.rawQuery("select * from " + selectedPlcTable, null);
               } else {
                  return myDB.rawQuery("select * from " + selectedPlcTable + " where " + DataBaseHelper.COLUMN_ADDRESS + " like ?" + " or " + DataBaseHelper.COLUMN_TAG + " like ?" + " or " + DataBaseHelper.COLUMN_COMMENT + " like ?", new String[]{"%" + constraint.toString() + "%", "%" + constraint.toString() + "%", "%" + constraint.toString() + "%"});
               }
            }
         });
         
         plcListView.setAdapter(userAdapter);
      }
   }
  
}
