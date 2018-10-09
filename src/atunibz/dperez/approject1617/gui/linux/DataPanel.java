package atunibz.dperez.approject1617.gui.linux;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import atunibz.dperez.approject1617.data.DataInspector.QueryAttribute;
import atunibz.dperez.approject1617.data.DataSorter.Sort;
import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.exceptions.InvalidXMLImportException;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.system.DataManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Panel which shows the data. Provides a GUI for the user to execute several operations: view a list of the tracks and their details, sort the
 * tracks by some parameters, query the tracks, add and delete a track or import tracks from an external file.
 * @author Davide Perez
 * @version 2.3
 * @since 25/7/2017
 */
public class DataPanel extends BackgroundedPanel {
	/**
	 * JList containing the tracks
	 */
	private JList<Track> list;
	/**
	 * JPanel in which the list is shown
	 */
	private JPanel listPanel;
	/**
	 * ScrollPane for the list
	 */
	private JScrollPane scrollPane;
	/**
	 * JButton that opens a panel to add a new track to the collection
	 */
	private JButton addBtn,
	/**
	 * JButton that deletes the current selected track OR restores the list if 
	 * the user has done a query.
	 */
					deleteBtn,
					/**
					 * JButton to do a data query
					 */
					queryBtn,
					/**
					 * JButton to import track from a file
					 */
					importBtn;
	/**
	 * JPanel that shows the current selected track's information
	 */
	private TrackDisplayer displayer;
	/**
	 * Sorts tracks by title
	 */
	private JRadioButton titleRB,
	/**
	 * Sorts tracks by artist
	 */
						artistRB,
						/**
						 * Sort tracks by album
						 */
						albumRB;
	/**
	 * ArrayList containing all the tracks in the {@code data.xml} file
	 */
	private ArrayList<Track> data;
	
	/**
	 * Constructor of the panel
	 */
	public DataPanel(){
		this.setImage(APManagerSystem.DEFAULT_BACKGROUND);
		this.setBackgroundLayout(BackgroundLayout.CENTERED);
		this.setLayout(new BorderLayout());
		this.data = APManagerSystem.getDataList();
		this.displayer = new TrackDisplayer();
		setUpComponents();
		//sets MainFrame menus content
		MainFrame.getMainFrame().getUserMenu().setEnabled(true);
		MainFrame.getMainFrame().getLoggedInMenu().setText("Currently logged in as: " + MainFrame.getMainFrame().getCurrentUser().getUsername());
		MainFrame.getMainFrame().getLoggedInMenu().setEnabled(true);
		APManagerSystem.getSystemLogger().fine("initialization completed");
		
	}
	
