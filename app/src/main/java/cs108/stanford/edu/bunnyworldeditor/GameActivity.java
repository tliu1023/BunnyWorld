//package cs108.stanford.edu.bunnyworldeditor;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.gson.Gson;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameActivity extends AppCompatActivity {
//    SQLiteDatabase db;
//    GameView gameView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_game);
//        gameView = (GameView) findViewById(R.id.game_view);
//        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
//        Intent intent = getIntent();
//        String gameName = intent.getStringExtra("game_name");
//        System.out.println(gameName);
//        String query = "SELECT gamesData FROM games WHERE name = '" + gameName +"' ";
//        Cursor cursor = db.rawQuery(query,null);
//        String json = "";
//        while (cursor.moveToNext()) {
//            json = cursor.getString(0);
//        }
//        Gson gson = new Gson();
//        Docs docs = gson.fromJson(json, Docs.class);
//        List<Page> pageList = new ArrayList<> (docs.pageDict.values());
//        System.out.println(pageList.size());
//        gameView.initializePages(pageList);
//    }
//}


package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    SQLiteDatabase db;
    GameView gameView;

    protected static CountDownTimer countDownTimer;
    protected TextView pointsText;
    protected TextView timerText;
    protected long timeLeftInMilliseconds = 5000; // 5min

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = (GameView) findViewById(R.id.game_view);
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        Intent intent = getIntent();
        String gameName = intent.getStringExtra("game_name");
        System.out.println(gameName);
        String query = "SELECT gamesData FROM games WHERE name = '" + gameName +"' ";
        Cursor cursor = db.rawQuery(query,null);
        String json = "";
        while (cursor.moveToNext()) {
            json = cursor.getString(0);
        }
        Gson gson = new Gson();
        Docs docs = gson.fromJson(json, Docs.class);
        List<Page> pageList = new ArrayList<> (docs.pageDict.values());
        System.out.println(pageList.size());
        gameView.initializePages(pageList);

        timerText = findViewById(R.id.timer);
        startTimer();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(millisUntilFinished/1000 + " sec");
            }

            @Override
            public void onFinish() {
                timerText.setText("Time Out!");
                // TODO: Go To lose page...
                gameView.performAction("goto", "losePage");
            }
        }.start();
    }

    public static void stopTimer() {
        countDownTimer.cancel();
    }
}




