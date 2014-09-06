package gears;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Internal object. This is the console that you see when you hit ~.
 * @author Zack
 */
public class GConsole extends GObject{
    public static final int numLines = 500;
    public int fps;
    public ArrayList<String> messages;
    public ArrayList<Object[]> realtimes;
    public int consolePosition1 = 0;
    private int consolePosition2 = 0;
    private int numRows;
    private int page = 0;


    public GConsole(double x, double y){
        super(x, y);
        messages = new ArrayList<>(numLines);
        realtimes = new ArrayList<>();
        exists = false;
    }

    public void initRows(){
        numRows = GBase.gameHeight/(GBase.consoleFontMetrics.getHeight()-5);
    }

    @Override
    public void update(){
        if(GBase.input.justPressed(113)){
            page = 1;
        }else if(GBase.input.justPressed(112)){
            page = 0;
        }
        if(GBase.input.mouseWheelMovement != 0 && messages.size() > numRows-1) {
            switch(page){
                case 0:
                    consolePosition1 += GBase.input.mouseWheelMovement*2;
                    if(consolePosition1 < 0)
                        consolePosition1 = 0;
                    if(consolePosition1 > messages.size()-numRows+1 && messages.size() > numRows-1)
                        consolePosition1 = messages.size()-numRows+1;
                break;

                case 1:
                    consolePosition2 += GBase.input.mouseWheelMovement*2;
                    if(consolePosition2 < 0)
                        consolePosition2 = 0;
                    if(consolePosition2 > realtimes.size()-numRows+1 && realtimes.size() > numRows-1)
                        consolePosition2 = realtimes.size()-numRows+1;
                break;
            }
        }
        super.update();
    }

    @Override
    public void render(Graphics2D g){
        g.setColor(new Color(10, 10, 10, 128));
        g.fillRect((int)x, (int)y, GBase.gameWidth-(int)x, GBase.gameHeight-(int)y);
        g.setColor(Color.white);
        g.setFont(GBase.consoleFont);
        String framerate = "FPS: " + fps;
        g.drawString(framerate, GBase.gameWidth - GBase.consoleFontMetrics.stringWidth("FPS: 000"), GBase.consoleFontMetrics.getHeight()-5);
        framerate = "  U: " + GObject.numUpdates;
        g.drawString(framerate, GBase.gameWidth - GBase.consoleFontMetrics.stringWidth("  U: 000"), 2*(GBase.consoleFontMetrics.getHeight()-5));
        framerate = "  D: " + GObject.numRenders;
        g.drawString(framerate, GBase.gameWidth - GBase.consoleFontMetrics.stringWidth("  D: 000"), 3*(GBase.consoleFontMetrics.getHeight()-5));

        GObject.numRenders = 0;
        GObject.numUpdates = 0;

        if(page == 0){
            int l = 1;
            for(int i = consolePosition1; i < ((messages.size() < numRows + i) ? messages.size() : numRows + i); i++){
                g.drawString(messages.get(i), 2, l*(GBase.consoleFontMetrics.getHeight()-5));
                l++;
            }
        }else{
            int l = 1;
            for(int i = consolePosition2; i < ((realtimes.size() < numRows + i) ? realtimes.size() : numRows + i); i++){
                try{
                    Field um = realtimes.get(i)[2].getClass().getField((String)realtimes.get(i)[1]);
                    g.drawString((String)realtimes.get(i)[0] + um.get(realtimes.get(i)[2]), 2, l*(GBase.consoleFontMetrics.getHeight()-5));
                }catch(Throwable t){
                    System.out.println(t);
                }
                l++;
            }
        }
        super.render(g);
    }

    public void addMessage(String message){
        boolean scrolled = false;
        if(messages.size() > numRows -1 && consolePosition1 == messages.size()-numRows+1){
            consolePosition1++;
            scrolled = true;
        }else if(messages.size() == numRows -1){
            consolePosition1++;
            scrolled = true;
        }
        messages.add(message);
        if(messages.size() > numLines){
            messages.remove(0);
            if(!scrolled) {consolePosition1 = consolePosition1 == 0 ? 0 : consolePosition1 -1;}
            else{consolePosition1 = consolePosition1 == messages.size()-numRows+2 ? messages.size()-numRows+1 : consolePosition1;}
        }
    }
     public void watch(String prefix, String fieldName, Object object){
         Object a[] = new Object[]{
             prefix,
             fieldName,
             object
         };
         realtimes.add(a);
     }
}
