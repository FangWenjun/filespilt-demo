package com.daoqidlv.filespilt.single.normal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.daoqidlv.filespilt.FileSpiltter;
import com.daoqidlv.filespilt.Master;
import com.daoqidlv.filespilt.Util;

/**
 * java编程实现：
 * CSV文件有8GB，里面有1亿条数据，每行数据最长不超过1KB，
 * 目前需要将这1亿条数据拆分为10MB一个的CSV写入到同目录下，
 * 要求每一个CSV的数据必须是完整行，所有文件不能大于10MB。
 * 
 * 服务器配置：4核CPU、10GB物理内存，请给出虚拟机的大致内存配置
 *
 */
public class NormalPoolMaster extends Master{

	private String splitSymbol;

	private int splitKeywordLocation;

	private String[] splitValues;

	private int subFileSizeLimit;

	private FileSpiltter fileSpiltter;

	private ExecutorService fileWritePool;

	public static class Builder {

		private String splitSymbol;
		private int splitKeywordLocation;
		private String[] splitValues;
		private int subFileSizeLimit;
		private FileSpiltter fileSpiltter;
		private ExecutorService fileWritePool;
		private String fileDir;
		private String fileName;

		public Builder(String fileDir, String fileName) {
			this.fileDir = fileDir;
			this.fileName = fileName;
		}

		public Builder bySize(int subFileSizeLimit) {
			this.subFileSizeLimit = subFileSizeLimit;
			return this;
		}

		public Builder byKeyword(String splitSymbol,
								 int splitKeywordLocation,
								 String[] splitValues) {
			this.splitSymbol = splitSymbol;
			this.splitKeywordLocation = splitKeywordLocation;
			this.splitValues = splitValues;
			this.fileSpiltter = new FileSpiltter.Builder(fileDir, fileName).byKeyword(splitSymbol,splitKeywordLocation,splitValues).build();
			return this;
		}

		public NormalPoolMaster build() {
			return new NormalPoolMaster(this);
		}

	}

	private NormalPoolMaster(Builder builder) {
		super(builder.fileDir, builder.fileName);
		this.splitSymbol = builder.splitSymbol;
		this.splitValues = builder.splitValues;
		this.splitKeywordLocation = builder.splitKeywordLocation;
		this.fileSpiltter = builder.fileSpiltter;
		this.fileWritePool = new ThreadPoolExecutor(4, 4, 0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(200));
	}


	@Override
	public void excute() {
		System.out.println("begin to spilt...");
		long startTime = System.currentTimeMillis();
		File file = new File(Util.genFullFileName(this.getFileDir(), this.getFileName()));
		BufferedReader reader = null;
		List<Future> futureList = new ArrayList<Future>();
		int sumSize = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String lineContent = "";
			while (reader.ready() && (lineContent = reader.readLine()) != null) {
				FileWriteTask fileWriteTask = this.fileSpiltter.spilt(lineContent);
				if(fileWriteTask != null) {
					//将任务提交pool处理
					Future future = this.fileWritePool.submit(fileWriteTask, fileWriteTask);
					futureList.add(future);
					sumSize += fileWriteTask.getFileSize();
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("读取源文件错误！");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("读取源文件错误！");
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("关闭源文件流错误！");
					e.printStackTrace();
				}				
			}
		}        
		
		Future futureTemp = null; 
		int totalFileWritenSize = 0;
		while(true) {
			for(Iterator<Future> it = futureList.iterator(); it.hasNext();) {
				futureTemp = it.next();
				if(futureTemp.isDone()) {
					try {
						totalFileWritenSize += ((FileWriteTask)futureTemp.get()).getFileWritenSize();
					} catch (InterruptedException e) {
						System.err.println("获取线程执行结果失败。");
						e.printStackTrace();
					} catch (ExecutionException e) {
						System.err.println("获取线程执行结果失败。");
						e.printStackTrace();
					}
					
					it.remove();
				}
			}
			if(futureList == null || futureList.size() == 0) {
				break;
			}
		}
		
		this.fileWritePool.shutdown();
		long endTime = System.currentTimeMillis();
		System.out.println("总耗时（ms）："+(endTime-startTime));		
		
		//检查源文件大小和最终写入文件大小和是否相等。
		if(sumSize == totalFileWritenSize) {
			System.out.println("文件拆分成功！源文件大小为："+sumSize+", 拆分后的子文件大小之后为："+totalFileWritenSize);
		} else {
			System.out.println("文件拆分失败！源文件大小为："+sumSize+", 拆分后的子文件大小之后为："+totalFileWritenSize);
		}
	}

	@Override
	public void excuteByKeyword() {
		System.out.println("begin to spilt...");
		long startTime = System.currentTimeMillis();
		File file = new File(Util.genFullFileName(this.getFileDir(), this.getFileName()));
		BufferedReader reader = null;
		List<Future> futureList = new ArrayList<Future>();
		int sumSize = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			String lineContent = "";
			FileSpiltter fileSpiltter = this.fileSpiltter;
			while (reader.ready() && (lineContent = reader.readLine()) != null) {
				sumSize += lineContent.length();
				fileSpiltter.splitByKeyWord(lineContent);
			}

			int size = fileSpiltter.fileWriteTaskHashMap.size();
			Iterator iter = fileSpiltter.fileWriteTaskHashMap.entrySet().iterator();
			while(iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String) entry.getKey();
				FileWriteTask val = (FileWriteTask) entry.getValue();
				if(val != null) {
					//将任务提交pool处理
					Future future = this.fileWritePool.submit(val, val);
					futureList.add(future);
				}

			}
		} catch (FileNotFoundException e) {
			System.err.println("读取源文件错误！");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("读取源文件错误！");
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("关闭源文件流错误！");
					e.printStackTrace();
				}
			}
		}

		Future futureTemp = null;
		int totalFileWritenSize = 0;
		while(true) {
			for(Iterator<Future> it = futureList.iterator(); it.hasNext();) {
				futureTemp = it.next();
				if(futureTemp.isDone()) {
					try {
						totalFileWritenSize += ((FileWriteTask)futureTemp.get()).getFileWritenSize();
					} catch (InterruptedException e) {
						System.err.println("获取线程执行结果失败。");
						e.printStackTrace();
					} catch (ExecutionException e) {
						System.err.println("获取线程执行结果失败。");
						e.printStackTrace();
					}

					it.remove();
				}
			}
			if(futureList == null || futureList.size() == 0) {
				break;
			}
		}

		this.fileWritePool.shutdown();
		long endTime = System.currentTimeMillis();
		System.out.println("总耗时（ms）："+(endTime-startTime));

		//检查源文件大小和最终写入文件大小和是否相等。
		if(sumSize == totalFileWritenSize) {
			System.out.println("文件拆分成功！源文件大小为："+sumSize+", 拆分后的子文件大小之后为："+totalFileWritenSize);
		} else {
			System.out.println("文件拆分失败！源文件大小为："+sumSize+", 拆分后的子文件大小之后为："+totalFileWritenSize);
		}

	}




}
