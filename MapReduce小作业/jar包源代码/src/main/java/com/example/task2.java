package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
// 如果您使用的是新的 API，请确保导入下面的包
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
// 多输出
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;

public class task2 {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 检查参数个数是否正确
        if (args.length != 2) {
            System.err.println("Usage: MainClass <input path> <output path>");
            System.exit(-1);
        }

        System.setProperty("HADOOP_USER_NAME","root");
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MultiTaskJob");

        job.setJarByClass(MainClass.class);
        job.setMapperClass(MultiTaskMapper.class);
        job.setReducerClass(MultiTaskReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // 从命令行获取输入和输出路径
        FileInputFormat.addInputPath(job, new Path(args[0])); // 输入路径
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // 输出路径

        // 设置Reduce任务数量，如果需要
        // job.setNumReduceTasks(1);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    // Mapper 类
    public static class MultiTaskMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text outKey = new Text();
        private Text outValue = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 忽略表头
            if (key.get() == 0 && value.toString().contains("city_of_airport")) {
                return;
            }

            String line = value.toString();
            // 使用逗号分隔，并考虑可能的逗号
            String[] fields = line.split(",", -1);

            // 确保字段数量足够
            if (fields.length >= 41) {
                String trx_datime_c1 = fields[6].trim();  // 第7列
                String trx_datime_c2 = fields[7].trim();  // 第8列
                String has_c2 = fields[15].trim();        // 第16列
                String exp_c1 = fields[16].trim();        // 第17列
                String exp_c2 = fields[17].trim();        // 第18列
                String concourse_c1 = fields[20].trim();  // 第21列
                String concourse_c2 = fields[21].trim();  // 第22列

                // 任务一：计算两次花费总和，以日期为键
                double expense1 = parseDouble(exp_c1);
                double expense2 = parseDouble(exp_c2);
                double totalExpense = expense1 + expense2;

                // 日期处理，只取日期部分
                String date_c1 = trx_datime_c1.split(" ")[0];
                String date_c2 = trx_datime_c2.split(" ")[0];

                // 任务一 - 输出
                if (!date_c1.isEmpty()) {
                    outKey.set("Task1_" + date_c1);
                    outValue.set(String.valueOf(totalExpense));
                    context.write(outKey, outValue);
                }
                if (!date_c2.isEmpty()) {
                    outKey.set("Task1_" + date_c2);
                    outValue.set(String.valueOf(totalExpense));
                    context.write(outKey, outValue);
                }

                // 任务二：以航站楼和日期为键
                String concourse1 = concourse_c1;
                String concourse2 = concourse_c2;

                // 任务二 - 输出
                if (!concourse1.isEmpty() && !date_c1.isEmpty()) {
                    outKey.set("Task2_" + concourse1 + "_" + date_c1);
                    outValue.set(String.valueOf(totalExpense));
                    context.write(outKey, outValue);
                }
                if (!concourse2.isEmpty() && !date_c2.isEmpty()) {
                    outKey.set("Task2_" + concourse2 + "_" + date_c2);
                    outValue.set(String.valueOf(totalExpense));
                    context.write(outKey, outValue);
                }

                // 任务三：过滤 has_c2 为 TRUE 的数据
                if ("TRUE".equalsIgnoreCase(has_c2)) {
                    outKey.set("Task3");
                    outValue.set(line);
                    context.write(outKey, outValue);
                }

            }
        }

        // 辅助方法：解析字符串为 double
        private double parseDouble(String str) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }

    // Reducer 类
    public static class MultiTaskReducer extends Reducer<Text, Text, NullWritable, Text> {
        private Text result = new Text();
        private MultipleOutputs<NullWritable, Text> multipleOutputs;

        @Override
        protected void setup(Context context){
            multipleOutputs = new MultipleOutputs<>(context);
        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String keyStr = key.toString();

            if (keyStr.startsWith("Task1_")) {
                // 任务一：汇总每天的花费总和
                double sumExpense = 0.0;
                for (Text val : values) {
                    sumExpense += parseDouble(val.toString());
                }
                String date = keyStr.substring(6); // 去掉前面的 "Task1_"
                String output = "日期: " + date + ", 总花费: " + sumExpense;
                result.set(output);
                multipleOutputs.write(NullWritable.get(), result, "Task1/output");

            } else if (keyStr.startsWith("Task2_")) {
                // 任务二：汇总航站楼每天的销售金额
                double sumExpense = 0.0;
                for (Text val : values) {
                    sumExpense += parseDouble(val.toString());
                }
                String[] parts = keyStr.substring(6).split("_", 2);
                String concourse = parts[0];
                String date = parts[1];
                // 按照指定格式输出
                String output = concourse + "\t" + date + "\t" + (long)sumExpense;
                result.set(output);
                multipleOutputs.write(NullWritable.get(), result, "Task2/output");

            } else if ("Task3".equals(keyStr)) {
                // 任务三：输出 has_c2 为 TRUE 的数据
                for (Text val : values) {
                    result.set(val.toString());
                    multipleOutputs.write(NullWritable.get(), result, "Task3/output");
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            multipleOutputs.close();
        }

        // 辅助方法：解析字符串为 double
        private double parseDouble(String str) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    }
}
