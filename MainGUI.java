
// For the GUI
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;

import java.awt.event.*; // Action listener

// For the system tray (alerts)
import java.awt.AWTException;
import java.awt.SystemTray;

// Lists
import java.util.LinkedList;
import java.util.ArrayList;

// File handling (serializing)
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MainGUI {

	private JFrame frame = new JFrame("Bill tracker");

	private Object[] saveList = retrieveFromFile(); // deserializing the file that holds the two things below
	private LinkedList<Bill> listOfBills = (LinkedList<Bill>) saveList[0]; // getting the deserialized linkedlist
	private ArrayList<Integer> remindList = (ArrayList<Integer>) saveList[1]; // getting the deserialized arraylist

	// Navigation bar buttons
	private JButton ovButton = new JButton("Overview");
	private JButton addNewButton = new JButton("Add new");
	private JButton settButton = new JButton("Settings");

	// Creating the GUIs which allows for validation
	private OverviewGUI overviewGUI = new OverviewGUI(listOfBills);
	private AddNewGUI addNewGUI = new AddNewGUI(listOfBills);
	private SettGUI settGUI = new SettGUI(remindList);

	private int panel = 0; // also for validation - to know which panel is currently visible (0 = overview)

	// Label used in the validations
	private JLabel confirmLabel = new JLabel("You will lose your unsaved info. Do you wish to proceed?");

	private Font buttonFont = new Font(null, Font.PLAIN, 24); // font for the nav buttons

	// Creating the main page (constructor)
	public MainGUI() {
		// Frame layout
		frame.setLayout(new MigLayout("", "[200][200][200]", ""));

		NavBar(); // adding in the nav bar

		frame.add(overviewGUI.getPanel(), "span"); // Starting GUI, when program is first opened

		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null); // puts the program in the center of the screen

		// Actions taken when user wants to close program
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // don't close

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				// if add new doesn't have empty fields and settings fields aren't changed
				if (addNewValidation() == 0 && settingsValidation() == 0) {
					saveList(); // Serializes list
					System.exit(0); // closes and stops program
				}
			}
		});

		/////////////////////////////
		// button action listeners //
		/////////////////////////////

		// opens overview
		ovButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if add new doesn't have empty fields and settings fields aren't changed
				if (addNewValidation() == 0 && settingsValidation() == 0) {
					buttonClicked(overviewGUI.getPanel(), ovButton, addNewButton, settButton);
					overviewGUI.reloadTable(listOfBills); // updates jtable
					panel = 0;
				}
			}
		});

		// opens add new
		addNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// if settings fields haven't been changed
				if (settingsValidation() == 0) {
					buttonClicked(addNewGUI.getPanel(), addNewButton, ovButton, settButton);
					panel = 1;
				}
			}
		});

		// opens settings
		settButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (addNewValidation() == 0) { // if add new doesn't have empty fields
					buttonClicked(settGUI.getPanel(), settButton, ovButton, addNewButton);
					panel = 2;
				}
			}
		});

		////////////////////////////////////
		// Sending out the reminder/alert //
		////////////////////////////////////

		Reminders reminders = new Reminders(listOfBills, remindList); // creates a list of bills that need a reminder
		if (reminders.hasAlerts()) {
			String messageAlertStr = reminders.getAlertMessageStr(); // the string used in the alert

			if (SystemTray.isSupported()) { // if the OS supports the system tray (windows)
			try {
				reminders.alertSender(messageAlertStr); // sends out the alert with the message
			} catch (AWTException e) { // catch doesn't do anything
			}
		}

			else { // OS doesn't support the system tray, so send out dialog box instead
				// html used to format
				JLabel alertLabel = new JLabel("<html><p style=\"width:250px\">"+messageAlertStr+"</p></html>");
				alertLabel.setFont(new Font(null,Font.PLAIN,16));
				JOptionPane.showMessageDialog(frame,alertLabel);
			}
		}
	}

	////////////////////
	// Navigation bar //
	////////////////////

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

	// Generic form of action taken when any of the nav buttons are clicked, used
	// above
	private void buttonClicked(JPanel targetPanel, JButton clickedButton, JButton unclickedButton1,
			JButton unclickedButton2) {
		frame.getContentPane().removeAll(); // Removes everything from the frame
		addNewGUI.resetUserFields(); // Resets the inputs in add new if the user added info to them
		settGUI.resetFields(remindList); // resets the inputs back to saved settings if user changed it

		NavBar(); // Adds the nav bar
		clickedButton.setEnabled(false); // sets this button to be unclickable
		unclickedButton1.setEnabled(true); // makes this button clickable
		unclickedButton2.setEnabled(true); // makes this button clickable

		frame.add(targetPanel, "span"); // adds the target panel

		frame.revalidate(); // these two reload the frame
		frame.repaint();
	}

	/////////////////
	// Validations //
	/////////////////

	// Validation for addnew
	private int addNewValidation() {
		int option = 0; // zero means close

		// if panel is addnew and inputs aren't empty
		if (panel == 1 && addNewGUI.isInputsEmpty() == false) {
			confirmLabel.setFont(new Font(null, Font.PLAIN, 16));
			option = JOptionPane.showConfirmDialog(frame, confirmLabel);
		}
		return option;
	}

	// Validation for settings
	private int settingsValidation() {
		int option = 0; // zero = close

		// if panel is settings and inputs were changed
		if (panel == 2 && settGUI.isChanged(remindList)) {
			confirmLabel.setFont(new Font(null, Font.PLAIN, 16));
			option = JOptionPane.showConfirmDialog(frame, confirmLabel);
		}

		return option;
	}

	/////////////////////////
	// Serialization stuff //
	/////////////////////////

	// Deserialization - retrieves linkedList from saveFile.ser
	private Object[] retrieveFromFile() {

		LinkedList<Bill> listOfBills = new LinkedList<Bill>();
		ArrayList<Integer> remindList = new ArrayList<Integer>(2);
		Object[] savedList = new Object[] { listOfBills, remindList };

		try {
			FileInputStream file = new FileInputStream("saveFile.ser");
			ObjectInputStream in = new ObjectInputStream(file);
//
			savedList = (Object[]) in.readObject();

			in.close();
			file.close();

		} catch (IOException i) { // Exception occurs when the SER file is empty
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
		}

		if (((ArrayList<Integer>) savedList[1]).isEmpty()) { // if the program is being started for the first time
			remindList.add(1);
			remindList.add(3);
		}

		return savedList;
	}

	// Serialization - saves linkedList and remindList to saveFile.ser
	private void saveList() {
		try {
			FileOutputStream file = new FileOutputStream("saveFile.ser");
			ObjectOutputStream out = new ObjectOutputStream(file);

			Object[] savedList = new Object[] { listOfBills, remindList };
			out.writeObject(savedList);

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
