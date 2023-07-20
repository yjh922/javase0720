package org.sp.app0720.unicasting;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUIServer extends JFrame {
	JPanel p_north;
	JTextField t_port;
	JButton bt;

	JTextArea area;
	JScrollPane scroll;
	ServerSocket server;// 대화용이 아닌 접속자 감지용 소켓

	Thread thread;// 메인 쓰레드가 대기 상태에 빠지지 않도록 하기 위한 별도의 쓰레드

	public GUIServer() {
		p_north = new JPanel();
		t_port = new JTextField("7777", 10);
		bt = new JButton("서버가동");

		area = new JTextArea();
		scroll = new JScrollPane(area);

		// 조립
		p_north.add(t_port);
		p_north.add(bt);

		add(p_north, BorderLayout.NORTH);
		add(scroll);

		setBounds(500, 200, 300, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		bt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				thread = new Thread() {
					public void run() {

						runServer();
					}
				};
				thread.start();

			}
		});
	}

	public void runServer() {

		int port = Integer.parseInt(t_port.getText());

		try {
			server = new ServerSocket(port);// 서버생성
			area.append("서버생성\n");

			area.append("서버가동 및 접속자 감지 중 ...\n");
			// 얻어진 소켓은 대화용 소켓이므로 접속한 상대방과의 통신을 위한 스트림을 얻을 수 있다.
			while (true) {
				//클라이언트가 접속을 하면 소켓이 반환되고 이 소켓은 대화용 소켓이므로
				//실제 대화를 처리하는 쓰레드에게 전달해야 한다.
				Socket socket = server.accept();
				area.append("접속자 감지\n");
				ServerMessageThread smt= new ServerMessageThread(socket, area);
				smt.start();
		}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new GUIServer();
	}
}
