import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;


public class Options extends JPanel {

    /**
     * Create the panel.
     */
    private JButton colorChoosen;
    private static Color color = (Color.BLACK);
    private static int option = 0; // Initially line is selected
	// 0 >> Line , 1 >> Circle , 2 >> Ellipse , 3 >> Triangle , 4 >> Rectangle , 5 >> Square
	private static String className; 
    private static JButton undo, redo;
	final JPanel shapePanel;
	private JPanel panel;
	private JSeparator separator;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JButton select;
	private DrawRegion drawArea;
	private JSeparator separator_3;
	private JSeparator separator_4;
	private static ArrayList<Shape> undoStack,redoStack;
	private JSeparator separator_5;
	public JButton newPage;
	private JSeparator separator_6;
	private static ArrayList<JButton> shapesButtonList;
	private Box box;
	private static ArrayList<String> subclassesNames;
	private JButton load;
	private static boolean buttonType; // false >> old | true >> new
	private static HashMap<String,Class> loadedClasses;
	private static HashMap<String,String> paths;
	private JButton delete;

	
	public Options(final DrawRegion drawArea)
	{
		this.drawArea = drawArea;
		loadedClasses = new HashMap<String,Class>();
		paths = new HashMap<String,String>();;
		subclassesNames = new ArrayList();
		// 0 >> Line , 1 >> Circle , 2 >> Ellipse , 3 >> Triangle , 4 >> Rectangle , 5 >> Square
		subclassesNames.add("Line");
		subclassesNames.add("Triangle");
		subclassesNames.add("Circle");
		subclassesNames.add("Ellipse");
		subclassesNames.add("Rectangle");
		subclassesNames.add("Square");
		
		initializeButtons();
		
		
		
		
		undoStack = new ArrayList();
		redoStack = new ArrayList();
//////////////////////////////////////////////////////////////////////////////////////////////	
		colorChoosen = new JButton();
		colorChoosen.setPreferredSize(new Dimension(30, 30));
		colorChoosen.setBackground(color);
		
		colorChoosen.addActionListener(

			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					
					Color prevColor = color;
					
					color = JColorChooser.showDialog(null, "Choose Color", color);
					if(color==null) color = (prevColor);
					
					colorChoosen.setBackground(color);
					
					if(!select.isEnabled() && drawArea.shapes.size()>0 && DrawRegion.getSelectedShape()>=0) 
					{	
						// adding the previous state to the undo stack
						Shape prevState = drawArea.shapes.get(drawArea.getSelectedShape()).makeCopy();
						prevState.setLastAction(2);
						undoStack.add(prevState); 
						
						// adding the final state to shapes
						drawArea.shapes.get(drawArea.getSelectedShape()).setColor(color); 
						drawArea.shapes.get(drawArea.getSelectedShape()).setLastAction(2);
						
						//Clearing the redo stack
						for (int i = redoStack.size()-1; i >=0; i--) 	redoStack.remove(i);
						
		           		Options.setUndoButton(true);
		           		Options.setRedoButton(false);
						
						drawArea.drawing();
					}
				}
			}
		
		);
		
///////////////////////////////////////////////////////////////////////////////////
		select = new JButton("");
		
		select.setPreferredSize(new Dimension(30, 30));
		select.setOpaque(false);
		select.setContentAreaFilled(false);
		select.setBorderPainted(false);
		
		select.setIcon(new ImageIcon(Options.class.getResource("/Pics/select.png")));
		
		select.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				select.setEnabled(false);
				for(int i = 0 ; i < shapesButtonList.size() ; i++)
				{
					shapesButtonList.get(i).setEnabled(true);
				}
				
				option = 100;

			}
		});
///////////////////////////////////////////////////////////////////////////////////
		
		separator = new JSeparator();
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setBackground(Color.LIGHT_GRAY);
		separator_1.setForeground(Color.GRAY);
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setForeground(Color.GRAY);
		separator_2.setBackground(Color.LIGHT_GRAY);
		
		separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setForeground(Color.GRAY);
		separator_3.setBackground(Color.LIGHT_GRAY);
		
