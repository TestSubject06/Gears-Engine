package gears;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GTileMap extends GObject {
	protected ArrayList<Integer> _data;
	protected int heightInTiles;
	protected int widthInTiles;
	protected Image _tiles;
	protected int _tileHeight;
	protected int _tileWidth;
	protected GTile[] _tileObjects;
	protected GRect[] _rects;
	protected int totalTiles;	
	protected Image tileMapBuffer;
	private boolean bufferDirty;
	protected int _helperColumns;
	protected int _helperRows;
	public GTileMap(){
		super(0, 0);
		widthInTiles = 0;
		heightInTiles = 0;
		totalTiles = 0;
		_data = null;
		_tileWidth = 0;
		_tileHeight = 0;
		_rects = null;
		_tileObjects = null;
		_tiles = null;
		bufferDirty = true;
	}
	
	public void loadMap(String mapData, Image tileGraphic, int tileWidth, int tileHeight, int drawIndex, int collideIndex){
		
		String[] columns;
		String[] rows = mapData.split("\n");
		heightInTiles = rows.length;
		_data = new ArrayList<>();
		int row = 0;
		int column = 0;
		while(row < heightInTiles){
			columns = rows[row++].split(",");
			if(columns.length <= 1){
				heightInTiles = heightInTiles-1;
				continue;
			}
			if(widthInTiles == 0)
				widthInTiles = columns.length;
			column = 0;
			while(column < widthInTiles){
				_data.add(Integer.valueOf(columns[column++]));
			}
		}
		
		_tiles = tileGraphic;
		_tileWidth = tileWidth;
		_tileHeight = tileHeight;
		if(_tileWidth == 0)
			_tileWidth = _tiles.getWidth(null);
		if(_tileHeight == 0)
			_tileHeight = _tiles.getHeight(null);
		
		int i = 0;
		int l = (_tiles.getWidth(null)/_tileWidth) * (_tiles.getHeight(null)/_tileHeight);
		_tileObjects = new GTile[l];
		while(i < l){
			_tileObjects[i] = new GTile(this, i, _tileWidth, _tileHeight, i >= drawIndex, i >= collideIndex);
			i++;
		}
		
		width = widthInTiles*_tileWidth;
		height = heightInTiles*_tileHeight;
		totalTiles = widthInTiles * heightInTiles;
		_rects = new GRect[totalTiles];
		i = 0;
		while(i < totalTiles)
			updateTile(i++);
		bufferDirty = true;
	}
	
	private void updateTile(int index){
		GTile tile = _tileObjects[_data.get(index)];
		if(tile == null || !tile.visible){
			_rects[index] = null;
			return;
		}
		int rx = _data.get(index)*_tileWidth;
		int ry = 0;
		if(rx >= _tiles.getWidth(null)){
			ry = (int)(rx/_tiles.getWidth(null))*_tileHeight;
			rx %= _tiles.getWidth(null);
		}
		_rects[index] = new GRect(rx, ry, _tileWidth, _tileHeight);
	}
	
	public void drawTilemap(Graphics2D g){
		_helperColumns = (int)Math.ceil(GBase.gameWidth/_tileWidth)+1;
		if(_helperColumns > widthInTiles)
			_helperColumns = widthInTiles;
		int _helperRows = (int)Math.ceil(GBase.gameHeight/_tileHeight)+1;
		if(_helperRows > heightInTiles)
			_helperRows = heightInTiles;
		tileMapBuffer = new BufferedImage(_helperColumns * _tileWidth, _helperRows * _tileHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bufferG = (Graphics2D)tileMapBuffer.getGraphics();
		bufferG.setColor(new Color(0, 0, 0, 0));
		bufferG.fillRect((int)x, (int)y, tileMapBuffer.getWidth(null), tileMapBuffer.getHeight(null));
		
		int rowIndex = 0;
		int row = 0;
		int column = 0;
		int columnIndex = 0;
		GRect rect;
		GPoint point = new GPoint(0,0);
		while(row < _helperRows){
			columnIndex = rowIndex;
			column = 0;
			point.x = 0;
			while(column < _helperColumns){
				rect = _rects[columnIndex];
				if(rect != null){
					bufferG.drawImage(_tiles, (int)point.x, (int)point.y, (int)point.x + rect.width, (int)point.y + rect.height, (int)rect.x, (int)rect.y, (int)rect.x + rect.width, (int)rect.y + rect.height, null);
				}
				point.x += _tileWidth;
				column++;
				columnIndex++;
			}
			rowIndex += widthInTiles;
			point.y += _tileHeight;
			row++;
		}
		g.drawImage(tileMapBuffer, 0, 0, tileMapBuffer.getWidth(null), tileMapBuffer.getHeight(null), (int)x, (int)y, (int)x + tileMapBuffer.getWidth(null), (int)y + tileMapBuffer.getHeight(null), null);
	}
	
	public void render(Graphics2D g){
		drawTilemap(g);
		GObject.numRenders++;
	}
}
