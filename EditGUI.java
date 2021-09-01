import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;

public class EditGUI extends InputGUI {

	private JFrame frame = new JFrame("Bill tracker");

	public EditGUI(Bill bill) {
		// Adding the components
		super();
		getSaveButton().setText("Save");

		// Converting the due date into its required forms
		LocalDate dueDate = bill.getDueDate();
		Date date = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		int dueDateDay = date.getDate();
		int dueDateMonth = date.getMonth();
		int dueDateYear = (date.getYear() + 1900);
		
		// Setting the text in the input fields to match the bill
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
				
				if (saveButtonClicked(selType,selDay)) {
					// Extracting the date, month and year individually to create a LocalDate
					int selDate = selDay.getDate();
					int selMonth = (selDay.getMonth() + 1); // Adding 1 fixes the month
					int selYear = (selDay.getYear() + 1900); // Adding 1900 fixes hthe year
										
					LocalDate localDueDate = LocalDate.of(selYear, selMonth, selDate);

					// Setting the inputs in the bill itself
					bill.setType(selType);
					bill.setPayPeriod(selPayPeriod);
					bill.setDueDate(localDueDate);
					bill.setNotes(selNotes);
					
					// Confirmation that it has been added
					getDialogLabel().setText("Bill saved");
					JOptionPane.showMessageDialog(getPanel(), getDialogLabel());
				}
			}
		});

		// Setting up the frame
		frame.setLayout(new MigLayout());

		frame.add(getPanel());

		frame.pack();
		frame.setVisible(true);
	}

	// Used by OverviewGUI to implement window listener
	public JFrame getFrame() {
		return frame;
	}	
}
