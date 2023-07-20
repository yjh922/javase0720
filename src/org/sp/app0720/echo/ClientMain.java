package org.sp.app0720.echo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientMain extends JFrame{
	JPanel p_north;
	JComboBox box;
	JTextField t_port;
	JButton bt;
	
	JTextArea area;
	JScrollPane scroll;
	
	JTextField t_input;
	Socket socket;//대화용 소켓
	
	public ClientMain() {
		p_north = new JPanel();
		box = new JComboBox();
		t_port = new JTextField("7777", 6);
		bt = new JButton("접속");
		
		area = new JTextArea();
		scroll = new JScrollPane(area);
		
		t_input = new JTextField();
		
		//아이피 채워넣기
		box.addItem("192.168.1.37");
		
		//조립
		p_north.add(box);
		p_north.add(t_port);
		p_north.add(bt);
		add(p_north, BorderLayout.NORTH);
		
		add(scroll);
		add(t_input, BorderLayout.SOUTH);
		
		setBounds(100,200,300,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		bt.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				connect();
			}
		});
		
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key=e.getKeyCode();
				if(key==KeyEvent.VK_ENTER) {//엔터치면
					//서버에 메시지 전송
					send();
				}
			}
		});
	}
	
	//에코서버에 접속하기
	public void connect() {
		 //소켓을 생성한다는 것은 접속을 수행하는 것이다
		String ip=(String)box.getSelectedItem();
		int port=Integer.parseInt(t_port.getText());
		try {
			socket = new Socket(ip,port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//서버에 메시지 보내기
	public void send() {
		try {
			OutputStream os=socket.getOutputStream();
			OutputStreamWriter writer=new OutputStreamWriter(os);
			BufferedWriter buffw=new BufferedWriter(writer);
			BufferedReader buffr= new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String msg=t_input.getText();//사용자가 입력한 값
			buffw.write(msg+"\n");
			buffw.flush();
			
			//입력초기화
			t_input.setText("");
			
			//서버호부터 받은 메시지를 로그로 남기기
			
			msg=buffr.readLine();
			area.append(msg+"\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}
}
