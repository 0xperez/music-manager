package atunibz.dperez.approject1617.gui.linux;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import atunibz.dperez.approject1617.exceptions.InvalidPasswordException;
import atunibz.dperez.approject1617.exceptions.InvalidUsernameException;
import atunibz.dperez.approject1617.exceptions.UsernameAlreadyExistsException;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.user.User;
import atunibz.dperez.approject1617.xml.XMLFactory;
import atunibz.dperez.approject1617.xml.XMLFactory.XMLType;
import atunibz.dperez.approject1617.xml.XMLUsers;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shows the registration form for a new user to register to APManager.
 * @author Davide Perez
 * @since 17/5/2017
 * @version 1.2
 *
 */
public class RegistrationPanel extends BackgroundedPanel {
	/**
	 * JTextField to input the username
	 */
	private JTextField userTF;
	/**
	 * JPasswordField to input the password
	 */
	private JPasswordField pswTF;
	/**
	 * JPasswordField to repeat the password
	 */
	private JPasswordField repeatPswTF;
	/**
	 * JButton to confirm the registration. When pressed, the input fields are checked to verify they have been
	 * correctly filled and calls {@link #register()}. 
	 */
	private JButton confirmBtn,
	/**
	 * JButton to navigate back to the LoginPanel
	 */
					backBtn;
	/**
	 * String representing the chosen username
	 */
	private String username;
	/**
	 * String representing the chosen password
	 */
	private String password;

	/**
	 * Constructor of the class
	 */
	public RegistrationPanel() {

		setLayout(new BorderLayout());
		this.setImage(APManagerSystem.DEFAULT_BACKGROUND);
		this.setBackgroundLayout(BackgroundLayout.CENTERED);
		setUpComponents();

	}
	/**
	 * Initializes the graphic components and sets up the UI.
	 */
	private void setUpComponents() {
		// title panel
		JLabel headerLbl = new JLabel("Registration");
		headerLbl.setFont(APManagerSystem.TITLE_FONT);
		headerLbl.setBorder(new EmptyBorder(new Insets(3, 5, 3, 5)));
		JPanel headerPanel = new JPanel();
		headerPanel.add(headerLbl);
		this.add(headerPanel, BorderLayout.PAGE_START);
		// central panel with the registration form
		JPanel contentPanel = new JPanel();
		//contentPanel.setPreferredSize(new Dimension(150, 150));
		contentPanel.setLayout(new GridBagLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		// username label
		JLabel userLbl = new JLabel("Username:");
		userLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcUserLbl = new GridBagConstraints();
		gbcUserLbl.insets = new Insets(15, 15, 5, 5);
		gbcUserLbl.gridx = 0;
		gbcUserLbl.gridy = 0;
		contentPanel.add(userLbl, gbcUserLbl);
		// username textfield
		userTF = new JTextField(10);
		GridBagConstraints gbcUserTF = new GridBagConstraints();
		gbcUserTF.insets = new Insets(15, 15, 5, 5);
		gbcUserTF.gridx = 1;
		gbcUserTF.gridy = 0;
		contentPanel.add(userTF, gbcUserTF);
		// password label
		JLabel pswLbl = new JLabel("Password:");
		pswLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcPswLbl = new GridBagConstraints();
		gbcPswLbl.insets = new Insets(15, 15, 5, 5);
		gbcPswLbl.gridx = 0;
		gbcPswLbl.gridy = 1;
		contentPanel.add(pswLbl, gbcPswLbl);
		// password textfield
		pswTF = new JPasswordField(10);
		GridBagConstraints gbcPswTF = new GridBagConstraints();
		gbcPswTF.insets = new Insets(15, 15, 5, 5);
		gbcPswTF.gridx = 1;
		gbcPswTF.gridy = 1;
		contentPanel.add(pswTF, gbcPswTF);
		// repeat password label
		JLabel repPswLbl = new JLabel("Repeat password:");
		repPswLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcRepPswLbl = new GridBagConstraints();
		gbcRepPswLbl.insets = new Insets(15, 15, 5, 5);
		gbcRepPswLbl.gridx = 0;
		gbcRepPswLbl.gridy = 2;
		contentPanel.add(repPswLbl, gbcRepPswLbl);
		// repeat password textfield
		repeatPswTF = new JPasswordField(10);
		GridBagConstraints gbcRepeatPsw = new GridBagConstraints();
		gbcRepeatPsw.insets = new Insets(15, 15, 5, 5);
		gbcRepeatPsw.gridx = 1;
		gbcRepeatPsw.gridy = 2;
		contentPanel.add(repeatPswTF, gbcRepeatPsw);
		// confirm button
		confirmBtn = new JButton("Confirm");
		confirmBtn.setFont(new Font("Purisa", Font.BOLD, 14));
		GridBagConstraints gbcConfirmBtn = new GridBagConstraints();
		gbcConfirmBtn.gridx = 1;
		gbcConfirmBtn.gridy = 3;
		gbcConfirmBtn.insets = new Insets(10, 0, 0, 15);
		confirmBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = userTF.getText();
				password = new String(pswTF.getPassword());
				
				if (checkInputFields()) {
					boolean valid = false;
					
					try {
						valid = ((XMLUsers) XMLFactory.newInstance().getXMLWriter(XMLType.USERS)).isUsernameValid(username);
					} catch (InvalidUsernameException e1) {
						if (e1 instanceof UsernameAlreadyExistsException) {
							APManagerSystem.getSystemLogger().info("a user tried to register with a username that already is in use");
							JOptionPane.showMessageDialog(RegistrationPanel.this, "This username is already in use. Please choose a different one", "APManager 2017", JOptionPane.WARNING_MESSAGE);
							userTF.setText("");
							return;
						} else {
							APManagerSystem.getSystemLogger().info("a user tried to register with an invalid username");
							JOptionPane.showMessageDialog(RegistrationPanel.this, "The username you inserted is not valid. Please try again", "APManager 2017", JOptionPane.WARNING_MESSAGE);
							userTF.setText("");
							return;
						}
					}
					
					if(valid)
						try {
							register();
						} catch (InvalidPasswordException e1) {
							String log = e1.getMessage();
							String msg = log.substring(0, 1).toUpperCase() + log.substring(1);//upper case 1st letter
							APManagerSystem.getSystemLogger().info("a username failed to register. " + msg);
							JOptionPane.showMessageDialog(RegistrationPanel.this, msg, "APManager 2017", JOptionPane.WARNING_MESSAGE);
							pswTF.setText("");
							repeatPswTF.setText("");
						}
					}
				}

		});
		contentPanel.add(confirmBtn, gbcConfirmBtn);
		
		backBtn = new JButton("Back");
		backBtn.setFont(APManagerSystem.DEFAULT_FONT);
		backBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!userTF.getText().equals("")){
					int dialogChoice = JOptionPane.showConfirmDialog(RegistrationPanel.this, "Leave this screen? Any change will be lost.", "APManager2017", JOptionPane.YES_NO_OPTION);
					if(dialogChoice == JOptionPane.YES_OPTION){
						MainFrame.getMainFrame().swapContentPane(new LoginPanel());
					}
					else{
						return;
					}
				}
				
				else{
					MainFrame.getMainFrame().swapContentPane(new LoginPanel());
				}
			}
		});

		this.add(backBtn, BorderLayout.SOUTH);
		//add panel with instructions about how to choose the username
		JPanel advice = new AdvicePanel();
		this.add(advice, BorderLayout.EAST);

	}
