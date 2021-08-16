import java.awt.Font;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class ViewGUI {
	
	private JFrame frame = new JFrame("Bill tracker");
	
	// All the labels
	private JLabel titleLabel = new JLabel();
	private JLabel dueDateLabel = new JLabel();
	private JLabel payPeriodLabel = new JLabel();
	private JLabel estDueDateLabel = new JLabel();
	private JLabel paidLabel = new JLabel();
	
	private Font labelFont = new Font(null,Font.PLAIN,16); // font for the labels
	
	public ViewGUI(Bill bill) {	
		frame.setLayout(new MigLayout()); // Layout manager
		
		// Setting the text of the fonts (and the strings it needs)
		String dueDateStr = new String(bill.getDueDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
		LocalDate estDueDate = bill.getDueDate().plusMonths(bill.getPayPeriod()); // Adding the months
		String estDueDateStr = new String(estDueDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
		
		String paidStr;
		if (bill.getPaid() == true) paidStr = "Yes";
		else paidStr = "No";
		
		titleLabel.setText("Viewing details of: "+bill.getType());
		dueDateLabel.setText("Due date: "+dueDateStr);
		payPeriodLabel.setText("Payment period: "+(String.valueOf(bill.getPayPeriod())+" month(s)"));
		estDueDateLabel.setText("Next estimated due date: "+estDueDateStr);
		paidLabel.setText("Paid: "+paidStr);
		
		// Setting the label fonts
		titleLabel.setFont(new Font(null,Font.PLAIN,20));
		dueDateLabel.setFont(labelFont);
		payPeriodLabel.setFont(labelFont);
		estDueDateLabel.setFont(labelFont);
		paidLabel.setFont(labelFont);
		
		// Adding labels to the frame
		frame.add(titleLabel,"align center, wrap, gapbottom 15"); // gapbottom makes a bigger space in between labels
		frame.add(dueDateLabel,"wrap");
		frame.add(payPeriodLabel,"wrap");
		frame.add(payPeriodLabel,"wrap");
		frame.add(estDueDateLabel,"wrap");
		frame.add(paidLabel);
		
		frame.pack();
		frame.setVisible(true);
	}
	
}
