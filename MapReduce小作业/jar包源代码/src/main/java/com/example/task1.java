package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class task1 {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 检查参数个数是否正确
        if (args.length != 2) {
            System.err.println("Usage: MainClass <input path> <output path>");
            System.exit(-1);
        }

        System.setProperty("HADOOP_USER_NAME","root");
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Class Statistics");

        job.setJarByClass(task1.class);
        job.setMapperClass(ClassMapper.class);
        job.setReducerClass(ClassReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // 从命令行获取输入和输出路径
        FileInputFormat.addInputPath(job, new Path(args[0])); // 输入路径
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // 输出路径

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    // Mapper 类
    public static class ClassMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text classNumber = new Text();
        private Text birthAndName = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split(",");
            if(fields.length == 3){
                String classNum = fields[0].trim(); // 班级号
                String birthDate = fields[1].trim(); // 出生时间
                String name = fields[2].trim(); // 姓名

                classNumber.set(classNum);
                birthAndName.set(birthDate + "," + name);

                context.write(classNumber, birthAndName);
            }
        }
    }

    // Reducer 类
    public static class ClassReducer extends Reducer<Text, Text, NullWritable, Text> {
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        private Text result = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String classNum = key.toString();

            int count = 0; // 计数器：每个班级的人数

            String oldestName = ""; // 年龄最大的人姓名
            String youngestName = ""; // 年龄最小的人姓名
            Date oldestDate = null; // 年龄最大的人出生日期
            Date youngestDate = null; // 年龄最小的人出生日期

            for(Text val : values){
                count++;
                String[] birthAndName = val.toString().split(",", 2);
                String birthDateStr = birthAndName[0];
                String name = birthAndName[1];

                try{
                    Date birthDate = dateFormat.parse(birthDateStr);
                    if(oldestDate == null || birthDate.before(oldestDate)){
                        oldestDate = birthDate;
                        oldestName = name;
                    }
                    if(youngestDate == null || birthDate.after(youngestDate)){
                        youngestDate = birthDate;
                        youngestName = name;
                    }
                } catch(ParseException e){
                    // 如果日期解析失败，输出错误日志
                    e.printStackTrace();
                }
            }

            // 准备输出结果
            String output = "班级号: " + classNum + ", 人数: " + count +
                    ", 年龄最大的人: " + oldestName +
                    ", 年龄最小的人: " + youngestName;

            result.set(output);
            context.write(NullWritable.get(), result);
        }
    }
}
