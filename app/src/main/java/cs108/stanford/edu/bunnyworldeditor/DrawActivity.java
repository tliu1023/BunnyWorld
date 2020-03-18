package cs108.stanford.edu.bunnyworldeditor;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.LinkedList;

public class DrawActivity extends AppCompatActivity {

    private int color_r = 255;
    private int color_g = 0;
    private int color_b = 0;
    private float linewidth = 10.0f;
    private float eraseWidth = 10.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(255,130,160));


        DrawableView dv = findViewById(R.id.drawCanvas);
        dv.setBackgroundColor(Color.WHITE);

        ImageView iv = findViewById(R.id.imageView_showLast);
        iv.setBackgroundColor(Color.WHITE);

        Button button_ChangeColor = findViewById(R.id.button_drawColor);
        button_ChangeColor.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePaintColor();
                    }
                }
        );

        Button button_ChangeLinewidth = findViewById(R.id.button_drawLinewidth);
        button_ChangeLinewidth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeLinewidth();
                    }
                }
        );

        Button button_eraser = findViewById(R.id.button_drawEraser);
        button_eraser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //changeEraserWidth();
                        saveEraseChange();
                        DrawableView.setErase();
                    }
                }
        );

    }
    private void changeLinewidth() {
        AlertDialog.Builder changeLinewidthDialog= new AlertDialog.Builder(DrawActivity.this);
        View dialogView = LayoutInflater.from(DrawActivity.this).inflate(R.layout.dialog_drawchangelinewidth,
                null);
        changeLinewidthDialog.setTitle("Change Linewidth");
        changeLinewidthDialog.setView(dialogView);

        SeekBar seekbar_linewidth = dialogView.findViewById(R.id.seekbar_linewidth);
        seekbar_linewidth.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        linewidth = (float) progress;
                        savePaintChange();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        changeLinewidthDialog.show();
    }

    public void clearCanvas(View view) {
        DrawableView drawableView = findViewById(R.id.drawCanvas);
        drawableView.clearCanvas();

        ImageView iv = findViewById(R.id.imageView_showLast);
        iv.setImageBitmap(Bitmap.createBitmap(drawableView.getWidth(),
                drawableView.getHeight(), Bitmap.Config.ARGB_8888));
    }

    public void saveShape(View view) {
        final AlertDialog.Builder shapeNameDialog= new AlertDialog.Builder(DrawActivity.this);
        View dialogView = LayoutInflater.from(DrawActivity.this).inflate(R.layout.dialog_drawshapename,
                null);
        shapeNameDialog.setTitle("Shape Name");
        shapeNameDialog.setView(dialogView);

        final EditText et = dialogView.findViewById(R.id.edittext_drawShapeName);
        int num = CreatedShapes.getInstance().getShapes().size() + 1;

        et.setText("Shape" + String.valueOf(num));

        shapeNameDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get shape name
                        String name = et.getText().toString();

                        DrawableView drawableView = findViewById(R.id.drawCanvas);
                        drawableView.setName(name);
                        drawableView.saveShape();

                        Toast.makeText(getApplicationContext(),
                                name + " Saved!", Toast.LENGTH_SHORT).show();
                    }
                });
        shapeNameDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "Canceled!", Toast.LENGTH_SHORT).show();
                    }
                });

        shapeNameDialog.show();
    }

    public void showLastShape(View view) {
        // find the target view
        ImageView iv = findViewById(R.id.imageView_showLast);
        HashMap<String, Bitmap> shapes = CreatedShapes.getInstance().getShapes();
        LinkedList<String> names = CreatedShapes.getInstance().getNames();

        // set the bitmap to image view
        iv.setImageBitmap(shapes.get(names.getLast()));
    }

    public void changePaintColor() {
        AlertDialog.Builder changeColorDialog= new AlertDialog.Builder(DrawActivity.this);
        View dialogView = LayoutInflater.from(DrawActivity.this).inflate(R.layout.dialog_drawchangecolor,
                null);
        changeColorDialog.setTitle("Change Color");
        changeColorDialog.setView(dialogView);

        SeekBar seekBar_R = dialogView.findViewById(R.id.seekbar_drawcolor_R);
        SeekBar seekBar_G = dialogView.findViewById(R.id.seekbar_drawcolor_G);
        SeekBar seekBar_B = dialogView.findViewById(R.id.seekbar_drawcolor_B);

        final View colorboard = dialogView.findViewById(R.id.View_colorBoard);


        seekBar_R.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        color_r = progress;
                        colorboard.setBackgroundColor(Color.rgb(color_r, color_g, color_b));
                        savePaintChange();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );

        seekBar_G.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        color_g = progress;
                        colorboard.setBackgroundColor(Color.rgb(color_r, color_g, color_b));
                        savePaintChange();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
        seekBar_B.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        color_b = progress;
                        colorboard.setBackgroundColor(Color.rgb(color_r, color_g, color_b));
                        savePaintChange();
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                }
        );
        changeColorDialog.show();
    }

    private void savePaintChange(){
        CreatedPaint paint = CreatedPaint.getInstance();
        paint.setColor(Color.rgb(color_r, color_g, color_b));
        paint.setLinewidth(linewidth);
        paint.setEraseWidth(eraseWidth);
    }

    public void saveEraseChange() {
        CreatedPaint paint = CreatedPaint.getInstance();
        paint.setColor(Color.WHITE);
        paint.setEraseWidth(eraseWidth);
    }
}