/////////////////////////////////////////////////////////////////////////////////
		undo = new JButton("");
		undo.setPreferredSize(new Dimension(30, 30));
		undo.setOpaque(false);
		undo.setContentAreaFilled(false);
		undo.setBorderPainted(false);
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{

				DrawRegion.setSelectedShape(-1);
				if(!undoStack.isEmpty())
				{
					Shape retrievedShape = undoStack.get(undoStack.size()-1); //getting last change
					undoStack.remove(undoStack.size()-1); // delete that change from the undo stack
					
					int id = retrievedShape.getID();
					int action = retrievedShape.getLastAction();
										
					switch(action)
					{
						case 1: /* delete created object */ System.out.println("Undo Creating... Deleted >>ID = "+id);
						
							Shape copy1 = drawArea.shapes.get(id).makeCopy(); //copy the current state to be added in the redo stack
							copy1.setLastAction(1);
							redoStack.add(copy1); // save current shape in redo stack
							
							drawArea.shapes.remove(drawArea.shapes.size()-1); // delete it
							
							break;
						
						case 2: /* Undo color changed */ System.out.println("Undo Color Changing... Color Changed >>ID = "+id);
							
							Shape copy2 = drawArea.shapes.get(id).makeCopy(); //copy the current state to be added in the redo stack
							copy2.setLastAction(2);
							redoStack.add(copy2); // save current shape in redo stack
							
							drawArea.shapes.get(id).setColor(retrievedShape.getColor()); //rechange color
							
							break;
						
						case 3: /* Undo movement or resizing */ System.out.println("Undo Movement/Resizing... Moved/Resized >>ID = "+id);
							
							Shape copy3 = drawArea.shapes.get(id).makeCopy(); //copy the current state to be added in the redo stack
							copy3.setLastAction(3);
							redoStack.add(copy3); //save current shape in redo stack
							
							drawArea.shapes.get(id).resize(retrievedShape.x1,retrievedShape.y1,retrievedShape.x2,retrievedShape.y2,0); //redo move
							
							break;

						case 4: /* Undo Deleting */ System.out.println("Undo Deleting... Recreated >>ID = "+id);
							Shape copy4 = retrievedShape.makeCopy();
							copy4.setLastAction(4);
							redoStack.add(copy4);
							
							drawArea.shapes.get(id).setAll(retrievedShape.x1, retrievedShape.y1, retrievedShape.x2, retrievedShape.y2, retrievedShape.stroke, retrievedShape.color, id);
							
							break;
					}	
					
					drawArea.drawing();
				}
				
				redo.setEnabled(true);
				if(undoStack.isEmpty())undo.setEnabled(false);
						
			}
		});
		undo.setIcon(new ImageIcon(Options.class.getResource("/pics/undo.png")));
		
/////////////////////////////////////////////////////////////////////////////////
		redo = new JButton("");
		redo.setIcon(new ImageIcon(Options.class.getResource("/pics/redo.png")));
		redo.setPreferredSize(new Dimension(30, 30));
		redo.setOpaque(false);
		redo.setContentAreaFilled(false);
		redo.setBorderPainted(false);
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{

				DrawRegion.setSelectedShape(-1);

				if(!redoStack.isEmpty())
				{
					Shape retrievedShape = redoStack.get(redoStack.size()-1).makeCopy();
					redoStack.remove(redoStack.size()-1); // delete existing element
					
					int id = retrievedShape.getID();
                    int action = retrievedShape.getLastAction();

					switch(action)
					{
						case 1: /* Recreat the deleted object */ System.out.println("Recreate the object... >> ID = "+id);
                           						
							Shape copy1 = retrievedShape.makeCopy(); //copy the current state to be added in the undo stack
							copy1.setLastAction(1);
							undoStack.add(copy1); //save current shape in undo stack
						
							drawArea.shapes.add(retrievedShape.makeCopy());	// create te shape						
                           
							break;
					
						case 2: /* Rechange the color */ System.out.println("Redo the Color... >> ID = "+id);
								
							Shape copy2 = drawArea.shapes.get(id).makeCopy(); //copy the current state to be added in the undo stack
							copy2.setLastAction(2);
							undoStack.add(copy2); //save current shape in undo stack
							
							drawArea.shapes.get(id).setColor(retrievedShape.getColor());
							
							break;
					
						case 3: /* Redo the Movement */ System.out.println("Redo the Movement... >> ID = "+id);
							
							Shape copy3 = drawArea.shapes.get(id).makeCopy(); //copy the current state to be added in the undo stack
							copy3.setLastAction(3);
							undoStack.add(copy3); //save current shape in undo stack
							
							drawArea.shapes.get(id).resize(retrievedShape.x1,retrievedShape.y1,retrievedShape.x2,retrievedShape.y2,0);							
							
							break;

						case 4: /* Redo the Deleting  */ System.out.println("Redo the Deleting... >> ID = "+id);
							
							Shape copy4 = retrievedShape.makeCopy(); //copy the current state to be added in the redo stack
							copy4.setLastAction(4);
							undoStack.add(copy4); // save current shape in redo stack
						
							drawArea.shapes.get(id).setAll(1, 1, 1, 1, 1, color.white, id);
							drawArea.shapes.get(id).setLastAction(4);
							break;
					} 

					drawArea.drawing();
				}
				undo.setEnabled(true);
				if(redoStack.isEmpty())redo.setEnabled(false);
			}
		});
