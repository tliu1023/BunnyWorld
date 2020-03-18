package cs108.stanford.edu.bunnyworldeditor;

import android.graphics.Color;

class CreatedPaint {
    private static final CreatedPaint ourInstance = new CreatedPaint();
    private int color;
    private float linewidth;
    private float eraseWidth;
    static CreatedPaint getInstance() {
        return ourInstance;
    }

    private CreatedPaint() {
        color = Color.RED;
        linewidth = 10.0f;
        eraseWidth = 10.0f;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        System.out.println(color);
    }

    public float getEraseWidth() {
        return eraseWidth;
    }

    public void setEraseWidth(float eraseWidth) {
        this.eraseWidth = eraseWidth;
    }

    public float getLinewidth() {

        return linewidth;
    }

    public void setLinewidth(float linewidth) {

        this.linewidth = linewidth;
    }
}
