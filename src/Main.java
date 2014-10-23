

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;


public class Main extends JFrame {

    private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Main frame = new Main();
					frame.setVisible(true);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 813, 473);
		
		
		contentPane = new JPanel();
		
		final DrawRegion drawArea = new DrawRegion();
		final Options OptionPanel = new Options(drawArea);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		
		JMenuItem load = new JMenuItem("Load");
		fileMenu.add(load);
		load.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(drawArea.getShapes().size()!=0)
				{
					int selectedOption = JOptionPane.showConfirmDialog(null, "Do you want a new drawing page?", "Choose", JOptionPane.YES_NO_OPTION); 
					if (selectedOption == JOptionPane.YES_OPTION) 
					{
						OptionPanel.createNewPage();
					}
				}
				JFileChooser fc = new JFileChooser();
				fc.setAcceptAllFileFilterUsed(false); // remove all types of files
				fc.addChoosableFileFilter(new FileFilter() { // add only 1 type
					@Override
					public String getDescription() {
						return ".XML Files";
					}
					
					@Override
					public boolean accept(File f) {
						
						return f.getName().endsWith(".xml");
					}
				});
				fc.addChoosableFileFilter(new FileFilter() { // add only 1 type
					@Override
					public String getDescription() {
						return ".JSON Files";
					}
					
					@Override
					public boolean accept(File f) {
						
						return f.getName().endsWith(".json");
					}
				});
				int returnVal = fc.showOpenDialog(Main.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION) // user choose a file
				{
					//get file
					File file = fc.getSelectedFile();
					String extension = file.getAbsolutePath().substring( file.getAbsolutePath().lastIndexOf('.'), file.getAbsolutePath().length());
					
					if(extension.equalsIgnoreCase(".xml"))
					{
						XMLDealer load  = new XMLDealer(OptionPanel,drawArea,file.getAbsolutePath());
						load.loadXML();
					}
					else 
					{
						JSONDealer load = new JSONDealer(OptionPanel,drawArea,file.getAbsolutePath());
						load.loadJSON();
					}
				}
			}
		});
		
		JMenu mnSaveAs = new JMenu("Save as");
		fileMenu.add(mnSaveAs);
		
		JMenuItem saveXML = new JMenuItem("XML");
		mnSaveAs.add(saveXML);
		
		saveXML.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(Main.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) 
	            {
	                File file = fc.getSelectedFile();
	                String path = file.getAbsolutePath();
	                XMLDealer saver;
	                
	                if(path.contains(".xml"))
	                {
	                	path=path.replaceAll(".xml", "");
	                }
	               saver  = new XMLDealer(OptionPanel,drawArea,path+".xml");
	                saver.saveXML();
	            } 
	            else 
	            {
	                System.out.println("Save command cancelled by user.");
	            }
			}
		});
		
		JMenuItem saveJSON = new JMenuItem("JSON");
		mnSaveAs.add(saveJSON);
		
		saveJSON.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showSaveDialog(Main.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) 
	            {
	                File file = fc.getSelectedFile();
	                String path = file.getAbsolutePath();
	                
	                if(path.contains(".json"))
	                {
	                	path=path.replaceAll(".json", "");
	                }
	                
	                JSONDealer saver  = new JSONDealer(OptionPanel,drawArea,path+".json");
	                saver.saveJSON();
	            } 
	            else 
	            {
	                System.out.println("Save command cancelled by user.");
	            }
			}
		});
		
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		contentPane.setLayout(new BorderLayout(0, 0));
		
		OptionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLACK, Color.GRAY, null, null));
		contentPane.add(OptionPanel, BorderLayout.NORTH);
		contentPane.add(drawArea, BorderLayout.CENTER);
		
			
	}

}
