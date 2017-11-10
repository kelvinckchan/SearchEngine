//package test;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import java.util.PriorityQueue;
//import java.util.Queue;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//import java.util.stream.Stream;
//
//import webCrawler.HtmlParser;
//import webCrawler.URLQueue;
//
//public class testThread {
//
//	public static void main(String[] args) {
//		testThread t = new testThread();
//		URLQueue.PushUnProcessedURLQueue("1");
//		t.create();
//		t.run();
//	}
//
//	private int MaxProcessedURLPoolSize = 130;// Y(MaxProcessedURLPoolSize)
//	private int MaxUnprocessedQueueSize = 1;// X(MaxUnprocessedQueueSize)
//
//	public void create() {
//		ArrayList<String> link = new ArrayList<String>();
//		IntStream.range(1, 111).forEach(i -> link.add(Integer.toString(i)));
//
//		int total = 30;
//		int start = 20;
//		int amount = 3;
//		int end = start + amount;
//
//		Web.webmap.put("1", new ArrayList<String>(Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10")));
//		Web.webmap.put("2", new ArrayList<String>(Arrays.asList("11", "12", "13", "14", "15", "16", "17", "18", "19")));
//
//		for (int u = 3; u < total; u++) {
//			ArrayList<Integer> in = new ArrayList<Integer>();
//			IntStream.range(start, end).forEach(i -> in.add(i));
//			System.out.println(in);
//			ArrayList<String> n = in.stream().map(i -> Integer.toString(i))
//					.collect(Collectors.toCollection(ArrayList<String>::new));
//			Web.webmap.put(Integer.toString(u), n);
//			start = end;
//			end += amount;
//		}
//		Web.webmap.put("30", null);
//
//		Web.getmap().forEach((k, v) -> {
//			System.out.println(k + "=> " + v);
//		});
//
//	}
//
//	private Queue<String> WaitingQueue = new LinkedList<String>();
//
//	public void run() {
//		ExecutorService executor = Executors.newFixedThreadPool(8);
//		Collection<Future<ArrayList<String>>> tasks = new LinkedList<Future<ArrayList<String>>>();
//		Future<ArrayList<String>> future;
//
//		while (URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize) {
//
//			while (URLQueue.getUnprocessedURLQueueSize() > 0
//					&& URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize) {
//				String pu = URLQueue.PollUnProcessedURLQueue();
//				future = executor.submit(new parser(pu));
//				tasks.add(future);
//
//			}
//			for (Future<?> currTask : tasks) {
//				try {
//					ArrayList<String> List = (ArrayList<String>) currTask.get();
//					System.out.println(tasks.size() + " Get: " + List);
//					if (List != null) {
//						addToWaitingQueue(List);
//					}
//				} catch (Throwable thrown) {
//				}
//			}
//			tasks.clear();
//			add();
//			System.out.println("[" + WaitingQueue.size() + "] WaitingQueue: " + WaitingQueue);
//			System.out.println(
//					"[" + URLQueue.getProcessedURLQueueSize() + "] ProcessedURL: " + URLQueue.getProcessedURLQueue());
//			System.out.println("[" + URLQueue.getUnprocessedURLQueueSize() + "] UnprocessedURL: "
//					+ URLQueue.getUnprocessedURLQueue());
//			if (WaitingQueue.size() == 0 && URLQueue.getUnprocessedURLQueueSize() == 0) {
//				break;
//			}
//		}
//		System.out.println("WebCrawler done: ");
//		System.out.println("[" + WaitingQueue.size() + "] WaitingQueue: " + WaitingQueue);
//		System.out.println(
//				"[" + URLQueue.getProcessedURLQueueSize() + "] ProcessedURL: " + URLQueue.getProcessedURLQueue());
//		System.out.println(
//				"[" + URLQueue.getUnprocessedURLQueueSize() + "] UnprocessedURL: " + URLQueue.getUnprocessedURLQueue());
//
//		System.exit(0);
//
//	}
//
//	public void addToWaitingQueue(ArrayList<String> ContainedURLList) {
//		ContainedURLList.forEach(l -> {
//			if (!WaitingQueue.contains(l) && !URLQueue.ProcessedURLisContain(l) && !URLQueue.UnprocessedURLisContain(l)
//					&& !URLQueue.getErrorURL().contains(l)) {
//				WaitingQueue.add(l);
//				System.out.println("---add to waiting: " + WaitingQueue);
//
//			}
//		});
//	}
//
//	public void add() {
//		// System.out.println("adding");
//		System.out.println("+[" + WaitingQueue.size() + "] WaitingQueue: " + WaitingQueue);
//		while (WaitingQueue.size() > 0 && URLQueue.getProcessedURLQueueSize() < MaxProcessedURLPoolSize
//				&& URLQueue.getUnprocessedURLQueueSize() < MaxUnprocessedQueueSize) {
//			String url = WaitingQueue.poll();
//			if (!URLQueue.getErrorURL().contains(url)) {
//				URLQueue.PushUnProcessedURLQueue(url);
//				System.out.println("+++addedd to unpro: " + URLQueue.getUnprocessedURLQueue());
//			}
//		}
//		System.out.println("added");
//	}
//
//}
//
//class parser implements Callable<ArrayList<String>> {
//
//	private String ParsingURL;
//
//	public parser(String ParsingURL) {
//		URLQueue.PushProcessedURLQueue(ParsingURL);
//		this.ParsingURL = ParsingURL;
//
//	}
//
//	@Override
//	public ArrayList<String> call() throws Exception {
//
//		return Web.getmap().get(ParsingURL);
//	}
//
//}
//
//class Web {
//
//	static Map<String, ArrayList<String>> webmap = new LinkedHashMap<String, ArrayList<String>>();
//	int url;
//
//	public Web() {
//
//	}
//
//	public void create() {
//
//	}
//
//	public static Map<String, ArrayList<String>> getmap() {
//		return webmap;
//	}
//
//}