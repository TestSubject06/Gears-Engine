package gears;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 *
 * @author Zack
 */
public class GSprite extends GObject{
    public static final int FLIP_HORZ = 1;
    public static final int FLIP_VERT = 2;

    public int orientation = 0;
    public Image fullImage;
    public int frameWidth;
    public int frameHeight;

    //Used to point to the top left of the source image, or source image's frame
    protected GPoint displayPoint;
    //A list of all the animations this sprite has been registered.
    protected Vector<GAnim> _animations;
    //A link to the current animation of the sprite, accessible to anything with access to the sprite.
    public GAnim curAnim;
    //This is the frame of the animation, NOT the index on the spritesheet
    protected int _curFrame;
    //This is the index on the spritesheet
    protected int _curIndex;
    //This is just the timer for advancing the animation
    protected double _frameTimer;
    //Is the animation finished playing?
    public boolean finished;
    //This is used as transparency
    public float alpha = 1.0f;

    //Don't touch this.
    //No, really. Don't.
    protected boolean dirty;

    public GSprite(double x, double y, Image simpleGraphic){
        super(x, y);
        displayPoint = new GPoint(0,0);
        _animations = new Vector<>();
        curAnim = null;
        _curFrame = 0;
        _curIndex = 0;
        _frameTimer = 0;
        finished = false;
        fullImage = simpleGraphic;
        if(simpleGraphic != null){
            frameWidth = simpleGraphic.getWidth(null);
            frameHeight = simpleGraphic.getHeight(null);
        }else{
            frameWidth = 0;
            frameHeight = 0;
        }
        width = frameWidth;
        height = frameHeight;
    }

    @Override
    public void update(){
        super.update();
        updateAnimation();
    }

    @Override
    public void render(Graphics2D g){
        if(fullImage != null){
        	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        	//Source X and Y depend on facing values. Left/Right affect where frameWidth is added in, Up/Down affect frameHeight.
        	g.drawImage(fullImage, 
        			(int)x+((orientation & 0x1)*frameWidth), 
        			(int)y+(((orientation & 0x2)>>1)*frameHeight), 
        			((int)x+frameWidth)+((orientation & 0x1)*(-frameWidth)), 
        			((int)y+frameHeight)+(((orientation & 0x2)>>1)*-frameHeight), 
        			(int)displayPoint.x, 
        			(int)displayPoint.y, 
        			(int)displayPoint.x+frameWidth, 
        			(int)displayPoint.y+frameHeight, 
        			null);
            super.render(g);
        }
    }

    /**
     * Loads a graphic after the sprite has been created.
     * Throws out the old graphic if this sprite had one.
     * The height and width do not necessarily mean that the image is animated, or has to be animated
     * to make use of them. For instance a single image with lots of boxes on it won't be animated,
     * but you can choose which one you want to display by using setFrame();
     * @param graphic The graphic that will be displayed. Either a single image, or a spritesheet.
     * @param animated True if this image is going to be animated
     * @param width The width of the frames of this image
     * @param height The height of the frames of this image
     */
    public void loadGraphic(Image graphic, boolean animated, int width, int height){
        fullImage = graphic;
        if(width == 0){
            if(animated)
                width = fullImage.getHeight(null);
        }
        frameWidth = width;
        if(height == 0){
            if(animated)
                height = width;
        }
        frameHeight = height;
        this.width = width;
        this.height = height;
    }

    /**
     * Adds a new animation to the list of animations this sprite has.
     * @param name The name of this animation. There's nothing stopping multiple animations with the same name. Don't do that.
     * @param frames A list of image indexes in order of how this animation should be played. pass as new int[]{i, j, k, l, n, m, o, etc...}
     * @param frameRate The number of times per second this animation advances the frame.
     * @param looped If true the animation will restart once it ends.
     */
    public void addAnimation(String name, int[] frames, double frameRate, boolean looped){
        _animations.add(new GAnim(name, frames, frameRate, looped));
    }

