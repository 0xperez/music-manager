package atunibz.dperez.approject1617.gui.linux;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import atunibz.dperez.approject1617.data.Track;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.system.DataManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Class that provides a GUI to allow the users to add a track to the data file. A track can be added if it has not
 * exactly the same artist and title of another saved track.
 * @author Davide Perez
 * @since 26/5/2017
 * @version 1.2
 *
 */
public class AddTrackPanel extends BackgroundedPanel {
	/**
	 * JTextField for set track title
	 */
	private JTextField titleTF,
	/**
	 * JTextField to insert artist
	 */
						artistTF, 
						/**
						 * JTextField to insert album
						 */
						albumTF,  
						/**
						 * JTextField to insert gender
						 */
						genderTF, 
						/**
						 * JTextField to insert label
						 */
						labelTF, 
						/**
						 * JTextField to insert writer
						 */
						writerTF,
						/**
						 * JTextField to insert year
						 */
						yearTF;
	/**
	 * JButton to go back to DataPanel
	 */
	private JButton backBtn,
	/**
	 * JButton to create a Track with inserted data
	 */
					addBtn;
	/**
	 * Standard constraints for the labels and textfields to specify their distances from other components
	 */
	private Insets grid = new Insets(3, 5, 3, 5);
	/**
	 * JLabel that warns the user he inserted an invalid value
	 */
	private JLabel warningLbl;
	
	/**
	 * Constructors that creates an instance of this class
	 */
	public AddTrackPanel(){
		
		setLayout(new BorderLayout());
		this.setImage(APManagerSystem.DEFAULT_BACKGROUND);
		this.setBackgroundLayout(BackgroundLayout.CENTERED);
		setUpComponents();
		
	}
	
