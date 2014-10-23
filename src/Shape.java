import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Shape {
	
    protected int x1,y1,x2,y2,ID,stroke;
    protected int lastAction;	 	
    // 1.Creating shape   
    // 2.Changing color
    // 3.Moving & Resizing
    // 4.Deleting
    protected Color color;
    protected boolean equalDimensions;
    
    public Shape(){}
	public Shape(int x1,int y1,int x2,int y2,int stroke,Color color,int ID)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.stroke = stroke;
		this.color = color;
		this.ID = ID;
		equalDimensions = false;
	}
	
	public Shape(int x1,int y1,int stroke,Color color)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.stroke = stroke;
		this.color = color;
		equalDimensions = false;
	}
	
	public  abstract void draw(Graphics g);
	public  abstract  Shape makeCopy();
	
	public void putInUndoStack()
	{
		Options.getUndoStack().add(this.makeCopy());
	}
	
	public void setColor(Color color)
	{	
		this.color = color;
	}
	
	public void setLastAction(int action)
	{
		lastAction = action; // color changed action
	}
	
	public int getLastAction()
	{
		return lastAction;
	}
	
    public void adjustDimensions()
    {
        if(x1>x2) 
        {
            int temp = x1;
            x1 = x2;
            x2 = temp;
        }
        
        if(y1>y2) 
        {
            int temp = y1;
            y1 = y2;
            y2 = temp;
        }
    }
    
    public int getID()
    {
    	return ID;
    }
    
    public Color getColor()
    {
    	return color;
    }
	
	public boolean checkSelection(int x, int y)
	{
		if( 	(x>x1-5 && x<x1+5 && y>y1-5 && y<y1+5) /*resize up-lift*/||
				(x>x2-5 && x<x2+5 && y>y1-5 && y<y1+5) /*resize up-right*/||
				(x>x1-5 && x<x1+5 && y>y2-5 && y<y2+5) /*resize down-lift*/||
				(x>x2-5 && x<x2+5 && y>y2-5 && y<y2+5) /*resize down-right*/) return true;
		
		if(x>=x1 && x<=x2 && y>=y1 && y<=y2) return true;
		return false;	
	}

	public void setX2(int finalX) 
	{
		x2 = finalX;
	}

	public void setY2(int finalY) 
	{
		y2 = finalY;
	}
	public int getX1()
	{
		return x1;
	}
	public int getY1()
	{
		return y1;
	}
	public int getX2()
	{
		return x2;
	}
	public int getY2()
	{
		return y2;
	}
	
	public void setAll(int x1,int y1,int x2,int y2,int stroke,Color color,int ID)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.stroke = stroke;
		this.color = color;
		this.ID = ID;
	}
	public void setSome(int x1,int y1,int stroke,Color color)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.stroke = stroke;
		this.color = color;
	}
	
	public int checkResize(int pressedX, int pressedY) {
		if(pressedX>=x1-5 && pressedX<=x1+5 && pressedY>=y1-5 && pressedY<=y1+5) /*up-left*/ {System.out.println("Resize 1"); return 1;}
		else if(pressedX>=x2-5 && pressedX<=x2+5 && pressedY>=y1-5 && pressedY<=y1+5) /*up-right*/ {System.out.println("Resize 2"); return 2;}
		else if(pressedX>=x1-5 && pressedX<=x1+5 && pressedY>=y2-5 && pressedY<=y2+5) /*down-left*/ {System.out.println("Resize 3"); return 3;}
		else if(pressedX>=x2-5 && pressedX<=x2+5 && pressedY>=y2-5 && pressedY<=y2+5) /*down-right*/ {System.out.println("Resize 4"); return 4;}
		return 0;
	}
	
	public void resize(int x1, int y1, int x2, int y2,int resizePosition){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
			
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public boolean getEqualDimmension()
	{
		return equalDimensions;
	}
	public void drawSelected(Graphics g) {
		g.setColor(color.GRAY);		
		for(int x = x1; x<=x2-5 ; x+=10){
			g.drawLine(x, y1, x+5, y1);
			g.drawLine(x, y2, x+5, y2);
		}
		
		for(int y = y1; y<=y2-5 ; y+=10){
			g.drawLine(x1, y, x1, y+5);
			g.drawLine(x2, y, x2, y+5);
		}
		
		g.fillRect(x1, y1, 5, 5);
		g.fillRect(x1, y2-5, 5, 5);
		g.fillRect(x2-5, y1, 5, 5);
		g.fillRect(x2-5, y2-5, 5, 5);
	}
	public int getStroke() {
		
		return stroke;
	}	
}
