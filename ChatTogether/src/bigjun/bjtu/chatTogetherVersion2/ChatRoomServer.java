package bigjun.bjtu.chatTogetherVersion2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class ChatRoomServer {   
	private ServerSocket ss;                // 服务器套接字

	private HashSet<Socket> allSockets;     // 客户端套接字集合

	public ChatRoomServer() {
		try {
			// 开启服务器，并绑定到8023端口
			ss = new ServerSocket(8023);
//			System.out.println("服务器已启动！");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 实例化客户端套接字集合
		allSockets = new HashSet<Socket>();
	}

	// 服务器业务方法
	public void startService() throws IOException {
		while (true) {
			// 如果有一个新的客户端连接，就返回一个客户端套接字
			Socket s = ss.accept();
			System.out.println("欢迎新用户来这里聊天！");
			// 将新的客户端套接字添加到客户端套接字集合中
			allSockets.add(s);
			// 为新加入的客户端单独创建一个事务处理线程并启动线程
			new ServerThread(s).start();
		}
	}

	// 服务器事务处理线程
	private class ServerThread extends Thread {
		
		Socket s;

		// 通过构造方法，将客户端连接套接字传递进来
		public ServerThread(Socket s) {
			this.s = s;
		}

		public void run() {
			
			BufferedReader br = null;

			try {
				// 读取客户端套接字的输入流，即客户端发送的信息，并转换为字节流
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
				// 无限循环
				while (true) {
					// 读取到一行之后，将结果传递给字符串
					String str = br.readLine();
					// 如果文本内容中包含%EXIT%，即客户端选择退出
					if (str.indexOf("%EXIT%") == 0) {
						// 从客户端套接字集合中移除退出的客户端连接
						allSockets.remove(s);
						// 服务器向所有客户端发送用户退出通知
						sendMessageTOAllClient(str.split(":")[1]+ " 用户已经退出聊天室");
						// 关闭退出的客户端套接字连接
						s.close();
						// 结束循环
						return;

					}
					// 向所有的客户端发送这个客户端发送的信息
					sendMessageTOAllClient(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 服务器向所有客户端发送文本内容方法
		public void sendMessageTOAllClient(String message) throws IOException {
			// 创建时间对象
			Date date = new Date();
			// 格式化系统读取的时间对象
			SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			// 在服务器程序控制台打印客户端发来的文本内容和添加时间
			System.out.println(message + "\t[" + df.format(date) + "]");
			// 循环客户端套接字集合中所有的客户端连接
			for (Socket s : allSockets) {
				// 创建服务器发送给客户端的输出流
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				// 向输出流中写入客户端发来的文本内容和添加时间
				pw.println(message + "\t[" + df.format(date) + "]");
				// 输出之后输出流刷新
				pw.flush();
			}

		}
	}
}
