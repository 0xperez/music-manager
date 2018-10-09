package atunibz.dperez.approject1617.gui.linux;

import javax.swing.border.EmptyBorder;
import atunibz.dperez.approject1617.system.APManagerSystem;
import atunibz.dperez.approject1617.xml.XMLFactory;
import atunibz.dperez.approject1617.xml.XMLFactory.XMLType;
import atunibz.dperez.approject1617.xml.XMLUsers;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Class representing the login panel.
 * @author Davide Perez
 * @since 17/5/2017
 * @version 1.3
 *
 */
public class LoginPanel extends BackgroundedPanel {
	/**
	 * JTextField where to insert username
	 */
	private JTextField usernameTF;
	/**
	 * JPasswordField where to insert the password
	 */
	private JPasswordField pswTF;
	/**
	 * JButton to log in. When pressed, the {@linkplain atunibz.dperez.approject1617.xml.XMLUsers#authenticate(String, String) XMLUsers.authenticate()} method is called.
	 */
	private JButton loginBtn, 
	/**
	 * JButton to register. When pressed, opens a RegistrationPanel.
	 */
					registerBtn;
	
	/**
	 * Constructor of the class
	 */
	public LoginPanel() {
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		this.setImage(APManagerSystem.DEFAULT_BACKGROUND);
		this.setBackgroundLayout(BackgroundLayout.CENTERED);
		setUpComponents();
		//setPreferredSize(new Dimension(650, 400));
	}
	/**
	 * Initializes the graphic components and sets up the UI.
	 */
	private void setUpComponents(){
		//title panel
		JLabel headerLbl = new JLabel("APManager 2017");
		headerLbl.setBorder(new EmptyBorder(new Insets(3, 5, 3, 5)));
		headerLbl.setFont(APManagerSystem.TITLE_FONT);
		JPanel headerPanel = new JPanel();
		headerPanel.add(headerLbl);
		//JLabel introLbl = new JLabel("Insert your credentials to log in or create an account if you are not subscribed!");
		//introLbl.setFont(MainFrame.DEFAULT_FONT);
		this.add(headerPanel, BorderLayout.PAGE_START);
		
		//panel holding username and password fields
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new GridBagLayout());
		contentPanel.setPreferredSize(new Dimension(200, 400));
		//contentPanel.setBorder(new LineBorder(Color.BLACK));
		this.add(contentPanel, BorderLayout.CENTER);
		//label password
		JLabel pswLbl = new JLabel("Password:");
		pswLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcPswLbl = new GridBagConstraints();
		gbcPswLbl.insets = new Insets(15, 15, 5, 5);
		gbcPswLbl.gridx = 0;
		gbcPswLbl.gridy = 1;
		contentPanel.add(pswLbl, gbcPswLbl);
		//password textfield
		pswTF = new JPasswordField(10);
		GridBagConstraints gbcpswTF = new GridBagConstraints();
		gbcpswTF.insets = new Insets(15, 15, 5, 5);
		gbcpswTF.gridx = 1;
		gbcpswTF.gridy = 1;
		contentPanel.add(pswTF, gbcpswTF);
		//label username
		JLabel usernameLbl = new JLabel("Username:");
		usernameLbl.setFont(APManagerSystem.DEFAULT_FONT);
		GridBagConstraints gbcUserLbl = new GridBagConstraints();
		gbcUserLbl.insets = new Insets(15, 15, 5, 5);
		gbcUserLbl.gridx = 0;
		gbcUserLbl.gridy = 0;
		contentPanel.add(usernameLbl, gbcUserLbl);
		//textfield username
		usernameTF = new JTextField(10);
		GridBagConstraints gbcUserTF = new GridBagConstraints();
		gbcUserTF.insets = new Insets(15, 15, 5 ,5);
		gbcUserTF.gridx = 1;
		gbcUserTF.gridy = 0;
		contentPanel.add(usernameTF, gbcUserTF);
		//login button with an icon
		loginBtn = new JButton(new ImageIcon("metadata/pics/login.png"));
		GridBagConstraints gbcLoginBtn = new GridBagConstraints();
		gbcLoginBtn.gridx = 1;
		gbcLoginBtn.gridy = 2;
		gbcLoginBtn.insets = new Insets(10, 0, 0, 15);
		loginBtn.setPreferredSize(new Dimension(45, 35));
		loginBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//put all this in a new method
				XMLFactory factory = XMLFactory.newInstance();
				XMLUsers xml = (XMLUsers) factory.getXMLWriter(XMLType.USERS);
				/*if(xml.authenticate(usernameTF.getText(), PasswordEncryptor.encryptPassword(new String(pswTF.getPassword())))){
					JOptionPane.showMessageDialog(null, "Welcome back, " + usernameTF.getText() + "!", "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
					MainFrame.getMainFrame().swapContentPane(new DataPanel());
				}*/
				if(xml.authenticate(usernameTF.getText(), new String(pswTF.getPassword()))){
					JOptionPane.showMessageDialog(LoginPanel.this, "Welcome back, " + usernameTF.getText() + "!", "APManager 2017", JOptionPane.INFORMATION_MESSAGE);
					MainFrame.getMainFrame().swapContentPane(new DataPanel());
				}
				else{
					JOptionPane.showMessageDialog(LoginPanel.this, "Invalid username or password!", "APManager 2017",
					JOptionPane.WARNING_MESSAGE);
					usernameTF.setText(null);
					pswTF.setText(null);
				}
	
			}
			
		});
		contentPanel.add(loginBtn, gbcLoginBtn);
		
		//signBtn = new JButton("REGISTER");
		GridBagConstraints gbcSignBtn = new GridBagConstraints();
		gbcSignBtn.gridx = 1;
		gbcSignBtn.gridy = 2;
		//contentPanel.add(signBtn, gbcSignBtn);
		
		//panel with the registration question
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new EmptyBorder(new Insets(40, 0, 40, 0)));
		JLabel registerLbl = new JLabel("Not registered yet?");
		registerLbl.setFont(APManagerSystem.DEFAULT_FONT);
		//registerLbl.setForeground(Color.BLUE);
		registerBtn = new JButton("Register now!");
		registerBtn.setFont(new Font("Purisa", Font.BOLD, 14));
		registerBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//swap to registration panel
				MainFrame.getMainFrame().swapContentPane(new RegistrationPanel());
			}
		});
		bottomPanel.add(registerLbl);
		bottomPanel.add(registerBtn);
		this.add(bottomPanel, BorderLayout.PAGE_END);
		
	}

}
