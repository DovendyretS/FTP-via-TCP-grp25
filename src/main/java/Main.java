import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws IOException {

		String path = System.getProperty("user.home")+"/downloads";
		String dir1 = "ubuntu/dists/artful-backports/";
		String fileName1 = "Release.gpg";
		String command1 = "RETR " + dir1 + fileName1 + "\n";

		String dir2 = "ubuntu/project/trace/";
		String fileName2 = "sadashbia.canonical.com.dists-timestamps";
		String command2 = "RETR " + dir2 + fileName2 + "\n";

		String hostname = "ftp.ubuntu.com";

		int port = 21;

		Socket clientSocket = new Socket(hostname, port);

		OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
		InputStream in = clientSocket.getInputStream();
		System.out.print(readLine(in));
		sendLine(out,"USER anonymous\n");
		System.out.print(readLine(in));
		sendLine(out, "PASS\n");
		System.out.print(readLine(in));
		sendLine(out, "PASV\n");


		System.out.print(readToChar(in, '('));
		String portInfo = readToChar(in, ')');
		System.out.print(portInfo + readLine(in));

		String[] totalPortInfo = portInfo.substring(0,portInfo.length()-1).split(",");

		String dataHost = totalPortInfo[0] + "." + totalPortInfo[1] + "." + totalPortInfo[2] + "." + totalPortInfo[3];

		int dataPort = Integer.parseInt(totalPortInfo[4])*256 + Integer.parseInt(totalPortInfo[5]);

		Socket dataSocket = new Socket(dataHost, dataPort);
		InputStream dataIn = dataSocket.getInputStream();
		sendLine(out, command1);



		File file1 = new File(path+"/file1.txt");
		FileOutputStream os1 = new FileOutputStream(file1);

		System.out.print(readLine(in));
		int bytesWritten = 0;
		int charByte;
		while (true) {
			charByte = dataIn.read();
			if (charByte == -1)
				break;
			os1.write(charByte);
			if (bytesWritten < 1024) {
				System.out.print(String.format("%8s", Integer.toBinaryString(charByte)).replace(' ', '0'));

				bytesWritten++;
			}
		}
		System.out.println();
		os1.flush();
		os1.close();
		dataIn.close();
		dataSocket.close();



		System.out.print(readLine(in));
		System.out.println("File " + dir1 + fileName1 + " has been downloaded and saved to 'user/downloads/file1.txt'");


		File file2 = new File(path+"/file2.txt");
		FileOutputStream os2 = new FileOutputStream(file2);

		sendLine(out, "PASV\n");

		System.out.print(readToChar(in, '('));
		portInfo = readToChar(in, ')');
		System.out.print(portInfo);
		System.out.print(readLine(in));

		totalPortInfo = portInfo.substring(0,portInfo.length()-1).split(",");

		dataHost = totalPortInfo[0] + "." + totalPortInfo[1] + "." + totalPortInfo[2] + "." + totalPortInfo[3];

		dataPort = Integer.parseInt(totalPortInfo[4])*256 + Integer.parseInt(totalPortInfo[5]);

		dataSocket = new Socket(dataHost, dataPort);
		dataIn = dataSocket.getInputStream();

		sendLine(out, command2);

		System.out.print(readLine(in));
		bytesWritten = 0;
		while (true) {
			charByte = dataIn.read();
			if (charByte == -1) {
				charByte = 0;
				break;
			}
			os2.write(charByte);
			if (bytesWritten < 1024) {
				System.out.print(String.format("%8s", Integer.toBinaryString(charByte)).replace(' ', '0'));
				bytesWritten++;
			}
		}
		System.out.println();
		System.out.print(readLine(in));
		os2.flush();
		os2.close();
		dataIn.close();
		dataSocket.close();
	}

	public static void sendLine(OutputStreamWriter out, String msg) throws IOException {
		out.write(msg);
		out.flush();
	}

	public static String readLine(InputStream in) throws IOException {
		return readToChar(in,'\n');
	}

	public static String readToChar(InputStream in, char c) throws IOException {
		String result = "";
		int resultByte = -2;
		while (resultByte != (int)c) {
			resultByte = in.read();
			result += (char)resultByte;
		}
		return result;
	}
}
