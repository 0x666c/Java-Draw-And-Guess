package io.x666c.pictionary.web.standalone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Server {
	
	public static void main(String[] args) throws Exception {
		new Server();
	}
	
	
	
	private ServerSocket socket;
	private List<Socket> connections = new ArrayList<Socket>(); // Use s.getRemoteSocketAddress() as UUID
	
	private ServerLogic logic;
	
	private Server() throws Exception {
		initFrame();
		
		logic = new ServerLogic();
		logic.start();
		System.out.println("Logic started");
		
		socket = new ServerSocket(8080);
		System.out.println("Listening on port 8080\n");
		System.out.println("Clients should send bytes 10\nand 16 to pass validation");
		System.out.println();
		
		while(true) {
			Socket s = socket.accept();
			InputStream in = s.getInputStream();
			if(in.read() == 10) { // Validation
				if(in.read() == 16) {
					connections.add(s);
					System.out.println("Accepted valid connection");
					continue;
				}
			}
			System.out.println("Rejected invalid connection");
		}
	}
	
	
	
	
	private static void initFrame() {
		JFrame f = new JFrame();
		f.setBackground(Color.BLACK);
		f.setLayout(new BorderLayout());
		
		JTextArea a = new JTextArea();
		a.setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 3), new EmptyBorder(1,1,1,1)));
		a.setPreferredSize(new Dimension(250, 400));
		a.setEditable(false);
		a.setLineWrap(true);
		a.setFont(new Font("Consolas", Font.PLAIN, 13));
		f.add(a, BorderLayout.CENTER);
		
		f.pack();
		
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Pictionary server");
		f.setLocationRelativeTo(null);
		
		f.setVisible(true);
		
		PrintStream redirect = new PrintStream(new JTextAreaOutputStream(a));
		System.setOut(redirect);
		System.setErr(redirect);
	}
	
}