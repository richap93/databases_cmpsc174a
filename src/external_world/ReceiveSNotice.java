package external_world;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class ReceiveSNotice extends JFrame {

	private JPanel contentPane;
	private JList _list;
	private JLabel companyName;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JTable table, table2;
	private JScrollPane scrollPane, scrollPane2;
	private JFrame frameReceiveNotice = this;
	private JTextField textField;
	private String orderno, location, stockno, manu, modelno;
	private int quantity;
	
	private static final String CHAR_LIST = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUM_LIST = "0123456789";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReceiveSNotice frame = new ReceiveSNotice();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ReceiveSNotice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Receive shipment notice");

		JLabel lblNewLabel = new JLabel("Select Order #:");
		lblNewLabel.setBounds(10, 30, 187, 15);
		contentPane.add(lblNewLabel);

		JScrollPane listScrollPane = new JScrollPane();

		String[] itemList =  setListData();
		_list = new JList(itemList);
		_list.setVisibleRowCount(8);
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setSelectedIndex(0);
		scrollPane = new JScrollPane(_list);
		scrollPane.setLocation(11, 48);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(265, 50);
		getContentPane().add(scrollPane);

		textField = new JTextField();
		textField.setBounds(163, 110, 114, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("Select");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = _list.getSelectedIndex();
				orderno = (String) _list.getSelectedValue();
				String snID = textField.getText();
				if (checkUniqueness(snID) == false) {
					return;
				}
				if (snID.equals("")) {
					JOptionPane.showMessageDialog(frameReceiveNotice,
							"Please enter a shipping notice ID.",
							"Error",
							JOptionPane.OK_OPTION);
				} else {
					scrollPane.setVisible(false);
					setCompany(orderno);
					updateDatabase(orderno, snID);
					getOrderedItems(orderno);
				}
			}
		});
		button.setBounds(288, 106, 89, 23);
		contentPane.add(button);

		JLabel lblProductsBeingShipped = new JLabel("Products being shipped:");
		lblProductsBeingShipped.setBounds(10, 160, 187, 15);
		contentPane.add(lblProductsBeingShipped);

		table = new JTable();

		table.setRowSelectionAllowed(false);
		table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		table.setBounds(22, 117, 400, 80);

		scrollPane = new JScrollPane(table);
		scrollPane.setToolTipText("");
		scrollPane.setLocation(10, 180);
		scrollPane.setSize(367, 100);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);

		JButton button_1 = new JButton("Back");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ExternalWorld().setVisible(true);
			}
		});
		button_1.setBounds(380, 12, 89, 23);
		contentPane.add(button_1);

		JLabel lblShippingNoticeId = new JLabel("Shipping notice ID is: ");
		lblShippingNoticeId.setBounds(10, 110, 187, 15);
		contentPane.add(lblShippingNoticeId);

		JLabel lblShippingCompany = new JLabel("Shipping company: ");
		lblShippingCompany.setBounds(10, 135, 187, 15);
		contentPane.add(lblShippingCompany);

		companyName = new JLabel("");
		companyName.setBounds(151, 135, 150, 15);
		contentPane.add(companyName);

		JButton btnProcess = new JButton("Process");
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to process this order?", "Process order",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					processOrder(orderno);
				} 
			
		}
	});
		btnProcess.setBounds(380, 257, 92, 23);
		contentPane.add(btnProcess);
		
}

