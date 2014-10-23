import java.awt.Color;
import java.awt.Graphics;


public class Circle extends Shape {
	
	public Circle() 
    {
    	super();
    	equalDimensions = true;
    }
	
	public Circle (int x1,int y1,int stroke,Color color)
	{
		super(x1,y1,stroke,color);
		equalDimensions = true;
	}

	public Circle(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		super(x1, y1, x2, y2, stroke, color,ID);
//		System.out.println("(x1,y1) = "+ x1 + "," + y1);
//		System.out.println("(x2,y2) = "+ x2 + "," + y2);
		equalDimensions = true;
	}
	
	@Override
	public Circle makeCopy()
	{
		Circle copy = new Circle(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}

	@Override
	public void draw(Graphics g) 
	{
//		g.setColor(Color.RED);
//		g.drawLine(x1, y1, x2, y2);
		
		g.setColor(color);
        int diameter;
        if(x1<x2 && y1<y2) // right down
        {
        	if(x2-x1 < y2-y1) diameter = x2-x1;
        	else diameter = y2 - y1;
        	g.fillOval(x1, y1, diameter, diameter);	
        }
        else if(x1>x2 && y1<y2) // left down
        {
        	if(x1-x2 < y2-y1) diameter = x1-x2;
        	else diameter = y2 - y1;
        	g.fillOval(x2, y1, diameter, diameter);
        }
        else if(x1<x2 && y1>y2)
        {
        	if(x2-x1 < y1-y2) diameter = x2-x1;
        	else diameter = y1 - y2;
        	g.fillOval(x1, y2, diameter, diameter);
        }
        
        else if(x1>x2 && y1>y2)
        {
        	if(x1-x2 < y1-y2) diameter = x1-x2;
        	else diameter = y1 - y2;
        	g.fillOval(x2, y2, diameter, diameter);
        }
        
	}
	
	@Override
	public boolean checkSelection(int x, int y)
	{
		int xCenter = 0;
		int yCenter = 0;
		int radius = 0;
		
		if( 	(x>x1-5 && x<x1+5 && y>y1-5 && y<y1+5) /*resize up-lift*/||
				(x>x2-5 && x<x2+5 && y>y1-5 && y<y1+5) /*resize up-right*/||
				(x>x1-5 && x<x1+5 && y>y2-5 && y<y2+5) /*resize down-lift*/||
				(x>x2-5 && x<x2+5 && y>y2-5 && y<y2+5) /*resize down-right*/) return true;
		
		if(x1<x2 && y1<y2) // right down
		{
	       	if(x2-x1 < y2-y1) radius = (x2-x1)/2;
	       	else radius = (y2 - y1)/2;	
	    }
	    else if(x1>x2 && y1<y2) // left down
	    {
	       	if(x1-x2 < y2-y1) radius = (x1-x2)/2;
	       	else radius = (y2 - y1)/2;
	    }
	    else if(x1<x2 && y1>y2)
	    {
	      	if(x2-x1 < y1-y2) radius =(x2-x1)/2;
	       	else radius = (y1 - y2)/2;
        }	        
	    else if(x1>x2 && y1>y2)
	    {
	      	if(x1-x2 < y1-y2) radius =(x1-x2)/2;
	       	else radius = (y1 - y2)/2;
        }
		xCenter = x1+radius;
		yCenter = y1+radius;
		
//		System.out.println("Center = "+ xCenter +"," +yCenter);
//		System.out.println("radius = " + radius);
//		System.out.println("(x,y) = "+ x+","+y);
		
		if(  Math.pow(x-xCenter,2)+Math.pow(y-yCenter,2) <= Math.pow(radius,2) ){
			System.out.println("Circle "+ ID + " Selected");
			return true;
		}
		
		return false;
	}
	
	@Override
    public void adjustDimensions(){
    	int side;
        if(x1<x2 && y1<y2) // right down
        {
        	if(x2-x1 < y2-y1) side = x2-x1;
        	else side = y2 - y1;
        	x2 = x1+side;
        	y2 = y1+side;
        }
        else if(x1>x2 && y1<y2) // left down
        {
        	if(x1-x2 < y2-y1) side = x1-x2;
        	else side = y2 - y1;
        	x1 = x2;
        	x2 = x1+side;
        	y2 = y1+side;
        }
        else if(x1<x2 && y1>y2)
        {
        	if(x2-x1 < y1-y2) side = x2-x1;
        	else side = y1 - y2;
        	y1 = y2;
        	x2 = x1+side;
        	y2 = y1+side;
        }
        
        else if(x1>x2 && y1>y2)
        {
        	if(x1-x2 < y1-y2) side = x1-x2;
        	else side = y1 - y2;
        	x1 = x2;
        	y1 = y2;
        	x2 = x1+side;
        	y2 = y1+side;
        }

    }
    
    @Override
	public void resize(int x1, int y1, int x2, int y2,int resizePosition){
		int side;
		if(x2-x1<y2-y1) side = x2-x1;
		else side = y2-y1;
		
    	if(resizePosition==0){ /* move >>0 >> x1,y1,x2,y2 changed */
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		
		}else if(resizePosition==1){//up-left >>1 >> x1 ,y1 changed
			this.x1 = x2-side;
			this.y1 = y2-side;
			this.x2 = x2;
			this.y2 = y2;
		
		}else if(resizePosition==2){// up-right >>2 >> x2 ,y1 changed
			this.x1 = x1;
			this.y1 = y2-side;
			this.x2 = x1+side;
			this.y2 = y2;
			
		}else if(resizePosition==3){//down-left >>3 >> x1 ,y2 changed
			this.x1 = x2-side;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y1+side;
			
		}else if(resizePosition==4){//down-right >>4 >> x2 ,y2 changed
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x1+side;
			this.y2 = y1+side;
			
		}
    }
}
