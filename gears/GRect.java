package gears;

/**
 * Handy representation of a rectangle, extended from the point class. Honestly
 * I have no idea what I'm doing with it.
 * @author Zack
 */
public class GRect extends GPoint {
    public int width;
    public int height;

    public GRect(double x, double y, int width, int height){
        super(x, y);
        this.height = height;
        this.width = width;
    }

}
