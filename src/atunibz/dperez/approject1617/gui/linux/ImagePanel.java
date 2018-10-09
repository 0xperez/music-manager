package atunibz.dperez.approject1617.gui.linux;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.system.DataManager;
import atunibz.dperez.approject1617.user.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/**
 * Instances of this class represent a panel where it is possible for a user to choose a profile picture.
 * The user can choose between a set of predefined pictures, upload another image or skip this passage. In this 
 * case, a default profile picture is assigned to the user.
 * @author Davide Perez
 * @version 2.0
 * @since 27/5/2017
 */
public class ImagePanel extends JPanel {
	/**
	 * ArrayList of ImageIcon objects
	 */
	private ArrayList<ImageIcon> images;
	/**
	 * ImageIcon representing the currently selected profile image
	 */
	private ImageIcon currentlySelectedImage;
	/**
	 * Central JPanel which shows the predefined pictures
	 */
	private JPanel picPanel; //contains the pics
	/**
	 * Scroll pane to provide a scroll to the picture panel
	 */
	private JScrollPane scrollPane;
	/**
	 * JButton to skip the profile picture selection
	 */
	private JButton skipBtn, 
	/**
	 * JButton to import a customed profile picture
	 */
					importBtn, 
	/**
	 * JButton to return to the LoginPanel without choosing any picture
	 */
					backBtn;
	
	/**
	 * Instance of a {@link SwingWorker}.
	 */
	private ThumbnailWorker picLoader;
	
