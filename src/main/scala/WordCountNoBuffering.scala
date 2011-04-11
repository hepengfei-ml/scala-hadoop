package wordcount

import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapred.{MapReduceBase, Mapper, Reducer, OutputCollector, Reporter}

object WordCountNoBuffering {

	val one  = new IntWritable(1)
	val word = new Text     // Value will be set in a non-thread-safe way!

	class Map extends MapReduceBase with Mapper[LongWritable, Text, Text, IntWritable] {
		
		def map(key: LongWritable, valueDocContents: Text, output: OutputCollector[Text, IntWritable], reporter: Reporter):Unit = {
			for {
				// In the Shakespeare text, there are also expressions like 
				//   As both of you--God pardon it!--have done.
				// So we also use "--" as a separator.
				wordString1 <- valueDocContents.toString.split("(\\s+|--)")  
        wordString  =  wordString1.replaceAll("[.,:;?!'\"]+", "")  // also strip out punctuation, etc.
			} {
				word.set(wordString)
				output.collect(word, one)
			}
		}
	}
}