/**
 * Checks if the password field and the confirm-password field have the same input.
 * @return true if the input is equal, false otherwise
 */
	private boolean confirmPsw(){

		String psw = new String(pswTF.getPassword());
		String repeatedPsw = new String(repeatPswTF.getPassword());
		if (psw.equals(repeatedPsw))
			return true;
		else{
			pswTF.setText("");
			repeatPswTF.setText("");
			return false;
		}

	}
	/**
	 * Checks if all text fields have been compiled
	 * @return true if all text fields contain at least one character, false otherwise
	 */
	private boolean checkInputFields() {
		//checks if the username is not blank
		if(userTF.getText().equals("")){
			JOptionPane.showMessageDialog(RegistrationPanel.this, "Please provide a username", "APManager 2017",
					JOptionPane.WARNING_MESSAGE); 
			return false;
		}
		
		//checks if the password field is not blank
		if (new String(pswTF.getPassword()).equals("")) {
			JOptionPane.showMessageDialog(RegistrationPanel.this, "Please provide a password", "APManager 2017",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		//checks if the repeat psw field is not blank
		if(new String(repeatPswTF.getPassword()).equals("")){
			JOptionPane.showMessageDialog(RegistrationPanel.this, "Please repeat your password", "APManager 2017",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;

	}
	
	/**
	 * Sets the current user of the APManager application to the new user and opens an ImagePanel.
	 * @throws InvalidPasswordException if the password is shorter than 5 characters
	 */
	private void register() throws InvalidPasswordException{
		if(password.length() < 5)
			throw new InvalidPasswordException("password must be at least 5 characters long");
		if(confirmPsw()){
			//DataManager.addUser(new User(username, password));//set current screen to ImagePanel and set currentUser to the new user. Add control when closed to inform the user that is still not saved
			MainFrame.getMainFrame().setCurrentUser(new User(username, password));
			MainFrame.getMainFrame().swapContentPane(new ImagePanel());
			APManagerSystem.getSystemLogger().info("a user registered with username " + username);
		}
		else
			throw new InvalidPasswordException("password and the repeated password don't match");
	}
	
	
	/**
	 * Side panel showing the guidelines about the username's and password's requirements.
	 * @author Davide Perez
	 * @version 1.0
	 * @since 18/5/2017
	 */
	private class AdvicePanel extends JPanel {
		/**
		 * Label showing an icon
		 */
		JLabel imageLabel;
		/**
		 * Constructor of the class
		 */
		public AdvicePanel() {
			imageLabel = new JLabel(new ImageIcon("metadata/pics/warning.png"));
			this.setLayout(new BorderLayout());
			this.add(imageLabel, BorderLayout.PAGE_START);
			this.setPreferredSize(new Dimension(200, 400));
			//find a way to make this size-independent
		}
		/**
		 * Shows the guidelines by painting the strings on the JPanel.
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// paints string on the panel. This ensures the text is correctly
			// displayed also when the panel is resized, because the method
			// paintComponent
			// is reinvoked at every modification of the layout
			g.setColor(Color.BLUE);
			g.drawString("The username must be at least", 0, 130);
			g.drawString("5 characters long and", 0, 145);
			g.drawString("can contain any combination of", 0, 160);
			g.drawString("alphanumeric characters,", 0, 175);
			g.drawString("but only one dash(-),", 0, 190);
			g.drawString("one underscore(_)", 0, 205);
			g.drawString("or one dot (.)", 0, 220);
			g.drawString("The password must be 5", 0, 235);
			g.drawString("characters long.", 0, 250);

		}

	}

}
