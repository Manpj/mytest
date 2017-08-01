package com.tairan.cloud.service.liushichuli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.junit.Test;

import com.beust.jcommander.internal.Maps;
import com.tairan.cloud.model.Student;

/**
 * 流式处理 demo
 * 
 * @author hzmpj
 *
 */
public class ListDemo {

	public static List<Student> students = new ArrayList<Student>() {
		{
			add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
			add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
			add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
			add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
			add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
			add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
			add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
			add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
			add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
			add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));
		}
	};

	public static List<Integer> nums = new ArrayList<Integer>() {
		{
			add(1);
			add(2);
			add(3);
			add(4);
			add(5);
			add(6);
			add(7);
			add(8);
			add(9);
			add(0);
			add(0);
		}
	};

	/**
	 * 包含整数的集合中筛选出所有的偶数
	 */
	public void test() {
		/**
		 * 正常
		 */
		// List<Integer> evens = Lists.newArrayList();
		// for (final Integer num : nums) {
		// if (num % 2 == 0) {
		// evens.add(num);
		// }
		// }
		/**
		 * 流式处理 stream() 操作将集合转换成一个流 filter() 执行我们自定义的筛选处理，这里是通过lambda表达式筛选出所有偶数
		 * collect() 对结果进行封装处理，并通过Collectors.toList()指定其封装成为一个List集合返回
		 * 
		 */
		// List<Integer> evens = nums.stream().filter(num -> num % 2 ==
		// 0).collect(Collectors.toList());
		/**
		 * 去重 distinct()
		 */
		List<Integer> evens = nums.stream().filter(num -> num % 2 == 0).distinct().collect(Collectors.toList());
		for (final Integer even : evens) {
			System.out.println(even);
		}
	}

	/**
	 * 筛选出所有武汉大学的学生 流式处理
	 */
	private void test1() {
		List<Student> whhandaxue = students.stream().filter(student -> "武汉大学".equals(student.getCollege()))
				.collect(Collectors.toList());
		/**
		 * limit 返回包含前n个元素的流,当集合大小小于n时，则返回实际长度
		 */
		List<Student> limit = students.stream().filter(student -> "土木工程".equals(student.getProfession())).limit(2)
				.collect(Collectors.toList());
		/**
		 * sorted 对流中元素进行排序，sorted要求待比较的元素必须实现Comparable接口
		 * 没有实现，可以将比较器作为参数传递给sorted(Comparable comparator)
		 * 
		 * 筛选出专业为土木工程的学生，并按年龄从小到大排序，筛选出年龄最小的两个学生
		 */
		List<Student> sorted = students.stream().filter(student -> "土木工程".equals(student.getProfession()))
				.sorted((s1, s2) -> s1.getX() - s2.getX()).limit(2).collect(Collectors.toList());
		/**
		 * skip 跳过前n个元素 找出排序在2之后的土木工程专业的学生 如果n大于满足条件的集合的长度，则会返回一个空的集合
		 */
		List<Student> skip = students.stream().filter(student -> "土木工程".equals(student.getProfession())).skip(2)
				.collect(Collectors.toList());

		for (final Student student : skip) {
			System.out.println(student);
		}
	}

	/**
	 * 映射操作map和flatMap
	 */
	private void testMap() {
		/**
		 * 筛选出所有专业为计算机科学的学生姓名：可以在filter筛选的基础之上，通过map将学生实体映射成为学生姓名字符串
		 */
		List<String> names = students.stream().filter(student -> "计算机科学".equals(student.getProfession()))
				.map(Student::getName).collect(Collectors.toList());
		/**
		 * mapToDouble、mapToInt、mapToLong
		 * 计算所有专业为计算机科学学生的年龄之和:通过将Student按照年龄直接映射为IntStream,直接调用提供的sum()方法达到目的，
		 * 使用数值流的好处还在于可以避免jvm装箱操作所带来的性能消耗。
		 */
		int totalAge = students.stream().filter(student -> "计算机科学".equals(student.getProfession()))
				.mapToInt(Student::getX).sum();
		System.out.println(totalAge);
		/**
		 * flatMap与map的区别在于flatMap是将一个流中的每个值都转成一个个流 然后再将这些流扁平化成为一个流。
		 * 字符串数组，希望输出构成这一数组的所有非重复字符
		 */
		String[] strs = { "java8", "is", "easy", "to", "use" };
		List<String[]> distinctStrs = Arrays.stream(strs).map(str -> str.split("")).distinct()
				.collect(Collectors.toList());
		for (final String[] strarray : distinctStrs) {
			for (int i = 0; i < strarray.length; i++) {
				System.out.print(strarray[i] + " ");
			}
		}
		System.out.println();
		// 执行map操作以后，得到是一个构成一个字符串的字符数组的流，此时执行distinct操作是基于在
		// 这些字符串数组之间的对比，达不到目的
		// distinct只有对于一个包含多个字符的流进行操作才能达到目的，即对String<String>进行操作，此时flatMap就可以达到目的
		List<String> distinctStrs1 = Arrays.stream(strs).map(str -> str.split("")).flatMap(Arrays::stream).distinct()
				.collect(Collectors.toList());
		// map(str -> str.split("")) 映射成为Stream<String[]>
		// flatMap(Arrays::stream) 扁平化为Stream<String>
		for (final String str : distinctStrs1) {
			System.out.print(str + " ");
		}
		/**
		 * 双冒号 例子：number -> Math.abs(number) 经过eta转换后是 Math::abs
		 * abs为Math类里面的方法(函数式程序风格)
		 */
		distinctStrs1.forEach(System.out::print);
		distinctStrs1.forEach(str -> System.out.println(str));
		/**
		 * 终端操作
		 */
		/**
		 * allMatch 检测是否全部都满足指定的参数行为，如果全部满足则返回true 检测是否所有的学生都已满18周岁
		 */
		boolean isAdult = students.stream().allMatch(student -> student.getX() >= 18);
		System.out.println(isAdult);
		/**
		 * anyMatch 检测是否存在一个或多个满足指定的参数行为，如果满足则返回true 检测是否有来自武汉大学的学生
		 */
		boolean haswu = students.stream().anyMatch(student -> "武汉大学".equals(student.getCollege()));
		System.out.println(haswu);
		/**
		 * noneMatch 检测是否不存在满足指定行为的元素，如果不存在则返回true 检测是否不存在专业为计算机科学的学生
		 */
		boolean noneji = students.stream().noneMatch(student -> "计算机科学".equals(student.getProfession()));
		System.out.println(noneji);
		/**
		 * findFirst 返回满足条件的第一个元素 选出专业为土木工程的排在第一个学生
		 */
		Optional<Student> optStu = students.stream().filter(student -> "土木工程".equals(student.getProfession()))
				.findFirst();
		// Optional
		// 不是对null关键字的一种替代，而是对于null判定提供了一种更加优雅的实现，从而避免NullPointExceptions
		if (optStu.isPresent()) {// isPresent方法用来检查Optional实例中是否包含值
			System.out.println(optStu.get());
			System.out.println(optStu.get().getName());
		}
		/**
		 * findAny 不一定返回第一个，而是返回任意一个 返回任意一个专业为土木工程的学生
		 */
		Optional<Student> optStu1 = students.stream().filter(student -> "土木工程".equals(student.getProfession()))
				.findAny();
		if (optStu1.isPresent()) {
			System.out.println(optStu1.get());
		}
		// 实际上对于顺序流式处理而言，findFirst和findAny返回的结果是一样的
		// 并行流式处理中使用findAny性能要比findFirst好
	}

	/**
	 * 归约
	 * 
	 * @param args
	 */
	public void testgy() {
		/**
		 * 计算机科学专业 所有学生年龄之和
		 */
		int totalage = students.stream().filter(student -> "计算机科学".equals(student.getProfession())).map(Student::getX)
				.reduce(0, (a, b) -> a + b);// 0是初始值
		System.out.println(totalage);
		// 进一步简化
		int totalAge1 = students.stream().filter(student -> "计算机科学".equals(student.getProfession())).map(Student::getX)
				.reduce(0, Integer::sum);
		System.out.println(totalAge1);
		// 采用无初始值的重载,需要注意返回Optional
		Optional totalAge2 = students.stream().filter(student -> "计算机科学".equals(student.getProfession()))
				.map(Student::getX).reduce(Integer::sum);
		System.out.println(totalAge2.get());
		/**
		 * 收集 Collectors.toList()是一个简单的操作，是对处理结果的封装，对应的还有toSet、toMap
		 */
		Map<Integer, String> names = students.stream().filter(student -> "计算机科学".equals(student.getProfession()))
				.collect(Collectors.toMap(Student::getNum, Student::getName));
		for (Map.Entry<Integer, String> name : names.entrySet()) {
			System.out.println(name.getKey() + "---" + name.getValue());
		}
		/**
		 * 收集器也提供了相应的归约操作，但是与reduce在内部实现上是有区别的，收集器更加适用于可变容器上的归约操作，这些收集器广义上
		 * 基于Collectors.reducing()实现。
		 */
		// 求学生的总人数
		long count = students.stream().collect(Collectors.counting());
		System.out.println(count);
		// 进一步简化
		long count1 = students.stream().count();
		System.out.println(count1);
		// 最大年龄
		Optional olderStudent = students.stream().collect(Collectors.maxBy((s1, s2) -> s1.getX() - s2.getX()));
		System.out.println(olderStudent.get());
		// 进一步简化
		Optional olderStudent1 = students.stream().collect(Collectors.maxBy(Comparator.comparing(Student::getX)));
		System.out.println(olderStudent1.get());
		// 最小年龄
		Optional olderStudent2 = students.stream().collect(Collectors.minBy(Comparator.comparing(Student::getX)));
		System.out.println(olderStudent2.get());
		// 年龄总和
		int totalAge3 = students.stream().collect(Collectors.summingInt(Student::getX));
		System.out.println(totalAge3);
		// 对应的还有summingLong、summingDouble
		// 年龄的平均值
		double avgAge = students.stream().collect(Collectors.averagingDouble(Student::getX));
		System.out.println(avgAge);
		// 对应的还有averagingLong、averagingDouble
		// 一次性得到元素个数、总和、均值、最大值、最小值
		IntSummaryStatistics statistics = students.stream().collect(Collectors.summarizingInt(Student::getX));
		System.out.println(statistics);
		// 对应的还有summarizingLong、summarizingDOuble
		/**
		 * 字符串拼接
		 */
		String namespj = students.stream().map(Student::getName).collect(Collectors.joining());
		System.out.println(namespj);
		String namespj1 = students.stream().map(Student::getName).collect(Collectors.joining("-"));
		System.out.println(namespj1);
	}

	/**
	 * 分组
	 */
	private void textfenzu() {
		/**
		 * 在数据库操作中，我们可以通过group by 关键字对查询到的数据进行分组，java8的流式处理也为我们提供了这样的功能
		 * Collectors.groupingBy 来操作集合。 按照学校对上面的学生进行分组
		 */
		Map<String, List<Student>> groups = students.stream().collect(Collectors.groupingBy(Student::getCollege));
		for (Map.Entry<String, List<Student>> group : groups.entrySet()) {
			System.out.println(group.getKey() + "--" + group.getValue());
		}
		// groupingBy 接收一个分类器，可以自定义分类器来实现需要封分类效果，上面演示的是一级分组，还可以定义多个分类器实现 多级分组
		// 按学校分组的基础之上再按照专业进行分组
		Map<String, Map<String, List<Student>>> group1 = students.stream()
				.collect(Collectors.groupingBy(Student::getCollege, Collectors.groupingBy(Student::getProfession)));
		for (Map.Entry<String, Map<String, List<Student>>> group : group1.entrySet()) {
			System.out.println(group.getKey() + "--" + group.getValue());
		}
		// 实际上 在groupingBy的第二个参数不是只能传递groupBy,还可以传递任意“Collector”类型，比如我们可以传递一个
		// Collector.counting 用以统计每个组的个数
		Map<String, Long> groups2 = students.stream()
				.collect(Collectors.groupingBy(Student::getCollege, Collectors.counting()));
		for (Map.Entry<String, Long> group : groups2.entrySet()) {
			System.out.println(group.getKey() + "--" + group.getValue());
		}
		// 如果我们不添加第二个参数。则编译器会默认帮我们添加一个 Collectors.toList()
	}

	/**
	 * 分区
	 */
	private void testfenqu() {
		/**
		 * 分区可以看作分组的一种特殊情况，在分区中key只有两种情况：true或false，目的是将待分区集合按照条件一分为二
		 * 流式处理利用：collectors.partitionBy()方法实现分区
		 */
		// 将学生分为武大学生和非武大学生
		Map<Boolean, List<Student>> partition = students.stream()
				.collect(Collectors.partitioningBy(student -> "武汉大学".equals(student.getCollege())));
		for (Map.Entry<Boolean, List<Student>> group : partition.entrySet()) {
			System.out.println(group.getKey() + "--" + group.getValue());
		}
		/**
		 * 分区相对分组的优势在于，我们可以同时得到两类结果，在一些应用场景下可以一步得到我们需要的所有结果
		 */
		// 将数组分为奇数和偶数
		/**
		 * 以上介绍的所有收集器均实现自接口java.util.stream.Collector 我们也可以实现该接口来定义自己的收集器
		 */
	}

	/**
	 * 并行流式数据处理
	 */
	private void testbingxing() {
		/**
		 * 流式处理中的很多都适合采用分而治之的思想，从而在处理集合较大时，极大的提高代码的性能，java8的设计者
		 * 也看到了这一点，所以提供了并行流式处理
		 * 
		 * 上面都是调用stream()方法来启动流式处理，java8还提供了parallelStream()来启动
		 * 并行流式处理，本质上基于java7的Fork-Join框架实现，其默认的线程数为宿主机的内核数。
		 * 
		 * 启动并行就会涉及到多线程的安全问题
		 */
	}

	/**
	 * 两个集合 有一个字段相等 如果另外一个字段也相等
	 * 
	 * @param args
	 */
	public void testMap111() {
		Map map1 = Maps.newHashMap();
		Map map2 = Maps.newHashMap();
	//	MapUtils.
		
	}
	
	
	
	
	public static void main(String[] args) {
		ListDemo demo = new ListDemo();
		// demo.test();
		// demo.test1();
		// demo.testMap();
		// demo.testgy();
		// demo.textfenzu();
		demo.testfenqu();
	}

}
