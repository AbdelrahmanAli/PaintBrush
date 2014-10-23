import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;


public class DrawRegion extends JPanel {
    
    private static int initialX,initialY,finalX,finalY,pressedX,pressedY,selectedShape=-1,resizePosition;
    private static boolean pressed,dragged,moved;
    private static Shape shape, copy;
    public static ArrayList<Shape> shapes;
    
    public DrawRegion() 
    {
        setBackground(Color.WHITE);
    	pressed = dragged = moved =false;
		shapes = new ArrayList();
		shape = null; // no object is being drawn yet
		addMouseMotionListener(new MouseActionClass());
		addMouseListener(new MouseActionClass());
	}
    
    public void clearShapes()
    {
    	for (int i = shapes.size()-1; i >=0; i--) shapes.remove(i);
    	Options.setUndoButton(false);
    	Options.setRedoButton(false);
    }
    
    public void drawing()
    {
       repaint();
    }
    
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(int i = 0 ; i < shapes.size() ; i++)	shapes.get(i).draw(g);
        if(selectedShape>-1) {   
        	shapes.get(selectedShape).drawSelected(g);
        	System.out.println("sele" + selectedShape);
        }else if(pressed && dragged){
        	shape.draw(g);
        }
    }
	
	private class MouseActionClass extends MouseInputAdapter
	{
		@Override
		public void mousePressed(MouseEvent event) 
		{
			pressedX = event.getX();
			pressedY = event.getY();
            
            // 0 >> Line , 1 >> Circle , 2 >> Ellipse , 3 >> Triangle , 4 >> Rectangle , 5 >> Square

			if(Options.getOption() != 100 && !Options.isLoadedButton()) // old shape
			{
				try 
				{
					if( Class.forName(Options.getClassName()).newInstance() instanceof Shape)
					{
						shape =  (Shape) Class.forName(Options.getClassName()).newInstance();
						shape.setSome(pressedX,pressedY,1,Options.getColor());
					}
					else System.out.println("mousePressed(): Class "+Options.getClassName()+" is not a child.");
				}
				catch (InstantiationException e) {e.printStackTrace();}
				catch (IllegalAccessException e) {e.printStackTrace();}
				catch (ClassNotFoundException e) {System.out.println("mousePressed(): Class "+Options.getClassName()+" Not Found");}
			}
			else if(Options.getOption() != 100 && Options.isLoadedButton())//loaded shape
			{
				Class loaded = (Class) (Options.getLoadedClasses()).get(Options.getClassName());
				try 
				{
					shape = (Shape) loaded.newInstance();
					shape.setSome(pressedX,pressedY,1,Options.getColor());
				} 
				catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
			}
						
			else if(Options.getOption() == 100) // select button
			{
				for(int i = shapes.size()-1; i>=0; i--)
				{
        			if(shapes.get(i).checkSelection(pressedX, pressedY))
        			{	
        				selectedShape = i;
        				
        				// Getting copy of the Object before Resizing or Moving
                  		copy = shapes.get(selectedShape).makeCopy();
                		copy.setLastAction(3);
        				
        				// Points before Resizing or Moving
        				initialX = shapes.get(i).x1; 
        				initialY = shapes.get(i).y1;
        				finalX = shapes.get(i).x2;
        				finalY = shapes.get(i).y2;
        				        				
        				// Getting the position of resizing or moving
                		resizePosition = shapes.get(selectedShape).checkResize(pressedX, pressedY);

        				System.out.println("Selected Shape id = "+i);
        				break;
        			}
        			else  selectedShape = -1;
        		}   
			}
    		drawing();
            if(shape!=null)	pressed = true;
		}
		
		@Override
		public void mouseDragged(MouseEvent event) 
		{
			
			if(event.getX()>=0 && event.getX()<=getWidth() && event.getY()>=0 && event.getY()<=getHeight())
            {
            	if(shape!=null && Options.getOption()!=100){ //during drawing
            		
            		selectedShape=-1;
            		
            		if(shape.getEqualDimmension()) // if the shape has equal dimensions
            		{
            			if(event.getX()<=(pressedX+getHeight()-pressedY) && event.getX()>=(pressedX-getHeight()+pressedY) )	finalX = event.getX();
            			if(event.getY() <= (pressedY + getWidth() - pressedX) && event.getY() >= (pressedY - getWidth() + pressedX))	finalY = event.getY();
            		}
            		else
            		{
            			finalX = event.getX();
            			finalY = event.getY();
            		}
            		// set x2,y2 for drawing
            		shape.setX2(finalX); 
            		shape.setY2(finalY);
                
            
            	}else if(Options.getOption()==100 && selectedShape>-1){ //during Moving or resizing

            		moved = true;
            		
            		int x1 = initialX+event.getX()-pressedX;
            		int y1 = initialY+event.getY()-pressedY;
            		int x2 = finalX+event.getX()-pressedX;
            		int y2 = finalY+event.getY()-pressedY;
            		
            		if(resizePosition==1){ //up-left >>1 >> x1 ,y1 changed
            			if(x1 >= finalX-10) x1 = finalX-10;
            			if(y1 >= finalY-10) y1 = finalY-10;
            			shapes.get(selectedShape).resize(x1,y1,finalX,finalY,resizePosition);
            	
            		}else if(resizePosition==2){ // up-right >>2 >> x2 ,y1 changed
            			if(y1 >= finalY-10) y1 = finalY-10;
            			if(x2 <= initialX+10) x2 = initialX+10;
            			shapes.get(selectedShape).resize(initialX,y1,x2,finalY,resizePosition);
            		
            		}else if(resizePosition==3){ //down-left >>3 >> x1 ,y2 changed
            			if(x1 >= finalX-10) x1 = finalX-10;
            			if(y2 <= initialY+10) y2 = initialY+10;
            			shapes.get(selectedShape).resize(x1,initialY,finalX,y2,resizePosition);
            		
            		}else if(resizePosition==4){ //down-right >>4 >> x2 ,y2 changed
            			if(x2 <= initialX+10) x2 = initialX+10;
            			if(y2 <= initialY+10) y2 = initialY+10;
            			shapes.get(selectedShape).resize(initialX,initialY,x2,y2,resizePosition);
            		
            		}else/* move >>0 >> x1,y1,x2,y2 changed */ shapes.get(selectedShape).resize(x1,y1,x2,y2,resizePosition); 		
            	}
        	
            	dragged = true;            	
        		drawing();
            }
            
            
            
		}
		
		
		@Override
		public void mouseReleased(MouseEvent event) 
		{
            if(dragged && !moved){ //Create a Shape
            	shape.adjustDimensions(); //adjust Xs and Ys
            	shape.setID(shapes.size()); // Assign ID , must be handled when a shape is deleted
           		shape.setLastAction(1);
           		shapes.add(shape); // add Shape
           		shapes.get(shapes.size()-1).putInUndoStack();
           		Options.setUndoButton(true);
           		Options.setRedoButton(false);
                //clear redo stack
    			for (int i = Options.getRedoStack().size()-1; i >=0; i--) 	Options.getRedoStack().remove(i);
           		
            }else if(dragged){ //adding the copy of the moved object before moving
            	copy.putInUndoStack();
        		//clear redo stack
    			for (int i = Options.getRedoStack().size()-1; i >=0; i--) 	Options.getRedoStack().remove(i);
           		Options.setUndoButton(true);
           		Options.setRedoButton(false);
            }       
   			pressed = false; // stop drawing shape
			shape = null; // get ready for new shape
            dragged = false;
            moved = false;
            resizePosition = 0;
            
		}
		
	}
	
	public static ArrayList<Shape> getShapes()
	{
		return shapes;
	}
	
	public static int getSelectedShape(){
		return selectedShape;
		
	}
	public static void setSelectedShape(int i){
		selectedShape = i;
	}
}
