package gears;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is the base of the game, this class handles all of the frame timing, and
 * makes sure that update and render are called for the state and the console.
 * @author Zack
 */
@SuppressWarnings("serial")
public class GGame extends JPanel{
    public static JFrame gameFrame; //Our window for the game.

    boolean firstFrame = true;
    int frameCount = 0;
    int remainder = 0;
    long targetFramerate = 1000/60, lastTime = 0, totalElapsedTime = 0, reportedFramerate = 0;
    Graphics2D bufferGraphics;
    Graphics2D consoleGraphics;
    Image offscreen;
    Dimension dim;
    GConsole console;
    Color bgColor;

    public GGame(GState startState, int dimX, int dimY, Color bgColor){
        super(); //Get the superclass set up

        //Initialize window things
        dim = new Dimension(dimX, dimY);
        setPreferredSize(dim);
        setSize(dim);
        setFocusable(true);
        setLayout(null);
        setBackground(new Color(0x0, true));

        //Initialize game things
        lastTime = System.currentTimeMillis();
        GBase.consoleFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);
        GBase.gameWidth = dim.width;
        GBase.gameHeight = dim.height;
        GBase.input = new GInput();
        console = new GConsole(0, 0);
        GBase.console = console;

        //Build our window
        gameFrame = new JFrame("Gears Engine");
        gameFrame.add(this);
        gameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //exitProcedure();
                System.exit(0);
            }
        });
        gameFrame.pack();
        GBase.gameInstance = this;
        this.bgColor = bgColor;
        if(bgColor != null)
            gameFrame.setBackground(bgColor);
        addKeyListener(GBase.input);
        addMouseWheelListener(GBase.input);
        addMouseListener(GBase.input);

        //Finally ready
        GBase.stateManager = new GStateManager(startState);
        gameFrame.setVisible(true);
    }

    @Override
    public void update(Graphics g){
        GBase.elapsed = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        GBase.elapsedSeconds = GBase.elapsed/1000.0;


        UpdateFPS();

        GBase.stateManager.update();

        if(GBase.input.justPressed(192)){
            console.exists = !console.exists;
            GObject.numRenders = 0;
            GObject.numUpdates = 0;
        }
        if(console.exists && console.active){
            console.update();
        }

        //'Update' the input last. the input is updated normally when the keys are pressed
        //so we clear all the 'just' flags at the end of the frame
        GBase.input.update(MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x, MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y);

        repaint();
      
    }

    /**
     * Updates the reported FPS.
     */
    private void UpdateFPS(){
        totalElapsedTime += GBase.elapsed;
        GBase.totalElapsedTime += GBase.elapsed;
        frameCount++;
        if(totalElapsedTime > 1000){
            reportedFramerate = (long) Math.round((double)frameCount / (double)totalElapsedTime*1000);
            frameCount = 0;
            totalElapsedTime -= 1000;

            console.fps = (int)reportedFramerate;
        }
    }

    /**
     * Draws the frame.
     * @param g Graphics passed down from the Almighty Java VM™
     */
    @Override
    public void paint(Graphics g){
        //We have to do the initial frame setup here because Java is a
        //piece of shit and will return a bunch of nulls if we do this in the
        //constructor.
        if(firstFrame){
            offscreen = createImage(dim.width, dim.height);
            bufferGraphics = (Graphics2D)offscreen.getGraphics();
            GBase.consoleFontMetrics = g.getFontMetrics(GBase.consoleFont);
            console.initRows();
            firstFrame = false;
        }else{
            bufferGraphics.clearRect(0, 0, dim.width, dim.height);

            GBase.stateManager.render(bufferGraphics);

            if(console.exists && console.visible){
                console.render(bufferGraphics);
            }
            g.drawImage(offscreen, 0, 0,this.getWidth(),this.getHeight(), this);
        }

        GBase.elapsed = System.currentTimeMillis() - lastTime;
        try{
            if(GBase.elapsed < targetFramerate){
                remainder += 6;
                if(remainder > 10){
                    Thread.sleep(targetFramerate - GBase.elapsed + 1);
                    remainder -= 10;
                }else{
                    Thread.sleep(targetFramerate - GBase.elapsed);
                }
            }else{
                remainder = 0;
            }
        }catch(Throwable t){
            //Get mad as shit, becuase this should never fail.
        }
        super.paint(g);
        update(g);
    }
}
