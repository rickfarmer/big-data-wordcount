package wordcount.container;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.yarn.annotation.YarnComponent;

@YarnComponent
public class WordCountReducer extends
		Reducer<Text, IntWritable, Text, IntWritable> {

	private static final org.apache.commons.logging.Log log = LogFactory
			.getLog(WordCountReducer.class);

	private IntWritable result = new IntWritable();

	@Autowired
	private Configuration configuration;

	public Text word;

	public WordCountReducer(Text word) {
		this.word = word;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WordCountReducer.class, args);

		int[] counts = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000 };
		ArrayList<IntWritable> countsList = new ArrayList<IntWritable>();
		for (int index = 0; index < counts.length; index++) {
			countsList.add(new IntWritable(counts[index]));
		}

		reduce(countsList);

	}

	public void reduce(Text key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		
		if (TextUtil.isBelowThreshold(sum, 1000) && TextUtil.containsNumericKey(key)) {
			result.set(sum);
			context.write(key, result);
		}
	}

	public static void reduce(Iterable<IntWritable> values) {

		int sum = 0;
		for (IntWritable val : values) {
			sum += val.get();
		}
		
		if (TextUtil.isBelowThreshold(sum, 1000)) {
			System.out.println(sum);
		}

	}

}