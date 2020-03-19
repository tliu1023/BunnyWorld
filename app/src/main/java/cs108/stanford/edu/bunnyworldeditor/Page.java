package cs108.stanford.edu.bunnyworldeditor;

import android.content.Context;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Page implements Serializable {
    public String name;
    public Shape selectedShape;
    public ArrayList<Shape> shapes;
    public HashSet<String> relatedShapes;
    public int backgroundId;

    public Page(String name) {
        this.name = name;
        this.selectedShape = null;
        this.shapes = new ArrayList<Shape> ();
        this.relatedShapes = new HashSet<> ();
        this.backgroundId = 2131230816;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + backgroundId;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Page)) {
            return false;
        }
        Page temp = (Page) obj;
        return temp.name.equals(this.name) &&
                temp.relatedShapes.equals(this.relatedShapes) &&
                temp.shapes.equals(this.shapes) &&
                temp.backgroundId == this.backgroundId;
    }

    @Override
    public String toString() {
        return "Page{" +
                "name='" + name + '\'' +
                ", selectedShape=" + selectedShape +
                ", shapes=" + shapes +
                ", backgroundId=" + backgroundId +
                '}';
    }
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }

    public HashSet<String> getRelatedShapes() {
        return relatedShapes;
    }

    public void setRelatedShapes(HashSet<String> relatedShapes) {
        this.relatedShapes = relatedShapes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape shape) {
        // unselect the previous one
        if (selectedShape != null) {
            selectedShape.setSelected(false);
        }
        if (shape != null) {
            shape.setSelected(true);
        }
        selectedShape = shape;
    }

    public Shape getShapeByLoc(float x, float y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape sh = shapes.get(i);
            if (x >= sh.xCoor && x <= sh.xCoor + sh.width && y >= sh.yCoor && y <= sh.yCoor + sh.height) {
                return sh;
            }
        }
        return null;
    }

    public void addShape(Shape shape) {
        if (!shapes.contains(shape)) {
            shapes.add(shape);
        }
    }

    public void removeShape(Shape shape) {
        for (Shape sh : shapes) {
            if (sh.equals(shape)) {
                shapes.remove(sh);
                return;
            }
        }
    }

    public Shape getShapeByName(String name) {
        for (Shape s : shapes) {
            if (s.id.equals(name)) {
                return s;
            }
        }
        return null;
    }

    public void drawPagePlay(Canvas canvas, Context context) {
        for (Shape shape : shapes) {
            shape.drawPicPlay(canvas, context);
        }
    }

    /*
    public void drawPage(Canvas canvas, Context context) {
        for (Shape shape : shapes) {
            shape.drawPic(canvas, context);
        }
    }
    */
}
