import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;


public class Line extends Shape {
	
	public Line() // nully constructor take no arguments must exist to use newInstance()
    {
    	super();
    	equalDimensions = false;
    }
	
	public Line (int x1,int y1,int stroke,Color color)
	{
		super(x1,y1,stroke,color);
	}

	public Line(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		super(x1, y1, x2, y2, stroke, color,ID);
	}

	@Override
	public void draw(Graphics g) 
	{
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
	}
	
	@Override
	public Line makeCopy()
	{
		Line copy = new Line(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}
	
	@Override
	public boolean checkSelection(int x, int y)
	{		
		if( 	(x>x1-5 && x<x1+5 && y>y1-5 && y<y1+5) /*resize up-lift*/||
				(x>x2-5 && x<x2+5 && y>y1-5 && y<y1+5) /*resize up-right*/) return true;
		
		float m = ((float)(y2 - y1)) / ((float)(x2 - x1)); //slope of the line selected
		float n = ((float)(y - y1)) / ((float)(x - x1)); // slope of the line bet. the origin point and the current point
		
		if(((x>=x1 && x<=x2)|| (x>=x2 && x<=x1)) && ((y>=y1 && y<=y2)|| (y>=y2 && y<=y1)) && n>=m-0.1 && n<= m+0.1){
			System.out.println("Line" + ID + "Selected");

			return true;
		}
		return false;
	}
	
	
	@Override
	public void adjustDimensions() {
		
	}
	
	@Override
	public void resize(int x1, int y1, int x2, int y2,int resizePosition){
		
		if(this.x1 > this.x2){
			this.x1 = x2;
			this.y1 = y1;
			this.x2 = x1;
			this.y2 = y2;
		}
		
	}

}