//////////////////////////////////////
		
		separator_4 = new JSeparator();
		
		separator_5 = new JSeparator();
		separator_5.setOrientation(SwingConstants.VERTICAL);
		separator_5.setForeground(Color.GRAY);
		separator_5.setBackground(Color.LIGHT_GRAY);
		
////////////////////////////////////
		
		newPage = new JButton("");
		newPage.setPreferredSize(new Dimension(30, 30));
		newPage.setOpaque(false);
		newPage.setContentAreaFilled(false);
		newPage.setBorderPainted(false);
		newPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				createNewPage();
			}

			
		});
		newPage.setIcon(new ImageIcon(Options.class.getResource("/pics/new_page.png")));

//////////////////////////////////////
		
		separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setForeground(Color.GRAY);
		separator_6.setBackground(Color.LIGHT_GRAY);
		
///////////////////////////////////
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		shapePanel = new JPanel();
		scrollPane.setViewportView(shapePanel);
		shapePanel.setLayout(new BoxLayout(shapePanel, BoxLayout.X_AXIS));
		for (int i = 0; i < shapesButtonList.size(); i++) 
		{
			if(i%2 == 0)
			{
	            box = Box.createVerticalBox();
	            box.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	        }
	        box.add(shapesButtonList.get(i));
	        shapePanel.add(box);
		}
/////////////////////////////////////////////////////////////////////////
		
		load = new JButton("");
		load.setIcon(new ImageIcon(Options.class.getResource("/pics/load.png")));
		load.setPreferredSize(new Dimension(30, 30));
		load.setOpaque(false);
		load.setContentAreaFilled(false);
		load.setBorderPainted(false);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				JFileChooser fc = new JFileChooser();
				fc.setAcceptAllFileFilterUsed(false); // remove all types of files
				fc.addChoosableFileFilter(new FileFilter() { // add only 1 type
					@Override
					public String getDescription() {
						return ".class Files";
					}
					
					@Override
					public boolean accept(File f) {
						
						return f.getName().endsWith(".class");
					}
				});
				
				int returnVal = fc.showOpenDialog(Options.this);
				if(returnVal == JFileChooser.APPROVE_OPTION) // user choose a file
				{
					//get file
					File file = fc.getSelectedFile();
					
					//get file name
					final String theClassName = file.getName().substring(0,file.getName().indexOf('.'));
					addClass(file,theClassName);
	
				}

				
			}
		});
		/////////////////////
		
		delete = new JButton("");
		delete.setPreferredSize(new Dimension(30, 30));
		delete.setOpaque(false);
		delete.setContentAreaFilled(false);
		delete.setBorderPainted(false);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				if(drawArea.shapes.size()>0 && drawArea.getSelectedShape()>-1) {
					
					Shape copy = drawArea.shapes.get(drawArea.getSelectedShape()).makeCopy();
					copy.setLastAction(4);
	           		copy.putInUndoStack();
					
					drawArea.shapes.get(drawArea.getSelectedShape()).setAll(1, 1, 1, 1, 1, color.white, drawArea.getSelectedShape());
	           		
	           		Options.setUndoButton(true);
					Options.setRedoButton(false);
					
					drawArea.setSelectedShape(-1);
					
	    			for (int i = Options.getRedoStack().size()-1; i >=0; i--) 	Options.getRedoStack().remove(i);
					
					drawArea.drawing();
				}
			}
		});
