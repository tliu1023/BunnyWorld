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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_adventure);
        setTitle("Adventure Selection");
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        listView = findViewById(R.id.loadListView);
        showSavings(tableName);
    }

    public void onCreateNew(View view) {
        Intent intent = new Intent(this, EditMain.class);
        intent.putExtra("new", true);
        intent.putExtra("fromLoad", false);
        startActivity(intent);
    }


    public void onLoad(View view){
        ListView listView = findViewById(R.id.loadListView);
        int pos = listView.getCheckedItemPosition();
        if(pos == AdapterView.INVALID_POSITION){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please select an adventure to edit on.",
                    Toast.LENGTH_SHORT);
            toast.show();
        } else{
            TextView selectedTextView = (TextView) adapter.getView(pos, null, listView);
            String gameName = selectedTextView.getText().toString();
            String sqlStr = "SELECT shapeDict, pageDict, curPage, isEdit, isSaved from "
                    + tableName
                    + " where name = '"
                    + gameName
                    +"';";
            Cursor tableCursor = db.rawQuery(sqlStr, null);
            String sd;
            String pd;
            String cp;
            boolean ie;
            boolean is;
            // only take the first string since all the data is saved in the first string
            if(tableCursor.moveToNext()){
                // gameData = tableCursor.getString(0);
                sd = tableCursor.getString(0);
                pd = tableCursor.getString(1);
                cp = tableCursor.getString(2);
                ie = tableCursor.getInt(3) == 1;
                is = tableCursor.getInt(4) == 1;
                Toast.makeText(this, "Load from " + gameName, Toast.LENGTH_SHORT).show();

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
            }else{
                Toast.makeText(this, "Error in retrieving data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showSavings(String tableName){
        String selectTable;
        selectTable = "SELECT * from " + tableName;
        Cursor tableCursor = db.rawQuery(selectTable, null);
        String[] fromArray = {"name"};
        int[] toArray = {android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1,
                tableCursor, fromArray, toArray, 0);
        ListView listView = findViewById(R.id.loadListView);
        listView.setAdapter(adapter);
    }

    // reset the Database + refresh list
    public void onReset(View view) {
        String resetStr = "DROP TABLE IF EXISTS games;";
        db.execSQL(resetStr);
        resetDatabase();
        refreshList();
    }

    // refresh the listView
    public void refreshList(){
        String selectTable = "SELECT name from " + tableName;
        Cursor tableCursor = db.rawQuery(selectTable, null);
        adapter.changeCursor(tableCursor);
        listView.setItemChecked(-1, true);
        listView.invalidateViews();
    }

    // delete the created map from list + refresh List.
    public void onDelete(View view){
        ListView listView = (ListView) findViewById(R.id.loadListView);
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
                Toast.makeText(this,
                        "Successfully delete " + gameName,
                        Toast.LENGTH_SHORT).show();
                refreshList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // reset the database
    private void resetDatabase() {
        String setupStr = "CREATE TABLE games ("
                + "name TEXT, shapeDict TEXT, pageDict TEXT, curPage TEXT, isEdit int, isSaved int, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";
        System.err.println(setupStr);
        db.execSQL(setupStr);
        Toast.makeText(getApplicationContext(),
                "Database Reset.", Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.load_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.select_mode:
                intent = new Intent(this, EditMain.class);
                startActivity(intent);
                break;
        }
        return true;
    }
    */
}




