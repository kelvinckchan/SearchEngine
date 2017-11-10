package webCrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import dataStorage.DataStore;

public class WebCrawler {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(WebCrawler.class);
	private static int ProcessedSite = 0;
	private int MaxProcessedURLPoolSize;// Y(MaxProcessedURLPoolSize)
	private int MaxUnprocessedQueueSize;// X(MaxUnprocessedQueueSize)
	private Queue<String> WaitingQueue = new LinkedList<String>();
	boolean debug, exportData;

	public static void main(String[] args) {
		WebCrawler w = new WebCrawler();
		// (y,x,url)
		// w.initialization(1, 1, "http://www.hkbu.edu.hk/tch/main/index.jsp");
		w.initialization(100, 10, "http://www.hkbu.edu.hk/tch/main/index.jsp", false, true);
		w.run();
	}

	public void initialization(int MaxProcessedURLPoolSize, int MaxUnprocessedQueueSize, String InitialURL,
			boolean debug, boolean exportData) {
		this.MaxProcessedURLPoolSize = MaxProcessedURLPoolSize;
		this.MaxUnprocessedQueueSize = MaxUnprocessedQueueSize;
		this.debug = debug;
		this.exportData = exportData;
		URLQueue.PushUnProcessedURLQueue(InitialURL);
		System.out.printf("[Initialization]- {X:%s, Y:%s, InitialURL:%s, debug:%s, export:%s}\n", MaxUnprocessedQueueSize,
				MaxProcessedURLPoolSize, InitialURL, debug, exportData);

	}

	List<Thread> ParserThreadList = new ArrayList<Thread>();
	Thread ParserThread = null;

	public void run() {
		try {
			ExecutorService executor = Executors.newFixedThreadPool(8);
			Collection<Future<ArrayList<String>>> tasks = new LinkedList<Future<ArrayList<String>>>();
			Future<ArrayList<String>> future;
			while (URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize) {

				while (URLQueue.getUnprocessedURLQueueSize() > 0
						&& URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize) {
					String pu = URLQueue.PollUnProcessedURLQueue();
					System.out.println("Start parse URL: " + pu);
					future = executor
							.submit(new HtmlParser(new URL(pu), MaxProcessedURLPoolSize, MaxUnprocessedQueueSize));
					tasks.add(future);
					ProcessedSite++;
				}

				if (URLQueue.getUnprocessedURLQueueSize() == MaxUnprocessedQueueSize)
					System.out.println("!!!!!!!!!!!!!Full!!!!!!!!!!!!!!!!!!!!!!!");
				for (Future<?> currTask : tasks) {
					try {
						ArrayList<String> List = (ArrayList<String>) currTask.get();
						System.out.println(tasks.size() + "Get: " + List);
						if (List != null && WaitingQueue.size() < MaxProcessedURLPoolSize) {
							addToWaitingQueue(List);
						}
					} catch (Throwable thrown) {
						logger.debug("ParserThread Thrown! " + thrown.getMessage());
					}
				}
				tasks.clear();
				add();

				System.out.println("[" + WaitingQueue.size() + "] WaitingQueue: " + WaitingQueue);
				System.out.println("[" + URLQueue.getProcessedURLQueueSize() + "] ProcessedURL: "
						+ URLQueue.getProcessedURLQueue());
				System.out.println("[" + URLQueue.getUnprocessedURLQueueSize() + "] UnprocessedURL: "
						+ URLQueue.getUnprocessedURLQueue());
				if (WaitingQueue.size() == 0 && URLQueue.getUnprocessedURLQueueSize() == 0) {
					break;
				}
			}

			System.out.println("Crawler Waiting: " + URLQueue.getProcessedURLQueueSize());

			logger.debug("Final visited url: " + ProcessedSite);
			logger.debug(
					"[" + URLQueue.getProcessedURLQueueSize() + "] ProcessedURL: " + URLQueue.getProcessedURLQueue());
			logger.debug("[" + URLQueue.getUnprocessedURLQueueSize() + "] UnprocessedURL: "
					+ URLQueue.getUnprocessedURLQueue());
			System.out.println("WebCrawler done: ");
			if (exportData)
				DataStore.output();
			if (debug)
				debug();

			executor.shutdown();
			// System.exit(0);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	public void debug() {
		DataStore.print();
		check();
	}

	public void check() {

		ArrayList<String> processed = URLQueue.getProcessedURLQueue().stream()
				.collect(Collectors.toCollection(ArrayList::new));
		ArrayList<String> init = DataStore.getContainedURLMap().get("http://www.hkbu.edu.hk/tch/main/index.jsp");

		logger.debug("[{}] processed: {}", processed.size(), processed);
		logger.debug("[{}] init: {}", init.size(), init);

		logger.debug("containsAll: " + processed.containsAll(init));

		List<String> proInin = processed.stream().filter(l -> !init.contains(l)).collect(Collectors.toList());
		List<String> proEdin = init.stream().filter(l -> !processed.contains(l)).collect(Collectors.toList());
		logger.debug("[{}] processed not in Init: {}", proInin.size(), proInin);
		logger.debug("[{}] Init not Processed: {}", proEdin.size(), proEdin);
		List<String> distinit = init.stream().distinct().collect(Collectors.toList());
		logger.debug("[{}] distinct init: {}", distinit.size(), distinit);

	}

	public void addToWaitingQueue(ArrayList<String> ContainedURLList) {
		ContainedURLList.forEach(l -> {
			if (!WaitingQueue.contains(l) && !URLQueue.ProcessedURLisContain(l) && !URLQueue.UnprocessedURLisContain(l)
					&& !URLQueue.getErrorURL().contains(l)) {
				WaitingQueue.add(l);

			}
		});
	}

	public void add() {
		while (WaitingQueue.size() > 0 && URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize
				&& URLQueue.getUnprocessedURLQueueSize() < MaxUnprocessedQueueSize) {
			String url = WaitingQueue.poll();
			if (!URLQueue.getErrorURL().contains(url)) {
				URLQueue.PushUnProcessedURLQueue(url);
				System.out.println(URLQueue.getUnprocessedURLQueueSize() + "+++add to unpro: " + url);
			}
		}
		System.out.println("added");
	}

}