package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
// 如果使用 Hadoop 2.x 及以上版本
import org.apache.hadoop.mapreduce.filecache.DistributedCache;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
// 导入必要的类
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
// 多输出
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class task3 {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, URISyntaxException {
        // 检查参数个数是否正确
        if (args.length != 3) {
            System.err.println("Usage: Task2 <sales data path> <airport data path> <output path>");
            System.exit(-1);
        }

        System.setProperty("HADOOP_USER_NAME","root");
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "JoinAndPartitionJob");

        job.setJarByClass(task3.class);
        job.setMapperClass(MultiTaskMapper.class);
        job.setReducerClass(MultiTaskReducer.class);

        job.setPartitionerClass(RegionPartitioner.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // 添加第一个输入文件（销售数据）
        FileInputFormat.addInputPath(job, new Path(args[0])); // 销售数据路径

        // 设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(args[2])); // 输出路径

        // 将第二个文件加入缓存
        job.addCacheFile(new URI(args[1])); // 目的地数据文件

        // 设置 Reduce 任务数量，与 region 数量相同
        job.setNumReduceTasks(3);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    // Mapper 类
    public static class MultiTaskMapper extends Mapper<LongWritable, Text, Text, Text> {
        private Text outKey = new Text();
        private Text outValue = new Text();

        private Map<String, String> airportData = new HashMap<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 读取缓存的目的地数据文件
            BufferedReader br = null;
            try {
                // 获取缓存文件（第二个输入文件）
                URI[] cacheFiles = context.getCacheFiles();
                if (cacheFiles != null && cacheFiles.length > 0) {
                    String line;
                    // 由于是单个文件，可以直接使用第一个文件
                    Path cachePath = new Path(cacheFiles[0].toString());
                    br = new BufferedReader(new InputStreamReader(
                            FileSystem.get(context.getConfiguration()).open(cachePath)));
                    // 读取文件并缓存到 HashMap 中
                    while ((line = br.readLine()) != null) {
                        // 解析目的地数据文件
                        String[] tokens = line.split(",", -1);
                        if (tokens.length >= 4) {
                            String region = tokens[0].trim();
                            String city_of_airport = tokens[1].trim().toUpperCase();
                            String country = tokens[2].trim();
                            String gdp = tokens[3].trim();
                            String value = region + "," + country + "," + gdp;
                            airportData.put(city_of_airport, value);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Exception reading cache file: " + e);
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        }

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
            if (fields.length >= 42) {
                String city_of_airport = fields[1].trim().toUpperCase();  // 第2列

                // 从缓存的目的地数据中获取信息
                String airportInfo = airportData.get(city_of_airport);

                if (airportInfo != null) {
                    // 输出连接后的数据
                    String joinedData = line + "," + airportInfo; // 将目的地数据加到销售数据后面

                    // 输出任务1的结果
                    outKey.set("Task1");
                    outValue.set(joinedData);
                    context.write(outKey, outValue);

                    // 获取 region，用于任务2
                    String[] airportInfoFields = airportInfo.split(",", -1);
                    String region = airportInfoFields[0].trim();

                    // 输出任务2的数据，键为 region
                    outKey.set(region);
                    outValue.set(joinedData);
                    context.write(outKey, outValue);

                } else {
                    // 如果找不到匹配的 city_of_airport，可以选择忽略或处理
                }
            }
        }
    }

    // 自定义分区器，根据 region 分区
    public static class RegionPartitioner extends Partitioner<Text, Text> {
        private static Map<String, Integer> regionMap = new HashMap<>();
        static {
            // 手动分配 region 的编号
            regionMap.put("INDIA SUB-CONTINENT", 0);
            regionMap.put("EUROPE", 1);
            regionMap.put("AFRICA", 2);
            // 如果有更多的 region，可以继续添加
        }

        @Override
        public int getPartition(Text key, Text value, int numPartitions) {
            String keyStr = key.toString();
            if (regionMap.containsKey(keyStr)) {
                return regionMap.get(keyStr) % numPartitions;
            } else {
                // 默认分区
                return 0;
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

            if ("Task1".equals(keyStr)) {
                // 任务一：输出连接后的数据
                for (Text val : values) {
                    result.set(val.toString());
                    multipleOutputs.write(NullWritable.get(), result, "Task1/output");
                }
            } else {
                // 任务二：根据 region 分区输出
                for (Text val : values) {
                    result.set(val.toString());
                    // 按照 region 输出到不同的文件夹
                    multipleOutputs.write(NullWritable.get(), result, "Task2/" + keyStr + "/part");
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            multipleOutputs.close();
        }
    }
}
