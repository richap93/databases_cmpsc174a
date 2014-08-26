package manager;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class SendOrder extends JFrame {

	private JPanel contentPane;
	private JTextField fieldManufacturer;
	private JTextField fieldOrder;
	//	private String[][] orderContents;
	List<String> items = new ArrayList<String>();
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private String manu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SendOrder frame = new SendOrder();
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
	public SendOrder() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblManufacturer = new JLabel("Manufacturer:");
		lblManufacturer.setBounds(23, 28, 132, 25);
		contentPane.add(lblManufacturer);

		fieldManufacturer = new JTextField();
		fieldManufacturer.setBounds(129, 28, 140, 25);
		contentPane.add(fieldManufacturer);
		fieldManufacturer.setColumns(10);

		JLabel lblEnterOrderIn = new JLabel("Enter order in format MODELNO QTY, ...");
		lblEnterOrderIn.setBounds(23, 65, 467, 19);
		contentPane.add(lblEnterOrderIn);

		fieldOrder = new JTextField();
		fieldOrder.setBounds(23, 90, 467, 25);
		contentPane.add(fieldOrder);
		fieldOrder.setColumns(10);

		JButton btnOrder = new JButton("Order");
		btnOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateArray(fieldOrder.getText());
				manu = fieldManufacturer.getText();
				makeOrder();

			}
		});
		btnOrder.setBounds(23, 134, 117, 25);
		contentPane.add(btnOrder);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ManagerHome().setVisible(true);
			}
		});
		btnBack.setBounds(373, 271, 117, 25);
		contentPane.add(btnBack);

		setTitle("Send order to manufacturer");

	}

	protected void makeOrder() {
		// TODO Auto-generated method stub
		int pid = generatePID();
		int orderID = generateOID();

		String query = "insert into eDepot_ShipmentProcess values('"+String.format("%06d",pid)+"', '"+String.format("%06d",orderID)+"', null, 'Ordered', '"+manu+"')";

		try {
			_statement.executeUpdate(query);
			
			for (String s: items) {
				String[] split = s.split("\\s+");
				String modelno = split[0]; 
				int qty = Integer.parseInt(split[1]);

				query = "insert into eDepot_ShipmentContents values('"+String.format("%06d",pid)+"', '"+modelno+"', "+qty+")";
				System.out.println(query);
				_statement.executeUpdate(query);
				
			}
			
			JOptionPane.showMessageDialog(this, 
					"Order completed. Your order ID is: "+String.format("%06d",orderID), "Order complete", JOptionPane.INFORMATION_MESSAGE);


		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int generateOID() {
		// TODO Auto-generated method stub
		int maxID = 1;

		String query = "SELECT MAX(orderno) FROM eDepot_ShipmentProcess";

		try {
			_rs = _statement.executeQuery (query);
			while (_rs.next()) {
				if (_rs.getString(1) != null) {
					maxID = Integer.parseInt(_rs.getString(1).trim()) + 1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxID;
	}

	private int generatePID() {
		// TODO Auto-generated method stub
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		int maxID = 1;

		String query = "SELECT MAX(pid) FROM eDepot_ShipmentProcess";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				if (_rs.getString(1)!= null) {
					maxID = Integer.parseInt(_rs.getString(1).trim()) + 1;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxID;
	}

	protected void populateArray(String order) {
		// TODO Auto-generated method stub
		items = Arrays.asList(order.split("\\s*,\\s*"));
		
	}

}
