package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class SaveEditGame extends AppCompatActivity {

    SQLiteDatabase db;
    String tableName = "games";
    String overwriteGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_edit_game);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.35));

        // Determine which table to save to
        Intent intent = getIntent();

        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String countNumStr = "SELECT name from " + tableName;
        Cursor tableCursor = db.rawQuery(countNumStr, null);

        overwriteGameName = "Game" + (tableCursor.getCount()+1);
        ((EditText) findViewById(R.id.saveNameText)).setText(overwriteGameName);
        db.close();
    }

    public void onSave(View view){
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);

        Docs d = mySingleton.getInstance().getDocStored();
        d.setSaved(true);
        mySingleton.getInstance().setDocStored(d);

        EditText nameText = findViewById(R.id.saveNameText);
        String gameName = nameText.getText().toString();
        // Intent intent = getIntent();
        String checkStr = "SELECT name from " + tableName +";";
        Cursor tableCursor = db.rawQuery(checkStr, null);
        while (tableCursor.moveToNext()) {
            if (tableCursor.getString(0).equals(gameName)) {
                String sqlStr = "DELETE from " + tableName
                        + " where name = '" + gameName  + "';";
                db.execSQL(sqlStr);
                Toast.makeText(this, "Rewriting " + gameName, Toast.LENGTH_SHORT).show();
            }
        }
        tableCursor.close();

        Gson gson = new Gson();
        String json1 = gson.toJson(mySingleton.getInstance().getDocStored().getShapeDict());
        String json2 = gson.toJson(mySingleton.getInstance().getDocStored().getPageDict());
        String json3 = gson.toJson(mySingleton.getInstance().getDocStored().getCurPage());
        String sqlCommand = "INSERT INTO games VALUES ('"
                + gameName + "','"
                + json1 + "','"
                + json2 + "','"
                + json3 + "',"
                + 1 + ","
                + 1 + ",NULL);";
        db.execSQL(sqlCommand);
        Toast.makeText(this, "Saved to DataBase", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(SaveEditGame.this,MainActivity.class));
        db.close();
    }

    public void onCancel(View view){
        // Redirect to the EditMode / PlayMode activity
        finish();
        startActivity(new Intent(this, EditMain.class));
    }
}



