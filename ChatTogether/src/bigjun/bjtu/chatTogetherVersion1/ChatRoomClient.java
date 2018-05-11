package bigjun.bjtu.chatTogetherVersion1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatRoomClient {
	
	private Socket s;            // 客户端套接字

	private BufferedReader br;   // 读取字节流

	private PrintWriter pw;      // 写入字节流

	// 构造方法
	public ChatRoomClient(String host, int port) throws UnknownHostException, IOException {
		// 新建客户端，并连接服务器
		s = new Socket(host, port);
		// 读取客户端套接字输入流
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		// 读取客户端套接字输出流
		pw = new PrintWriter(s.getOutputStream());
	}

	// 客户端发送消息方法
	public void sendMessage(String str) {
		pw.println(str);
		pw.flush();
	}

	// 客户端接收消息方法
	public String reciveMessage() {
		try {
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 客户端关闭套接字方法
	public void close() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
