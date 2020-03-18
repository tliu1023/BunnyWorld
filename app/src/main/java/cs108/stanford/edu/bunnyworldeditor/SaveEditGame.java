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
    boolean isEdit;
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
        overwriteGameName = intent.getStringExtra("gameName");
        ((EditText) findViewById(R.id.nameText)).setText(overwriteGameName);
        isEdit = intent.getBooleanExtra("isEdit", false);

    }

    public void onSave(View view){
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        mySingleton.getInstance().docStored.isSaved=true;
        EditText nameText = (EditText) findViewById(R.id.nameText);

        String gameName = nameText.getText().toString();
        Intent intent = getIntent();
        String checkStr = "SELECT name from " + tableName +";";
        Cursor tableCursor = db.rawQuery(checkStr, null);
        while (tableCursor.moveToNext()) {
            if (tableCursor.getString(0).equals(gameName)) {
                String sqlStr = "DELETE from "
                        + tableName
                        + " where name = '"
                        + gameName
                        + "';";
                db.execSQL(sqlStr);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Rewriting " + gameName,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(mySingleton.getInstance().docStored);
        String sqlCommand = "INSERT INTO games VALUES ('"
                + gameName
                + "','"
                + json
                + "',NULL);";
        System.err.println(sqlCommand);
        db.execSQL(sqlCommand);
        System.err.println("Finished");
        System.out.println(sqlCommand);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Saved to DataBase", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent1 = new Intent(SaveEditGame.this,MainActivity.class);
        startActivity(intent1);

    }

    public void onCancel(View view){
        // Redirect to the EditMode / PlayMode activity
        Intent intent;
        intent = new Intent(this, EditMain.class);
        startActivity(intent);
    }
}



