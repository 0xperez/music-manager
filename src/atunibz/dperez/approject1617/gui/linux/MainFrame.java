package atunibz.dperez.approject1617.gui.linux;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import atunibz.dperez.approject1617.data.DataInspector.QueryAttribute;
import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.system.DataManager;
import atunibz.dperez.approject1617.user.User;

/**
 * The top-level window in which the current application's panel is displayed. This class defines
 * a set of menus providing information about the current session and giving the user access to a set 
 * of operations which includes exit the application, logout and permanently deleting the account.
 * Designed by using the singleton design pattern.
 * @author Davide Perez
 * @version 2.0
 * @since 18/5/2017
 */
public class MainFrame extends JFrame {
	/**
	 * Unique instance of the class (the application requires only a single MainFrame)
	 */
	private static final MainFrame instance = new MainFrame();
	/**
	 * The currently logged in user
	 */
	private static User currentUser;
	/**
	 * Specifies the number of tracks added by the current user during this session
	 */
	public static int CURRENT_SESSION_ADDED = 0;
	/**
	 * Specifies the number of tracks deleted by the current user during this session
	 */
	public static int CURRENT_SESSION_DELETED = 0;
	/**
	 * Menu bar containing all the menus
	 */
	
	private JMenuBar menuBar;
	/**
	 * Menu containing the options to get current user's info and to logout
	 */
	private JMenu userMenu;
	/**
	 * This menu acts as an "indicator" to show which user is currently logged in
	 */
	private JMenu loggedInMenu;
	/**
	 * Menu item which allows the user to see info about his profile and activities
	 */
	private JMenuItem profileItem;
	/**
	 * Menu item which allows the user to log out
	 */
	private JMenuItem logoutItem;
	
