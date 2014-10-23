import java.awt.Color;
import java.awt.Graphics;


public class Ellipse extends Shape {
    
	
	public Ellipse() 
    {
    	super();
    	equalDimensions = false;
    }
	
	public Ellipse(int x1,int y1,int stroke,Color color)
	{
		super(x1,y1,stroke,color);
	}

	public Ellipse(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		super(x1, y1, x2, y2, stroke, color,ID);
	}
	
	@Override
	public Ellipse makeCopy()
	{
		Ellipse copy = new Ellipse(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}

	@Override
	public void draw(Graphics g) 
	{
        g.setColor(color);
        
        if(x1<x2 && y1<y2)
        g.fillOval(x1, y1, x2-x1, y2-y1);
        else if(x1>x2 && y1<y2)
        g.fillOval(x2, y1, x1-x2, y2-y1);
        else if(x1<x2 && y1>y2)
        g.fillOval(x1, y2, x2-x1, y1-y2);
        else if(x1>x2 && y1>y2)
        g.fillOval(x2, y2, x1-x2, y1-y2);
	}
	
	@Override
	public boolean checkSelection(int x, int y)
	{
		if( 	(x>x1-5 && x<x1+5 && y>y1-5 && y<y1+5) /*resize up-lift*/||
				(x>x2-5 && x<x2+5 && y>y1-5 && y<y1+5) /*resize up-right*/||
				(x>x1-5 && x<x1+5 && y>y2-5 && y<y2+5) /*resize down-lift*/||
				(x>x2-5 && x<x2+5 && y>y2-5 && y<y2+5) /*resize down-right*/) return true;
		
		int xCenter = 0;
		int yCenter = 0;
		int a = 0;
		int b = 0;
		
        if(x1<x2 && y1<y2){
        a = x2-x1;
        b = y2-y1;
        }else if(x1>x2 && y1<y2){
        a = x1-x2;
        b = y2-y1;
        }else if(x1<x2 && y1>y2){
        a = x2-x1; 
        b = y1-y2;
        }else if(x1>x2 && y1>y2){
        a = x1-x2;
        b = y1-y2;
        }
        a/=2;
        b/=2;
		xCenter = x1+a;
		yCenter = y1+b;
		
//		System.out.println("Center = "+ xCenter +"," +yCenter);
//		System.out.println("a = " + a);
//		System.out.println("b = " + b);
//		System.out.println("(x,y) = "+ x+","+y);
		
		if( (Math.pow(x-xCenter,2)/Math.pow(a,2) + Math.pow(y-yCenter,2)/Math.pow(b,2)) <= 1){
			System.out.println("Ellipse "+ ID + " Selected");
			return true;
		}
		
		return false;
	}

}
