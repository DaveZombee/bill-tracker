import java.awt.Font;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class OverviewGUI {

	private JPanel panel = new JPanel();

	private String[] columnHeaders = { "Type", "Notes", "Pay period", "Due date","Paid" }; // Table column headings
	private JTable actualTable = new JTable(fillTable(), columnHeaders);
	private JScrollPane billTable = new JScrollPane(actualTable); // Lets the column headings can be seen
	
	// Constructor for the overview GUI
	public OverviewGUI() {
		actualTable.setDefaultEditor(Object.class, null); // table can't be edited
		actualTable.getTableHeader().setReorderingAllowed(false); // columns can't be dragged
		actualTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // only one row can be selected
		actualTable.getTableHeader().setResizingAllowed(false); // cannot resize the columns
		actualTable.setRowHeight(actualTable.getRowHeight() + 10); // making the row size bigger

		// Setting the font sizes
		actualTable.setFont(new Font(null,Font.PLAIN,16)); // Sets font of the info in the table
		actualTable.getTableHeader().setFont(new Font(null,Font.PLAIN,18)); // Sets font of the table headers
		
		// Layout and adding table
		panel.setLayout(new MigLayout());		
		panel.add(billTable,"width 595!, height 200!, align center"); 
		// (it's the JScrollPane that's being added, but that's how it works)
	}

	
	// Lets MainGUI access this panel of GUI
	public JPanel getPanel() {
		return panel;
	}
	
	private String[][] fillTable() {
		Bill tempBill = new Bill(); // to access the linked list
		int listLength = tempBill.getList().size();
		
		String[][] tableData = new String[listLength][4]; // [rows][columns]
		
		for (int i = 0; i < listLength; i++) {
			tableData[i] = tempBill.getList().get(i).getArray();
		}
		
		return tableData;
	}
	

}
