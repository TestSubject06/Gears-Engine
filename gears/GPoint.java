package gears;

/**
 * Handy collection of an x and a y variable in a point class. Will eventually have
 * basic vector functions.
 * @author Zack
 */
public class GPoint {
    public double x;
    public double y;

    public GPoint(double x, double y){
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "GPoint: (" + (int)x + "," + (int)y + ")";
    }
}
