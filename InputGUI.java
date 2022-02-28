// GUI
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;

// Used for date picker
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;

// Used for date formatting
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter; // Used for both date picker and formatting
import javax.swing.JSpinner.DefaultEditor; // for the JSpinners to have wider text fields

public class InputGUI {

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

	// Button for saving inputs
	private JButton saveButton = new JButton();

	// Dialog box label
	private JLabel dialogLabel = new JLabel();

	// Constructor
	public InputGUI() {
		panel.setLayout(new MigLayout());

		payPeriodPicker.setEditor(new JSpinner.DefaultEditor(payPeriodPicker)); // makes spinner text uneditable
		
		// Setting spinner text to be centered
		JSpinner.DefaultEditor spinEditor = (DefaultEditor) payPeriodPicker.getEditor();
		spinEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);

		// Font setting
		settingFonts();

		// adding and formatting components
		panel.add(reqLabel, "wrap");

		panel.add(typeLabel, "align right");
		panel.add(typeField, "span,wrap");

		panel.add(payPeriodLabel);
		panel.add(payPeriodPicker,"width 40!");
		panel.add(payPeriodInfo, "wrap");

		panel.add(dueDateLabel, "align right");
		panel.add(dueDatePicker, "wrap");

		panel.add(notesLabel, "align right");
		panel.add(notesField, "span,wrap");

		panel.add(saveButton, "cell 3 5,gaptop 15"); // Button goes to bottom left
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

	// Font setter to not clog up the constructor
	private void settingFonts() {
		Font fontSize = new Font(null, Font.PLAIN, 18);

		reqLabel.setFont(new Font(null, Font.ITALIC, 18)); // special font for italics
		typeLabel.setFont(fontSize);
		payPeriodLabel.setFont(fontSize);
		dueDateLabel.setFont(fontSize);
		notesLabel.setFont(fontSize);
		payPeriodInfo.setFont(new Font(null, Font.ITALIC, 13));

		typeField.setFont(fontSize);
		payPeriodPicker.setFont(fontSize);
		notesField.setFont(fontSize);
		dueDatePicker.setFont(fontSize);
		dueDatePicker.getJFormattedTextField().setFont(new Font(null, Font.PLAIN, 17));

		saveButton.setFont(fontSize);

		dialogLabel.setFont(new Font(null, Font.PLAIN, 16));
	}

	// validates and checks the inputs
	public boolean saveButtonClicked(String selType, Date selDay) {
		boolean saved = false;

		// If the required fields are empty
		if (selType.isEmpty() || selDay == null) { // don't need for payperiod since it can't ever be empty
			dialogLabel.setText("You didn't fill in all of the required fields. Try again.");
			JOptionPane.showMessageDialog(getPanel(), getDialogLabel());
		}

		// if date has already been passed, bill is not saved
		else if (selDay.before(new Date())) { // new Date() gives the current date
			dialogLabel.setText("The selected due date has already passed. Select another.");
			JOptionPane.showMessageDialog(getPanel(), getDialogLabel());
		}

		// if the input fields passes the validations, then save the inputs
		else {
			saved = true;
		}
		
		return saved;
	}

	// Getters for the input fields, button and dialogue box
	public JTextField getTypeField() {
		return typeField;
	}

	public JSpinner getPayPeriodPicker() {
		return payPeriodPicker;
	}

	public JTextField getNotesField() {
		return notesField;
	}

	public JDatePickerImpl getDueDatePicker() {
		return dueDatePicker;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public JLabel getDialogLabel() {
		return dialogLabel;
	}

	// Allows MainGUI and subclasses to access the JPanel
	public JPanel getPanel() {
		return panel;
	}
