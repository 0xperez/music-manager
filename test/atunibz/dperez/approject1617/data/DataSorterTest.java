package atunibz.dperez.approject1617.data;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import atunibz.dperez.approject1617.data.DataSorter.Sort;
import atunibz.dperez.approject1617.system.APManagerSystem;


public class DataSorterTest {

	DataSorter sorter = new DataSorter();
	
	
	//gets the track list and checks if it is unsorted by comparing their titles.
	//then sorts it with the DataSorter object and checks if it is sorted
	@Test
	public void testSortByTitle(){
		//list of tracks
		ArrayList<Track> unsortedTracks = APManagerSystem.getDataList();
		//list of titles (strings)
		ArrayList<String> unsortedTitles = new ArrayList<String>();
		//populate the list
		for(Track t : unsortedTracks){
			unsortedTitles.add(t.getTitle());
		}
		//check if unsorted
		assertFalse(isSorted(unsortedTitles));
		//sort and populate the corresponding title list
		ArrayList<Track> sortedTracks = sorter.sortBy(Sort.TITLE);
		ArrayList<String> sortedTitles = new ArrayList<String>();
		for(Track t : sortedTracks){
			sortedTitles.add(t.getTitle());
		}
		//checks if succesfully sorted
		assertTrue(isSorted(sortedTitles));
	}
	
	//gets the track list and checks if it is unsorted by comparing their artist.
	//then sorts it with the DataSorter object and checks if it is sorted
	@Test
	public void testSortByArtist(){
		ArrayList<Track> unsortedTracks = APManagerSystem.getDataList();
		ArrayList<String> unsortedArtists = new ArrayList<String>();
		for(Track t : unsortedTracks){
			unsortedArtists.add(t.getArtist());
		}
		assertFalse(isSorted(unsortedArtists));
		ArrayList<Track> sortedTracks = sorter.sortBy(Sort.ARTIST);
		ArrayList<String> sortedArtists = new ArrayList<String>();
		for(Track t : sortedTracks){
			sortedArtists.add(t.getArtist());
		}
		assertTrue(isSorted(sortedArtists));
	}
	
	//gets the track list and checks if it is unsorted by comparing their albums.
	//then sorts it with the DataSorter object and checks if it is sorted
	@Test
	public void testSortByAlbum(){
		ArrayList<Track> unsortedTracks = APManagerSystem.getDataList();
		ArrayList<String> unsortedAlbums = new ArrayList<String>();
		for(Track t : unsortedTracks){
			unsortedAlbums.add(t.getAlbum());
		}
		assertFalse(isSorted(unsortedAlbums));
		ArrayList<Track> sortedTracks = sorter.sortBy(Sort.ALBUM);
		ArrayList<String> sortedAlbums = new ArrayList<String>();
		for(Track t : sortedTracks){
			sortedAlbums.add(t.getAlbum());
		}
		assertTrue(isSorted(sortedAlbums));
	}
	
	//checks if a given arraylist of strings is sorted or not
	public boolean isSorted(ArrayList<String> list){
		//compares every string with the previous one. If every string is lexicographically smaller
		//(i.e , it comes before) than the next one, then the list is sorted
		for(int i = 1; i < list.size(); i++){
			if(list.get(i - 1).compareTo(list.get(i)) > 0)
				return false;
		}
		return true;
	}

}
