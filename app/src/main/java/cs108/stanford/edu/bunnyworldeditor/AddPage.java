package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_page);
        setTitle("Add Page");

        // Set pop window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.3));
    }

    public void onAddNewPage(View view){
        EditText newPageName = (EditText) findViewById(R.id.newPageName);
        String name = newPageName.getText().toString();
        Docs d = mySingleton.getInstance().docStored;
        Intent intent = new Intent(this, EditMain.class);

        if(name.length() == 0){
            //default
            int i = 2;
            while(d.pageDict.containsKey("page"+String.valueOf(i))){
                i++;
            }
            name="page"+String.valueOf(i);
        }
        try{
            d.addPage(name);
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        finish();
        startActivity(intent);
    }

    public void onCancelAdding(View view){
        Intent intent = new Intent(this, EditMain.class);
        startActivity(intent);
    }
}