    /**
     * Internal function. Updates the animation of the sprite. Does nothing if the current
     * animation is null.
     */
    protected void updateAnimation(){
        if(curAnim != null && curAnim.delay > 0 && (curAnim.looped || !finished)){
            _frameTimer += GBase.elapsedSeconds;
            while(_frameTimer > curAnim.delay){
                _frameTimer -= curAnim.delay;
                if(_curFrame == curAnim.frames.length-1){
                    if(curAnim.looped){
                        _curFrame = 0;
                    }
                    finished = true;
                }else{
                    _curFrame++;
                }
                _curIndex = curAnim.frames[_curFrame];
                dirty = true;
            }
        }
        if(dirty)
            calcFrame();
    }

    /**
     * Internal function that calculates where the displayPoint should be. This
     * is then used to display the correct frame of animation.
     */
    protected void calcFrame(){
        int indexX = _curIndex*frameWidth;
        int indexY = 0;
        int widthHelper = fullImage.getWidth(null);
        if(indexX >= widthHelper){
            indexY = (int)(indexX/widthHelper)*frameHeight;
            indexX = indexX % widthHelper;
        }
        displayPoint.x = indexX;
        displayPoint.y = indexY;
        dirty = false;
    }

    /**
     * Plays the animation with the given name in the list. Prints an error to the
     * console if the animation doesn't exist.
     * @param animName The name of the animation to play.
     * @param forceRestart If the animation should be forcefully restarted if it's already playing.
     */
    public void play(String animName, boolean forceRestart){
        //Are we already playing that animation, and it's not even done yet?
        if(curAnim != null && (animName.equals(curAnim.name) && !forceRestart && (curAnim.looped || !finished)))
            return;
        _curFrame = 0;
        _curIndex = 0;
        _frameTimer = 0;
        for(int i = 0; i < _animations.size(); i++){
            if(_animations.get(i).name.equals(animName)){
                curAnim = _animations.get(i);
                if(curAnim.delay <= 0)
                    finished = true;
                else
                    finished = false;
                _curIndex = curAnim.frames[_curFrame];
                dirty = true;
                return;
            }
        }
        GBase.log("WARNING: No animation called \"" + animName + "\"");
    }

    /**
     * Plays the animation with the given name. Prints an error to the
     * console if the animation doesn't exist. Does not force restart.
     * @param animName The name of the animation to play.
     */
    public void play(String animName){
        play(animName, false);
    }
    
    /**
     * If the sprite is already playing an animation, it just restarts it.
     */
    public void restartAnimation(){
    	if(curAnim != null){
    		_curFrame = 0;
    		_curIndex = 0;
    		_frameTimer = 0;
    		if(curAnim.delay <= 0)
                finished = true;
            else
                finished = false;
            _curIndex = curAnim.frames[_curFrame];
            dirty = true;
            return;
    	}
    }

    /**
     * Creates a square image with the specified height and width, with the specified color.
     * @param width The width of the square to be created
     * @param height The height of the square to be created
     * @param color The color of the square to be created
     */
    public void createImage(int width, int height, Color color){
        BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        //Calling getGraphics() on a buffered image returns a new Graphics object every time.
        //Why? Fuck if I know, java is dumb a lot of the time.
        Graphics g = temp.getGraphics();
        g.setColor(color);
        g.fillRect(0,0,width,height);
        fullImage = temp;
        frameHeight = height;
        frameWidth = width;
        g.dispose();
        this.width = width;
        this.height = height;
    }
    
    /**
     * Returns true if the supplied point lies within the bounds of this sprite object. It's a very
     * basic hull check.
     * @return Returns true if the point is within the bounds of this sprite, false otherwise
     */
    public boolean containsPoint(GPoint point){
    	if(point.x >= x && point.x <= x+frameWidth && point.y >= y && point.y <= y+frameHeight){
    		return true;
    	}
    	return false;
    }
    
    /**
     * This is a basic hull vs hull check.
     * @return returns true if the supplied GRect intersects this sprite object, false otherwise
     */
    public boolean intersectsRect(GRect rect){
    	//if(rect.x >= x && rect.x <= x+frameWidth)
    	return false;
    }
    
    public void setCurIndex(int newIndex){
    	if(newIndex != _curIndex){
    		_curIndex = newIndex;
    		calcFrame();
    	}
    }
}
