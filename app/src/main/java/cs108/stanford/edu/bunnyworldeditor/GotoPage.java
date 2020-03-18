package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GotoPage extends AppCompatActivity {
    public Spinner pageSpinner;
    String pageSelected = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goto_page);
        setTitle("Go to Page");

        // Set pop window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.3));

        pageSelected = mySingleton.getInstance().getDocStored().getCurPage().getName();

        pageSpinner = findViewById(R.id.pageSpinner);
        ArrayList<String> pageNames = new ArrayList<>(mySingleton.getInstance().docStored.pageDict.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageNames);
        pageSpinner.setPrompt("Go to Page");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageSpinner.setAdapter(adapter);
        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                pageSelected = parent.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    public void onGotoPage(View view){
        Docs d = mySingleton.getInstance().getDocStored();
        d.setCurPage(d.getPageDict().get(pageSelected));
        mySingleton.getInstance().setDocStored(d);

        Intent intent = new Intent(this, EditMain.class);
        finish();
        startActivity(intent);
    }

    public void onCancelGoto(View view){
        Intent intent = new Intent(this, EditMain.class);
        finish();
        startActivity(intent);
    }
}

