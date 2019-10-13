#软件工程-个人项目-地铁出行路线规划的命令行程序总结
[TOC]
##任务
实现一个能处理正确输入的命令行的计算地铁线路最短路径的程序
>[作业要求](https://edu.cnblogs.com/campus/buaa/2019BUAASummerSETraining/homework/3407)

[项目规划](https://www.cnblogs.com/bunnywwwwyj/p/11563321.html)
注：课程要求是北京地铁的实现（和规划不同）

[gihub](https://github.com/BrocaZ/SE_subway)

## 实现

####语言选择
和规划有出入。
写了部分c的代码后，还是选择了较易实现的java

####数据结构
- 邻接表的存储
``` 
private static ArrayList<ArrayList<Edge>> g;

class Edge{
int next;
int belong;
}
```
- 路线的存储
`private static Edge[] path;`

- 线路存储
`private static ArrayList<String> lines;`

- 站点名称存储
`private static ArrayList<String> stations;`

- 保存换乘的次数
`private static int[] ctimes;
`
- 一些辅助的数据结构
名称和下标的映射
```
private static Map<String,Integer> Line2Num;
private static Map<String,Integer> Station2Num;
```

- 和规划的不同
	- 同规划不同，由于换乘点太多，一个点会属于很多站点，计算换乘次数好像没有这么方便，所以并没有将路线信息存储在站点中而是直接存储在边的信息里。
	- 规划时并没有将`优先输出换乘次数少的路线`这一需求考虑进去，所以实现时多了一些。

####算法
- 算法分析
也是和规划的并不相同。
规划时大致决定了是用dijkstra实现最短路

然后回去复习数据结构的时候发现
dijkstra 是 有权图的单源最短路算法
就目前的需求，地铁的每个边权重相同，所以并不需要优先收录最短的然后更新
(但好像用dijstra也没太大差别吧(逃
所以为了省事就用了无权图的单源最短路算法(BFS)

考虑输出换乘次数少这一需求的时候
是边遍历，边判断是否需要换乘，然后更新 存储换乘次数的数组
然后当碰到站的数量相同时，再比较换乘次数，选择次数少的

- 关键代码

```
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
					System.out.println(t+" "+ctimes[w]);
					if(t < ctimes[w]) {
						path[w].next = v;
						path[w].belong = b;
						ctimes[w] = t;
					}
				}
			}
		}
	}
```


##输入输出
- 按照示例要求需处理以下3类命令行输入

`subway.exe -map subway.txt`

`subway.exe -a 1号线 -map subway.txt -o station.txt`

`subway.exe -b 洪湖里 复兴路 -map subway.txt -o routine.txt`

- 按照要求输入的最短路文件示例如下:
```
3
洪湖里
西站
6号线
复兴路
```
- 输入的线路信息(subway.txt)
接受放在同一路径下 ANSI编码 后缀为`.txt`的文件
```
1号线 苹果园 古城 ...
2号线 西直门 积水潭 ...
...
```
- 输出的线路查询结果同上
```
1号线  苹果园 古城 ...
```

##测试
![北京地铁](https://images.cnblogs.com/cnblogs_com/bunnywwwwyj/1553238/o_subway_map.jpg)

####正确性测试
- `-a`的正确性测试
`java subway -a 1号线 -map subway.txt -o station.txt`

- 无需换乘的最短路测试
`java subway -b 南锣鼓巷 金台路 -map subway.txt -o line.txt`

- 需要换乘的最短路测试
`java subway -b 立水桥 张自忠路 -map subway.txt -o line.txt`

- 同时有多条最短路的测试
`java subway -b 复兴门 宣武门 -map subway.txt -o line.txt`
注：该测试样例有两条长度为2的路，一条换乘，一条不换乘
<!-- ![correct-test]() -->
![](https://img2018.cnblogs.com/blog/1806390/201910/1806390-20191014022940489-459302200.jpg)

####鲁棒性测试
- 命令行输入错误
    - 无效的参数
    `java subway -wrong`
    - 无效的文件
    `java subway -a 1号线 -map xxx.txt -o station.txt`
    - 无效站点
    `java subway -b 乱七 八糟 -map subway.txt -o line.txt`
    - 无效的路线
    `java subway -a xx线 -map subway.txt -o station.txt`
- 输入站点信息的文件格式错误
用空格隔开，格式错误也能读。
- 输出文件routine.txt已存在
会覆盖掉原文件，原文件打开好像也没关系。
<!-- ![robust-test](https://github.com/BrocaZ/SE_subway/blob/master/robust-test.jpg) -->
![](https://img2018.cnblogs.com/blog/1806390/201910/1806390-20191014022950938-1606304906.jpg)

##总结

####不足
- 如上文列出来的，规划和实际有较大出入，语言的选择，数据结构，算法上都和规划的不太一样。有些是没考虑全面，有些写的太草率了。大概，改动最少的应该是测试那块。
- 拖延的有点严重，嗯，深刻反省，不拖延的话，大概能干更多的事吧。
- 由于刚学习数据结构的时候用的都是c语言实现。所以虽然写的是java程序，但是用了很多static的函数来实现，感觉很不面向对象。完全就是用函数式的思路写。
- 因为项目小，还是个人项目。所以基本上没怎么考虑过代码的可读性。
- 类创建的也不规范，封装的也不好，程序的再利用性感觉也不好，这些都是可以改进提升的点吧。

####收获
- 大致走了一遍这样的流程，是能感觉到前期规划能让后面干的时候思路更加清晰。
- 前期想好的测试，实际测试的时候确实有测出多个错误。
- 感受到知识确实要多复习，忘得好快啊。
- 有个开始规划和结束总结的时间，有种有始有终的感觉。


