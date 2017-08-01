import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.io.*;

public class ChatClient extends Frame {
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;
	
	TextField tfTxt = new TextField();
	
	TextArea taContent = new TextArea();

	Thread tRecv = new Thread(new RecvThraed());
	
	public static void main(String[] args) throws Exception{
		new ChatClient().launchFrame();
	}

	public void launchFrame(){
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt,BorderLayout.SOUTH);
		add(taContent,BorderLayout.NORTH);
		pack();
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing( WindowEvent e ) {
				disconnect();
				System.exit(0);
			}
			
		});
		tfTxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
		tRecv.start();
	}
	
	public void connect() {
		try {
			s = new Socket("localhost",8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
System.out.println("I have connected!");
			bConnected  = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect () {
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		/*
		try {
			bConnected = false;
			tRecv.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
				dis.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		*/
	
	private class TFListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");
			try {
//System.out.println(s);
				dos.writeUTF(str);
				dos.flush();
//				dos.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class RecvThraed implements Runnable{

		public void run() {
			while(bConnected){
				try {
					String str = dis.readUTF();
					//System.out.println(str);
					taContent.setText(taContent.getText() + str +"\n");
				} catch (SocketException e) {
					System.out.println("ÍË³öÁË.....");
				} catch (EOFException e) {
					System.out.println("tui chu le ,byebye!");
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
	}
	
}
