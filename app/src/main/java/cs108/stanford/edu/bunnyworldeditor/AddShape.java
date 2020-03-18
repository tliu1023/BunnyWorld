package cs108.stanford.edu.bunnyworldeditor;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.LinkedList;


public class AddShape extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_shape);
        setTitle("Add Shape");

        // Set pop window size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*0.8), (int) (height*0.8));


        Resources res = getResources();

        // file from res folder
        String imageNames[] = {"carrot", "carrot2", "death", "duck", "fire", "mystic"};
        final int number_o_files = imageNames.length;

        // from self-created folder
        int num = CreatedShapes.getInstance().getShapes().size();
        LinkedList<String> imageNames_created = CreatedShapes.getInstance().getNames();
        final shapeViewData[] shapeViewDatas = new shapeViewData[number_o_files + num];

        // set up listView
        for (int idx = 0; idx < number_o_files; idx++) {
            int imageId = res.getIdentifier(imageNames[idx], "drawable", getPackageName());
            shapeViewDatas[idx] = new shapeViewData(imageNames[idx], imageId);
        }

        for(int idx = number_o_files; idx < number_o_files + num; idx++){
            // assign a imageID to created shapes
            int imageId = (idx-number_o_files + 1) * 100;
            shapeViewDatas[idx] = new shapeViewData(imageNames_created.get(idx-number_o_files), imageId);
        }

        final ArrayAdapter<shapeViewData> adapter =
                new ArrayAdapter<shapeViewData>(this, R.layout.edit_shape_view, shapeViewDatas) {
                    @Override
                    public View getView(int position,
                                        View convertView,
                                        ViewGroup parent) {
                        shapeViewData currentData = shapeViewDatas[position];
                        if (convertView == null) {
                            convertView = getLayoutInflater()
                                    .inflate(R.layout.edit_shape_view, null, false);
                        }

                        if(currentData.imageId > 10000000) {
                            ImageView imageView = convertView.findViewById(R.id.image);
                            TextView imageName =  convertView.findViewById(R.id.imageName);
                            imageView.setImageResource(currentData.imageId);
                            imageName.setText(currentData.imageName);
                        }else{
                            ImageView imageView =  convertView.findViewById(R.id.image);
                            TextView imageName = convertView.findViewById(R.id.imageName);

                            // get singleton
                            CreatedShapes cs = CreatedShapes.getInstance();
                            LinkedList<String> imageNames_created = cs.getNames();
                            HashMap<String, Bitmap> images_created = cs.getShapes();
                            String thisName = imageNames_created.get((currentData.imageId/100)-1);
                            imageName.setText(thisName);
                            imageView.setImageBitmap(images_created.get(thisName));
                            //.setImageBitmap(shapes.get(names.getLast()));
                        }
                        return convertView;
                    }
                };

        ListView shapeListView = findViewById(R.id.shapeListView);
        shapeListView.setAdapter(adapter);


        // click on listView
        shapeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Docs temp = mySingleton.getInstance().docStored;

                // shape ID -- unique
                int curShapeID = temp.shapeDict.size();
                System.out.println("Shape Size: " + curShapeID);
                Shape newShape = new Shape("newShape00" + String.valueOf(curShapeID+1), 160, 160, 0, 0);
                // shape name -- can duplicate
                shapeViewData currentData = shapeViewDatas[position];
                newShape.setImgPath(currentData.imageName);
                try {
                    // add the newshape into Docs
                    temp.addShape(newShape);
                    mySingleton.getInstance().setDocStored(temp);

                    // start new activity
                    finish();
                    Intent intent = new Intent(AddShape.this, EditMain.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class shapeViewData{
        String imageName;
        int imageId;

        public shapeViewData(String imageName, int imageId){
            this.imageName = imageName;
            this.imageId = imageId;
        }
    }

    // go back to PageView
    public void onCancelAddShape(View view){
        Intent intent = new Intent(this, EditMain.class);
        finish();
        startActivity(intent);
    }
}
