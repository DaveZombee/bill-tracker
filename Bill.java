import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Bill {

	private String type;
	private LocalDate dueDate;
	private int payPeriod;
	private String notes;
	private boolean paid;

	private static LinkedList<Bill> listOfBills = new LinkedList<Bill>();
	
	// Constructor for making a bill
	public Bill(String type, int year, int month, int date, int payPeriod, String notes) {
		this.type = type;
		dueDate = LocalDate.of(year, month, date);
		this.payPeriod = payPeriod;
		this.notes = notes;

		paid = false; // default setting - it isn't paid
	}

	// Null constructor for when a bill is made to access the linked list
	public Bill() {	}
	
	// Creates an array, which the JTable in the overview GUI needs
	public String[] getArray() {
		String formattedDate = dueDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")); // formats date
		
		String[] billArray = { type, notes, (String.valueOf(payPeriod)+" month(s)"), formattedDate};
		return billArray;
	}
	
	// Allows for adding of bills to the linked list from other classes
	public void addToList(Bill bill) {
		listOfBills.add(bill);
	}
	
	// temporary linked list getter
	public LinkedList<Bill> getList() {
		return listOfBills;
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

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public static void main(String[] args) {
		// Create linked list to store existing bills
		
//		listOfBills.add(new Bill("type", 2004, 11, 4, 3, ""));
//		listOfBills.add(new  Bill("gas", 2021, 12, 4, 3, ""));
//		
	

	}

}