delete.setIcon(new ImageIcon(Options.class.getResource("/pics/delete.png")));
		
		
		
		

//////////////////////////////////////////////////////////////////////////////////////////////

		
		
		
		
		
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
					.addGap(2)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(colorChoosen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(select, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(undo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(redo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(delete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_5, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(newPage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(load, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_6, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addGap(443)
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(separator_4, GroupLayout.PREFERRED_SIZE, 42, Short.MAX_VALUE)
					.addGap(66))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(34)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(colorChoosen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addComponent(separator_2, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
							.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
							.addComponent(separator_3, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(10)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(newPage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(redo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(undo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(load, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(delete, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(8))
							.addComponent(separator_5, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
							.addComponent(separator_6, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(select, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(26, Short.MAX_VALUE))
		);
		
		
		
		setLayout(groupLayout);
	}
	
	public void initializeButtons()
	{
		shapesButtonList = new ArrayList();
		boolean firstTimeInClassFound = true;
		int addedClassCounter = 0;
		for(int i = 0 ; i < subclassesNames.size() ; i++)
		{
			try 
			{
				// check if the instance from the class is an instance of the Shape class
				if( Class.forName(subclassesNames.get(i)).newInstance() instanceof Shape)
				{
					// add to the hashmap
					loadedClasses.put(subclassesNames.get(i),Class.forName(subclassesNames.get(i)));
					
					shapesButtonList.add(new JButton());
					
					shapesButtonList.get(addedClassCounter).setPreferredSize(new Dimension(30, 30));
					shapesButtonList.get(addedClassCounter).setOpaque(false);
					shapesButtonList.get(addedClassCounter).setContentAreaFilled(false);
					shapesButtonList.get(addedClassCounter).setBorderPainted(false);

					//File pic = new File(Options.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"\\pics\\"+subclassesNames.get(i).toLowerCase()+".png");

					// if there exist a picture
					if((Options.class.getResource("/pics/"+subclassesNames.get(i).toLowerCase()+".png")!=null))
					shapesButtonList.get(addedClassCounter).setIcon(new ImageIcon(Options.class.getResource("/pics/"+subclassesNames.get(i).toLowerCase()+".png")));
					else 
					shapesButtonList.get(addedClassCounter).setIcon(new ImageIcon(Options.class.getResource("/pics/new.png")));
					
					final int index = addedClassCounter;
					final int nameIndex = i;
					shapesButtonList.get(addedClassCounter).addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {

							shapesButtonList.get(index).setEnabled(false);
							select.setEnabled(true);
							for(int j = 0 ; j < shapesButtonList.size() ; j++)
							{
								if(j!=index)
								shapesButtonList.get(j).setEnabled(true);
							}
							option = index;
							className = subclassesNames.get(nameIndex);
							buttonType = false;
						}
					});
					
					if(firstTimeInClassFound)
					{
						shapesButtonList.get(option).setEnabled(false);
						className = subclassesNames.get(i);
						firstTimeInClassFound = false;
					}
					
					addedClassCounter++;
					
					
				}
				else System.out.println("addButtons(): Class is not a child.");
			}
			catch (ClassNotFoundException e) {System.out.println("addButtons(): Class is missing.");} 
			catch (InstantiationException e) {e.printStackTrace();}
			catch (IllegalAccessException e) {e.printStackTrace();}
			
			
		}
	}
	
	public void addClass(File file,final String theClassName)
	{
		if(loadedClasses.get(theClassName) == null) // prevent class duplication
		{
			//get file directory
			String fileDirectory = file.getAbsolutePath().substring(0,file.getAbsolutePath().length()-6-theClassName.length());
		
			File directory = new File(fileDirectory);
			URL url;
			try 
			{
				url = directory.toURI().toURL();
				URL[] urls = new URL[]{url};
				ClassLoader cl = new URLClassLoader(urls);
				Class cls = cl.loadClass(theClassName);
				
				paths.put(theClassName,file.getAbsolutePath()); // any old class doesn't exist in paths hashmap
				// creating button
				// check if the instance from the class is an instance of the Shape class
				shapesButtonList.add(new JButton());
				
				loadedClasses.put(theClassName,cls);
				
				shapesButtonList.get(shapesButtonList.size()-1).setPreferredSize(new Dimension(30, 30));
				shapesButtonList.get(shapesButtonList.size()-1).setOpaque(false);
				shapesButtonList.get(shapesButtonList.size()-1).setContentAreaFilled(false);
				shapesButtonList.get(shapesButtonList.size()-1).setBorderPainted(false);

						// if there exist a picture
				if((Options.class.getResource("/pics/"+theClassName.toLowerCase()+".png")!=null))
				shapesButtonList.get(shapesButtonList.size()-1).setIcon(new ImageIcon(Options.class.getResource("/pics/"+theClassName.toLowerCase()+".png")));
				else 
				shapesButtonList.get(shapesButtonList.size()-1).setIcon(new ImageIcon(Options.class.getResource("/pics/new.png")));
						
				final int index = shapesButtonList.size()-1;
				shapesButtonList.get(shapesButtonList.size()-1).addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						shapesButtonList.get(index).setEnabled(false);
						select.setEnabled(true);
						for(int j = 0 ; j < shapesButtonList.size() ; j++)
						{
							if(j!=index)
							shapesButtonList.get(j).setEnabled(true);
						}
						option = index;
						className = theClassName;
						buttonType = true;
					}
				});
				
				if((shapesButtonList.size()-1)%2==0)
				{
					box = Box.createVerticalBox();
		            box.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		        }
		        box.add(shapesButtonList.get(shapesButtonList.size()-1));
		        shapePanel.add(box);
			
		        shapePanel.revalidate();
			}
			catch (MalformedURLException | ClassNotFoundException e1) {e1.printStackTrace();}
		}
		else JOptionPane.showMessageDialog(null,"Class Already Exist.","Error Message",JOptionPane.ERROR_MESSAGE );
	}
	
	public int actionKind()
	{
		
		return 0; //no action
	}
	
	public static Color getColor()
	{
		return color;
	}
	
	public static int getOption()
	{
		return option;
	}
	public static String getClassName()
	{
		return className;
	}
	public static ArrayList<JButton> getShapesButtonList()
	{
		return shapesButtonList;
	}
	
	public static ArrayList<Shape> getUndoStack()
	{
		return undoStack;
	}
	
	public static ArrayList<Shape> getRedoStack()
	{
		return redoStack;
	}
	public static HashMap getLoadedClasses()
	{
		return loadedClasses;
	}
	public static HashMap getPaths()
	{
		return paths;
	}
	public static boolean isLoadedButton()
	{
		return buttonType;
	}
	public void createNewPage() 
	{
		drawArea.clearShapes(); // clear shapes
		// clear stacks
		
		for (int i = undoStack.size()-1; i >=0; i--) 	undoStack.remove(i);
		for (int i = redoStack.size()-1; i >=0; i--) 	redoStack.remove(i);
		
		drawArea.drawing();
	}
	public static void setUndoButton(boolean enable){
		undo.setEnabled(enable);
	}
	public static void setRedoButton(boolean enable){
		redo.setEnabled(enable);
	}
	public static void fix()
	{
		for(int i = 0 ; i < subclassesNames.size() ; i++)
		{
			try {
				if( Class.forName(subclassesNames.get(i)).newInstance() instanceof Shape)
				{
					// add to the hashmap
					loadedClasses.put(subclassesNames.get(i),Class.forName(subclassesNames.get(i)));
				}
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}





































//final JComboBox shapeList = new JComboBox(picViewName);
//
//shapeList.addItemListener
//(
//		new ItemListener()
//		{
//			public void itemStateChanged(ItemEvent event)
//			{
//				if(event.getStateChange() == ItemEvent.SELECTED)
//				{
//					picture.setIcon(shapes[shapeList.getSelectedIndex()]);
//					shapeChoosen = shapeList.getSelectedIndex();
//				}
//			}
//		}
//		
//);









//Image[] images = new Image[6];
//for (int i = 0; i < 6; i++)
//{
//	try 
//	{
//		images[i] = ImageIO.read(getClass().getResource("/pics/"+picName[i])) ;
//	} 
//	catch (IOException e) {
//		e.printStackTrace();
//	}
//	images[i] = images[i].getScaledInstance(30,30,0);
//}