	/**
	 * Constructor with no parameters. Sets up the frame graphic components and adds a {@link LoginPanel} to the content pane.
	 */
	private MainFrame() {
		setTitle("AP Collection Manager 2017");
		setPreferredSize(new Dimension(850, 620));
		setUpMenu();
		setJMenuBar(menuBar);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.getContentPane().add(new LoginPanel());
		pack();
		setVisible(true);
		//setResizable(false);
		//adds a listener to ask confirm when closing the MainFrame
		this.addWindowListener(new WindowListener(){
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				if(JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to quit the application?", 
				"APManager2017", JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
					APManagerSystem.stop();
			}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
	}
	
	/**
	 * Sets up the menus and their items
	 */
	private void setUpMenu() {
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem exitItem = new JMenuItem("Exit");
		JMenuItem aboutItem = new JMenuItem("About...");
		//loads the "About" item. Gives some info about me and some technical info
		aboutItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String about = "This application has been developed by Davide Perez Cuevas, student of Computer Science Bachelor\n at Free University of Bolzano, "
							+ "as a project for the Advanced Programming exam.";
				String info = "Data file path: " + APManagerSystem.DATA_FILE.getAbsolutePath()+
						"\nCurrent number of tracks saved: " + APManagerSystem.getDataList().size() +
						"\nUser file path: " + APManagerSystem.USERS_FILE.getAbsolutePath();
				String metrics = "Number of files: 36\nNumber of classes: 76 (inner and anonyomous classes included)\nNumber of methods: 268\nLines: 5458";
				JOptionPane.showMessageDialog(MainFrame.this, about + "\n\n" + info + "\n\n" + metrics, "About", JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});
		menuBar.add(menu);
		menu.add(aboutItem);
		menu.add(exitItem);
		menu.addSeparator();
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = JOptionPane.showConfirmDialog(MainFrame.this, "Do you want to exit?", "APManager2017",
						JOptionPane.YES_NO_OPTION);
				if (returnVal == JOptionPane.YES_OPTION){
					APManagerSystem.stop();
				}
			}
		});
		userMenu = new JMenu("Profile");
		logoutItem = new JMenuItem("Logout");
		userMenu.setEnabled(false);
		loggedInMenu = new JMenu("Currently logged in as: -");
		loggedInMenu.setEnabled(false);
		logoutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to log out?",
						"APManager2017", JOptionPane.YES_NO_OPTION);
				if (returnVal == JOptionPane.YES_OPTION) {
					APManagerSystem.getSystemLogger().info("session terminated. " + currentUser.getUsername() + " logged out");
					currentUser = null;
					CURRENT_SESSION_ADDED = 0;
					CURRENT_SESSION_DELETED = 0;
					loggedInMenu.setText("Currently logged in as: -");
					loggedInMenu.setEnabled(false);
					userMenu.setEnabled(false);
					swapContentPane(new LoginPanel());
				}
			}
		});
		menuBar.add(userMenu);
		menuBar.add(loggedInMenu);
		profileItem = new JMenuItem("My info");
		userMenu.add(profileItem);
		userMenu.add(logoutItem);
		profileItem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showDialog();
				
			}
			
		});
	}
	
	/**
	 * Convenience method that invokes {@link BufferedImage#getScaledInstance(int, int, int)} in order
	 * to get a scaled instance of the current user's profile picture.
	 * @return a scaled version of the original profile picture
	 */
	public ImageIcon getScaledInstance(){
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new File(currentUser.getProfilePicPath()));
		} catch (IOException e) {
			APManagerSystem.getSystemLogger().severe("error while loading profile picture");
			e.printStackTrace();
		}
		Image scaledImage = bufferedImage.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon scaledProfilePic = new ImageIcon(scaledImage);//get a scaled profile image
		//String userInfo = "Hello, " + currentUsername + "!";
		
		//JOptionPane.showMessageDialog(MainFrame.this, userInfo, "APManager2017", JOptionPane.INFORMATION_MESSAGE, scaledProfilePic);
		return scaledProfilePic;
	}
	
	/**
	 * Shows user's info in a dialog window when the corresponding menu item is pressed. It is possible for
	 * the user to see some of his usage statistics, such as the total number of records added since the registration and 
	 * the number of added and deleted records for the current session. Also allows the user to deactivate his account. <p>
	 * <b>Note:</b> when an account is deactivated, it is permanently deleted and there is no way to recover the credentials.
	 * If a user deactivates the account and successively wants to login again, he will have to create a new profile.
	 */
	private void showDialog(){
		String[] choices = {"STATS", "BACK", "DEACTIVATE ACCOUNT"};
		int returnVal = JOptionPane.showOptionDialog(MainFrame.this, "Hello, " + currentUser.getUsername() + "!", "APManager2017", JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, getScaledInstance(), choices, choices[0]);
		
		//if STATS, count how many tracks did the current user add by using the findBy(ADDED BY) method of DataInspector.class
		if(returnVal == 0){
			String currentUsername = MainFrame.getMainFrame().getCurrentUser().getUsername();
			ArrayList<Track> entries = DataManager.findBy(QueryAttribute.ADDED_BY, currentUsername , APManagerSystem.getDataList());//returns all the tracks ADDED by the current user
			String message = "You added " + CURRENT_SESSION_ADDED + " tracks and deleted " + CURRENT_SESSION_DELETED + " tracks during this session!\n";
			JOptionPane.showMessageDialog(MainFrame.this, message + "Since you registered to APManager2017, you added " + 
			entries.size() + " tracks!", "Stats - " + currentUsername, JOptionPane.INFORMATION_MESSAGE, getScaledInstance());
		}
		//BACK (value 1 of the String[])
		if(returnVal == 1)
			return;
		//DEACTIVATE (value 2 of String[])
		if(returnVal == 2){
			int choice = JOptionPane.showConfirmDialog(MainFrame.this, "Are you sure you want to deactivate your account?\n Your " + 
			"account will be permanently deleted and you won't be able to access the database anymore", "APManager2017", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION){
				JOptionPane.showMessageDialog(MainFrame.this, "Thank you for having used APManager2017!", "APManager2017", JOptionPane.OK_OPTION);
				DataManager.deleteUser(currentUser.getUsername());
				currentUser = null;
				loggedInMenu.setText("Currently logged in as: -");
				loggedInMenu.setEnabled(false);
				userMenu.setEnabled(false);
				swapContentPane(new LoginPanel());
			}
		}	
	}
	
	/**
	 * Changes the current view of the MainFrame.
	 * @param contentPane the new panel to show
	 */
	public void swapContentPane(JPanel contentPane) {

		JPanel currentContentPane = (JPanel) getContentPane();
		currentContentPane.removeAll();
		currentContentPane.add(contentPane);
		currentContentPane.revalidate();
		currentContentPane.repaint();
	}
	
	/**
	 * Getter for the singleton instance 
	 * @return the instance of MainFrame
	 */
	public static MainFrame getMainFrame() {
		return instance;
	}
	/**
	 * Getter for the current user
	 * @return the currently logged in user
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	/**
	 * Setter for the current user
	 * @param user the user to set as current user
	 */
	public void setCurrentUser(User user) {
		MainFrame.currentUser = user;
	}
	/**
	 * Getter for the user menu
	 * @return the user menu
	 */
	public JMenu getUserMenu() {
		return userMenu;
	}
	/**
	 * Getter for the menu that indicates the currently logged in user
	 * @return the logged in menu
	 */
	public JMenu getLoggedInMenu() {
		return loggedInMenu;
	}

}
