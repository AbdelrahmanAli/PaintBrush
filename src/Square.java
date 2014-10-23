import java.awt.Color;
import java.awt.Graphics;


public class Square extends Shape {
	
	public Square() 
    {
    	super();
    	equalDimensions = true;
    }
    
	public Square(int x1,int y1,int stroke,Color color)
	{
		super(x1,y1,stroke,color);
		equalDimensions = true;
	}

	public Square(int x1, int y1, int x2, int y2, int stroke, Color color,int ID) 
	{
		
		super(x1, y1, x2, y2, stroke, color,ID);
		equalDimensions = true;
	}
	
	@Override
	public Square makeCopy()
	{
		Square copy = new Square(x1, y1, x2, y2, stroke, color,ID);
		copy.lastAction = lastAction;
		return copy;
	}

    @Override
	public void draw(Graphics g) 
	{
		g.setColor(color);
        int side;
        if(x1<x2 && y1<y2) // right down
        {
        	if(x2-x1 < y2-y1) side = x2-x1;
        	else side = y2 - y1;
        	g.fillRect(x1, y1, side, side);
        }
        else if(x1>x2 && y1<y2) // left down
        {
        	if(x1-x2 < y2-y1) side = x1-x2;
        	else side = y2 - y1;
        	g.fillRect(x2, y1, side, side);
        }
        else if(x1<x2 && y1>y2)
        {
        	if(x2-x1 < y1-y2) side = x2-x1;
        	else side = y1 - y2;
        	g.fillRect(x1, y2, side, side);
        }
        
        else if(x1>x2 && y1>y2)
        {
        	if(x1-x2 < y1-y2) side = x1-x2;
        	else side = y1 - y2;
        	g.fillRect(x2, y2, side, side);
        }
        
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