	/**
	 * Constructor of the panel
	 */
	public ImagePanel(){
		picLoader = new ThumbnailWorker();
		setLayout(new BorderLayout());
		setUpComponents();
		APManagerSystem.getSystemLogger().fine("initialization completed");
	}
	/**
	 * Initializes the graphic components and sets up the UI.
	 */
	private void setUpComponents(){
		JLabel headerLbl = new JLabel("Please clik to select a profile picture...");
		headerLbl.setFont(APManagerSystem.TITLE_FONT);
		headerLbl.setBorder(new EmptyBorder(new Insets(3, 5, 3, 5)));
		this.add(headerLbl, BorderLayout.PAGE_START);
		
		picPanel = new JPanel();
		scrollPane = new JScrollPane();
		picLoader.execute();
		
		skipBtn = new JButton("SKIP");
		importBtn = new JButton("IMPORT FROM DISK...");
		importBtn.addActionListener(new ImportPicListener());
		backBtn = new JButton("BACK");
		backBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = JOptionPane.showConfirmDialog(ImagePanel.this, "Leave this page? No profile pic will be set", "APManager2017", JOptionPane.YES_NO_OPTION);
				if(returnVal == JOptionPane.YES_OPTION){
					User currentUser = MainFrame.getMainFrame().getCurrentUser();
					currentUser.setProfilePicPath(APManagerSystem.DEFAULT_PROFILEPIC.toString());
					DataManager.addUser(currentUser);
					MainFrame.getMainFrame().setCurrentUser(null);
					MainFrame.getMainFrame().swapContentPane(new LoginPanel());
				}
				else return;
			}
		});
		skipBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = JOptionPane.showConfirmDialog(ImagePanel.this, "Leave this page? No profile pic will be set", "APManager2017", JOptionPane.YES_NO_OPTION);
				if(returnVal == JOptionPane.YES_OPTION){
					User currentUser = MainFrame.getMainFrame().getCurrentUser();
					currentUser.setProfilePicPath(APManagerSystem.DEFAULT_PROFILEPIC.toString());
					DataManager.addUser(currentUser);
					MainFrame.getMainFrame().setCurrentUser(currentUser);
					MainFrame.getMainFrame().swapContentPane(new DataPanel());
				}
			}
			
		});
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		JLabel bottomLbl = new JLabel("...or import one!");
		bottomLbl.setFont(APManagerSystem.TITLE_FONT);
		JPanel btnPanel = new JPanel();
		btnPanel.add(backBtn);
		btnPanel.add(importBtn);
		btnPanel.add(skipBtn);
		bottomPanel.add(bottomLbl);
		bottomPanel.add(btnPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Listener that allows the import of a customed image file that will be setted as the user's profile picture.
	 * @author Davide Perez
	 * @since 28/7/2017
	 * @version 1.0
	 *
	 */
	public class ImportPicListener implements ActionListener{
		/**
		 * JFileChooser to navigate the system and choose the picture
		 */
		private JFileChooser chooser;
		/**
		 * Filter to allow the selection of pictures exclusively
		 */
		private FileNameExtensionFilter filter;
		
		/**
		 * Constructor of the class
		 */
		ImportPicListener(){
			chooser = new JFileChooser();
			filter = new FileNameExtensionFilter("Image files", "jpeg", "png", "jpg");
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.addChoosableFileFilter(filter);
		}
		
		/**
		 * Copies a file from a path to the default pictures folder of the application. 
		 * @param source the file to be copied
		 * @return a File reference with the new location as path
		 * @throws IOException if an IO problem occurs
		 */
		private File copyInDefaultFolder(File source) throws IOException{
			Path sourcePath = source.toPath();
			//adds the username + ppap1617 to the filename
			String destName = "metadata/profilepics/customed/" + MainFrame.getMainFrame().getCurrentUser().getUsername() + "ppap1617." + getFileExtension(source);
			Path destPath = Paths.get(System.getProperty("user.dir") + "/" + destName);
			//Path destPath = Paths.get(System.getProperty("user.dir") + "/metadata/profilepics/customed/" + MainFrame.getMainFrame().getCurrentUser().getUsername() + "ppap1617." + getFileExtension(source));
			Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
			return new File(destName);
		}
		
		/**
		 * Gets the extension of a file. If the file has no extension, returns an empty string.
		 * @param file a file
		 * @return the extension of the file
		 */
		private String getFileExtension(File file){
			String fileName = file.getName();
			if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) //if the filename does contain a . and does not begin with a .
				return fileName.substring(fileName.lastIndexOf(".") + 1);         //then return the string representing the extension, i.e what is after the .
			return "";															  //else the file does not have an extension
		}
		
		/**
		 * Allows the user to choose a profile picture from outside the application environment. Copies the 
		 * chosen picture in the default folder and sets it as the profile picture of the user.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			int returnVal = chooser.showOpenDialog(ImagePanel.this);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File importedFile = chooser.getSelectedFile();
				try {
					File profilePic = copyInDefaultFolder(importedFile);
					User currentUser = MainFrame.getMainFrame().getCurrentUser();
					currentUser.setProfilePicPath(profilePic.getPath());
					DataManager.addUser(currentUser);
					APManagerSystem.getSystemLogger().info("an external picture has been imported");
				} catch (IOException e) {
					APManagerSystem.getSystemLogger().warning("unable to import the profile picture");
					JOptionPane.showMessageDialog(ImagePanel.this, "Could not import thie profile picture. The default profile picture will be setted", "APManager2017", JOptionPane.WARNING_MESSAGE);
					User currentUser = MainFrame.getMainFrame().getCurrentUser();
					currentUser.setProfilePicPath(APManagerSystem.DEFAULT_PROFILEPIC.toString());//set default profile pic
					DataManager.addUser(currentUser);
				}
				JOptionPane.showMessageDialog(ImagePanel.this, "Welcome to APManager2016, " + MainFrame.getMainFrame().getCurrentUser().getUsername() + "!" , "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
				APManagerSystem.getSystemLogger().info("logged in as " + MainFrame.getMainFrame().getCurrentUser().getUsername());
				MainFrame.getMainFrame().swapContentPane(new DataPanel());
			}
			
		}
		
	}
	
	/**
	 * MouseListener that allows the user to select one of the predefined profile images by clicking on it.
	 * @author Davide Perez
	 * @version 1.3
	 * @since 28/5/2017
	 */
	private class SelectionListener implements MouseListener{
		/**
		 * Gets an ImageIcon out of a JLabel and sets it as the user's profile picture.
		 * @throws NullPointerException if the JLabel does not have an ImageIcon
		 * @param thumbnail a JLabel containing an ImageIcon
		 */
		public void setAsProfilePic(JLabel thumbnail){
			int returnVal = JOptionPane.showConfirmDialog(ImagePanel.this, "Do you want to choose this as profile picture?", "APManager2017", JOptionPane.YES_NO_OPTION);
			if(returnVal == JOptionPane.YES_OPTION){
				currentlySelectedImage = (ImageIcon) thumbnail.getIcon();
				MainFrame.getMainFrame().getCurrentUser().setProfilePicPath(currentlySelectedImage.toString());
				DataManager.addUser(MainFrame.getMainFrame().getCurrentUser());
				JOptionPane.showMessageDialog(ImagePanel.this, "Welcome to APManager2017!", "APManager2017", JOptionPane.INFORMATION_MESSAGE);
				APManagerSystem.getSystemLogger().info("logged in as " + MainFrame.getMainFrame().getCurrentUser().getUsername());
				MainFrame.getMainFrame().swapContentPane(new DataPanel());

			}
			else{
				picPanel.removeAll();
				for(ImageIcon current : images){
					JLabel pic = new JLabel(current);
					pic.setBorder(new LineBorder(Color.BLACK));
					pic.addMouseListener(new SelectionListener());
					picPanel.add(pic);
				}
				picPanel.revalidate();
				picPanel.repaint();
				
			}
		}
		/**
		 * Adds a red border to the currently selected picture.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			JLabel current = (JLabel) e.getComponent();
			current.setBorder(new LineBorder(Color.RED, 5));
			APManagerSystem.getSystemLogger().fine("clicked");
			picPanel.repaint();
			picPanel.revalidate();
			setAsProfilePic(current);
			
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	
	//loads the images from the picture. When the images are > 10 or even when
	//swapping from a panel to this, there is a sensible delay because the EDT waits 
	//for the images to be loaded and blocks any other grafical update. By running the 
	//code on a worker thread, there is no delay
	/**
	 * This SwingWorker reads the pictures from the default profile picture folder of the application and 
	 * loads it in the panel. Reading the profile pictures, especially when they are more than 10, becomes
	 * quite a time-consuming task: since the code involving graphics is all executed on the Event Dispatch Thread, 
	 * the GUI "freezes" until all the pictures have been loaded and the application experiences a sensible delay.
	 * To solve this problem the operation of loading the picture's thumbnails is executed on a SwingWorker, which
	 * runs in a background thread performing GUI indipendent tasks and create no delay to the application.
	 * @author Davide Perez
	 * @version 1.5
	 * @since 29/5/2017
	 * @see SwingWorker
	 *
	 */
	private class ThumbnailWorker extends SwingWorker<ArrayList<ImageIcon>, Void>{
		/**
		 * The total number of default profile pictures. Since the panel in which they will be
		 * shown uses GridLayout, the total number can be seen as the total "area" of the grid
		 * (which is a rectangular grid) 
		 */
		private int area; //"area" of the rectangle given from the total of the pics
		/**
		 * Specifies how many pictures will be put in a single row (x dimension of the grid)
		 */
		private int x = 4;
		/**
		 * Specifies how many pictures will be put in the columns (y dimension, depends on the fixed x)
		 */
		private int y = 0;
		
		/**
		 * Reads the content of the folder containing the default profile picture files into
		 * an ArrayList of ImageIcon objects.<p>
		 * <b>Note:</b> Swing is <b>not thread safe.</b> Swing threading policy states that all the GUI code must run on the EDT, hence the 
		 * code running in this method should not interact with the GUI (step that should be performed by {@link #done()}.
		 */
		@Override
		protected ArrayList<ImageIcon> doInBackground() throws Exception {
			APManagerSystem.getSystemLogger().fine("loading profile pictures in background");
			ArrayList<ImageIcon> pics = new ArrayList<ImageIcon>();
			File[] picsToFile = APManagerSystem.PROFILE_PICS.listFiles();
			area = picsToFile.length;
			for(File current : picsToFile){
				if(!current.isDirectory())
					pics.add(new ImageIcon(current.getPath()));
			}
			y = (int) area/x + (area%x > 0 ? 1 : 0);
			//the "height" of the rectangle representing the pics is evaluated in the following way;
			//if the division between the area and the x (fixed length) has a remainder, then an additional
			//row is added to the total (because more space is needed to fit the remaining pics, however the 
			//last row will not be completely filled unless I don't have a multiple of 4 as pic number)
			//Recall: casting always cuts the decimal part (round down)
			return pics;
		}
		
		
		/**
		 * The code in this method is executed directly in the EDT. Updates the GUI by creating
		 * a JLabel for every ImageIcon and adding it in the related panel.
		 */
		//the code in done() is executed directly in the EDT after doInBackground() has finished. So it is sure to invoke ui updates from here.
		//remember to revalidate and repaint when adding ProfluePics
		protected void done(){
				APManagerSystem.getSystemLogger().fine("profile pictures loaded. Updating UI");
			try {
				images = this.get();
				for(ImageIcon current : images){
					JLabel pic = new JLabel(current);
					pic.setBorder(new LineBorder(Color.BLACK));
					pic.addMouseListener(new SelectionListener());
					picPanel.add(pic);
				}
				picPanel.setLayout(new GridLayout(x, y, 1, 1));
				scrollPane.setViewportView(picPanel);
				add(scrollPane, BorderLayout.CENTER);
				revalidate();
				repaint();
			} catch (InterruptedException | ExecutionException e) {
				//these exception should never be thrown, but they are logged for a matter of
				//completeness
				APManagerSystem.getSystemLogger().severe("critical error in a background thread");
				e.printStackTrace();
			}
			

		}
		
	}
	

}
