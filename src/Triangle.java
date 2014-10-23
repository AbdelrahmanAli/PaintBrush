import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Triangle extends Shape {
    
	private boolean type;
    private int[] xPoints,yPoints;
    
    public Triangle() 
    {
    	super();
    	equalDimensions = false;
    }

	public Triangle(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		super(x1, y1, x2, y2, stroke, color,ID);

        xPoints = new int[3];
        yPoints = new int[3];
        
        xPoints[0] = x1+(x2-x1)/2; //top     
        yPoints[0] = y1; //top
        
        xPoints[1] = x1; //base1
        yPoints[1] = y2; //base1
        
        xPoints[2] = x2; //base2
        yPoints[2] = y2; //base2
   
//		System.out.println("(x0,y0) = " + xPoints[0] + "," + yPoints[0]);
//		System.out.println("(x1,y1) = " + xPoints[1] + "," + yPoints[1]);
//		System.out.println("(x2,y2) = " + xPoints[2] + "," + yPoints[2]);
	}
	
	@Override
	public Triangle makeCopy()
	{
		Triangle copy = new Triangle(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}
	
	public Triangle(int x1,int y1,int stroke,Color color)
	{
		super(x1, y1, stroke, color);
        xPoints = new int[3];
        yPoints = new int[3];
	}
	public void setAll(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.stroke = stroke;
		this.color = color;
		this.ID = ID;
		equalDimensions = false;		
        xPoints = new int[3];
        yPoints = new int[3];
        xPoints[0] = x1+(x2-x1)/2;
        yPoints[0] = y1;
        xPoints[1] = x1;
        yPoints[1] = y2;
        xPoints[2] = x2;
        yPoints[2] = y2;
	}
	
	public void setSome (int x1,int y1,int stroke,Color color)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.stroke = stroke;
		this.color = color;
		equalDimensions = false;
		xPoints = new int[3];
        yPoints = new int[3];
	}
    
    @Override
    public void setX2(int finalX) 
	{
		x2 = finalX;
        xPoints[0] = x1+(x2-x1)/2;
        xPoints[1] = x1;
        xPoints[2] = x2;
	}
    
    @Override
	public void setY2(int finalY) 
	{
		y2 = finalY;
        yPoints[0] = y1;
        yPoints[1] = y2;
        yPoints[2] = y2;
	}

	@Override
	public void draw(Graphics g) {
        
        g.setColor(color);
        g.fillPolygon(xPoints,yPoints, 3);
        
	}
	
	@Override
	public boolean checkSelection(int x, int y)
	{
		if( 	(x>x1-5 && x<x1+5 && y>y1-5 && y<y1+5) /*resize up-lift*/||
				(x>x2-5 && x<x2+5 && y>y1-5 && y<y1+5) /*resize up-right*/||
				(x>x1-5 && x<x1+5 && y>y2-5 && y<y2+5) /*resize down-lift*/||
				(x>x2-5 && x<x2+5 && y>y2-5 && y<y2+5) /*resize down-right*/) return true;

//		System.out.println("(x0,y0) = " + xPoints[0] + "," + yPoints[0]);
//		System.out.println("(x1,y1) = " + xPoints[1] + "," + yPoints[1]);
//		System.out.println("(x2,y2) = " + xPoints[2] + "," + yPoints[2]);
//		System.out.println("(x,y) = " + x + "," + y);
//		
		float m1 = ((float)(yPoints[1]) - yPoints[0]) / ((float) (xPoints[1] - xPoints[0])); //slope of right side
//		System.out.println("m1 = " + m1);
		float m2 = ((float)(yPoints[2] - yPoints[0])) / ((float) (xPoints[2] - xPoints[0])); // slope of lift side
//		System.out.println("m2 = " + m2);
		float n = ((float)(y - yPoints[0])) / ((float) (x - xPoints[0])); //slope of line between the top point and the point of selection
//		System.out.println("n = " + n);
		
		if( xPoints[1] <=  xPoints[2] && yPoints[0] <= yPoints[1]){ //right-down
			if(x<=xPoints[2] && x>=xPoints[1] && y>=yPoints[0] && y<=yPoints[1] && ((x<xPoints[0] && n<=m1 && n<=m2) || (x>xPoints[0] && n>=m2 && n>=m1))){
				System.out.println("Trianle " + ID + " Selected");
//				System.out.println("right-down");
				return true;
			}
		}else if( xPoints[1] <=  xPoints[2] ){ //right-up
			if(x<=xPoints[2] && x>=xPoints[1] && y<=yPoints[0] && y>=yPoints[1] && ((x<xPoints[0] && n>=m1 && n>=m2) || (x>xPoints[0] && n<=m2 && n<=m1))){
				System.out.println("Trianle " + ID + " Selected");
//				System.out.println("right-up");
				return true;
			}
		}else if(yPoints[0] <= yPoints[1]){ //left-down
			if(x<=xPoints[1] && x>=xPoints[2] && y>=yPoints[0] && y<=yPoints[1] && ((x<xPoints[0] && n<=m1 && n<=m2) || (x>xPoints[0] && n>=m2 && n>=m1))){
				System.out.println("Trianle " + ID + " Selected");
//				System.out.println("left-down");
				return true;
			}
		}else{ //left-up
			if(x>=xPoints[2] && x<=xPoints[1] && y<=yPoints[0] && y>=yPoints[1] && ((x<xPoints[0] && n>=m1 && n>=m2) || (x>xPoints[0] && n<=m2 && n<=m1))){
				System.out.println("Trianle " + ID + " Selected");
//				System.out.println("left-up");
				return true;
			}
		}
		return false;	
	}
	
	@Override
	public void resize(int x1, int y1, int x2, int y2,int resizePosition){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
        xPoints = new int[3];
        yPoints = new int[3];
        xPoints[0] = x1+(x2-x1)/2;
        yPoints[0] = y1;
        xPoints[1] = x1;
        yPoints[1] = y2;
        xPoints[2] = x2;
        yPoints[2] = y2;
	}
}
