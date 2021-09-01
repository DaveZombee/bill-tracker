import javax.swing.*;
import java.util.Date;
import java.util.LinkedList;
import java.awt.Font;
import java.awt.event.*;
import java.time.LocalDate;

public class AddNewGUI extends InputGUI {

	// Constructor for the add new GUI
	public AddNewGUI(LinkedList<Bill> listOfBills) {
		super(); // Inheriting the input fields and the associated texts
		getSaveButton().setText("Add bill"); // Set text of the button

		// Actions when the add button is clicked
		getSaveButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Extracting the user inputs from all of the fields
				String selType = getTypeField().getText();
				int selPayPeriod = (Integer) getPayPeriodPicker().getValue();
				String selNotes = getNotesField().getText();
				Date selDay = (Date) getDueDatePicker().getModel().getValue();

				if (saveButtonClicked(selType,selDay)) {
					// Extracting the date, month and year individually to create a LocalDate
					int selDate = selDay.getDate();
					int selMonth = (selDay.getMonth() + 1); // Adding the number fixes the month
					int selYear = (selDay.getYear() + 1900); // ^^
					
					LocalDate localDueDate = LocalDate.of(selYear, selMonth, selDate);

					// Adding the bill to the list
					listOfBills.add(new Bill(selType, localDueDate, selPayPeriod, selNotes));

					// Confirmation that it has been added
					getDialogLabel().setText("New bill added");
					JOptionPane.showMessageDialog(getPanel(), getDialogLabel());

					resetUserFields(); // Resets fields to be empty
				}
			}
		});
	}

	// Resets all the different inputs to be empty
	public void resetUserFields() {
		Integer resetSpinner = 1; // used for JSpinner, because it wants an object

		getTypeField().setText("");
		getPayPeriodPicker().setValue(resetSpinner);
		getDueDatePicker().getJFormattedTextField().setText(""); // Makes text field empty
		getDueDatePicker().getModel().setValue(null); // Makes the actual datepicker null
		getNotesField().setText("");
	}

	// Validating (used at the nav buttons) - if user has input info still in the fields
	public boolean inputsEmpty() {
		boolean empty = true;

		// Getting the inputs
		String selType = getTypeField().getText();
		int selPayPeriod = (Integer) getPayPeriodPicker().getValue();
		String selNotes = getNotesField().getText();
		Date selectedDay = (Date) getDueDatePicker().getModel().getValue();

		// if typefield isn't empty or pay picker isn't one or notesfield isn't empty or
		// selected day isn't null
		if (selType.isEmpty() == false || selPayPeriod != 1 || selNotes.isEmpty() == false || selectedDay != null) {
			empty = false;
		}

		return empty;
	}

}
