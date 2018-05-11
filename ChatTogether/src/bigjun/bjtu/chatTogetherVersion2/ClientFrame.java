package bigjun.bjtu.chatTogetherVersion2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JTextField field;         // 信息发送文本框

	private JLabel label;             // 显示用户名

	private JPanel panel1;             // 下方面板

	private JPanel panel2;            // 文字显示面板
	
	private JTextArea area;           // 信息接收文本域

	private JButton button;           // 发送按钮

	private String userName;          // 用户名称

	private ChatRoomClient client;    // 客户端连接对象

	//构造函数
	public ClientFrame() {
		//使用do-while循环可以保证先运行一次
		do {
			try {
				String host = JOptionPane.showInputDialog(this, "请输入服务器IP地址");
				if (host == null) {
					//如果输入为空，则关闭程序
					System.exit(0);
				}
				//连接服务器的8023端口
				client = new ChatRoomClient(host, 8023);
			} catch (IOException e) {
				e.printStackTrace();
				//出现异常后，输入异常提示信息
				JOptionPane.showMessageDialog(this, "网络无法连接，请重新输入IP地址");
			}
		} while (client == null);// 如果客户端没有关闭，则一直连接
		
		String str = JOptionPane.showInputDialog(this, "请输入用户名:");
		userName = str.trim();
		
		field = new JTextField(50);
		label = new JLabel(userName + "说：");
		area = new JTextArea(20, 20);
		button = new JButton("发送");
		panel1 = new backgroundJPanel1();
		panel2 = new backgroundJPanel2();

		inti();
		addEventHandler();
	}

	//初始化方法
	public void inti() {
		
		area.setLineWrap(true);                       //设置自动换行
		area.setWrapStyleWord(true);			      //设置断行不断子功能
		area.setOpaque(false);						  //设置显示文本框透明
		area.setEditable(false);                      //设置显示文本框不能修改文字
		area.setFont(new Font("宋体",Font.BOLD,20));   //设置字体
		area.setForeground(Color.RED);                //设置文字颜色
		JScrollPane jsp = new JScrollPane(area);
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		panel2.add(jsp);
		
		this.getContentPane().add(panel2);
		this.setTitle("大家一起来聊天吧！");
		
		button.setFont(new Font("宋体",Font.BOLD,20));
		label.setFont(new Font("宋体",Font.BOLD,20));
		label.setForeground(Color.BLUE);
		field.setFont(new Font("宋体",Font.BOLD,20));
		panel1.add(label);
		panel1.add(field);
		panel1.add(button);
		
		//下方面板，布局位置为南
		this.add(panel1, BorderLayout.SOUTH);
	}

	// 显示窗口方法
	public void showMe() {
		this.pack();                                                 // 调整此窗口的大小，以适合其子组件的首选大小和布局
		this.setVisible(true);                                       // 窗口可显示
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // 点击右上角的叉号，不做任何操作
		this.setResizable(false);                                    // 不可改变窗口大小
		new ReadMessageThread().start();                             // 开启读取消息线程                     
	}

	// 添加监听方法
	public void addEventHandler() {
		// 开启按钮监听
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 判断字符输入框是否为空，为空的时候不能发送
				if (field.getText().trim().equals("")) {
				} else {
					// 点击发送按钮后，向服务器发送文本内容
					client.sendMessage(userName + ":" + field.getText());
					// 点击发送按钮后，文本输入框置空
					field.setText("");
				}
			}
		});

		// 开启显示窗口监听
		this.addWindowListener(new WindowAdapter() {
			//窗口关闭时的处理方法
			public void windowClosing(WindowEvent atg0) {
				// 弹出提示框的内容
				int op = JOptionPane.showConfirmDialog(ClientFrame.this,"请选择是或否：", "退出聊天室？", JOptionPane.YES_NO_OPTION);
				// 如果选择确定
				if (op == JOptionPane.YES_OPTION) {
					// 客户端发送退出消息
					client.sendMessage("%EXIT%:" + userName);
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					client.close(); // 关闭客户端连接
					System.exit(0); // 关闭程序
				}
			}
		});
	}

	// 读取消息线程
	private class ReadMessageThread extends Thread {

		// 线程run方法
		public void run() {
			// 无限循环，不停地监听有没有新消息
			while (true) {
				// 客户端收到服务器发来的文本内容
				String str = client.reciveMessage();
				// 将收到的文本内容添加到文本框中
				area.append(str + "\n");
			}
		}
	}
	
	// 设置JPane背景图片类
	public class backgroundJPanel1 extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		ImageIcon icon;
		Image image;
		
		public backgroundJPanel1() {
			icon = new ImageIcon("background/bg01.jpg");
			image = icon.getImage();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0,+this.getWidth(), this.getHeight(), null); 
		}
		
	}
	
	// 设置JTextArea背景图片类
	public class backgroundJPanel2 extends JPanel {
		private static final long serialVersionUID = 1L;
		
		Image img;
		
		public backgroundJPanel2() {
			img = new ImageIcon("background/Me.jpg").getImage();
			setPreferredSize(new Dimension(713, 583));
			this.setOpaque(false);
			this.setLayout(new BorderLayout());
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0,+this.getWidth(), this.getHeight(), null); 
		}
	}
	

	// 程序入口main方法
	public static void main(String[] args) {
		new ClientFrame().showMe();
	}
}
