// GUI
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;

import java.awt.event.*; // action listeners

// dealing with the due date time
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EditGUI extends InputGUI {

	private JFrame frame = new JFrame("Bill tracker");
	private JLabel confirmLabel = new JLabel("You will lose your unsaved info. Do you want to proceed?");

	public EditGUI(Bill bill) {
		super(); // inheriting the constructor
		getSaveButton().setText("Save"); // setting the text in the button

		// Converting the due date into its required forms
		LocalDate dueDate = bill.getDueDate();
		Date date = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		int dueDateDay = date.getDate();
		int dueDateMonth = date.getMonth();
		int dueDateYear = (date.getYear() + 1900);

		// Setting the text in the input fields to match the given bill
		getTypeField().setText(bill.getType());
		getPayPeriodPicker().setValue(bill.getPayPeriod());
		getNotesField().setText(bill.getNotes());

		getDueDatePicker().getModel().setDay(dueDateDay);
		getDueDatePicker().getModel().setMonth(dueDateMonth);
		getDueDatePicker().getModel().setYear(dueDateYear);
		getDueDatePicker().getModel().setSelected(true);

		// Action taken when save button is clicked
		getSaveButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Getting the inputs
				String selType = getTypeField().getText();
				int selPayPeriod = (Integer) getPayPeriodPicker().getValue();
				String selNotes = getNotesField().getText();
				Date selDay = (Date) getDueDatePicker().getModel().getValue();

				if (saveButtonClicked(selType, selDay)) {
					// Extracting the date, month and year individually to create a LocalDate
					int selDate = selDay.getDate();
					int selMonth = (selDay.getMonth() + 1); // Adding 1 fixes the month
					int selYear = (selDay.getYear() + 1900); // Adding 1900 fixes the year

					LocalDate selLocalDueDate = LocalDate.of(selYear, selMonth, selDate);

					// Setting the inputs in the bill itself
					bill.setType(selType);
					bill.setPayPeriod(selPayPeriod);
					bill.setDueDate(selLocalDueDate);
					bill.setNotes(selNotes);

					// Confirmation that it has been added
					getDialogLabel().setText("Bill saved");
					
					// Shows dialog to confirm bill has been saved
					int buttonClick = JOptionPane.showOptionDialog(null, getDialogLabel(), "Bill tracker",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
					
					if (buttonClick == 0 || buttonClick == -1) { // if user has closed the dialog box, close the frame
						frame.setVisible(false);
						frame.dispose();
						frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); // triggers windowlistener
					}
				}
			}
		});

		// Setting up the frame
		frame.setLayout(new MigLayout());

		frame.add(getPanel());

		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		// Actions taken when user wants to close program
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // don't close

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				if (isInputsChanged(bill)) {
					confirmLabel.setFont(new Font(null, Font.PLAIN, 16));
					int option = JOptionPane.showConfirmDialog(frame, confirmLabel);

					if (option == 0) {
						frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only the edit frame
					}
				}

				else frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only the edit frame 
			}
		});
	}

	// Used by OverviewGUI to implement window listener
	public JFrame getFrame() {
		return frame;
	}

	// Checking if the inputs have been changed but haven't been saved
	private boolean isInputsChanged(Bill bill) {
		boolean changed = false;

		// Getting the inputs
		String selType = getTypeField().getText();
		int selPayPeriod = (Integer) getPayPeriodPicker().getValue();
		String selNotes = getNotesField().getText();
		Date selDay = (Date) getDueDatePicker().getModel().getValue();

		int selDate = selDay.getDate();
		int selMonth = (selDay.getMonth() + 1); // Adding 1 fixes the month
		int selYear = (selDay.getYear() + 1900); // Adding 1900 fixes the year

		LocalDate selDueDate = LocalDate.of(selYear, selMonth, selDate); // making localdate to compare

		// Getting the info from the saved bill
		String billType = bill.getType();
		int billPayPeriod = bill.getPayPeriod();
		String billNotes = bill.getNotes();
		LocalDate billDueDate = bill.getDueDate();

		// check if they're the same as the currently saved bill info
		if (selType.equals(billType) == false || selPayPeriod != billPayPeriod || selNotes.equals(billNotes) == false
				|| selDueDate.compareTo(billDueDate) != 0) {
			changed = true;
		}

		return changed;
	}
}
