import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.awt.event.*;

public class MainGUI {

	private JFrame frame = new JFrame("Bill tracker");

	// Navigation bar buttons
	private JButton OVButton = new JButton("Overview");
	private JButton AddNewButton = new JButton("Add new");
	private JButton SettButton = new JButton("Settings");

	// Creating two GUIs which allows for validation
	private AddNewGUI addNewGUI = new AddNewGUI();
	private SettGUI settGUI = new SettGUI();

	private int panel = 0; // also for validation - to see which panel is currently visible

	private Font fontSetter = new Font(null, Font.PLAIN, 24); // font for the nav buttons

	// Creating the main page
	public MainGUI() {
		frame.setLayout(new MigLayout("", "[200][200][200]", ""));

		NavBar();
		OVButton.setEnabled(false); // can't click overview button when starting

		frame.add(new OverviewGUI().getPanel(), "span"); // Starting GUI, when program is first opened

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		/////////////////////////////
		// BUTTON ACTION LISTENERS //
		/////////////////////////////
		
		// Button toggles overview
		OVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidation()) { // if add new doesn't have empty fields
					buttonClicked(new OverviewGUI().getPanel());
					unclickableButton(OVButton, AddNewButton, SettButton);
					panel = 0;
				}
			}
		});

		// Button toggles add new
		AddNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(addNewGUI.getPanel());
				unclickableButton(AddNewButton, OVButton, SettButton);
				panel = 1;
			}
		});

		// Button toggles settings
		SettButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidation()) { // if add new doesn't have empty fields
					buttonClicked(settGUI.getPanel());
					unclickableButton(SettButton, OVButton, AddNewButton);
					panel = 2;
				}
			}
		});
	}

	// Creating the navigation bar and its functions
	public void NavBar() {

		frame.add(OVButton, "growx, width 200!");
		frame.add(AddNewButton, "growx, width 200!");
		frame.add(SettButton, "wrap,growx,width 200!");

		// Setting the font size of the buttons
		OVButton.setFont(fontSetter);
		AddNewButton.setFont(fontSetter);
		SettButton.setFont(fontSetter);

	}

	// Generic form of action taken when any of the nav buttons are clicked, used above
	private void buttonClicked(JPanel targetPanel) {
		frame.getContentPane().removeAll(); // Removes everything from the frame
		addNewGUI.resetUserFields(); // Resets the inputs if the user added info to them

		NavBar(); // Adds the nav bar 
		frame.add(targetPanel, "span"); // adds the target panel

		frame.revalidate();
		frame.repaint();
	}

	// Sets button to be unclickable, used in actionlistener
	private void unclickableButton(JButton clickedButton, JButton unclickedButton1, JButton unclickedButton2) {
		clickedButton.setEnabled(false);
		unclickedButton1.setEnabled(true);
		unclickedButton2.setEnabled(true);
	}

	// Validation for addnew
	private boolean addNewValidation() {
		boolean valid = true;

		if (panel == 1 && addNewGUI.inputValidation() == false) {
			int option = JOptionPane.showConfirmDialog(frame, "You will lose your unsaved info. Do you want to proceed?");

			if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.NO_OPTION) {
				valid = false;
			}
		}

		return valid;
	}
	
	// Need to make validation for the settings

	public static void main(String args[]) {
		new MainGUI();
	}

}
