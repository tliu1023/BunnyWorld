package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    SQLiteDatabase db;
    GameView gameView;
    Docs d;

    protected static CountDownTimer countDownTimer;
    protected TextView pointsText;
    protected TextView timerText;
    protected long timeLeftInMilliseconds = 120000; // 2min

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
        d = new Docs();
        db = openOrCreateDatabase("GamesDB", MODE_PRIVATE,null);
        Intent intent = getIntent();
        String gameName = intent.getStringExtra("game_name");

        String sqlStr = "SELECT shapeDict, pageDict, curPage, isEdit, isSaved from games"
                + " where name = '"
                + gameName
                +"';";
        Cursor tableCursor = db.rawQuery(sqlStr, null);
        String sd = "";
        String pd = "";
        String cp = "";
        boolean ie = false;
        boolean is = false;

        if (tableCursor.moveToNext()) {
            sd = tableCursor.getString(0);
            pd = tableCursor.getString(1);
            cp = tableCursor.getString(2);
            ie = tableCursor.getInt(3) == 1;
            is = tableCursor.getInt(4) == 1;
            Toast.makeText(this, "Load " + gameName, Toast.LENGTH_SHORT).show();
        }


        Gson gson = new Gson();
        HashMap<String, Shape> shapeDict = gson.fromJson(sd, new TypeToken<HashMap<String, Shape>>() {}.getType());
        HashMap<String, Page> pageDict = gson.fromJson(pd, new TypeToken<HashMap<String, Page>>() {}.getType());
        Page curPage = gson.fromJson(cp, new TypeToken<Page>() {}.getType());

        d.setShapeDict(shapeDict);
        d.setPageDict(pageDict);
        d.setCurPage(curPage);
        d.setEdit(ie);
        d.setSaved(is);

        List<Page> pageList = new ArrayList<> (d.pageDict.values());
        System.out.println(pageList.size());
        gameView.initializePages(pageList);

        timerText = findViewById(R.id.timer);
        startTimer();

        tableCursor.close();
        db.close();
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




