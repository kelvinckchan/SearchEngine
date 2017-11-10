package webCrawler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Queue;

public class URLQueue {

	private static final Queue<String> ProcessedURLQueue = new LinkedList<String>();
	private static final Queue<String> UnprocessedURLQueue = new LinkedList<String>();
	private static ArrayList<String> ErrorURL = new ArrayList<String>();

	public synchronized static void addErrorURL(String url) {
		ErrorURL.add(url);
	}
	public synchronized static ArrayList<String> getErrorURL() {
		return ErrorURL;
	}
	public synchronized static Queue<String> getProcessedURLQueue() {
		return ProcessedURLQueue;
	}

	public synchronized static Queue<String> getUnprocessedURLQueue() {
		return UnprocessedURLQueue;
	}

	public synchronized static boolean ProcessedURLisContain(String url) {
		return ProcessedURLQueue.contains(url);
	}

	public synchronized static boolean UnprocessedURLisContain(String url) {
		return UnprocessedURLQueue.contains(url);
	}

	public synchronized static int getProcessedURLQueueSize() {
		return ProcessedURLQueue.size();
	}

	public synchronized static int getUnprocessedURLQueueSize() {
		return UnprocessedURLQueue.size();
	}

	public synchronized static void PushProcessedURLQueue(String url) {
		ProcessedURLQueue.add(url);
	}

	public synchronized static void PushUnProcessedURLQueue(String url) {
		UnprocessedURLQueue.add(url);
	}

	public synchronized static boolean RemoveErrorURLFromProcessedURLQueue(String url) {
		return ProcessedURLQueue.remove(url);
	}

	public synchronized static String PollProcessedURLQueue() {
		return ProcessedURLQueue.poll();
	}

	public synchronized static String PollUnProcessedURLQueue() {
		return UnprocessedURLQueue.poll();
	}

}
