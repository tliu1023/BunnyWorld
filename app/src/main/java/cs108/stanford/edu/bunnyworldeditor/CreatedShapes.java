package cs108.stanford.edu.bunnyworldeditor;

import android.graphics.Bitmap;
import java.util.HashMap;
import java.util.LinkedList;

class CreatedShapes {

    private HashMap<String, Bitmap> shapes;
    private LinkedList<String> names;
    private static final CreatedShapes ourInstance = new CreatedShapes();

    static CreatedShapes getInstance() {
        return ourInstance;
    }

    private CreatedShapes() {
        shapes = new HashMap<>();
        names = new LinkedList<>();
    }

    public void addShape(String name, Bitmap bitmap){
        shapes.put(name, bitmap);
        names.add(name);
    }

    public HashMap<String, Bitmap> getShapes() {
        return shapes;
    }

    public LinkedList<String> getNames() {
        return names;
    }

    @Override
    public String toString() {
        return "CreatedShapes{" +
                "shapes=" + shapes +
                '}';
    }




}
