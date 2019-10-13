

import java.io.*;
import java.util.*;

public class subway {
	private static ArrayList<ArrayList<Edge>> g;
	private static Map<String,Integer> Line2Num;
	private static Map<String,Integer> Station2Num;
	private static int totalStation;
	private static int totalLine;
	private static ArrayList<String> lines;
	private static ArrayList<String> stations;
	
	private static int[] dist;
	private static Edge[] path;
	private static int[] ctimes;
//	private static int[] change;
//	public Solve(){
//		 g = new ArrayList<ArrayList<Edge>>();
//		 Line2Num = new HashMap<String,Integer>();
//		 Station2Num = new HashMap<String, Integer>();
//		 lines = new ArrayList<String>();
//	}
	static void init() {
		int size = Station2Num.size();
		dist = new int[size+5];
		path = new Edge[size+5];
		ctimes = new int[size+5];
		for (int i = 0; i < size; i++) {
			dist[i] = -1;
			path[i] = new Edge(-1,-1);
//			path[i].next = -1;
//			path[i].belong = -1;
			ctimes[i] = -1;
		}
	}
	public static void getShortest(int s,int d){
		init();
		dist[s] = 0;
		ctimes[s] = 0;
		Queue<Integer> q = new LinkedList<Integer>();
		
		q.add(s);
		boolean isArrived = false;
		while(!q.isEmpty()) {
			int v = q.poll();
			for(Edge e : g.get(v)) {
				int w = e.next;
				int b = e.belong;
				if(dist[w] == -1) {
					dist[w] = dist[v] + 1;
					path[w].next = v;
					path[w].belong = b;
					ctimes[w] = ctimes[v];
					if(path[v].belong != path[w].belong) {
						ctimes[w] += 1;
					}
					if(w == d) {
						isArrived = true;
					}
					if(!isArrived) {
						q.add(w);
					}
				}else if(dist[w] == dist[v]+1){
					int t = ctimes[v];
					if(path[v].belong != b) {
						t += 1;
					}
					if(t < ctimes[w]) {
						path[w].next = v;
						path[w].belong = b;
						ctimes[w] = t;
					}
				}
			}
		}
//		System.out.println(dist[d]);
//		System.out.println(ctimes[d]);
	}
	public static void writeShortest(int s,int d,String fileName) {
		try {
			File writeName = new File(fileName);
			writeName.createNewFile();// 如果有同名的话覆盖掉。
			try (FileWriter writer = new FileWriter(writeName); BufferedWriter out = new BufferedWriter(writer)) {
				StringBuilder str = new StringBuilder();
				int i = d;
//				int t = path[i].next;
				int last = path[d].belong;
				while( i != -1) {
//					str.insert(0, " "+stations.get(i));
					int cur = path[i].belong;
					if(last != cur) {
//						int temp = str.indexOf(" ");
						String l = lines.get(last);
						String lname = l.substring(0,l.indexOf(" "));
						if(path[i].next != -1)
			 				str.insert(0, "\n"+lname);
					}
					last = cur;
					str.insert(0, "\n"+stations.get(i));
					i = path[i].next;
				}
				str.insert(0, dist[d]+1);
				System.out.println(str);
				out.write(str.toString());
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void writeB(int s,int d) {
		StringBuilder str = new StringBuilder();
		int i = d;
//		int t = path[i].next;
		int last = path[d].belong;
		while( i != -1) {
//			str.insert(0, " "+stations.get(i));
			int cur = path[i].belong;
			if(last != cur) {
//				int temp = str.indexOf(" ");
				String l = lines.get(last);
				String lname = l.substring(0,l.indexOf(" "));
				if(path[i].next != -1)
					str.insert(0, "\n"+lname);
			}
			last = cur;
			str.insert(0, "\n"+stations.get(i));
			i = path[i].next;
		}
		str.insert(0, dist[d]+1);
		System.out.println(str);
	}
	public static void readFile(String fileName) {
		g = new ArrayList<ArrayList<Edge>>();
		Line2Num = new HashMap<String,Integer>();
		Station2Num = new HashMap<String, Integer>();
		lines = new ArrayList<String>();
		stations = new ArrayList<String>();
		try (FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader)
		) {
			String line;
			while ((line = br.readLine()) != null) {
//				System.out.println(line);
				lines.add(line);
				String[] buf = line.split(" ");
				Line2Num.put(buf[0], totalLine);
				int l = buf.length;
				int curS = totalStation;
				int lastS = 0;
				for(int i = 1; i < l;i++) {
					lastS = curS;
					if(Station2Num.get(buf[i]) == null) {
						Station2Num.put(buf[i],totalStation);
						curS = totalStation;
						totalStation++;
						stations.add(buf[i]);
						g.add(new ArrayList<Edge>());
					}else {
						curS = Station2Num.get(buf[i]);
					}
//					System.out.println(curS+" "+totalStation);
					if(i != 1) {
						g.get(lastS).add(new Edge(curS,totalLine));
						g.get(curS).add(new Edge(lastS,totalLine));
					}
				}
				totalLine++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeA(String fileName,String text) {
        try {
            File writeName = new File(fileName);
            writeName.createNewFile();//如果有同名的话覆盖掉。
            try (FileWriter writer = new FileWriter(writeName);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(text);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	static void findLine(String lName,String filename) {
		Integer a = Line2Num.get(lName);
		if(a == null) {
			System.out.println("线路不存在");
			return;
		}
		String l = lines.get(a);
		writeA(filename,l);
	}
	public static boolean isTxT(String f) {
		if(f == null) return false;
		if(f.substring(f.indexOf("."),f.length()).equals(".txt")) {
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		int l = args.length;
		if(l == 0) {
			System.out.println("请输入参数");
			return;
		}
		boolean out = false;
		boolean in = false;
		boolean opa = false;
		boolean opb = false;
		String infile = null;
		String outfile = null;
		String line = null;
		String dest = null;
		String sourse = null;
		
		for(int i = 0; i < l; i++) {
			switch(args[i]) {
				case "-a":
					i++;
					line = args[i];
					if(line.charAt(0) != '-') {
						opa = true;
					}
					break;
				case "-b":
					sourse = args[++i];
					dest = args[++i];
					if(sourse.charAt(0) != '-' && dest.charAt(0) != '-') {
						opb = true;
					}
					break;
				case "-map":
					infile = args[++i];
					if(isTxT(infile)) {
						in = true;
					}
					break;
				case "-o":
					outfile = args[++i];
					if(isTxT(outfile)) {
						out = true;
					}
					break;
				default:
					System.out.println("请输入正确的命令");
					System.out.println("如:java subway -a 1号线 -map subway.txt -o station.txt");
					System.out.println("或:subway.exe -b 洪湖里 复兴路 -map subway.txt -o routine.txt");
			}
		}
		if(opa && opb) {
			System.out.println("一次只能执行一个操作");
			return;
		}
		if(!out) {
			System.out.println("缺少指定的输出文件 -o b.txt");
			return;
		}
		if(!in) {
			System.out.println("缺少指定的输入文件 -map a.txt");
			return;
		}
		if(!opa && !opb) {
			System.out.println("请输入正确的操作 -a xxx || -b xxx xxx");
			return;
		}
		
		if(opa) {
			readFile(infile);
			findLine(line, outfile);
		}else if(opb){
			readFile(infile);
			Integer x = Station2Num.get(sourse);
			Integer y = Station2Num.get(dest);
			if(x == null || y == null) {
				System.out.println("站点不存在");
				return;
			}
			getShortest(x,y);
			writeShortest(x, y, outfile);
		}
	}
}

class Edge{
	int next;
	int belong;
	public Edge(int next, int belong){
		this.next = next;
		this.belong = belong;
	}
}
