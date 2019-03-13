import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws IOException {

		String dir1 = "RETR ubuntu/dists/artful-backports/Release.gpg\n";
		String dir2 = "RETR /ubuntu/project/trace/sadashbia.canonical.com.dists-timestamps\n";
		//String dir2 = "RETR ubuntu/pool/main/a/ally-profile-manager/ally-profile-manager_0.1.10.orig.tar.xz\n";

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
		while (ans != (int)'(') {
			ans = in.read();
			System.out.print((char)ans);
		}
		String portInfo = "";
		while (ans != (int)')') {
			ans = in.read();
			portInfo += (char)ans;
			System.out.print((char)ans);
		}
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}

		String[] totalPortInfo = portInfo.substring(0,portInfo.length()-1).split(",");

		String dataHost = totalPortInfo[0] + "." + totalPortInfo[1] + "." + totalPortInfo[2] + "." + totalPortInfo[3];

		int dataPort = Integer.parseInt(totalPortInfo[4])*256 + Integer.parseInt(totalPortInfo[5]);

		Socket dataSocket = new Socket(dataHost, dataPort);
		InputStream dataIn = dataSocket.getInputStream();


		out.write(dir1.toCharArray(), 0, dir1.length());
		out.flush();


		String path = System.getProperty("user.home")+"/downloads";
		File file1 = new File(path+"/file1.txt");

		FileOutputStream os1 = new FileOutputStream(file1);
		int b = -2;
		while (b != (int)'\n') {
			b = in.read();
			System.out.print((char)b);
		}
		int bytesWritten = 0;
		int charByte;
		while (true) {
			charByte = dataIn.read();
			if (charByte == -1) {
				charByte = 0;
				break;
			}
			os1.write(charByte);
			if (bytesWritten < 1024) {
				System.out.print(Integer.toBinaryString(charByte));
				bytesWritten++;
			}
		}
		System.out.println();
		os1.flush();
		os1.close();
		dataIn.close();
		dataSocket.close();



		b = -2;
		while (b != (int)'\n') {
			b = in.read();
			System.out.print((char)b);
		}
		System.out.println("File " + dir1 + " has been downloaded and saved to 'user/downloads/file1.txt'");


		File file2 = new File(path+"/file2.txt");
		FileOutputStream os2 = new FileOutputStream(file2);

		out.write("PASV\n");
		out.flush();

		ans = -2;
		while (ans != (int)'(') {
			ans = in.read();
			System.out.print((char)ans);
		}
		portInfo = "";
		while (ans != (int)')') {
			ans = in.read();
			portInfo += (char)ans;
			System.out.print((char)ans);
		}
		while (ans != (int)'\n') {
			ans = in.read();
			System.out.print((char)ans);
		}

		totalPortInfo = portInfo.substring(0,portInfo.length()-1).split(",");

		dataHost = totalPortInfo[0] + "." + totalPortInfo[1] + "." + totalPortInfo[2] + "." + totalPortInfo[3];

		dataPort = Integer.parseInt(totalPortInfo[4])*256 + Integer.parseInt(totalPortInfo[5]);

		dataSocket = new Socket(dataHost, dataPort);
		dataIn = dataSocket.getInputStream();


		out.write(dir2.toCharArray(), 0, dir2.length());
		out.flush();



		b = -2;
		while (b != (int)'\n') {
			b = in.read();
			System.out.print((char)b);
		}
		bytesWritten = 0;
		while (true) {
			charByte = dataIn.read();
			if (charByte == -1) {
				charByte = 0;
				break;
			}
			os2.write(charByte);
			if (bytesWritten < 1024) {
				System.out.print(Integer.toBinaryString(charByte));
				bytesWritten++;
			}
		}
		System.out.println();
		b = -2;
		while (b != (int)'\n') {
			b = in.read();
			System.out.print((char)b);
		}
		os2.flush();
		os2.close();
		dataIn.close();
		dataSocket.close();

	}
}