protected void processOrder(String orderno) {
		// TODO Auto-generated method stub
	String query = "SELECT SP.manufacturer FROM eDepot_ShipmentProcess SP, eDepot_ShipmentContents SC "
			+ "WHERE SP.orderno ='"+orderno+"' AND SP.pid=SC.pid";
	
	
	try {
		PreparedStatement stmt = _conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();	
		
		while (rs.next()) {
			manu = rs.getString("manufacturer");
		}
	
		query = "SELECT SC.modelno, SC.quantity FROM eDepot_ShipmentProcess SP, eDepot_ShipmentContents SC "
				+ "WHERE SP.orderno ='"+orderno+"' AND SP.pid=SC.pid";
		
		stmt = _conn.prepareStatement(query);
		rs = stmt.executeQuery();
		
		while (rs.next()) {
			
			modelno = rs.getString("modelno");
			quantity = rs.getInt("quantity");
			
			query = "SELECT * FROM eDepot_Products WHERE modelno = '"+modelno+"' AND manufacturer = '"+manu+"'";
			
			stmt = _conn.prepareStatement(query);
			ResultSet exists = stmt.executeQuery();
			
			int size = 0;
			while (exists.next()) {
				stockno = exists.getString("stockno");
				
				query = "UPDATE eDepot_Products SET replenishment = '"+quantity+"' WHERE stockno = '"+stockno+"'";
				
				stmt = _conn.prepareStatement(query);
				stmt.executeUpdate();
				
				size++;
			}
			
			boolean foundValid = false;
			
			if (size == 0) {
				
				stockno = "";
				
				while (foundValid == false) {
					
					stockno = stockno + getRandomLetter() + getRandomLetter();
					stockno = stockno + getRandomNum() + getRandomNum() + getRandomNum() + getRandomNum() + getRandomNum();
					
					query = "SELECT * FROM eDepot_Products WHERE stockno ='"+stockno+"'";
					
					stmt = _conn.prepareStatement(query);
					exists = stmt.executeQuery();
					
					if (!exists.isBeforeFirst()) {
						foundValid = true;
						break;
					} 
				}
				
				String queryUpdate = "INSERT INTO eDepot_Products values('"+stockno+"',0,2,10, null,"+quantity+",'"+manu+"','"+modelno+"')";
				stmt.executeUpdate(queryUpdate);
				
			} 
			
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		
	}

protected void insertNewProduct(String query) {
	// TODO Auto-generated method stub
	try {
		PreparedStatement stmt = _conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private String getRandomLetter() {
	Random r = new Random();
	int num = r.nextInt(26 - 0) + 0;
	char letter = CHAR_LIST.charAt(num);
	return String.valueOf(letter);
}

private int getRandomNum() {
	Random r = new Random();
	int num = r.nextInt(10 - 0) + 0;
	return num;
}

protected boolean checkUniqueness(String snID) {
		// TODO Auto-generated method stub
	String query = "SELECT * FROM eDepot_ShipmentProcess WHERE snotice = '"+snID+"'";
	
	try {
		PreparedStatement stmt = _conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();	
		
		int size = 0;
		while (rs.next()) {
			size++;
		}
	
		if (size != 0) {
			JOptionPane.showMessageDialog(frameReceiveNotice,
					"Shipping notice already exists",
					"Error",
					JOptionPane.OK_OPTION);
			return false;
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return true;
	}

protected void updateDatabase(String orderno, String snID) {
		// TODO Auto-generated method stub
	
	String query = "UPDATE eDepot_ShipmentProcess SET snotice = '"+snID+"', status = 'In Transit' WHERE orderno = '"+orderno+"'";
	
	try {
		PreparedStatement stmt = _conn.prepareStatement(query);
		stmt.executeUpdate();	
	
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private void setCompany(String orderno) {
	// TODO Auto-generated method stub
	String query = "SELECT * FROM eDepot_ShipmentProcess WHERE orderno = ?"; 

	String company = "";

	try {
		PreparedStatement stmt = _conn.prepareStatement(query);
		stmt.setString(1, orderno);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			company = rs.getString("manufacturer");
			companyName.setText(company);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

protected void getOrderedItems(String orderno) {
	// TODO Auto-generated method stub
	String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	String strUsername = "dschuerhoff";
	String strPassword = "X280715";

	String query = "SELECT SC.modelno, SC.quantity "
			+ "FROM eDepot_ShipmentContents SC, eDepot_ShipmentProcess SP "
			+ "WHERE SP.orderno = ? AND SP.pid=SC.pid";

	try {

		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		PreparedStatement stmt = _conn.prepareStatement(query);
		stmt.setString(1, orderno);
		_rs = stmt.executeQuery();

		int size = 0;
		while (_rs.next()) {
			size++;
		}

		Object[][] tableContents = new Object[size][2];

		_rs = stmt.executeQuery();


		int pos = 0;
		while (_rs.next()) {
			tableContents[pos][0] = _rs.getString("modelno");
			tableContents[pos][1] = _rs.getInt("quantity");
			pos++;
		}


		table2 = new JTable();

		table2.setModel(new DefaultTableModel(tableContents,new String[] {"Product", "Quantity"}));

		table2.setRowSelectionAllowed(false);
		table2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));

		table2.setBounds(22, 117, 400, 80);
		scrollPane2 = new JScrollPane(table2);
		scrollPane2.setToolTipText("");
		scrollPane2.setLocation(10, 180);
		scrollPane2.setSize(367, 100);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table2.getColumnModel().getColumn(0).setPreferredWidth(300);
		contentPane.add(scrollPane2);

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


}


private String[] setListData() {

	ArrayList<String> _items = new ArrayList<String>();
	String[] _itemList = {""};

	String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	String strUsername = "dschuerhoff";
	String strPassword = "X280715";

	String query = "SELECT orderno "
			+ "FROM eDepot_ShipmentProcess "
			+ "WHERE status='Ordered'"; 


	try {
		DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
		_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
		_statement = _conn.createStatement();
		_rs = _statement.executeQuery (query);

		while (_rs.next()) {
			_items.add(_rs.getString("orderno"));
		}

		_itemList = new String[_items.size()];
		_itemList = _items.toArray(_itemList);

	} catch (SQLException e) {
		e.printStackTrace();
	}

	return _itemList;
}
}
