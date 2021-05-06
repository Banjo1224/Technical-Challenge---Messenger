package messenger;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.sql.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
//import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Messenger {

	private static int num = 1330000000;
	
	public Messenger() {
		
		// DECLARE COMPONENTS
		
		
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		JButton button = new JButton("Submit");
		JTextField toUsers = new JTextField("blah@DOMAIN.COM");
		JTextField fromUser = new JTextField(10);
		JTextField subject = new JTextField("SECURE:");
		JTextArea body = new JTextArea(10, 30);
		JLabel field1 = new JLabel("To: ");
		JLabel field2 = new JLabel("From: ");
		JLabel field3 = new JLabel("Subject: ");
		JLabel field4 = new JLabel("Body: ");
		JLabel field5 = new JLabel(); // blank field for layout
		
		// SUBMIT LOGIC
		
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final String sender = fromUser.getText();
				final String reciever = toUsers.getText();
				final String sub = subject.getText();
				final String bod = body.getText();
				submit(sender, reciever, sub, bod);
			}
			
		});
		
		// SET UP PANEL LAYOUT
		
		panel.add(field1);
		panel.add(toUsers);
		panel.add(field2);
		panel.add(fromUser);
		panel.add(field3);
		panel.add(subject);
		panel.add(field4);
		panel.add(field5);
		panel.add(body);
		panel.add(button);
		
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(6, 2));
//		
//		// ADD PANEL TO FRAME
//		
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("Messenger");
		frame.pack();
		frame.setVisible(true);
//		
	}

	public void submit(String sender, String reciever, String sub, String body) {
				
		int counter = 0;
		String email = "";
		int index = 0;
		
		String emails[] = new String[20];
		
		int REVERSALS = 0;
		int REPLACES = 0;
		
		for (int i=0; i<reciever.length(); i++) {
			char c = reciever.charAt(i);

			
			if(c == ',') {
				counter++;
				email = reciever.substring(index, i);
//				System.out.println(email);
				emails[counter] = email.toUpperCase();
				index = i + 1;
//				System.out.println(index);
//				System.out.println(counter);
				
			} else if(c == ' ') {
				index++;
			} else if(i == reciever.length() - 1) {
				counter++;
				email = reciever.substring(index, i + 1);
//				System.out.println(email);
				emails[counter] = email.toUpperCase();
				index = i;
//				System.out.println(index);
//				System.out.println(counter);
			}
		}
		
		// TEST FOR DOMAIN.COM
		for (String e: emails) {
			if (e != null) {
//				System.out.println(e);
				int INDEX = e.indexOf("DOMAIN.COM");
				
				if (INDEX > 0) {
					Replace(body);
//					System.out.println(Replace(body));
					REPLACES++;
				}
				
			}
		}
		
		// TEST FOR "SECURE:"
//		System.out.println(sub);
//		System.out.println(sub.substring(0,6));
		if (sub.length()>6) {
			if (sub.substring(0, 7).equals("SECURE:")) {
				Reverse(body);
				REVERSALS++;
			}
		}
		
		// TEST FOR 10 CONSECUTIVE NUMBERS
		int nums = 0;
		for (int i=1; i<body.length(); i++ ) {
			char c = body.charAt(i);
			char last = body.charAt(i-1);
			if (c >= '0' && c <= '9') {
				if (last >= '0' && c <= '9') {
					nums++;	
				} else {
					nums = 0;
				}
				
			}
		}
		
		if (nums >= 10) {
//			System.out.println(Replace(body));
			Replace(body);
			REPLACES++;
//			System.out.println(Reverse(body));
			Reverse(body);
			REVERSALS++;
		}
		
		System.out.println(REPLACES);
		System.out.println(REVERSALS);
		
		SaveFile(sender, reciever, sub, body);
		
		
	}
	
	public void SaveFile(String sender, String reciever, String subject, String body) {
		
		final String fileName = String.valueOf(num) + ".txt";
		
		String url = "jdbc:mysql://localhost/messages";
		String username = "root";
		String password = "password!";
		// ^^^ I know this is a cardinal sin, please forgive me for the hackjob of a database implementation, I promise I'm smarter than this
		
		try (Connection connect = DriverManager.getConnection(url, username, password)) {
			System.out.println("Database Connected");
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		String Query = "INSERT INTO messages (id, filename, data) VALUES (default, " + 
				"\n To :" + reciever + 
				"\n From: " + sender + 
				"\n Subject: " + subject + 
				"\n Body: " +
				"\n" + body + ");";
		
		connect.
		
		
		File data = new File("./docs/" + fileName);
		PrintWriter printer;
		try {
			num++;
			printer = new PrintWriter(data);
			printer.println("To: " + reciever);
			printer.println("From: " + sender);
			printer.println("Subject: " + subject);
			printer.println("Body: ");
			printer.println(body);
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public String Reverse(String input) {
		String output = "";
		StringBuilder str = new StringBuilder();
		str.append(input);
		str.reverse();
		output = str.toString();
		return output;
	}
	
	public String Replace(String input) {
		String output = "";
		StringBuilder str = new StringBuilder();
		str.append(input);
		for (int i=0; i<str.length(); i++) {
			char c = str.charAt(i);
			
			if (c == '$') {
				str.setCharAt(i, 'e');
			}
			
			if (c == '^') {
				str.setCharAt(i, 'y');
			}
			
			if (c == '&') {
				str.setCharAt(i, 'u');
			}
		}
		output = str.toString();
		return output;
	}
	
	public static void main(String [] args) {
//		System.out.println("this is painful after javascript :cryface:");
		new Messenger();
	}
}
