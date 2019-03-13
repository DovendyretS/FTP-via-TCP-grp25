import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws IOException {
		//Setting op variables
		String path = System.getProperty("user.home")+"/downloads";
		String serverPathFile1 = "ubuntu/dists/artful-backports/Release.gpg";
		String serverPathFile2 = "ubuntu/project/trace/sadashbia.canonical.com.dists-timestamps";

		//Connection vars
		String hostname = "ftp.ubuntu.com";
		int port = 21;

		//Establish connection
		Socket clientSocket = new Socket(hostname, port);
		OutputStreamWriter out = new OutputStreamWriter(clientSocket.getOutputStream());
		InputStream in = clientSocket.getInputStream();

		//Logging in (as anonymous)
		System.out.print(readLine(in));
		sendLine(out,"USER anonymous\n");
		System.out.print(readLine(in));
		sendLine(out, "PASS\n");
		System.out.print(readLine(in));

		//Downloading files
		downloadFile(path + "/file1.txt", serverPathFile1, in, out);
		downloadFile(path + "/file2.txt", serverPathFile2, in, out);

		//Closing connections
		in.close();
		out.close();
		clientSocket.close();
	}

	private static void downloadFile(String localPath, String serverPath, InputStream in, OutputStreamWriter out) throws IOException {
		//we download in passive mode
		sendLine(out, "PASV\n");

		//calculate host location for download data stream
		System.out.print(readToChar(in, '('));
		String portInfo = readToChar(in, ')');
		System.out.print(portInfo + readLine(in));
		String[] totalPortInfo = portInfo.substring(0,portInfo.length()-1).split(",");
		String dataHost = totalPortInfo[0] + "." + totalPortInfo[1] + "." + totalPortInfo[2] + "." + totalPortInfo[3];
		int dataPort = Integer.parseInt(totalPortInfo[4])*256 + Integer.parseInt(totalPortInfo[5]);

		//Connecting to data-stream host
		Socket dataSocket = new Socket(dataHost, dataPort);
		InputStream dataIn = dataSocket.getInputStream();

		//Says to org host we want to download
		sendLine(out, "RETR " + serverPath + "\n");
		System.out.print(readLine(in));

		//Creating file
		File file1 = new File(localPath);
		FileOutputStream os1 = new FileOutputStream(file1);

		//Writing to file
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

		//Closes and saves file
		os1.flush();
		os1.close();
		dataIn.close();
		dataSocket.close();

		//Printing download status
		System.out.print(readLine(in));
		System.out.println("File " + serverPath + " has been downloaded and saved to 'user/downloads/file1.txt'");
	}

	private static void sendLine(OutputStreamWriter out, String msg) throws IOException {
		out.write(msg);
		out.flush();
	}

	private static String readLine(InputStream in) throws IOException {
		return readToChar(in,'\n');
	}

	private static String readToChar(InputStream in, char c) throws IOException {
		String result = "";
		int resultByte = -2;
		while (resultByte != (int)c) {
			resultByte = in.read();
			result += (char)resultByte;
		}
		return result;
	}
}
