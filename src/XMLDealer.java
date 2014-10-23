import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLDealer 
{
	private Options optionsPanel;
	private DrawRegion drawArea;
	private String directory;
	
	public XMLDealer(Options optionsPanel, DrawRegion drawArea, String directory)
	{
		this.optionsPanel = optionsPanel;
		this.drawArea = drawArea;
		this.directory = directory;
	}
	
	public void loadXML()
	{
		
		//clear screen
		
		 SAXBuilder builder = new SAXBuilder();
		 File xmlFile = new File(directory);
		 
  
		 try 
		 {		
			// converted file to document object
			Document document = builder.build(xmlFile);
			// get root node from xml  
			Element rootNode = document.getRootElement();
			
				Element optionsXML = rootNode.getChild("Options");
				List<Element> classesNamesLoaded = (List<Element>) optionsXML.getChild("LoadedClassesNames").getChildren();
				
				// class name , path
				String[][] classesNamesArray = new String[classesNamesLoaded.size()][2];
				
				int i = 0;
				for(Element e: classesNamesLoaded)
			    {
					classesNamesArray[i][0] = e.getChildText("class");
					classesNamesArray[i][1] = e.getChildText("button");
					i++;
			    }
				
				addClasses(classesNamesArray);
				
				Element drawRegionXML = rootNode.getChild("DrawRegion");
				List<Element> shapesLoaded = (List<Element>) drawRegionXML.getChild("Shapes").getChildren();
				
				// shapeClass >> 0 , X1 >> 1, Y1 >> 2, X2 >> 3, Y2 >> 4, stroke >> 5, color >> 6
				String[][] shapesArray = new String[shapesLoaded.size()][7];
				
				i = 0;
				for(Element e: shapesLoaded)
			    {
					shapesArray[i][0] = e.getChildText("shapeClass");
					shapesArray[i][1] = e.getChildText("X1");
					shapesArray[i][2] = e.getChildText("Y1");
					shapesArray[i][3] = e.getChildText("X2");
					shapesArray[i][4] = e.getChildText("Y2");
					shapesArray[i][5] = e.getChildText("stroke");
					shapesArray[i][6] = e.getChildText("color");
					i++;
			    }
				
				addShapes(shapesArray);
				System.out.println("aaaaaaaaaaaaaaa");
			
		 }
		 catch (JDOMException | IOException e) {e.printStackTrace();}  
	 
		 
	}
	
	public void addClasses(String[][] classesNamesArray)
	{
		for(int i = 0 ; i < classesNamesArray.length ; i++)
		{
			String className = classesNamesArray[i][0];
			String path = classesNamesArray[i][1];
			if(!path.equals("nopath") && !optionsPanel.getLoadedClasses().containsKey(className))
			{
				File file = new File(path);
				System.out.println("class "+file.getName()+" exist:"+file.exists());
				if(file.exists() && file.getName().substring(file.getName().lastIndexOf('.'), file.getName().length()).equals(".class"))
				{	optionsPanel.addClass(file,className);	}
			}
			
		}
	}
	
	public void addShapes(String[][] shapesArray)
	{
			System.out.println(optionsPanel.getLoadedClasses().size());
		for(int i = 0 ; i < shapesArray.length ; i++)
		{
			System.out.println("232323");
			if(optionsPanel.getLoadedClasses().containsKey(shapesArray[i][0])) // if the class exist
			{
				System.out.println("asdascacasd");
				int RGB[] = stringToRGBColor(shapesArray[i][6]);
				//check if the class newly added or already exist
				if(optionsPanel.getPaths().containsKey(shapesArray[i][0])) // newly added
				{
					Class loaded = (Class) (Options.getLoadedClasses()).get(shapesArray[i][0]);
					Shape loadedShape;
					try 
					{
						System.out.println("hhhhhhhh");
						loadedShape = (Shape) loaded.newInstance();
						
						int x1 = Integer.parseInt(shapesArray[i][1]);
						int y1 = Integer.parseInt(shapesArray[i][2]);
						int x2 = Integer.parseInt(shapesArray[i][3]);
						int y2 = Integer.parseInt(shapesArray[i][4]);
						int stroke = Integer.parseInt(shapesArray[i][5]);
						Color color = new Color(RGB[0], RGB[1], RGB[2]);
						int ID =optionsPanel.getLoadedClasses().size(); 
						loadedShape.setAll(x1, y1, x2, y2, stroke, color, ID);
						
						drawArea.getShapes().add(loadedShape);
					} 
					catch (InstantiationException | IllegalAccessException e) {e.printStackTrace();}
					
					
				}
				else //already exist
				{
System.out.println("zzzz");
					// shapeClass >> 0 , X1 >> 1, Y1 >> 2, X2 >> 3, Y2 >> 4, stroke >> 5, color >> 6
					Shape loadedShape;
					try 
					{
						loadedShape = (Shape) Class.forName(shapesArray[i][0]).newInstance();
						
						int x1 = Integer.parseInt(shapesArray[i][1]);
						int y1 = Integer.parseInt(shapesArray[i][2]);
						int x2 = Integer.parseInt(shapesArray[i][3]);
						int y2 = Integer.parseInt(shapesArray[i][4]);
						int stroke = Integer.parseInt(shapesArray[i][5]);
						Color color = new Color(RGB[0], RGB[1], RGB[2]);
						int ID =optionsPanel.getLoadedClasses().size();
						loadedShape.setAll(x1, y1, x2, y2, stroke, color, ID);
						System.out.println("ccccc");
						drawArea.getShapes().add(loadedShape);
					}
					catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {e.printStackTrace();}
				}
			}
			
			
		}
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
			
	
	public void saveXML()
	{
		 try {
			 
				Element classes = new Element("classes");
				Document doc = new Document();
				doc.setRootElement(classes);
		 
				Element options = new Element("Options");
//				options.addContent(new Element("option").setText(optionsPanel.getOption()+""));
//				options.addContent(new Element("className").setText(optionsPanel.getClassName()));
				
				Element loadedClassesNames = new Element("LoadedClassesNames");
				
				System.out.println("bi "+optionsPanel.getLoadedClasses().size());

				Iterator it = optionsPanel.getLoadedClasses().entrySet().iterator();
				System.out.println("ai "+optionsPanel.getLoadedClasses().size());

				int i = 0;
			    while (it.hasNext()) 
			    {
			        Map.Entry pairs = (Map.Entry)it.next();
					System.out.println("pair "+optionsPanel.getLoadedClasses().size());
					
					String key = (String)pairs.getKey();
					
			        Element loadedClass = new Element("classElement"); // class name
			        Element className = new Element("class").setText(pairs+"");
			        loadedClass.addContent(className);
			        Element classPath;
			        //class path
			        if(optionsPanel.getPaths().get(pairs.getKey()+"")!=null)
			        {
			         classPath = new Element("button").setText((String) optionsPanel.getPaths().get(pairs.getKey()));
			        }
			        else
			        {
			         classPath = new Element("button").setText("nopath");
			        }
			        loadedClass.addContent(classPath);
			        loadedClassesNames.addContent(loadedClass);
			        i++;
			    }
			    options.addContent(loadedClassesNames);

				doc.getRootElement().addContent(options);
		 
				Element drawRegion = new Element("DrawRegion");
				Element shapes = new Element("Shapes");
				Element myShape = new Element("shape");
				for(int k = 0 ; k < drawArea.getShapes().size() ; k++)
				{
					Element shapeClass = new Element("shapeClass").setText(drawArea.getShapes().get(k).getClass().getName());
					Element x1 = new Element("X1").setText(drawArea.getShapes().get(k).getX1()+"");
					Element y1 = new Element("Y1").setText(drawArea.getShapes().get(k).getY1()+"");
					Element x2 = new Element("X2").setText(drawArea.getShapes().get(k).getX2()+"");
					Element y2 = new Element("Y2").setText(drawArea.getShapes().get(k).getY2()+"");
					Element stroke = new Element("stroke").setText(drawArea.getShapes().get(k).getStroke()+"");
					Element color = new Element("color").setText(drawArea.getShapes().get(k).getColor()+"");
					
					myShape.addContent(shapeClass);
					myShape.addContent(x1);
					myShape.addContent(y1);
					myShape.addContent(x2);
					myShape.addContent(y2);
					myShape.addContent(stroke);
					myShape.addContent(color);
					
					shapes.addContent(myShape);
					myShape = new Element("shape");
				}
				drawRegion.addContent(shapes);
		 
				doc.getRootElement().addContent(drawRegion);
		 
				// new XMLOutputter().output(doc, System.out);
				XMLOutputter xmlOutput = new XMLOutputter();
		 
				// display nice nice
				xmlOutput.setFormat(Format.getPrettyFormat());
				//create file to save in
				FileOutputStream stream = new FileOutputStream(directory);
				xmlOutput.output(doc, new FileWriter(directory));
				System.out.println("s "+optionsPanel.getLoadedClasses().size());
				System.out.println("File Saved!");
				//optionsPanel.fix();
				System.out.println("final "+optionsPanel.getLoadedClasses().size());
			  } catch (IOException io) {
				System.out.println(io.getMessage());
			  }
	}

}
