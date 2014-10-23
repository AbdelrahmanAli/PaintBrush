import java.awt.Color;
import java.awt.Graphics;


public class Rectangle extends Shape {
	
	public Rectangle() 
    {
    	super();
    	equalDimensions = false;
    }

    public Rectangle(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) {
		super(x1, y1, x2, y2, stroke, color,ID);
	}
	
	public Rectangle(int x1,int y1,int stroke,Color color){
		super(x1, y1, stroke, color);
	}
	
	@Override
	public Rectangle makeCopy()
	{
		Rectangle copy = new Rectangle(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}

	@Override
	public void draw(Graphics g) 
	{
        g.setColor(color);
        
        if(x1<x2 && y1<y2)
        g.fillRect(x1, y1, x2-x1, y2-y1);
        else if(x1>x2 && y1<y2)
        g.fillRect(x2, y1, x1-x2, y2-y1);
        else if(x1<x2 && y1>y2)
        g.fillRect(x1, y2, x2-x1, y1-y2);
        else if(x1>x2 && y1>y2)
        g.fillRect(x2, y2, x1-x2, y1-y2);
	}
    


}
