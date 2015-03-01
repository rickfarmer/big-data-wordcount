package wordcount.container;

//import org.apache.hadoop.conf.Configuration;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class WordCountInvertor {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(WordCountInvertor.class, args);

		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: wordcountinvertor <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "word count invertor");
		job.setJarByClass(WordCountInvertor.class);
		job.setMapperClass(WordCountInvertorMapper.class);
		job.setCombinerClass(WordCountInvertorReducer.class);
		job.setReducerClass(WordCountInvertorReducer.class);
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	public static class WordCountInvertorMapper extends
			Mapper<Object, Text, IntWritable, Text> {

		private IntWritable count = new IntWritable(0);
		private int keyAsInt = 0;
		private Text word = new Text();
		private String wordAsString = null;

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());

			if (itr.countTokens() == 2) {
				wordAsString = (String) itr.nextElement();
				word.set(wordAsString.trim());
				keyAsInt = TextUtil.getIntValue((String) itr.nextElement());
				// System.out.println("keyAsInt=" + keyAsInt + ", word:" + word.toString());
				count = new IntWritable(keyAsInt);
				
				if (TextUtil.hasValue(word)) {
					// System.out.println("count=" + count + ", word:" + word.toString());
					context.write(count, word);
				}
			}
		}
	}

	public static class WordCountInvertorReducer extends
			Reducer<IntWritable, Text, IntWritable, Text> {

		public void reduce(IntWritable key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {

			if (TextUtil.isBelowThreshold(key.get(), 1000)) {
				for (Text val : values) {
					// System.out.println("key=" + key + ", val=" + val);
					context.write(key, val);
				}
			}
		}
	}
}
