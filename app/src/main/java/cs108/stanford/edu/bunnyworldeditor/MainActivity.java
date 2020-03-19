package cs108.stanford.edu.bunnyworldeditor;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    public static final int MAX_NUM_GAMES = 6;
    SQLiteDatabase db;
    protected String gameName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        Cursor tablesCursor = db.rawQuery( "SELECT * FROM sqlite_master WHERE type='table' AND name='games';", null);
        System.out.println(tablesCursor.getCount());
        if (tablesCursor.getCount() == 0) {
            setupDatabase();
        }
        tablesCursor.close();
        db.close();
    }

    public void goToGamePage(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game_name", gameName);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onStart(View view) {
        // pop up the window
        setPopupWindow(view);
    }

    public void onStartEditor(View view) {
        Intent intent = new Intent(this, LoadAdventure.class);
        startActivity(intent);
    }

    private void setupDatabase() {
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String clearStr = "DROP TABLE IF EXISTS games;";
        db.execSQL(clearStr);
        // tabel content
        // gameName(str), shapeDict(str), pageDict(str), curPage(str), isEdit(boolean), isSaved(boolean), _id
        String setupStr = "CREATE TABLE games ("
                + "name TEXT, shapeDict TEXT, pageDict TEXT, curPage TEXT, isEdit INTEGER, isSaved INTEGER, "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";
        System.err.println(setupStr);
        db.execSQL(setupStr);
        db.close();
    }

    // select all the game name from database
    protected List<String> loadGameNames() {
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        List<String> names = new ArrayList<>();
        String query = "SELECT name FROM games";
        Cursor cursor = db.rawQuery( query,null);
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return names;
    }

    protected void setPopupWindow(View view) {
        try {
            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.popup_window_selectgame, null);

            setRadionButtonText(popupView);

            // create the popup window
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            // handle click events on window buttons
            Button goButton = popupView.findViewById(R.id.go_game);
            Button resetButton = popupView.findViewById(R.id.reset);

            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RadioGroup radioGroup = popupView.findViewById(R.id.radiogroup_names);
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = popupView.findViewById(radioId);
                    String option = radioButton.getText().toString();

                    if(!option.equals("Empty")) {
                        gameName = option;
                        popupWindow.dismiss();
                        goToGamePage(view);
                    }
                }
            });

            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDatabase();
                    popupWindow.dismiss();
                }
            });

//            // dismiss the popup window when touched
//            popupView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popupWindow.dismiss();
//                    return true;
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void setRadionButtonText(View layout) {
        //load game names
        List<String> gameNames = loadGameNames();
        int i = 0;

        RadioButton radioButton;
        while (i <= 5) {
            switch(i) {
                case 0: radioButton = layout.findViewById(R.id.game1);
                    if (gameNames.size() > i)
                        radioButton.setText(gameNames.get(i));

                    break;
                case 1: radioButton = layout.findViewById(R.id.game2);
                    if (gameNames.size() > i) {
                        radioButton.setText(gameNames.get(i));
                    }
                    break;
                case 2: radioButton = layout.findViewById(R.id.game3);
                    if (gameNames.size() > i) {
                        radioButton.setText(gameNames.get(i));
                    }
                    break;
                case 3: radioButton = layout.findViewById(R.id.game4);
                    if (gameNames.size() > i) {
                        radioButton.setText(gameNames.get(i));
                    }
                    break;
                case 4: radioButton = layout.findViewById(R.id.game5);
                    if (gameNames.size() > i) {
                        radioButton.setText(gameNames.get(i));
                    }
                    break;
                case 5: radioButton = layout.findViewById(R.id.game6);
                    if (gameNames.size() > i) {
                        radioButton.setText(gameNames.get(i));
                    }
                    break;
                default:
            }
            i++;
        }

    }

    protected void resetDatabase() {
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        String resetStr = "DROP TABLE IF EXISTS games;";
        db.execSQL(resetStr);
        setupDatabase();
        db.close();
    }
}