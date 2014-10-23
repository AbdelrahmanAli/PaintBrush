import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class JSONDealer 
{
	private Options optionsPanel;
	private DrawRegion drawArea;
	private String directory;
	
	public JSONDealer(Options optionsPanel, DrawRegion drawArea, String directory)
	{
		this.optionsPanel = optionsPanel;
		this.drawArea = drawArea;
		this.directory = directory;
	}
	
	public void loadJSON()
	{
			JSONParser parser = new JSONParser();
		try 
		{
			Object obj = parser.parse(new FileReader(directory));
			JSONObject jsonObject = (JSONObject) obj;
//////////////////////////////////////////////////////////////////////////////////
			// load classes
			long noOfClasses = (long) jsonObject.get("noOfClasses");
			for(int i = 0 ; i < noOfClasses ; i++)
			{
				JSONArray currentClass = (JSONArray) jsonObject.get("class"+i);
				Iterator<String> iterator = currentClass.iterator();
				
				String className = iterator.next();
				String path = iterator.next();
				 //class exist
				if(!path.equals("nopath") && !optionsPanel.getLoadedClasses().containsKey(className))
				{
					File file = new File(path);
					System.out.println("class "+file.getName()+" exist:"+file.exists());
					// check if the class exist in the correct path
					if(file.exists() && file.getName().substring(file.getName().lastIndexOf('.'), file.getName().length()).equals(".class"))
					{	optionsPanel.addClass(file,className);	}
				}
			}
//////////////////////////////////////////////////////////////////////////////////
			//load shapes
			long noOfShapes = (long) jsonObject.get("noOfShapes");
			for(int i = 0 ; i < noOfShapes ; i++)
			{
				// shapeClass >> 0 , X1 >> 1, Y1 >> 2, X2 >> 3, Y2 >> 4, stroke >> 5, color >> 6, id >> 7
				JSONArray currentShape = (JSONArray) jsonObject.get("shape"+i);
				
				Iterator<String> iterator = currentShape.iterator();
				String shapeClass = iterator.next();
				
				
				if(optionsPanel.getLoadedClasses().containsKey(shapeClass)) // if the class exist
				{
					
					int x1 = Integer.parseInt(iterator.next());
					int y1 = Integer.parseInt(iterator.next());
					int x2 = Integer.parseInt(iterator.next());
					int y2 = Integer.parseInt(iterator.next());
					int stroke = Integer.parseInt(iterator.next());
					int RGB[] = {Integer.parseInt(iterator.next()),Integer.parseInt(iterator.next()),Integer.parseInt(iterator.next())};
					int ID = optionsPanel.getLoadedClasses().size();
					
					//check if the class newly added or already exist
					if(optionsPanel.getPaths().containsKey(shapeClass)) // newly added
					{
						Class loaded = (Class) (Options.getLoadedClasses()).get(shapeClass);
						Shape loadedShape;
						try 
						{
							loadedShape = (Shape) loaded.newInstance();
							Color color = new Color(RGB[0], RGB[1], RGB[2]);
							loadedShape.setAll(x1, y1, x2, y2, stroke, color, ID);
							
							drawArea.getShapes().add(loadedShape);
						} 
						catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
						
						
					}
					else //already exist
					{

						// shapeClass >> 0 , X1 >> 1, Y1 >> 2, X2 >> 3, Y2 >> 4, stroke >> 5, color >> 6, id >> 7
						Shape loadedShape;
						try 
						{
							loadedShape = (Shape) Class.forName(shapeClass).newInstance();
							Color color = new Color(RGB[0], RGB[1], RGB[2]);
							loadedShape.setAll(x1, y1, x2, y2, stroke, color, ID);
							drawArea.getShapes().add(loadedShape);
						}
						catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {e.printStackTrace();}
					}
				}
			}
		}
		catch (IOException | ParseException e) {e.printStackTrace();}
		System.out.println("aaaaaaaaaaaaaaa");
		drawArea.drawing();
	}
	
	
	public static int[] stringToRGBColor(String color)
	{
		//R >> 0 , G >> 1 , B >> 2
		int RGB[] = new int[3];
		//java.awt.Color[r=0,g=0,b=0]
		color = color.substring(color.indexOf('[')+1,color.length()-1);
		RGB[0] = Integer.parseInt(color.substring(color.indexOf('=')+1,color.indexOf(',')));
		color = color.substring(color.indexOf(',')+1,color.length());
		RGB[1] = Integer.parseInt(color.substring(color.indexOf('=')+1,color.indexOf(',')));
		color = color.substring(color.indexOf(',')+1,color.length());
		RGB[2] = Integer.parseInt(color.substring(color.indexOf('=')+1,color.length())); 
		
		return RGB;
	}
	
	public void saveJSON()
	{
		JSONObject obj = new JSONObject();
		
		obj.put("noOfClasses", optionsPanel.getLoadedClasses().size());
		
		Iterator it = optionsPanel.getLoadedClasses().entrySet().iterator();
		int i = 0;
	    while (it.hasNext()) 
	    {
	    	Map.Entry pairs = (Map.Entry)it.next();
	    	
			JSONArray list = new JSONArray();
			list.add(pairs.getKey()); // class name
			
			if(optionsPanel.getPaths().containsKey(pairs.getKey()))// is added class
			list.add(optionsPanel.getPaths().get(pairs.getKey()));
			else list.add("nopath"); // is old class
			
			obj.put("class"+i, list);
			i++;
		}
		
	    obj.put("noOfShapes", drawArea.getShapes().size());
	    
	    for(i = 0 ; i < drawArea.getShapes().size() ; i++)
	    {
	    	// shapeClass >> 0 , X1 >> 1, Y1 >> 2, X2 >> 3, Y2 >> 4, stroke >> 5, color >> 6, id >> 7
	    	JSONArray list = new JSONArray();
	    	list.add(drawArea.getShapes().get(i).getClass().getName()); // class name
	    	list.add(drawArea.getShapes().get(i).getX1()+"");
	    	list.add(drawArea.getShapes().get(i).getY1()+""); 
	    	list.add(drawArea.getShapes().get(i).getX2()+""); 
	    	list.add(drawArea.getShapes().get(i).getY2()+""); 
	    	list.add(drawArea.getShapes().get(i).getStroke()+""); 
	    	int[] RGB = stringToRGBColor(drawArea.getShapes().get(i).getColor().toString());
	    	list.add(RGB[0]+"");
	    	list.add(RGB[1]+"");
	    	list.add(RGB[2]+"");
	    	obj.put("shape"+i, list);
	    }
	    
	    try {
	    	 
			FileWriter file = new FileWriter(directory);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
			System.out.println("JSON file saved.");
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
