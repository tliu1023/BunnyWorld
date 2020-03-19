package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoadAdventure extends AppCompatActivity {
    SQLiteDatabase db;
    String tableName = "games";
    SimpleCursorAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_adventure);
        setTitle("Adventure Selection");
        listView = findViewById(R.id.loadListView);
        showSavings(tableName);
    }

    public void onCreateNew(View view) {
        System.out.println("onCreateNew");
        Intent intent = new Intent(this, EditMain.class);
        intent.putExtra("new", true);
        intent.putExtra("fromLoad", false);
        startActivity(intent);
    }

    public void onLoad(View view){
        System.out.println("onLoad");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);

        ListView listView = findViewById(R.id.loadListView);
        int pos = listView.getCheckedItemPosition();
        if(pos == AdapterView.INVALID_POSITION){
            Toast.makeText(this, "Select an adventure to load.", Toast.LENGTH_SHORT).show();
        } else{
            TextView selectedTextView = (TextView) adapter.getView(pos, null, listView);
            String gameName = selectedTextView.getText().toString();
            String sqlStr = "SELECT shapeDict, pageDict, curPage, isEdit, isSaved from "
                    + tableName
                    + " where name = '"
                    + gameName
                    +"';";
            Cursor tableCursor = db.rawQuery(sqlStr, null);
            String sd = "";
            String pd = "";
            String cp = "";
            boolean ie = false;
            boolean is = false;
            // only take the first string since all the data is saved in the first string
            if(tableCursor.moveToNext()){
                // gameData = tableCursor.getString(0);
                sd = tableCursor.getString(0);
                pd = tableCursor.getString(1);
                cp = tableCursor.getString(2);
                ie = tableCursor.getInt(3) == 1;
                is = tableCursor.getInt(4) == 1;
                Toast.makeText(this, "Load from " + gameName, Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Error in retrieving data", Toast.LENGTH_SHORT).show();
            }
            // open editPage activity
            Intent intent = new Intent(LoadAdventure.this, EditMain.class);
            intent.putExtra("new", false);
            intent.putExtra("fromLoad", true);
            intent.putExtra("shapeDict", sd);
            intent.putExtra("pageDict", pd);
            intent.putExtra("curPage", cp);
            intent.putExtra("isEdit", ie);
            intent.putExtra("isSaved", is);
            startActivity(intent);
        }
        db.close();
    }

    private void showSavings(String tableName){
        System.out.println("showSaving");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String selectTableStr = "SELECT * from " + tableName + ";";
        Cursor tableCursor = db.rawQuery(selectTableStr, null);
        String[] fromArray = {"name"};
        int[] toArray = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1,
                tableCursor, fromArray, toArray, 0);
        ListView listView = findViewById(R.id.loadListView);
        listView.setAdapter(adapter);
        db.close();
    }

    @Override
    protected void onDestroy() {
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        super.onDestroy();
    }

    // reset the Database + refresh list
    public void onReset(View view) {
        System.out.println("onReset");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String resetStr = "DROP TABLE IF EXISTS games;";
        db.execSQL(resetStr);
        resetDatabase();
        refreshList();
        db.close();
    }

    // refresh the listView
    public void refreshList(){
        System.out.println("refreshList");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String selectTable = "SELECT * from " + tableName + ";";
        Cursor tableCursor = db.rawQuery(selectTable, null);
        adapter.changeCursor(tableCursor);
        listView.setItemChecked(-1, true);
        listView.invalidateViews();
        tableCursor.close();
        db.close();
    }

    // delete the created map from list + refresh List.
    public void onDelete(View view){
        System.out.println("onDelete");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        ListView listView = findViewById(R.id.loadListView);
        int pos = listView.getCheckedItemPosition();
        if(pos == AdapterView.INVALID_POSITION){
            Toast.makeText(this,
                    "Please select a record first",
                    Toast.LENGTH_SHORT).show();
        } else{
            // find the selected item and convert to a textView
            TextView selectedTextView = (TextView) adapter.getView(pos, null, listView);
            String gameName;
            gameName = selectedTextView.getText().toString();

            String sqlStr = "DELETE from "
                    + tableName
                    + " where name = '"
                    + gameName
                    +"';";
            try{
                db.execSQL(sqlStr);
                Toast.makeText(this, "Successfully delete " + gameName, Toast.LENGTH_SHORT).show();
                refreshList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        db.close();
    }

    // reset the database
    private void resetDatabase() {
        System.out.println("resetDatabase");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String setupStr = "CREATE TABLE games ("
                + "name TEXT, shapeDict TEXT, pageDict TEXT, curPage TEXT, isEdit INTEGER, isSaved INTEGER, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";
        System.err.println(setupStr);
        db.execSQL(setupStr);
        Toast.makeText(this, "Database Reset.", Toast.LENGTH_SHORT).show();
        db.close();
    }
}




