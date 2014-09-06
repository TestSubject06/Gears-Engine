package gears;

/**
 * Internal class for holding animations for sprites. Don't touch it.
 * @author Zack
 */
public class GAnim {
    public String name;
    public double delay;
    public int[] frames;
    public boolean looped;

    public GAnim(String name, int[] frames, double frameRate, boolean looped){
        this.name = name;
        this.frames = frames;
        if(frameRate > 0)
            delay = 1.0/frameRate;
        this.looped = looped;
    }
}
