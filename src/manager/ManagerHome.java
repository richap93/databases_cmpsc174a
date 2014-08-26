package manager;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class ManagerHome extends JFrame {

	private JPanel contentPane;

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerHome frame = new ManagerHome();
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
	public ManagerHome() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Print Monthly Summary");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 11));
		btnNewButton.setForeground(SystemColor.textText);
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MonthlySummary().setVisible(true);				
			}
		});
		btnNewButton.setBounds(28, 38, 200, 41);
		contentPane.add(btnNewButton);
		
		JButton btnAdjustCustomerStatus = new JButton("Adjust Customer Status");
		btnAdjustCustomerStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new AdjustCustStatus().setVisible(true);
			}
		});
		btnAdjustCustomerStatus.setForeground(Color.BLACK);
		btnAdjustCustomerStatus.setFont(new Font("Dialog", Font.BOLD, 11));
		btnAdjustCustomerStatus.setBackground(UIManager.getColor("Button.background"));
		btnAdjustCustomerStatus.setBounds(28, 91, 200, 41);
		contentPane.add(btnAdjustCustomerStatus);
		
		JButton btnSendOrder = new JButton("Send Order");
		btnSendOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new SendOrder().setVisible(true);
			}
		});
		btnSendOrder.setForeground(Color.BLACK);
		btnSendOrder.setFont(new Font("Dialog", Font.BOLD, 11));
		btnSendOrder.setBackground(UIManager.getColor("Button.background"));
		btnSendOrder.setBounds(28, 144, 200, 41);
		contentPane.add(btnSendOrder);
		
		JButton btnChangePrice = new JButton("Change Price");
		btnChangePrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ChangePrice().setVisible(true);
			}
		});
		btnChangePrice.setForeground(Color.BLACK);
		btnChangePrice.setFont(new Font("Dialog", Font.BOLD, 11));
		btnChangePrice.setBackground(UIManager.getColor("Button.background"));
		btnChangePrice.setBounds(28, 196, 200, 41);
		contentPane.add(btnChangePrice);
		
		JButton btnDeleteUnnecessarySales = new JButton("Delete Unnecessary Sales");
		btnDeleteUnnecessarySales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete unnecessary orders?", "Delete orders",
				        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					deleteOrders();
				} else {
				    // go back to the frame
				}
			}
		});
		btnDeleteUnnecessarySales.setForeground(Color.BLACK);
		btnDeleteUnnecessarySales.setFont(new Font("Dialog", Font.BOLD, 11));
		btnDeleteUnnecessarySales.setBackground(UIManager.getColor("Button.background"));
		btnDeleteUnnecessarySales.setBounds(252, 38, 220, 41);
		contentPane.add(btnDeleteUnnecessarySales);
		
		JButton btnChangeDiscountRules = new JButton("Change Discount Rules");
		btnChangeDiscountRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ChangeDiscount().setVisible(true);
			}
		});
		btnChangeDiscountRules.setForeground(Color.BLACK);
		btnChangeDiscountRules.setFont(new Font("Dialog", Font.BOLD, 11));
		btnChangeDiscountRules.setBackground(UIManager.getColor("Button.background"));
		btnChangeDiscountRules.setBounds(252, 91, 220, 41);
		contentPane.add(btnChangeDiscountRules);
		
		JButton btnChangeShippingPolicy = new JButton("Change Shipping Policy");
		btnChangeShippingPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ChangeShippingPolicy().setVisible(true);
			}
		});
		btnChangeShippingPolicy.setForeground(Color.BLACK);
		btnChangeShippingPolicy.setFont(new Font("Dialog", Font.BOLD, 11));
		btnChangeShippingPolicy.setBackground(UIManager.getColor("Button.background"));
		btnChangeShippingPolicy.setBounds(252, 144, 220, 41);
		contentPane.add(btnChangeShippingPolicy);
		
		JButton btnLogOut = new JButton("Log out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Login().setVisible(true);
			}
		});
		btnLogOut.setBounds(373, 271, 117, 25);
		contentPane.add(btnLogOut);
		
		this.setTitle("Manager - Home");
	}

	protected void deleteOrders() {

		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";
		
		String customersQuery = "SELECT id FROM emart_customers";
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			Connection conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(customersQuery);
			
			while (rs.next()) {
				
				String custId = rs.getString("id");
				
				String query = "select  ec.id from emart_orders ec where ec.id in (select id from ("
						+ "select a.*,rank() over(order by odate desc,id desc) r "
						+ "from (select o.odate,o.id, o.customer from emart_orders o where o.customer='"+custId.trim()+"') a) where r>3)";
				
				Statement stmt2 = conn.createStatement();
				ResultSet ordersDelete = stmt2.executeQuery(query);
				
				while (ordersDelete.next()) {
					
					String orderID = ordersDelete.getString(1);
					
					String contentsQuery = "DELETE FROM eMart_OrderContents WHERE orderID = '"+orderID+"'";
					Statement stmt3 = conn.createStatement();
					stmt3.executeUpdate(contentsQuery);
					
					String orderDelete = "DELETE FROM eMart_Orders WHERE ID = '"+orderID+"'";
					stmt3.executeUpdate(orderDelete);
					
					stmt3.close();
					
				}
				
				stmt2.close();
			}
			
			stmt.close();
			rs.close();
			
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(this,
				"Orders deleted.",
				"",
				JOptionPane.INFORMATION_MESSAGE);		
	}


}
