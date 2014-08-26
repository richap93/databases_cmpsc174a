package manager;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class MonthlySummary extends JFrame {

	private JPanel contentPane;
	private JComboBox comboBox;
	private JTable table;
	private JScrollPane scrollPane;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JTextField month;
	private JTextField year;
	private JLabel lblMonth, lblYear, lblCustomer, lblSales, lblCustName, labelSalesAmount;
	private JButton btnPrint;
	private String monthSelected, yearSelected, printBy, custName, custAmount;
	private JFrame frameSummary = this;
	private final String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
	private final String strUsername = "dschuerhoff";
	private final String strPassword = "X280715";
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonthlySummary frame = new MonthlySummary();
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
	public MonthlySummary() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("Monthly Summary");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"amount of sales by product", "amount of sales by category", "customer with most purchase"}));
		comboBox.setBounds(24, 33, 242, 20);
		contentPane.add(comboBox);
		
		JButton btnSelect = new JButton("Select");
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printBy = comboBox.getSelectedItem().toString();
				
				lblMonth = new JLabel("Month:");
				lblMonth.setBounds(24, 69, 70, 15);
				contentPane.add(lblMonth);
				
				lblYear = new JLabel("Year:");
				lblYear.setBounds(142, 69, 70, 15);
				contentPane.add(lblYear);
				
				month = new JTextField();
				month.setBounds(81, 67, 45, 20);
				contentPane.add(month);
				month.setColumns(10);
				
				year = new JTextField();
				year.setColumns(10);
				year.setBounds(185, 67, 60, 19);
				contentPane.add(year);
				
				btnPrint = new JButton("Print");
				btnPrint.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						monthSelected = month.getText();
						yearSelected = year.getText();
						if (printBy.equals("amount of sales by product") || printBy.equals("amount of sales by category")) {
						
							printSummary(printBy);
						
						} else {
							
							lblCustomer = new JLabel("Customer: ");
							lblCustomer.setBounds(24, 130, 117, 20);
							contentPane.add(lblCustomer);
							
							lblSales = new JLabel("Sales: ");
							lblSales.setBounds(24, 155, 117, 20);
							contentPane.add(lblSales);
							
							lblCustName = new JLabel("");
							lblCustName.setBounds(110, 133, 150, 15);
							contentPane.add(lblCustName);
							
							labelSalesAmount = new JLabel("");
							labelSalesAmount.setBounds(110, 158, 150, 15);
							contentPane.add(labelSalesAmount);
							
							frameSummary.repaint();
							
							findCustomerMostSales();
							
						}

					}
				});
				btnPrint.setBounds(278, 64, 117, 25);
				contentPane.add(btnPrint);
				
				frameSummary.repaint();
				}
				
		});
		
		btnSelect.setBounds(278, 31, 117, 25);
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

	}

	protected void findCustomerMostSales() {
		// TODO Auto-generated method stub
		
		int min = Integer.parseInt(yearSelected)*10000+Integer.parseInt(monthSelected)*100;
		int max;
		
		if (month.equals("12")){
			max = (Integer.parseInt(yearSelected)+1)*10000+1*100;
		} else {
			max = Integer.parseInt(yearSelected)*10000+((Integer.parseInt(monthSelected))+1)*100;
		}
		
		String query = 	"SELECT O.customer, SUM(OC.quantity*OC.price) total "
				+ "FROM eMart_OrderContents OC, eMart_Orders O "
				+ "WHERE O.id=OC.orderID AND O.odate>"+min+" AND O.odate<"+max+" "
				+ "GROUP BY O.customer";
		
		try {
			
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				lblCustName.setText(_rs.getString(1));
				labelSalesAmount.setText("$"+Float.toString(_rs.getFloat(2)));
				
				frameSummary.repaint();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void printSummary(String printBy) {
		// TODO Auto-generated method stub
		
		int min = Integer.parseInt(yearSelected)*10000+Integer.parseInt(monthSelected)*100;
		int max;
		
		if (month.equals("12")){
			max = (Integer.parseInt(yearSelected)+1)*10000+1*100;
		} else {
			max = Integer.parseInt(yearSelected)*10000+((Integer.parseInt(monthSelected))+1)*100;
		}
		
		String query;
		
		if (printBy.equals("amount of sales by product")) {
			query = "SELECT OC.stockno, SUM(OC.quantity*OC.price) total "
					+ "FROM eMart_OrderContents OC, eMart_Orders O "
					+ "WHERE O.id=OC.orderID AND O.odate>"+min+" AND O.odate<"+max+" "
					+ "GROUP BY OC.stockno";
			
		} else {
			query = "SELECT I.category, SUM(OC.quantity*OC.price) total "
					+ "FROM eMart_OrderContents OC, eMart_Orders O, eMart_Items I "
					+ "WHERE I.stockno=OC.stockno AND OC.orderID=O.id AND O.odate>"+min+" AND O.odate<"+max+" "
							+ "GROUP BY I.category";
		}

		try {
			
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			int size = 0;
			while (_rs.next()) {
				size++;
			}

			Object[][] tableContents = new Object[size][2];

			_rs = _statement.executeQuery(query);

			int pos = 0;
			
			while (_rs.next()) {
				
				tableContents[pos][0] = _rs.getString(1);
				tableContents[pos][1] = _rs.getFloat(2);
				pos++;
				
			}
		
			table = new JTable();
	
			table.setModel(new DefaultTableModel(tableContents,new String[] {"Product/Category", "Sales"}));
	
			table.setRowSelectionAllowed(false);
			table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
	
			table.setBounds(22, 117, 425, 80);
			scrollPane = new JScrollPane(table);
			scrollPane.setToolTipText("");
			scrollPane.setLocation(24, 100);
			scrollPane.setSize(367, 100);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			table.getColumnModel().getColumn(0).setPreferredWidth(300);
			contentPane.add(scrollPane);
			
			frameSummary.repaint();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
