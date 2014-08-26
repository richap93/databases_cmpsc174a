package external_world;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import database_Initialization.DB_Ini_Main;

/**
 * @author Richa Patel
 *Date: 03/12/2014
 */
public class ExternalWorld extends JFrame implements ActionListener {

	private JPanel contentPane;
	final JFrame frameHome = this;
	final JFrame frameCheckQty = new CheckItem();
	final JFrame frameOrder = new Order();
	final JFrame frameShipment = new ReceiveShipment();
	final JFrame frameNotice = new ReceiveSNotice();

	/**
	 * Launch the application.
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ExternalWorld frame = new ExternalWorld();
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
	public ExternalWorld() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 489, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setTitle("eDepot");
		
		JButton btnNewButton = new JButton("Fill Order");
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnNewButton.setForeground(SystemColor.textText);
		btnNewButton.setBackground(UIManager.getColor("Button.background"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameHome.setVisible(false);
				frameOrder.setVisible(true);
			}
		});
		btnNewButton.setBounds(28, 230, 200, 41);
		contentPane.add(btnNewButton);
		
		JButton btnCheckItem = new JButton("Check Item ");
		btnCheckItem.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnCheckItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameHome.setVisible(false);
				frameCheckQty.setVisible(true);
			}
		});
		btnCheckItem.setForeground(Color.BLACK);
		btnCheckItem.setBackground(UIManager.getColor("Button.background"));
		btnCheckItem.setBounds(28, 179, 200, 41);
		contentPane.add(btnCheckItem);
		
		JButton btnReceiveShipment = new JButton("Receive Shipment Notice");
		btnReceiveShipment.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnReceiveShipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameHome.setVisible(false);
				frameNotice.setVisible(true);
			}
		});
		btnReceiveShipment.setForeground(Color.BLACK);
		btnReceiveShipment.setBackground(UIManager.getColor("Button.background"));
		btnReceiveShipment.setBounds(28, 75, 200, 41);
		contentPane.add(btnReceiveShipment);
		
		JButton btnReceiveShipment_1 = new JButton("Receive Shipment");
		btnReceiveShipment_1.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnReceiveShipment_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frameHome.setVisible(false);
				frameShipment.setVisible(true);
			}
		});
		btnReceiveShipment_1.setForeground(Color.BLACK);
		btnReceiveShipment_1.setBackground(UIManager.getColor("Button.background"));
		btnReceiveShipment_1.setBounds(28, 127, 200, 41);
		contentPane.add(btnReceiveShipment_1);
		
		JLabel lblNewLabel = new JLabel("Welcome to eDepot! ");
		lblNewLabel.setForeground(Color.BLACK);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		lblNewLabel.setBounds(28, 23, 267, 41);
		contentPane.add(lblNewLabel);
		
		JButton btnSetLocationOf = new JButton("Set location of products");
		btnSetLocationOf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new SetLocation().setVisible(true);
			}
		});
		btnSetLocationOf.setForeground(Color.BLACK);
		btnSetLocationOf.setFont(new Font("Dialog", Font.PLAIN, 11));
		btnSetLocationOf.setBackground(UIManager.getColor("Button.background"));
		btnSetLocationOf.setBounds(250, 75, 200, 41);
		contentPane.add(btnSetLocationOf);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
