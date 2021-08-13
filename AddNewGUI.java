import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.Font;
import java.awt.event.*;

// Used for the date picker
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;

// Used for the date formatting
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFormattedTextField.AbstractFormatter;

public class AddNewGUI {

	private JPanel panel = new JPanel();
	private SpinnerModel payPeriodAmts = new SpinnerNumberModel(1, 1, 12, 1); // For the payment period options

	// All the labels
	private JLabel reqLabel = new JLabel("*required field");
	private JLabel typeLabel = new JLabel("Type*");
	private JLabel payPeriodLabel = new JLabel("Payment period*");
	private JLabel dueDateLabel = new JLabel("Due date*");
	private JLabel notesLabel = new JLabel("Notes");
	private JLabel payPeriodInfo = new JLabel("(time between payments, in months)"); // Payment period: [JSpinner] *info*

	// All of the input fields
	private JTextField typeField = new JTextField(15);
	private JSpinner payPeriodPicker = new JSpinner(payPeriodAmts);
	private JTextField notesField = new JTextField(15);
	private JDatePickerImpl dueDatePicker = dueDatePicker(); // function used because it's quite lengthy to make

	// Button to add the bill
	private JButton addBillButton = new JButton("Add bill");

	// Constructor for the add new GUI
	public AddNewGUI() {
		panel.setLayout(new MigLayout());
		payPeriodPicker.setEditor(new JSpinner.DefaultEditor(payPeriodPicker)); // makes spinner text uneditable
		
		// Font setting
		settingFonts();
		
		// adding and formatting components
		panel.add(reqLabel, "wrap");
		
		panel.add(typeLabel, "align right");
		panel.add(typeField, "span,wrap");
		
		panel.add(payPeriodLabel);
		panel.add(payPeriodPicker);
		panel.add(payPeriodInfo, "wrap");
		
		panel.add(dueDateLabel, "align right");
		panel.add(dueDatePicker, "wrap");
		
		panel.add(notesLabel, "align right");
		panel.add(notesField, "span,wrap");

		// Actions when the add button is clicked
		addBillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Extracting the user inputs from all of the fields
				String selType = typeField.getText();
				int selPayPeriod = (Integer) payPeriodPicker.getValue();
				String selNotes = notesField.getText();
				Date selectedDay = (Date) dueDatePicker.getModel().getValue();

				// Validation - if any of the first three fields are empty, the bill is not added
				if (selType.isEmpty() || selectedDay == null) { // don't need for payperiod since it's default to 1
					JOptionPane.showMessageDialog(panel, "You didn't fill in all of the required fields. Try again.");
				}

				else {
					// Extracting the date, month and year individually
					int selDate = selectedDay.getDate();
					int selMonth = (selectedDay.getMonth() + 1); // Adding the number fixes the month
					int selYear = (selectedDay.getYear() + 1900); // ^^

					// Creating an object to access the methods (for the linked list)
					Bill tempBill = new Bill(selType, selYear, selMonth, selDate, selPayPeriod, selNotes);
					tempBill.getList().add(tempBill);

					// Confirmation that it has been added
					JOptionPane.showMessageDialog(panel, "New bill added");

					resetUserFields(); // Resets fields to be empty
				}
			}
		});

		panel.add(addBillButton, "cell 3 5,gaptop 15"); // Button goes to bottom left

	}

	// Creating the due date picker in a function
	private JDatePickerImpl dueDatePicker() {
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl dueDatePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl dueDatePicker = new JDatePickerImpl(dueDatePanel, new DateLabelFormatter());

		return dueDatePicker;
	}

	// For date formatting of the date picker
	private class DateLabelFormatter extends AbstractFormatter {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
		
		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}
	}

	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}

	// Resets all the different inputs to be empty
	public void resetUserFields() {
		Integer resetSpinner = 1; // used for JSpinner, because it wants an object

		typeField.setText("");
		payPeriodPicker.setValue(resetSpinner);
		dueDatePicker.getJFormattedTextField().setText(""); // Makes text field empty
		dueDatePicker.getModel().setValue(null); // Makes the actual datepicker null
		notesField.setText("");
	}
	
	// Font setter to not clog up the constructor
	private void settingFonts() {
		Font fontSize = new Font(null, Font.PLAIN, 18);
		
		reqLabel.setFont(new Font(null,Font.ITALIC, 18)); // special font for italics
		typeLabel.setFont(fontSize);
		payPeriodLabel.setFont(fontSize);
		dueDateLabel.setFont(fontSize);
		notesLabel.setFont(fontSize);
		payPeriodInfo.setFont(new Font(null,Font.ITALIC,13));
		
		typeField.setFont(fontSize);
		payPeriodPicker.setFont(fontSize);
		notesField.setFont(fontSize);
		dueDatePicker.setFont(fontSize);
		dueDatePicker.getJFormattedTextField().setFont(new Font(null,Font.PLAIN,17));
		addBillButton.setFont(fontSize);
	}
	
	// Validating (used at the nav buttons) - if user has input info but hasn't saved
	public boolean inputValidation() {
		boolean empty = true;
		
		// Getting the inputs
		String selType = typeField.getText();
		int selPayPeriod = (Integer) payPeriodPicker.getValue();
		String selNotes = notesField.getText();
		Date selectedDay = (Date) dueDatePicker.getModel().getValue();
		
		// if typefield isn't empty or pay picker isn't one or notesfield isn't empty or selected day isn't null
		if(selType.isEmpty() == false || selPayPeriod != 1 || selNotes.isEmpty() == false || selectedDay != null) {
			empty = false;
		}
			
		return empty;
	}
}
