package manager;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class ChangeShippingPolicy extends JFrame {

	private JPanel contentPane;
	private JLabel lblCurrentShippingPolicy, lblShippingHandling, lblSnH, lblWaivedIfOrder;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JLabel lblNewShippingPolicy;
	private JLabel lblNewSnH;
	private JLabel lblNewWaived;
	private JTextField fieldSnH;
	private JTextField fieldWaived;
	private JButton btnUpdate;
	private JButton btnBack;
	private JTextField fieldDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChangeShippingPolicy frame = new ChangeShippingPolicy();
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
	public ChangeShippingPolicy() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblCurrentShippingPolicy = new JLabel("Current Shipping Policy");
		lblCurrentShippingPolicy.setFont(new Font("Dialog", Font.BOLD, 16));
		lblCurrentShippingPolicy.setBounds(23, 28, 222, 21);
		contentPane.add(lblCurrentShippingPolicy);

		lblShippingHandling = new JLabel("Shipping & handling fee:");
		lblShippingHandling.setBounds(23, 56, 222, 21);
		contentPane.add(lblShippingHandling);

		String curSnH = String.valueOf(getSnHFee());
		Double disc = Double.parseDouble(curSnH)*100;

		lblSnH = new JLabel((Math.round(disc * 100.0) / 100.0)+"%");
		lblSnH.setBounds(211, 59, 70, 15);
		contentPane.add(lblSnH);

		lblWaivedIfOrder = new JLabel("Waived if order exceeds:");
		lblWaivedIfOrder.setBounds(23, 80, 201, 18);
		contentPane.add(lblWaivedIfOrder);

		String curWaived = String.valueOf(getCurWaived());
		Double waived = Double.parseDouble(curWaived);

		JLabel lblCurWaived = new JLabel(""+Math.round(waived * 100.0) / 100.0);
		lblCurWaived.setBounds(211, 83, 70, 15);
		contentPane.add(lblCurWaived);

		lblNewShippingPolicy = new JLabel("New Shipping Policy");
		lblNewShippingPolicy.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewShippingPolicy.setBounds(23, 133, 222, 21);
		contentPane.add(lblNewShippingPolicy);

		lblNewSnH = new JLabel("Shipping & handling fee:");
		lblNewSnH.setBounds(23, 160, 222, 21);
		contentPane.add(lblNewSnH);

		lblNewWaived = new JLabel("Waived if order exceeds:");
		lblNewWaived.setBounds(23, 183, 201, 18);
		contentPane.add(lblNewWaived);

		fieldSnH = new JTextField();
		fieldSnH.setBounds(210, 161, 114, 19);
		contentPane.add(fieldSnH);
		fieldSnH.setColumns(10);

		fieldWaived = new JTextField();
		fieldWaived.setBounds(210, 183, 114, 19);
		contentPane.add(fieldWaived);
		fieldWaived.setColumns(10);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sh = fieldSnH.getText();
				String waived = fieldWaived.getText();
				String date = fieldDate.getText();
				if (!(sh.equals("")) || (waived.equals("")) || (date.equals(""))) {
					updateDatabase(sh, waived, date);
				}
			}
		});
		btnUpdate.setBounds(337, 205, 101, 18);
		contentPane.add(btnUpdate);

		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new ManagerHome().setVisible(true);
			}
		});
		btnBack.setBounds(373, 271, 117, 25);
		contentPane.add(btnBack);

		JLabel lblDate = new JLabel("Date (dd/mm/yyyy):");
		lblDate.setBounds(23, 206, 157, 17);
		contentPane.add(lblDate);

		fieldDate = new JTextField();
		fieldDate.setBounds(211, 204, 114, 19);
		contentPane.add(fieldDate);
		fieldDate.setColumns(10);

		this.setTitle("Shipping policy");


	}

	protected void updateDatabase(String sh, String waived, String date) {
		// TODO Auto-generated method stub

		String[] dateSplit = date.split("/");
		int day = Integer.parseInt(dateSplit[0]);
		int month = Integer.parseInt(dateSplit[1]);
		int year = Integer.parseInt(dateSplit[2]);

		int dateInt = year*10000+month*100+day;

		String queryGetRecent = "select * from (select a.*,rank() over (order by startdate desc) r from (select * from emart_discounts d where d.startdate<20140330) a ) where r=1";

		double shDouble= Double.parseDouble(sh);

		try {
			ResultSet rs = _statement.executeQuery(queryGetRecent);
			while (rs.next()) {

				float newP = rs.getFloat(4);
				float green = rs.getFloat(5);
				float silver = rs.getFloat(6);
				float gold = rs.getFloat(7);
				
				String queryInsert = "INSERT into emart_discounts values("+dateInt+", "+shDouble/100+", "+Double.parseDouble(waived)+", "+newP+", "+green+", "+silver+", "+gold+")";

				_statement.executeUpdate(queryInsert);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private double getCurWaived() {
		// TODO Auto-generated method stub
		double curWaived = 0.00;

		String query = "SELECT waived FROM emart_discounts";

		try {
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				curWaived = _rs.getFloat(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return curWaived;
	}

	private double getSnHFee() {
		// TODO Auto-generated method stub
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		double curSnH = 0.00;

		String query = "SELECT sandh FROM emart_discounts";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			_rs = _statement.executeQuery (query);

			while (_rs.next()) {
				curSnH = _rs.getFloat(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}	

		return curSnH;

	}
}
