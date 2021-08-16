import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Bill implements Serializable { // serializable so it can save the linkedList

	private String type;
	private LocalDate dueDate;
	private int payPeriod;
	private String notes;
	private boolean paid;
	
	// Constructor for making a bill
	public Bill(String type, int year, int month, int date, int payPeriod, String notes) {
		this.type = type;
		dueDate = LocalDate.of(year, month, date);
		this.payPeriod = payPeriod;
		this.notes = notes;

		paid = false; // default - it isn't paid
	}
	
	// Creates an array, which the JTable in the overview GUI needs
	public String[] getArray() {
		String formattedDate = dueDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")); // formats date
		
		String strPaid; // format paid to be yes or no instead of true or false
		if (paid == true) strPaid = "Yes";
		else strPaid = "No";
		
		String[] billArray = { type, notes, (String.valueOf(payPeriod)+" month(s)"), formattedDate, strPaid};
		return billArray;
	}
		
	// Getters and setters for editing and specific viewing of a bill payment
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public int getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(int payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean getPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

}
