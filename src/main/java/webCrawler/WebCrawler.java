package webCrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebCrawler {

	public WebCrawler() {

	}

	public static void main(String[] args) {
		WebCrawler w = new WebCrawler();
		// w.fetch("http://www.hkbu.edu.hk/eng/main/index.jsp", "./fetched.html");
		// w.fetch("http://www.hkbu.edu.hk/tch/main/index.jsp", "./fetchedCH.html");

		w.run();
	}

	private final String initialURL = "http://www.hkbu.edu.hk/tch/main/index.jsp";
	private static int ProcessedSite = 0;

	public void run() {
		try {
			Thread ParserThread = null;
			// if (URLQueue.getProcessedURLSize() < 1) {
			while (URLQueue.getProcessedURLSize() < 10) {
				URLQueue.PushUnProcessedURL(initialURL);
				System.out.println("=> ProcessedURLSize(): " + URLQueue.getProcessedURLSize());
				if (URLQueue.getUnprocessedURLSize() > 0) {
					String pu = URLQueue.PollUnProcessedURL();
					System.out.println("parse Unprocessed: " + initialURL);
					ParserThread = new Thread(new HtmlParser(new URL(pu)));
					ParserThread.start();
				}
				// try {
				// ParserThread.join();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				ProcessedSite++;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	public void fetch(String _url, String fileName) {
		URL url;
		System.err.println("Try to fetch from : " + _url);
		// get URL content
		try {
			url = new URL(_url);
			URLConnection conn = url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String FetchedHtml;
			while ((FetchedHtml = in.readLine()) != null)
				System.out.println(FetchedHtml);
			System.err.println(_url + " [Done]");
			in.close();
			// open the stream and put it into BufferedReader
			// OutputToFile(fileName, new BufferedReader(new
			// InputStreamReader(conn.getInputStream())));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void OutputToFile(String fileName, BufferedReader br) throws IOException {
		String inputLine;
		// save to this filename
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
		// use FileWriter to write file
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		while ((inputLine = br.readLine()) != null) {
			bw.write(inputLine);
		}
		bw.close();
		br.close();
	}
}
