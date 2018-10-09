package atunibz.dperez.approject1617.data;

import java.util.ArrayList;
import atunibz.dperez.approject1617.system.APManagerSystem;
/**
 * Utility class to query the internal tracklist. Depending on one of the track's
 * parameter, an instance of this class returns an {@link ArrayList} of Tracks
 * containing the tracks that match the query parameter.
 * @author Davide Perez
 * @version 1.3
 * @since 27/7/2017
 *
 */
public class DataInspector {
	/**
	 * Enumerated type representing the field of a {@link Track} under which the query
	 * will be made.
	 * @author Davide Perez
	 * @version 1.0
	 * @since 27/7/2017
	 *
	 */
	public enum QueryAttribute{
		/**
		 * Represents the title field
		 */
		TITLE,
		/**
		 * Represents the artist field
		 */
		ARTIST, 
		/**
		 * Represents the album field
		 */
		ALBUM,
		/**
		 * Represents the gender field
		 */
		GENDER,
		/**
		 * Represents the label field
		 */
		LABEL,
		/**
		 * Represents the label field
		 */
		WRITER,
		/**
		 * Represents the year field
		 */
		YEAR,
		/**
		 * Represents the addedBy field
		 */
		ADDED_BY
	}
	/**
	 * A reference to the internal tracklist, required to query tracks even
	 * when the user starts a query in a sorted version of such list.
	 */
	private ArrayList<Track> dataList;
	
	
	/**
	 * Constructs a DataInspector object and initializes its field
	 */
	public DataInspector(){
		dataList = APManagerSystem.getDataList();
	}
	/**
	 * Queries the data under a specified attribute. This is the only public method
	 * and it invokes the right helper method depending on the attribute specified
	 * @param attribute a Track's field
	 * @param query the String to query
	 * @return an ArrayList of Tracks whose elements satisfy the query
	 */
	public ArrayList<Track> findBy(QueryAttribute attribute, String query){
		
		switch(attribute){
			case TITLE: return findByTitle(query);
			case ARTIST: return findByArtist(query);
			case ALBUM: return findByAlbum(query);
			case LABEL: return findByLabel(query);
			case WRITER: return findByWriter(query);
			case GENDER: return findByGender(query);
			case YEAR: return findByYear(Integer.parseInt(query));
			case ADDED_BY: return findByAddedBy(query);
		}
		return null;
	}
	
	/**
	 * Queries the data by the track's title. Includes results that does not
	 * exactly match the query, but are similar to it, to make the query more flexible
	 * @param title a String representing the title
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByTitle(String title){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getTitle().equalsIgnoreCase(title) || current.getTitle().toLowerCase().contains(title.toLowerCase()))
				result.add(current);
		}
		return result;	
	}
	/**
	 * Queries the data by the track's artist. Since many tracks have more than one
	 * artist, it is not necessary for the query to exactly match a track's artist
	 * field.
	 * @param artist a String representing the artist
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByArtist(String artist){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getArtist().equalsIgnoreCase(artist) || current.getArtist().toLowerCase().contains(artist.toLowerCase()))
				result.add(current);
		}
		return result;	
	}
	/**
	 * Queries the data by the track's album
	 * @param album a String representing the album
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByAlbum(String album){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getAlbum().equalsIgnoreCase(album))
				result.add(current);
		}
		return result;
	}
	/**
	 * Queries the data by the track's gender
	 * @param gender a String representing the gender
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByGender(String gender){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getGender().equalsIgnoreCase(gender))
				result.add(current);
		}
		return result;
	}
	/**
	 * Queries the data by the track's label
	 * @param label a String representing the label
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByLabel(String label){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getLabel().equalsIgnoreCase(label))
				result.add(current);
		}
		return result;
	}
	/**
	 * Queries the data by the track's writer. Since many tracks have more than one
	 * writer, it is not necessary to exactly match the field character by character
	 * @param writer a String representing the writer
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByWriter(String writer){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getWriter().equalsIgnoreCase(writer) || current.getWriter().toLowerCase().contains(writer.toLowerCase()))
				result.add(current);
		}
		return result;
	}
	/**
	 * Queries the data by the track's year
	 * @param year an int representing the year
	 * @return an ArrayList of Tracks with the elements that satisfy the query
	 */
	private ArrayList<Track> findByYear(int year){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getYear() == year)
				result.add(current);
		}
		return result;
	}
	
	/**
	 * Queries the data by the user that added it to the database
	 * @param username a String representing the user's username
	 * @return an ArrayList of Tracks representing the tracks the user with that username added
	 */
	private ArrayList<Track> findByAddedBy(String username){
		
		ArrayList<Track> result = new ArrayList<Track>();
		for(Track current : dataList){
			if(current.getAddedBy().equals(username))
				result.add(current);
		}
		return result;
	
	}
	
	public void setData(ArrayList<Track> toQuery){
		this.dataList = toQuery;
	}
	
}