	/**
	 * Initializes the graphic components and sets up the UI.
	 */
	private void setUpComponents(){
		// title panel
		JLabel headerLbl = new JLabel("Add a new track to the collection!");
		headerLbl.setFont(APManagerSystem.TITLE_FONT);
		headerLbl.setBorder(new EmptyBorder(new Insets(3, 5, 3, 5)));
		JPanel headerPanel = new JPanel();
		headerPanel.add(headerLbl);
		this.add(headerPanel, BorderLayout.PAGE_START);
		//
		JPanel contentPanel = new JPanel();
		//contentPanel.setPreferredSize(new Dimension(150, 150));
		contentPanel.setLayout(new GridBagLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		// username
		JLabel titleLbl = new JLabel("Title:");
		titleLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcTitleLbl = new GridBagConstraints();
		gbcTitleLbl.insets = grid;
		gbcTitleLbl.gridx = 0;
		gbcTitleLbl.gridy = 0;
		contentPanel.add(titleLbl, gbcTitleLbl);
		titleTF = new JTextField(10);
		GridBagConstraints gbcTitleTF = new GridBagConstraints();
		gbcTitleTF.insets = grid;
		gbcTitleTF.gridx = 1;
		gbcTitleTF.gridy = 0;
		contentPanel.add(titleTF, gbcTitleTF);
		//artist
		JLabel artistLbl = new JLabel("Artist:");
		artistLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcArtistLbl = new GridBagConstraints();
		gbcArtistLbl.insets = grid;
		gbcArtistLbl.gridx = 0;
		gbcArtistLbl.gridy = 1;
		contentPanel.add(artistLbl, gbcArtistLbl);
		artistTF = new JTextField(10);
		GridBagConstraints gbcArtistTF = new GridBagConstraints();
		gbcArtistTF.insets = grid;
		gbcArtistTF.gridx = 1;
		gbcArtistTF.gridy = 1;
		contentPanel.add(artistTF, gbcArtistTF);
		//album
		JLabel albumLbl = new JLabel("Album:");
		albumLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcAlbumLbl = new GridBagConstraints();
		gbcAlbumLbl.insets = grid;
		gbcAlbumLbl.gridx = 0;
		gbcAlbumLbl.gridy = 2;
		contentPanel.add(albumLbl, gbcAlbumLbl);
		albumTF = new JTextField(10);
		GridBagConstraints gbcAlbumTF = new GridBagConstraints();
		gbcAlbumTF.insets = grid;
		gbcAlbumTF.gridx = 1;
		gbcAlbumTF.gridy = 2;
		contentPanel.add(albumTF, gbcAlbumTF);
		//gender
		JLabel genderLbl = new JLabel("Gender:");
		genderLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcGenderLbl = new GridBagConstraints();
		gbcGenderLbl.insets = grid;
		gbcGenderLbl.gridx = 0;
		gbcGenderLbl.gridy = 3;
		contentPanel.add(genderLbl, gbcGenderLbl);
		genderTF = new JTextField(10);
		GridBagConstraints gbcGenderTF = new GridBagConstraints();
		gbcGenderTF.insets = grid;
		gbcGenderTF.gridx = 1;
		gbcGenderTF.gridy = 3;
		contentPanel.add(genderTF, gbcGenderTF);
		//label
		JLabel labelLbl = new JLabel("Label:");
		labelLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcLabelLbl = new GridBagConstraints();
		gbcLabelLbl.insets = grid;
		gbcLabelLbl.gridx = 0;
		gbcLabelLbl.gridy = 4;
		contentPanel.add(labelLbl, gbcLabelLbl);
		labelTF = new JTextField(10);
		GridBagConstraints gbcLabelTF = new GridBagConstraints();
		gbcLabelTF.insets = grid;
		gbcLabelTF.gridx = 1;
		gbcLabelTF.gridy = 4;
		contentPanel.add(labelTF, gbcLabelTF);
		//writer
		JLabel writerLbl = new JLabel("Writer:");
		writerLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcWriterLbl = new GridBagConstraints();
		gbcWriterLbl.insets = grid;
		gbcWriterLbl.gridx = 0;
		gbcWriterLbl.gridy = 5;
		contentPanel.add(writerLbl, gbcWriterLbl);
		writerTF = new JTextField(10);
		GridBagConstraints gbcWriterTF = new GridBagConstraints();
		gbcWriterTF.insets = grid;
		gbcWriterTF.gridx = 1;
		gbcWriterTF.gridy = 5;
		contentPanel.add(writerTF, gbcWriterTF);
		//year
		JLabel yearLbl = new JLabel("Year:");
		yearLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcYearLbl = new GridBagConstraints();
		gbcYearLbl.insets = grid;
		gbcYearLbl.gridx = 0;
		gbcYearLbl.gridy =6;
		contentPanel.add(yearLbl, gbcYearLbl);
		yearTF = new JTextField(10);
		GridBagConstraints gbcYearTF = new GridBagConstraints();
		gbcYearTF.insets = grid;
		gbcYearTF.gridx = 1;
		gbcYearTF.gridy = 6;
		contentPanel.add(yearTF, gbcYearTF);
		warningLbl = new JLabel("You can insert only numbers between 0 and 9");
		warningLbl.setForeground(Color.RED);
		warningLbl.setVisible(false);
		GridBagConstraints gbcWarningLbl = new GridBagConstraints();
		gbcWarningLbl.gridx = 2;
		gbcWarningLbl.gridy = 6;
		contentPanel.add(warningLbl, gbcWarningLbl);
		addBtn = new JButton("Add");
		addBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fieldsAreCompiled())
					addTrack();
				else
					JOptionPane.showMessageDialog(MainFrame.getMainFrame(), "Please compile all the fields. Input can not start with a space!", "AP Collection Manager 2017", JOptionPane.WARNING_MESSAGE);
			}
		});
		GridBagConstraints gbcAddBtn = new GridBagConstraints();
		gbcAddBtn.insets = grid;
		gbcAddBtn.gridx = 1;
		gbcAddBtn.gridy = 7;
		contentPanel.add(addBtn, gbcAddBtn);
		
		backBtn = new JButton("Back");
		backBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.getMainFrame().swapContentPane(new DataPanel());
			}
		});
		this.add(backBtn, BorderLayout.SOUTH);
	}
	
	/**
	 * Method called when {@linkplain AddTrackPanel#addBtn addBtn} is pressed, adds a track
	 * with the attributes specified by the user. Also checks if the input string in the year JTextField
	 * is a valid integer.
	 */
	private void addTrack(){
		String title = titleTF.getText();
		String artist = artistTF.getText();
		String album = albumTF.getText();
		String gender = genderTF.getText();
		String label = labelTF.getText();
		String writer = writerTF.getText();
		int year = 0;
		try{
			year = Integer.parseInt(yearTF.getText());
		}
		catch(NumberFormatException e){//if the input is not a year but some other string
			yearTF.setText(null);
			warningLbl.setVisible(true);
			return;
		}
		Track track = new Track(title, artist, album, gender, label, writer, year);
		track.setAddedBy(MainFrame.getMainFrame().getCurrentUser().getUsername());
		int dataSizePrevious = APManagerSystem.getDataList().size();//the number of tracks BEFORE the new one is added
		APManagerSystem.getSystemLogger().fine("prev: " + dataSizePrevious);
		DataManager.addTrack(track);
		int dataSizeAfter = APManagerSystem.getDataList().size();//the number of tracks AFTER the new one is added
		APManagerSystem.getSystemLogger().fine("after: " + dataSizeAfter);
		
		if(dataSizePrevious != dataSizeAfter)//if the size has changed, a new track has been succesfully added
			JOptionPane.showMessageDialog(this, "You added a new track!", "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
		else//the user attempted to insert a track that already exists
			JOptionPane.showMessageDialog(this, "This track already exists!", "APManager 2017", JOptionPane.WARNING_MESSAGE);
		//this has been done because the method void add(XMLWritable track) of XMLTracks.class is inherited from
		//the XMLWriter interface, hence its return type cannot be changed and it cannot declare exceptions in the
		//signature. This is a "trick" used because I want to show the messageDialogs only when a track is manually
		//added, not when imported from a file (otherwise in case of large imports it would be a pointless process to
		//show many times the "track added" message.
		MainFrame.getMainFrame().swapContentPane(new DataPanel());
	}
	
	/**
	 * Checks if all the text fields have been compiled
	 * @return true if all the text fields contain at least one character, false otherwise
	 */
	private boolean fieldsAreCompiled(){
		if(titleTF.getText().equals("") || titleTF.getText().startsWith(" "))
			return false;
		if(artistTF.getText().equals("") || artistTF.getText().startsWith(" "))
			return false;
		if(albumTF.getText().equals("") || albumTF.getText().startsWith(" "))
			return false;
		if(genderTF.getText().equals("") || genderTF.getText().startsWith(" "))
			return false;
		if(labelTF.getText().equals("") || labelTF.getText().startsWith(" "))
			return false;
		if(writerTF.getText().equals("") || writerTF.getText().startsWith(" "))
			return false;
		return true;
	}
	
	
	
	
	
}
