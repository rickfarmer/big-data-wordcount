package wordcount.container;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.yarn.annotation.YarnComponent;

@YarnComponent
public class WordCountMapper extends Mapper<Object, Text, Text, IntWritable> {

	private static final org.apache.commons.logging.Log log = LogFactory
			.getLog(WordCountMapper.class);

	public Text word;

	public WordCountMapper(Text word) {
		this.word = word;
	}

	@Autowired
	private Configuration configuration;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WordCountMapper.class, args);

		if (args.length != 1) {
			System.err.println("Usage: wordcountmapper <in>");
			System.exit(2);
		}

		String in = FileIO.read(args[0]);
		Text value = new Text(in);
		map(value);

	}

	private final static IntWritable one = new IntWritable(1);
	private WordCountMapper data = new WordCountMapper(new Text());

	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());

		while (itr.hasMoreTokens()) {
			data.word.set(TextUtil.cleanText(itr.nextToken()));
			context.write(data.word, one);
		}
	}

	public static void map(Text value) {
		StringTokenizer itr = new StringTokenizer(value.toString());

		while (itr.hasMoreTokens()) {
			String currentWord = TextUtil.cleanText(itr.nextToken());
			System.out.println(currentWord);
		}
	}

}