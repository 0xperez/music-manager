package atunibz.dperez.approject1617.system;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.gui.linux.MainFrame;
import atunibz.dperez.approject1617.xml.XMLFactory;
import atunibz.dperez.approject1617.xml.XMLFactory.XMLType;
import atunibz.dperez.approject1617.xml.XMLTracks;

/**
 * This class binds shared system resources, providing a global access point to them to the different classes
 * which compose the application. Also manages the global logging and provides delegate methods to start and stop APManager, ensuring
 * that when the application runs all the resources have been already correctly initialized. <br>
 * The class is designed by following a simplified implementation of the context design pattern: it acts
 * as the "application" context, holding values that are referred anywhere inside the application by almost all classes,
 * such as the data list and the fonts (which are the same in every panel).
 * @author Davide Perez
 * @version 1.5
 * @since 25/5/2017
 *
 */
public final class APManagerSystem {

	// all font constants, lists, and stuff like this. Since they are final,
	//they can be made public because they are immutable
	/**
	 * The font used in the panel headers and titles
	 */
	public static final Font TITLE_FONT = new Font("Purisa", Font.BOLD, 40);
	/**
	 * The font used in all the labels
	 */
	public static final Font DEFAULT_FONT = new Font("Purisa", Font.BOLD, 18);
	/**
	 * The background used in the panels
	 */
	public static final Image DEFAULT_BACKGROUND = backgroundInit();
	/**
	 * File representing the default profile pictures folder
	 */
	public static final File PROFILE_PICS = new File("metadata/profilepics");
	/**
	 * File representing the users.xml file where the users credentials are stored
	 */
	public static final File USERS_FILE = new File("xml/users.xml");
	/**
	 * File representing the data.xml file where the tracks are stored
	 */
	public static final File DATA_FILE = new File("xml/data.xml");
	/**
	 * Represents the default profile picture, automatically used when the user does not select anyone
	 */
	public static final ImageIcon DEFAULT_PROFILEPIC = new ImageIcon("metadata/pics/0.png");
	/**
	 * {@link ArrayList} holding all the {@link Track} objects that are listed in the {@code data.xml} files
	 */
	private static ArrayList<Track> DATA;
	
	/**
	 * Global {@link Logger} instance. Although the Logger class provides a way to access to a global logger
	 * by default, but it is a pratice to avoid since may cause unexpected and unpredictable deadlocks
	 * due to the concurrent initialization of the {@linkplain java.util.logging.LogManager LogManager}.
	 * 
	 */
	private static Logger SYSTEM_LOGGER = Logger.getLogger("APLogger");
	
	/**
	 * Initializes the data list
	 */
	//loads DATA arraylist
	static{
		XMLFactory f = XMLFactory.newInstance();
		DATA = ((XMLTracks) f.getXMLWriter(XMLType.TRACKS)).getList();
		setupLogger();
	}
	
	/**
	 * Helper method that loads the background image. It is necessary to handle the {@link IOException} thrown by {@link ImageIO#read(File)}
	 * @return the background image
	 */
	private static Image backgroundInit() {
		Image backgroundImg = null;
		try {
			backgroundImg = ImageIO.read(new File("metadata/pics/canvas2.png"));
		} catch (IOException e) {
			SYSTEM_LOGGER.log(Level.SEVERE, "an exception occoured while loading the background image");
		}

		return backgroundImg;

	}
	
	/**
	 * Convenience method that calls {@link #refreshList()} and return {@link #DATA} again
	 * @return an updated instance of the data list
	 */
	//because otherwise when a modification was performed it could not be updated if the variable was final
	public static ArrayList<Track> getDataList(){
		refreshList();
		return DATA;
	}
	/**
	 * Updates the {@link #DATA} list. Must be invoked every time a track is added or deleted to ensure the 
	 * file content and the data list are not out of sync.
	 */
	//called every time DATA is retrieved to ensure it is always up to date
	private static void refreshList(){
		XMLFactory f = XMLFactory.newInstance();
		DATA = ((XMLTracks) f.getXMLWriter(XMLType.TRACKS)).getList();
	}
	
	/**
	 * getter for the global logger instance
	 * @return the logger instance
	 */
	public static Logger getSystemLogger(){
		return SYSTEM_LOGGER;
	}
	
	/**
	 * Sets up the logger so that log records are written into file depending on their {@link Level}.
	 */
	private static void setupLogger(){
		FileHandler fhAll = null;
		FileHandler fhWarn = null;
		FileHandler fhSev = null;
		SimpleFormatter form = new SimpleFormatter();
		try {
			fhAll = new FileHandler("log/all%g.txt", 3145728, 5, true);
			fhWarn = new FileHandler("log/warnings%g.txt", 2097152, 4, true);
			fhSev = new FileHandler("log/errors%g.txt", 1048576, 3, true);
		} catch (SecurityException | IOException e) {
			SYSTEM_LOGGER.log(Level.SEVERE, "error while initializing the logger");
			e.printStackTrace();
		}
		fhAll.setFormatter(form);
		fhWarn.setFormatter(form);
		fhSev.setFormatter(form);
		fhAll.setLevel(Level.INFO);
		//add a filter to log only warning messages. It is required to do so because
		//by setting the handler level to WARNING, also the SEVERE messages are logged
		fhWarn.setFilter(new Filter(){
			@Override
			public boolean isLoggable(LogRecord record) {
				return record.getLevel().equals(Level.WARNING);
			}
			
		});
		fhSev.setLevel(Level.SEVERE);
		SYSTEM_LOGGER.addHandler(fhAll);
		SYSTEM_LOGGER.addHandler(fhWarn);
		SYSTEM_LOGGER.addHandler(fhSev);
	}
	
	/**
	 * Starts the application adding a new thread on the Event Dispatch Thread.
	 */
	public static void start(){
		//ensures that the application starts only after all the graphic code in EDT has been correctly
		//initialized
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				MainFrame.getMainFrame();
				SYSTEM_LOGGER.info("application started");
			}
		});
	}
	
	
	/**
	 * Stops the application. This is a convenience method for {@link System#exit(int)}.
	 */
	public static void stop(){
		SYSTEM_LOGGER.info("application terminated");
		System.exit(0);
	}
}
