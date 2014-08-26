package manager;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField user;
	private JTextField pw;
	private Statement _statement;
	private ResultSet _rs;
	private Connection _conn;
	private JFrame frameHome = new ManagerHome();
	private JFrame frameLogin = this;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 510, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		this.setTitle("Login");
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setFont(new Font("Dialog", Font.BOLD, 20));
		lblLogin.setBounds(138, 76, 89, 29);
		contentPane.add(lblLogin);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(138, 127, 104, 15);
		contentPane.add(lblUsername);
		
		user = new JTextField();
		user.setBounds(224, 125, 114, 19);
		contentPane.add(user);
		user.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(138, 154, 104, 15);
		contentPane.add(lblPassword);
		
		pw = new JTextField();
		pw.setColumns(10);
		pw.setBounds(224, 152, 114, 19);
		contentPane.add(pw);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = user.getText();
				String password = pw.getText();
				checkValidity(username, password);
			}
		});
		btnLogin.setBounds(249, 181, 89, 29);
		contentPane.add(btnLogin);
	}

	protected void checkValidity(String username, String password) {
		// TODO Auto-generated method stub
		String query = "select * from eMart_Customers c where c.id='"+username+"' AND c.manager=1";
		
		String strConn = "jdbc:oracle:thin:@uml.cs.ucsb.edu:1521:xe";
		String strUsername = "dschuerhoff";
		String strPassword = "X280715";

		try {
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			_conn = DriverManager.getConnection(strConn,strUsername,strPassword);
			_statement = _conn.createStatement();
			ResultSet rs = _statement.executeQuery(query);
			
			rs.next();
				try {
					if(rs.getString(2).trim().equals(password)){
						dispose();
						frameHome.setVisible(true);
					}else{
						JOptionPane.showMessageDialog(frameLogin, "Password incorrect!", "Error", JOptionPane.OK_OPTION);
					};
				} catch (SQLException e1){
					JOptionPane.showMessageDialog(frameLogin, "User does not exist!", "Error", JOptionPane.OK_OPTION);
				}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

}
