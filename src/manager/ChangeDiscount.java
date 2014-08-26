package manager;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class ChangeDiscount extends JFrame {

	private JPanel contentPane;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private String status, date;
	private JComboBox comboBox;
	private JLabel lblCurrentDiscount, lblNewDiscount, lblCurValue;
	private JTextField newDiscount;
	private JButton btnConfirm;
	private JFrame frameChangeDisc = this;
	private JTextField dateEntered;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeDiscount frame = new ChangeDiscount();
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
	public ChangeDiscount() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setTitle("Change discount rules");
		
		JLabel lblNewLabel = new JLabel("Change discount for:");
		lblNewLabel.setBounds(10, 47, 173, 15);
		contentPane.add(lblNewLabel);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"New", "Green", "Silver", "Gold"}));
		comboBox.setBounds(171, 44, 211, 20);
		contentPane.add(comboBox);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				status = comboBox.getSelectedItem().toString();
				
				JLabel lblDate = new JLabel("Date (dd/mm/yyyy):");
				lblDate.setBounds(10, 74, 141, 20);
				contentPane.add(lblDate);
				
				dateEntered = new JTextField();
				dateEntered.setBounds(156, 75, 114, 20);
				contentPane.add(dateEntered);
				dateEntered.setColumns(10);
				
				JButton btnChangeDiscount = new JButton("Change discount");
				btnChangeDiscount.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						date = dateEntered.getText();
						
						String curDisc = String.valueOf(getDiscount());

						lblCurrentDiscount = new JLabel("Current discount:");
						lblCurrentDiscount.setBounds(10, 130, 150, 20);
						contentPane.add(lblCurrentDiscount);
						
						lblNewDiscount = new JLabel("New discount (%):");
						lblNewDiscount.setBounds(10, 155, 150, 20);
						contentPane.add(lblNewDiscount);
						
						Double disc = Double.parseDouble(curDisc)*100;
						
						lblCurValue = new JLabel((Math.round(disc * 100.0) / 100.0)+"%");
						lblCurValue.setBounds(150, 134, 70, 15);
						contentPane.add(lblCurValue);
						
						newDiscount= new JTextField();
						newDiscount.setBounds(150, 156, 114, 19);
						contentPane.add(newDiscount);
						newDiscount.setColumns(10);
						
						btnConfirm = new JButton("Confirm");
						btnConfirm.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								double discountNew = Double.parseDouble(newDiscount.getText());
								updateDiscount(discountNew);
							}


						});
						btnConfirm.setBounds(10, 187, 117, 25);
						contentPane.add(btnConfirm);
						
						frameChangeDisc.repaint();
						
						
					}
				});
				btnChangeDiscount.setBounds(282, 76, 190, 18);
				contentPane.add(btnChangeDiscount);
				
				frameChangeDisc.repaint();

			}
		});
		btnSelect.setBounds(394, 43, 78, 22);
		contentPane.add(btnSelect);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ManagerHome().setVisible(true);
			}
		});
		btnBack.setBounds(373, 271, 117, 25);
		contentPane.add(btnBack);
		
		JScrollPane listScrollPane = new JScrollPane();

		String[] itemList =  {"New", "Green", "Silver", "Gold"};
		
	}
	
	
	private double getDiscount() {
		// TODO Auto-generated method stub
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";
		
		double curDisc = 0.00;
		
		String query = "SELECT "+status+" FROM emart_discounts";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);
			
			while (_rs.next()) {
				curDisc = _rs.getFloat(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		return curDisc;
	}
	
	private void updateDiscount(double discountNew) {
		// TODO Auto-generated method stub
		
		int day = Integer.parseInt(date.substring(0, 2));
		int month = Integer.parseInt(date.substring(3, 5));
		int year = Integer.parseInt(date.substring(6, 10));
		
		int dateInt = year*10000+month*100+day;
		
		String queryGetRecent = "select * from (select a.*,rank() over (order by startdate desc) r from (select * from emart_discounts d where d.startdate<20140330) a ) where r=1";

		try {
			ResultSet rs = _statement.executeQuery(queryGetRecent);
			while (rs.next()) {
				
				float sh = rs.getFloat(2);
				float waived = rs.getFloat(3);
				float newP = rs.getFloat(4);
				float green = rs.getFloat(5);
				float silver = rs.getFloat(6);
				float gold = rs.getFloat(7);
				
				String queryInsert = "";
				
				if (status.equals("New")) {
					queryInsert = "INSERT into emart_discounts values("+dateInt+", "+sh+", "+waived+", "+discountNew/100+", "+green+", "+silver+", "+gold+")";
				} else if (status.equals("Green")) {
					queryInsert = "INSERT into emart_discounts values("+dateInt+", "+sh+", "+waived+", "+newP+", "+discountNew/100+", "+silver+", "+gold+")";
				} else if (status.equals("Silver")) {
					queryInsert = "INSERT into emart_discounts values("+dateInt+", "+sh+", "+waived+", "+newP+", "+green+", "+discountNew/100+", "+gold+")";
				} else {
					queryInsert = "INSERT into emart_discounts values("+dateInt+", "+sh+", "+waived+", "+newP+", "+green+", "+silver+", "+discountNew/100+")";
				}
				
				_statement.executeUpdate(queryInsert);
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
