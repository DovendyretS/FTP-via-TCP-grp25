import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws IOException {

		String dir1 = "RETR ubuntu/dists/artful-backports/Release.gpg\n";
		//String dir2 = "mget ubuntu\\pool\\main\\a\\ally-profile-manager\\ally-profile-manager_0.1.10.orig.tar.xz\r\n";

		String hostname = "ftp.ubuntu.com";

		int port = 21;

		Socket clientSocket = new Socket(hostname, port);

		OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());

		InputStream in = clientSocket.getInputStream();

		int ans = -2;
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}
		out.write("USER anonymous\n");
		out.flush();
		ans = -2;
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}
		out.write("PASS\n");
		out.flush();
		ans = -2;
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}

		out.write("PASV\n");
		out.flush();
		ans = -2;
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}
		Socket clientSocketlisten = new Socket(hostname, port + 1);

		out.write(dir1.toCharArray(), 0, dir1.length());
		out.flush();


		String path = System.getProperty("user.home")+"/downloads";
		File file1 = new File(path+"/file1.txt");
		File file2 = new File(path+"/file2.txt");

		FileOutputStream os1 = new FileOutputStream(file1);
		//FileOutputStream os2 = new FileOutputStream(file2);
		int b = -2;
		for (int i = 0; i < 1024 && b != (int)'\n'; i++) {
			b = in.read();
			os1.write(b);
			System.out.print((char)b);
		}
		while (b != (int)'\n') {
			b = in.read();
			os1.write(b);
			System.out.println((char)b);
		}
		os1.flush();



		clientSocket.close();



	}
}
