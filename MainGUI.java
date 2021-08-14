import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class MainGUI {

	private JFrame frame = new JFrame("Bill tracker");
	
	private LinkedList<Bill> listOfBills = retrieveFromFile(); // deserializes linkedlist

	// Navigation bar buttons
	private JButton OVButton = new JButton("Overview");
	private JButton AddNewButton = new JButton("Add new");
	private JButton SettButton = new JButton("Settings");

	// Creating two GUIs which allows for validation
	private AddNewGUI addNewGUI = new AddNewGUI(listOfBills);
	private SettGUI settGUI = new SettGUI();

	private int panel = 0; // also for validation - to see which panel is currently visible

	private Font fontSetter = new Font(null, Font.PLAIN, 24); // font for the nav buttons

	// Creating the main page
	public MainGUI() {
		frame.setLayout(new MigLayout("", "[200][200][200]", ""));

		NavBar();
		OVButton.setEnabled(false); // can't click overview button when starting
		frame.add(new OverviewGUI(listOfBills).getPanel(), "span"); // Starting GUI, when program is first opened
		
		frame.pack();
		frame.setVisible(true);
		
		// Action taken when window is closed
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				saveList();  // Serializes list
				System.exit(0);
			}
		});
		
		/////////////////////////////
		// BUTTON ACTION LISTENERS //
		/////////////////////////////
		
		// Button toggles overview
		OVButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidation()) { // if add new doesn't have empty fields
					buttonClicked(new OverviewGUI(listOfBills).getPanel(), OVButton, AddNewButton, SettButton);
					panel = 0;
				}
			}
		});

		// Button toggles add new
		AddNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(addNewGUI.getPanel(), AddNewButton, OVButton, SettButton);
				panel = 1;
			}
		});

		// Button toggles settings
		SettButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidation()) { // if add new doesn't have empty fields
					buttonClicked(settGUI.getPanel(), SettButton, OVButton, AddNewButton);
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
	private void buttonClicked(JPanel targetPanel, JButton clickedButton, JButton unclickedButton1, JButton unclickedButton2) {
		frame.getContentPane().removeAll(); // Removes everything from the frame
		addNewGUI.resetUserFields(); // Resets the inputs if the user added info to them

		NavBar(); // Adds the nav bar 
		clickedButton.setEnabled(false); // sets this button to be unclickable
		unclickedButton1.setEnabled(true); // makes this button clickable
		unclickedButton2.setEnabled(true); // same as above
		
		frame.add(targetPanel, "span"); // adds the target panel

		frame.revalidate();
		frame.repaint();
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
	
	// Need to make validation for the settings (can do it in the same method as addNewValidation)
	
	// Deserialization - retrieves linkedList from list.ser
	private LinkedList<Bill> retrieveFromFile(){
		
		try {
			FileInputStream file = new FileInputStream("list.ser");
			ObjectInputStream in = new ObjectInputStream(file);
			listOfBills = (LinkedList<Bill>) in.readObject();
			
			in.close();
			file.close();
		} catch (IOException i){ // Exception occurs when the SER file is empty
		} catch (ClassNotFoundException c){
			c.printStackTrace();
		}
		
		return listOfBills;
		
	}
	
	// Serialization - saves linkedList to list.ser
	private void saveList() {
		try {
			FileOutputStream file = new FileOutputStream("list.ser");
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			out.writeObject(listOfBills);
			
			out.close();
			file.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new MainGUI();
	}

}
