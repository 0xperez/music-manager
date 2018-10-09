package atunibz.dperez.approject1617.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import atunibz.dperez.approject1617.system.APManagerSystem;

/**
 * Utility class to sort the internal {@link ArrayList} of {@link Track} objects representing the {@code data.xml} file content depending on some of their attributes.
 * @author Davide Perez
 * @since 26/7/2017
 * @version 1.2
 *
 */
public class DataSorter {
	/**
	 * Enumerated type which specifies an attribute to follow to order the tracks
	 * @author Davide Perez
	 * @since 26/7/2017
	 * @version 1.0
	 */
	public enum Sort{
		/**
		 * Specify to order the tracks by their title
		 */
		TITLE, 
		/**
		 * Specify to order the tracks by their artist
		 */
		ARTIST, 
		/**
		 * Specify to order the tracks by their album
		 */
		ALBUM
	}
	
	/**
	 * Sorts the data list by the attribute specified. This is the only invokable method of this class
	 * as it invokes the correct sort method depending on the parameter's value.
	 * @param attribute a value of the Sort enum which specifies the attribute to consider while sorting
	 * @return an ArrayList of Track containing the objects that match the query
	 */
	public ArrayList<Track> sortBy(Sort attribute){
		switch(attribute){
			case TITLE: return sortByTitle();
			case ARTIST: return sortByArtist();
			case ALBUM: return sortByAlbum();
		}
		
		return null;
	}
	
	/**
	 * Helper method which sorts the data list in alphabetic order considering the track's title
	 * @return an sorted version of the data ArrayList
	 */
	private ArrayList<Track> sortByTitle(){
		ArrayList<Track> sorted = APManagerSystem.getDataList();//refresh. Otherwise does not show a song if added
		//during the same session
		Collections.sort(sorted, new Comparator<Track>(){
			@Override
			public int compare(Track o1, Track o2) {
				return o1.getTitle().compareTo(o2.getTitle());
			}
		}); 
		
		return sorted;
	}
	
	/**
	 * Helper method which sorts the data list in alphabetic order considering the artist's title
	 * @return a sorted version of the data ArrayList
	 */
	private ArrayList<Track> sortByArtist(){
		ArrayList<Track> sorted = APManagerSystem.getDataList();
		Collections.sort(sorted, new Comparator<Track>(){
			@Override
			public int compare(Track o1, Track o2) {
				return o1.getArtist().compareTo(o2.getArtist());
			}
		}); 
		return sorted;
	}
	
	/**
	 * Helper method which sorts the data list in alphabetic order considering the track's album
	 * @return a sorted version of the data ArrayList
	 */
	private ArrayList<Track> sortByAlbum(){
		ArrayList<Track> sorted = APManagerSystem.getDataList();
		Collections.sort(sorted, new Comparator<Track>(){
			@Override
			public int compare(Track o1, Track o2) {
				return o1.getAlbum().compareTo(o2.getAlbum());
			}
		}); 
		
		return sorted;
	}
	
}

	