	/**
	 * Initializes the graphic components and sets up the UI
	 */
	private void setUpComponents(){
		JPanel headerPanel = new IconLabel("metadata/pics/header.png", "MUSIC DATABASE", new Font("Purisa", Font.BOLD, 40), true);
		this.add(headerPanel, BorderLayout.PAGE_START);
		data = APManagerSystem.getDataList();//load list from XML
		scrollPane = new JScrollPane();
		listPanel = new JPanel();
		list = createJList(data);//new JList which is shown on screen
		list.setVisibleRowCount(22);
		listPanel.add(scrollPane);
		scrollPane.setViewportView(list);
		listPanel.add(displayer);
		list.addListSelectionListener(new TrackListener());
		
		this.add(listPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel = new JPanel();
		addBtn = new JButton("ADD");
		queryBtn = new JButton("FIND");
		deleteBtn = new JButton("DELETE");
		importBtn = new JButton("IMPORT");
		addBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.getMainFrame().swapContentPane(new AddTrackPanel());
			}
		});
		queryBtn.addActionListener(new QueryListener());
		importBtn.addActionListener(new ImportListener());
		deleteBtn.addActionListener(new DeleteListener());
		deleteBtn.setEnabled(false);
		buttonsPanel.add(addBtn);
		buttonsPanel.add(queryBtn);
		buttonsPanel.add(deleteBtn);
		buttonsPanel.add(importBtn);
		bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		JPanel sortPanel = new JPanel();
		JLabel sortLbl = new JLabel("Sort by:");
		sortLbl.setFont(APManagerSystem.DEFAULT_FONT);
		titleRB = new JRadioButton("title");
		artistRB = new JRadioButton("artist");
		albumRB = new JRadioButton("album");
		ButtonGroup group = new ButtonGroup();
		group.add(titleRB);
		group.add(albumRB);
		group.add(artistRB);
		sortPanel.add(sortLbl);
		sortPanel.add(titleRB);
		sortPanel.add(artistRB);
		sortPanel.add(albumRB);
		setSortListeners();//add listeners for sort radio buttons
		bottomPanel.add(sortPanel, BorderLayout.NORTH);

	}
	
	/**
	 * Allows the user to query the data by choosing from the track's attributes. If there is
	 * at least one track found, the list is updated to show the found track(s).
	 * @return true if at least an occurence is found, false otherwise
	 */
	private boolean query(){
		//possible chances to sort the data list (refers to the attributes specified in JList
		Object[] queries = {QueryAttribute.TITLE, QueryAttribute.ARTIST, QueryAttribute.ALBUM, QueryAttribute.GENDER,
							QueryAttribute.LABEL, QueryAttribute.WRITER, QueryAttribute.YEAR, QueryAttribute.ADDED_BY};
		QueryAttribute attr = (QueryAttribute)JOptionPane.showInputDialog(
                DataPanel.this,
                "Find the track(s) by:\n",
                "Find songs",
                JOptionPane.PLAIN_MESSAGE,
                null,
                queries,
                QueryAttribute.TITLE);
		if(attr == null)
			return false;
		String query = JOptionPane.showInputDialog(DataPanel.this, "Insert " + attr.toString().toLowerCase() + ":");
		//System.out.println(DataManager.findBy(attr, query, data).toString());
		ArrayList<Track> results = DataManager.findBy(attr, query, data);
		//if at least one track satisfies the requirements, show in a list
		if(results.size() > 0 && results != null){
			list = createJList(results);
			list.addListSelectionListener(new TrackListener());
			list.setVisibleRowCount(22);
			scrollPane.setViewportView(list);
			return true;
		}
		else{
			JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "No tracks found", "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
	}

	/**
	 * Sets the {@link ActionListener} objects for the sort radio buttons. Each radio button, if clicked, 
	 * sorts the data depending on the lexicographical order of the chosen attribute.
	 */
	private void setSortListeners(){
		titleRB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//list = createJList(DataManager.sortBy(Sort.TITLE));
				data = DataManager.sortBy(Sort.TITLE);
				list = createJList(data);
				list.addListSelectionListener(new TrackListener());
				list.setVisibleRowCount(22);
				scrollPane.setViewportView(list);
				APManagerSystem.getSystemLogger().fine("data sorted by parameter TITLE");
			}
		});
		artistRB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//list = createJList(DataManager.sortBy(Sort.ARTIST));
				data = DataManager.sortBy(Sort.ARTIST);
				list = createJList(data);
				list.addListSelectionListener(new TrackListener());
				list.setVisibleRowCount(22);
				scrollPane.setViewportView(list);
				APManagerSystem.getSystemLogger().fine("data sorted by parameter ARTIST");
			}
		});
		
		albumRB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				data = DataManager.sortBy(Sort.ALBUM);
				//list = createJList(DataManager.sortBy(Sort.ALBUM));
				list = createJList(data);
				list.addListSelectionListener(new TrackListener());
				list.setVisibleRowCount(22);
				scrollPane.setViewportView(list);
				APManagerSystem.getSystemLogger().fine("data sorted by parameter ALBUM");
				
			}
			
		});
	}
	
	/**
	 * Creates a {@link JList} out of an {@link ArrayList} of {@link Track} objects.
	 * @param list an ArrayList of Track objects
	 * @return a JList of Track objects
	 */
	private JList<Track> createJList(ArrayList<Track> list){
		
		DefaultListModel<Track> model = new DefaultListModel<>();
		
		for(Track current : list){
			
			model.addElement(current);
			APManagerSystem.getSystemLogger().fine("track added to list");
		}
		
		return new JList<Track>(model);
		
	}
	
	
	/**
	 * Gets the selected object from the JList and shows its fields values on the {@link TrackDisplayer}.
	 */
	private void displayTrack(){
		
		Track current = list.getSelectedValue();
		displayer.title.setText(current.getTitle());
		displayer.artist.setText(current.getArtist());
		displayer.gender.setText(current.getGender());
		displayer.album.setText(current.getAlbum());
		displayer.label.setText(current.getLabel());
		displayer.year.setText(current.getYear() + "");
		displayer.writer.setText(current.getWriter());
		displayer.addedBy.setText(current.getAddedBy());
	}
	
	
	/**
	 * ActionListener for {@linkplain DataPanel#queryBtn queryBtn}. This listener is added to the JButton
	 * when a query is succesfully done.
	 * @author Davide Perez
	 * @version 1.3
	 *
	 */
	private class RestoreListener implements ActionListener{
		/**
		 * Reloads the {@linkplain DataPanel#data data} ArrayList and adds it again to the JList. Then it removes itself, after
		 * having added a QueryListener to allow the user to query again.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			data = APManagerSystem.getDataList();
			//list = createJList(APManagerSystem.getDataList());
			list = createJList(data);
			scrollPane.setViewportView(list);
			list.setVisibleRowCount(22);
			list.addListSelectionListener(new TrackListener());
			queryBtn.addActionListener(new QueryListener());
			queryBtn.setText("FIND");
			addBtn.setEnabled(true);
			deleteBtn.setEnabled(true);
			titleRB.setEnabled(true);
			artistRB.setEnabled(true);
			albumRB.setEnabled(true);
			queryBtn.removeActionListener(this);
		}
		
	}
	
	/**
	 * ActionListener for {@linkplain DataPanel#queryBtn queryBtn}. This listener removes itself after having
	 * added a RestoreListener when the query perspective is open.
	 * @author Davide Perez
	 * @version 1.3
	 *
	 */
	private class QueryListener implements ActionListener{
		/**
		 * Allows the user to open the query menu by pressing the JButton.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if(query()){
			queryBtn.setText("RESTORE");
			queryBtn.addActionListener(new RestoreListener());
			addBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
			titleRB.setEnabled(false);
			artistRB.setEnabled(false);
			albumRB.setEnabled(false);
			queryBtn.removeActionListener(this);
			}
			else return;
		}
		
	}
	
	/**
	 * Action Listener for the {@linkplain DataPanel#deleteBtn deleteBtn}. Used to delete 
	 * the currently selected track.
	 * @version 1.2
	 * @author Davide Perez
	 *
	 */
	private class DeleteListener implements ActionListener{
		/**
		 * Deletes the currently selected track and updates the GUI.
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(list.isSelectionEmpty()){
				JOptionPane.showMessageDialog(DataPanel.this, "Please select a track to delete it", "APManager 2017", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Track track = list.getSelectedValue();
			int dialogChoice = JOptionPane.showConfirmDialog(DataPanel.this,
					track.toString() + "\nAre you sure you want to delete this track? Cannot be undone.", "Delete", JOptionPane.YES_NO_OPTION);
			if (dialogChoice == JOptionPane.YES_OPTION){
				APManagerSystem.getSystemLogger().fine("Before: " + data.toString());
				DataManager.deleteTrack(track);
				data = APManagerSystem.getDataList();
				APManagerSystem.getSystemLogger().fine("After: " + data.toString());
				list = createJList(data);
				list.addListSelectionListener(new TrackListener());
				list.setVisibleRowCount(22);
				scrollPane.setViewportView(list);
				//because otherwise the file is removed from the list and the xml, but not from the array
				//until the end of the session, hence it is retrievable using query
				
			}
			else
				return;
			
		}
		
	}
	
	/**
	 * Listener for the JList.
	 * @author Davide Perez
	 * @version 1.1
	 */
	private class TrackListener implements ListSelectionListener{
		/**
		 * Displays the currently selected track on the related panel.
		 */
		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			deleteBtn.setEnabled(true);
			if (!list.getValueIsAdjusting())
				displayTrack();
		}
	}
	
	/**
	 * Listener for {@linkplain DataPanel#importBtn importBtn}. Allows the user to select
	 * a file from the system to import new tracks.
	 * @author Davide Perez
	 * @version 1.3
	 */
	private class ImportListener implements ActionListener{
		final JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter;
		/**
		 * Constructor for the listener
		 */
		ImportListener() {
			chooser.setDialogTitle("Choose an .xml file as source...");
			filter = new FileNameExtensionFilter(".xml files", "xml");
			chooser.setAcceptAllFileFilterUsed(false);//remove "All file" from the options
			chooser.setFileFilter(filter);//set my filter
		}
		/**
		 * The selected file is passed as parameter in {@link DataManager#importTracks(File)}.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int returnVal = chooser.showOpenDialog(DataPanel.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				APManagerSystem.getSystemLogger().info("selected file is loading");
				File dataSource = chooser.getSelectedFile();
				int dialogChoice = JOptionPane.showConfirmDialog(DataPanel.this,
						"Import tracks from \"" + dataSource.getPath() + "\"?", "Import", JOptionPane.YES_NO_OPTION);
				if (dialogChoice == JOptionPane.YES_OPTION){
					try {
						DataManager.importTracks(dataSource);
					} catch (InvalidXMLImportException e1) {
						// TODO Auto-generated catch block
						APManagerSystem.getSystemLogger().severe("invalid import. " + dataSource.getPath() + " is not a valid data xml file");
						JOptionPane.showMessageDialog(DataPanel.this, "Invalid import. The XML file must "
								+ "reflect the structure of the \"data.xml\" file. See the documentation for more information", "APManager 2017", JOptionPane.WARNING_MESSAGE);
					}
					data = APManagerSystem.getDataList();
					list = createJList(data);
					list.addListSelectionListener(new TrackListener());
					list.setVisibleRowCount(22);
					scrollPane.setViewportView(list);
				}
				else {
					return;
				}
			}
		}
	}
	
	
	/**
	 * Side panel which is used to show the currently selected track.
	 * @author Davide Perez
	 *@version 1.3
	 */
	private class TrackDisplayer extends JPanel {
		
		JLabel title, artist, album, gender, label, writer, year, addedBy;
		/**
		 * Constructor
		 */
		public TrackDisplayer() {

			this.setLayout(new GridBagLayout());
			addTrackLabels();
			addFieldLabels();
		}
		
		/**
		 * Inspects the {@link Track} class via reflection and creates a JLabel for every field.
		 */
		//iteratively adds the labels via reflection
		//done to reduce the number of lines of code to be written and to make the component more reusable
		private void addFieldLabels() {
			Field[] fields = Track.class.getDeclaredFields();
			GridBagConstraints gbcLbl = new GridBagConstraints();
			gbcLbl.gridx = 0;
			gbcLbl.gridy = 0;
			gbcLbl.insets = new Insets(7, 7, 0, 0);
			//work on this to modify insets and looks (want coloured labels and more spaces between one label and the other
			//maybe some icons too)
			for (Field current : fields) {
				//this.add(new JLabel(current.getName().toUpperCase() + ":"), gbcLbl);
				IconLabel lbl = new IconLabel("metadata/icons/" + current.getName() + ".png", current.getName().toUpperCase() + ":", false);
				//lbl.setBackground(Color.BLUE);
				this.add(lbl, gbcLbl);
				//this.add(new IconLabel("metadata/icons/" + current.getName() + ".png", current.getName().toUpperCase() + ":", false), gbcLbl);
				gbcLbl.gridy++;

			}
		}
		
		/**
		 * Adds the JLabel objects that will show current track's information
		 */
		private void addTrackLabels(){
			//title label
			title = new JLabel();
			GridBagConstraints gbcTitle = new GridBagConstraints();
			gbcTitle.gridx = 1;
			gbcTitle.gridy = 0;
			this.add(title, gbcTitle);
			//artist label
			artist = new JLabel();
			GridBagConstraints gbcArtist = new GridBagConstraints();
			gbcArtist.gridx = 1;
			gbcArtist.gridy = 1;
			this.add(artist, gbcArtist);
			//album label
			album = new JLabel();
			GridBagConstraints gbcAlbum = new GridBagConstraints();
			gbcAlbum.gridx = 1;
			gbcAlbum.gridy = 2;
			this.add(album, gbcAlbum);
			//gender label
			gender = new JLabel();
			GridBagConstraints gbcGender = new GridBagConstraints();
			gbcGender.gridx = 1;
			gbcGender.gridy = 3;
			this.add(gender, gbcGender);
			//Label label
			label = new JLabel();
			GridBagConstraints gbcLabel = new GridBagConstraints();
			gbcLabel.gridx = 1;
			gbcLabel.gridy = 4;
			this.add(label, gbcLabel);
			//writer label
			writer = new JLabel();
			GridBagConstraints gbcWriter = new GridBagConstraints();
			gbcWriter.gridx = 1;
			gbcWriter.gridy = 5;
			this.add(writer, gbcWriter);
			//year label
			year = new JLabel();
			GridBagConstraints gbcYear = new GridBagConstraints();
			gbcYear.gridx = 1;
			gbcYear.gridy = 6;
			this.add(year, gbcYear);
			//added by label
			addedBy = new JLabel();
			GridBagConstraints gbcAddedBy = new GridBagConstraints();
			gbcAddedBy.gridx = 1;
			gbcAddedBy.gridy = 7;
			gbcAddedBy.insets = new Insets(0, 5, 0, 0);
			this.add(addedBy, gbcAddedBy);
		}
	}
}
	
