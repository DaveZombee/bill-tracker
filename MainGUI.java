import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.LinkedList;

public class MainGUI {

	private JFrame frame = new JFrame("Bill tracker");

	private LinkedList<Bill> listOfBills = retrieveFromFile(); // deserializes linkedlist from file

	// Navigation bar buttons
	private JButton ovButton = new JButton("Overview");
	private JButton addNewButton = new JButton("Add new");
	private JButton settButton = new JButton("Settings");

	// Creating the GUIs which allows for validation
	private OverviewGUI overviewGUI = new OverviewGUI(listOfBills);
	private AddNewGUI addNewGUI = new AddNewGUI(listOfBills);
	private SettGUI settGUI = new SettGUI();

	private int panel = 0; // also for validation - to see which panel is currently visible (0 = overview)

	private JLabel confirmLabel = new JLabel("You will lose your unsaved info. Do you want to proceed?");

	private Font buttonFont = new Font(null, Font.PLAIN, 24); // font for the nav buttons
	private Font confirmFont = new Font(null, Font.PLAIN, 16); // font for the confirmation

	// Creating the main page
	public MainGUI() {
		// Some testin with the due dates ////////////////////////////////////
//		if (listOfBills.get(1).getDueDate().compareTo(LocalDate.now()) == 0){
//			System.out.println("The date is today 0o0"); // positive = future, negative = past, 0 = today
//		}
		//////////////////////////////////////////////////////////////////////
		
		// Frame layout
		frame.setLayout(new MigLayout("", "[200][200][200]", ""));
		
		NavBar(); // adding in the nav bar
		
		frame.add(overviewGUI.getPanel(), "span"); // Starting GUI, when program is first opened
	
		frame.pack();
		frame.setVisible(true);
		
		// Actions taken when user wants to close program
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // don't close
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				if (addNewValidationCloser() == 0) { // if add new doesn't have empty fields
					saveList();  // Serializes list
					System.exit(0); // closes and stops program
				}		
			}
		});
		
		/////////////////////////////
		// BUTTON ACTION LISTENERS //
		/////////////////////////////
		
		// Button toggles overview
		ovButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidationCloser() == 0) { // if add new doesn't have empty fields
					buttonClicked(overviewGUI.getPanel(), ovButton, addNewButton, settButton);
					overviewGUI.reloadTable(listOfBills); // updates jtable
					
					panel = 0;
				}
			}
		});

		// Button toggles add new
		addNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(addNewGUI.getPanel(), addNewButton, ovButton, settButton);
				panel = 1;
			}
		});

		// Button toggles settings
		settButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidationCloser() == 0) { // if add new doesn't have empty fields
					buttonClicked(settGUI.getPanel(), settButton, ovButton, addNewButton);
					panel = 2;
				}
			}
		});
	}

	// Creating the navigation bar and its functions
	public void NavBar() {
		frame.add(ovButton, "growx, width 200!");
		frame.add(addNewButton, "growx, width 200!");
		frame.add(settButton, "wrap,growx,width 200!");

		// Setting the font size of the buttons
		ovButton.setFont(buttonFont);
		addNewButton.setFont(buttonFont);
		settButton.setFont(buttonFont);

		ovButton.setEnabled(false); // can't click the overview button
	}

	// Generic form of action taken when any of the nav buttons are clicked, used above
	private void buttonClicked(JPanel targetPanel, JButton clickedButton, JButton unclickedButton1,
			JButton unclickedButton2) {
		frame.getContentPane().removeAll(); // Removes everything from the frame
		addNewGUI.resetUserFields(); // Resets the inputs if the user added info to them

		NavBar(); // Adds the nav bar
		clickedButton.setEnabled(false); // sets this button to be unclickable
		unclickedButton1.setEnabled(true); // makes this button clickable
		unclickedButton2.setEnabled(true); // same as above

		frame.add(targetPanel, "span"); // adds the target panel

		frame.revalidate(); // these two reload the frame
		frame.repaint();
	}

	// Validation for addnew
	private int addNewValidationCloser() {
		int option = 0; // zero means close

		// if panel is addnew and inputs aren't empty
		if (panel == 1 && addNewGUI.inputsEmpty() == false) {
			confirmLabel.setFont(confirmFont);
			option = JOptionPane.showConfirmDialog(frame, confirmLabel);

		}

		return option;
	}

	// Need to make validation for the settings (could do it in the same method as addNewValidation)

	// Deserialization - retrieves linkedList from list.ser
	private LinkedList<Bill> retrieveFromFile() {

		LinkedList<Bill> listOfBills = new LinkedList<Bill>();

		try {
			FileInputStream file = new FileInputStream("list.ser");
			ObjectInputStream in = new ObjectInputStream(file);
			listOfBills = (LinkedList<Bill>) in.readObject();

			in.close();
			file.close();
		} catch (IOException i) { // Exception occurs when the SER file is empty
		} catch (ClassNotFoundException c) {
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
		new MainGUI(); // starts up the program
	}
}
