package atunibz.dperez.approject1617.system;

import java.io.File;
import java.util.ArrayList;
import atunibz.dperez.approject1617.data.DataInspector;
import atunibz.dperez.approject1617.data.DataSorter;
import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.exceptions.InvalidXMLImportException;
import atunibz.dperez.approject1617.gui.linux.MainFrame;
import atunibz.dperez.approject1617.data.DataInspector.QueryAttribute;
import atunibz.dperez.approject1617.data.DataSorter.Sort;
import atunibz.dperez.approject1617.user.User;
import atunibz.dperez.approject1617.xml.XMLFactory;
import atunibz.dperez.approject1617.xml.XMLTracks;
import atunibz.dperez.approject1617.xml.XMLUsers;
import atunibz.dperez.approject1617.xml.XMLFactory.XMLType;
/**
 * Collection of static convenience methods to add/remove users and tracks, sort/query tracks and import tracks from external sources.<br>
 * This class uses the facade design pattern to wrap in a single, simplified class all the application's feature
 * which involve operations on data. Also reduces the code complexity of the whole application by reducing the data operations to 
 * simple calls to this class' methods. Since all methods are static, it is not even necessary to instantiate the object.
 * @author Davide Perez
 * @since 23/5/2017
 * @version 2.0
 *
 */
public class DataManager {
	//facade pattern: embedded functions of this complex classes combined here
	private static DataInspector searcher;
	private static DataSorter sorter;
	private static XMLFactory factory;
	private static XMLTracks data;
	private static XMLUsers users;
	
	
	/**
	 * Static inizialization of instance variables
	 */
	static{
		searcher = new DataInspector();
		sorter = new DataSorter();
		factory = XMLFactory.newInstance();
		data = (XMLTracks) factory.getXMLWriter(XMLType.TRACKS);
		users = (XMLUsers) factory.getXMLWriter(XMLType.USERS);
	}
	/**
	 * Convenience method for add() in {@link XMLUsers}.
	 * @param user the user to add
	 */
	public static void addUser(User user){
			users.add(user);
	}
	/**
	 * Convenience method for remove() in {@link XMLUsers}.
	 * @param username the username of the user to delete
	 */
	public static void deleteUser(String username){
		users.remove(username);
		APManagerSystem.getSystemLogger().warning(MainFrame.getMainFrame().getCurrentUser().getUsername() + " 's account has been deactivated");
	}
	/**
	 * Convenience method for add() in {@link XMLTracks}.
	 * @param track the track to add
	 */
	public static void addTrack(Track track){
		data.add(track);
		APManagerSystem.getSystemLogger().info("new track added");
	}
	/**
	 * Convenience method for deleteTrack() in {@link XMLTracks}.
	 * @param track Track object which corresponds to the track to delete
	 */
	public static void deleteTrack(Track track){
		data.deleteTrack(track);
		APManagerSystem.getSystemLogger().info("track deleted");
	}
	/**
	 * Convenience method for {@link XMLTracks#importTracks(File)}.
	 * @param dataSource the file with the tracks to import
	 * @throws InvalidXMLImportException if the file does not satisfy the {@linkplain atunibz.dperez.approject1617.xml.XMLImporter import policy}.
	 */
	public static void importTracks(File dataSource) throws InvalidXMLImportException{
		data.importTracks(dataSource);
		APManagerSystem.getSystemLogger().info("import completed");
	}
	/**
	 * Convenience method for {@link DataInspector#findBy(QueryAttribute, String)}.
	 * @param attribute the SearchAttribute specifying the field to search
	 * @param query the string to query
	 * @param toQuery the ArrayList of Tracks to query
	 * @return a call to the {@linkplain DataInspector#findBy(QueryAttribute, String) underlying method}
	 */
	public static ArrayList<Track> findBy(QueryAttribute attribute, String query, ArrayList<Track> toQuery){
		searcher.setData(toQuery);
		return searcher.findBy(attribute, query);
	}
	/**
	 * Convenience method for {@link DataSorter#sortBy(Sort)}.
	 * @param attribute an instance of the Sort enum
	 * @return a call to the {@linkplain DataSorter#sortBy(Sort) underlying method}.
	 */
	public static ArrayList<Track> sortBy(Sort attribute){
		return sorter.sortBy(attribute);
	}

}
