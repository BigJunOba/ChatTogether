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
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel panel1;             // 下方面板

	private JPanel panel2;            // 文字显示面板
	
	private static JTextArea area;           // 信息接收文本域

	private JButton button;           // 发送按钮
	
	private ChatRoomServer CRServer;

	//构造函数
	public ServerFrame() {
		
		CRServer = new ChatRoomServer();
		
		area = new JTextArea(20, 20);
		button = new JButton("关闭服务器");
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
		this.setTitle("服务器端窗口");
		
		button.setFont(new Font("宋体",Font.BOLD,20));
		panel1.add(button);
		
		//下方面板，布局位置为南
		this.add(panel1, BorderLayout.SOUTH);
		
		setConsoleMessateToArea();
		System.out.println("服务器已启动！");
	}

	// 显示窗口方法
	public void showMe() {
		this.pack();                                                 // 调整此窗口的大小，以适合其子组件的首选大小和布局
		this.setVisible(true);                                       // 窗口可显示
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);   // 点击右上角的叉号，不做任何操作
		this.setResizable(false);                                    // 不可改变窗口大小
		try {
			CRServer.startService();
		} catch (IOException e) {
			e.printStackTrace();
		}                    
	}

	// 添加监听方法
	public void addEventHandler() {
		// 开启按钮监听
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// 开启显示窗口监听
		this.addWindowListener(new WindowAdapter() {
			//窗口关闭时的处理方法
			public void windowClosing(WindowEvent atg0) {
				// 弹出提示框的内容
				int op = JOptionPane.showConfirmDialog(ServerFrame.this,"请选择是或否：", "关闭服务器？", JOptionPane.YES_NO_OPTION);
				// 如果选择确定
				if (op == JOptionPane.YES_OPTION) {
					//TODO
					System.exit(0); // 关闭程序
				}
			}
		});
	}
	
	// 设置JPane背景图片类
	public class backgroundJPanel1 extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		ImageIcon icon;
		Image image;
		
		public backgroundJPanel1() {
			icon = new ImageIcon(getClass().getResource("/background/bg01.jpg"));
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
			img = new ImageIcon(getClass().getResource("/background/bg03.jpg")).getImage();
			setPreferredSize(new Dimension(540, 908));
			this.setOpaque(false);
			this.setLayout(new BorderLayout());
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(img, 0, 0,+this.getWidth(), this.getHeight(), null); 
		}
	}
	
	public void setConsoleMessateToArea() {
		
		OutputStream textAreaStream = new OutputStream() {
			
			@Override
			public void write(int b) throws IOException {
				area.append(String.valueOf((char)b));
			}
			
			public void write(byte b[], int off, int len) throws IOException {
				area.append(new String(b, off, len));
			}
			
			public void write(byte b[]) throws IOException {
				area.append(new String(b));
			}
		};
		
		PrintStream myOut = new PrintStream(textAreaStream);
		System.setOut(myOut);
		System.setErr(myOut);
		
	}

	// 程序入口main方法
	public static void main(String[] args) throws InterruptedException {
		new ServerFrame().showMe();
	}
}
