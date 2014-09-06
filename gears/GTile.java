package gears;

public class GTile extends GObject {

	private int index;
	private boolean collisions;
	private GTileMap tilemap;
	
	public GTile(GTileMap tilemap, int index, int width, int height, boolean visible, boolean collision){
		super(0, 0, width, height);
		this.tilemap = tilemap;
		this.index = index;
		this.visible = visible;
		this.collisions = collisions;
	}
	
	public void destroy(){
		super.destroy();
		tilemap = null;
	}
}
