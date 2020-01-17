package com.daoqidlv.filespilt;

import com.daoqidlv.filespilt.disruptor.DisruptorMaster;
import com.daoqidlv.filespilt.mutil.FileWriteTask;
import com.daoqidlv.filespilt.mutil.MutilThreadReadMaster;
import com.daoqidlv.filespilt.single.forkjoin.ForkJoinPoolMaster;
import com.daoqidlv.filespilt.single.normal.NormalPoolMaster;
import sun.plugin.security.JDK11ClassFileTransformer;

import java.io.File;
import java.util.HashMap;


public abstract class Master {

	private String fileDir;

	private String fileName;

	private int subFileSizeLimit;

	private FileSpiltter fileSpiltter;

	private String splitSymbol;

	private int splitKeywordLocation;

	private String[] splitValues;

	private HashMap<String, FileWriteTask> subFileMap;



	public Master(String fileDir, String fileName, int subFileSizeLimit) {
		this.fileDir = fileDir;
		this.fileName = fileName;
		//TODO 更好的做法：根据上送的单位值进行换算
		this.subFileSizeLimit = subFileSizeLimit * 1024 * 1024;
		this.fileSpiltter = new FileSpiltter(this.subFileSizeLimit, this.fileDir, this.fileName);
	}


	public Master(String fileDir,
				  String fileName,
				  String splitSymbol,
				  int splitKeywordLocation,
				  String[] splitValues) {
		this.fileDir = fileDir;
		this.fileName = fileName;
		this.splitSymbol = splitSymbol;
		this.splitKeywordLocation = splitKeywordLocation;
		this.splitValues = splitValues;
		this.fileSpiltter = new FileSpiltter(this.fileDir, this.fileName, this.splitSymbol, this.splitKeywordLocation, this.splitValues);
	}

	/**
	 * 工厂方法，根据masterType产生master示例
	 *
	 * @param masterType master类型
	 *                   NORMAL —— 返回NormalPoolMaster，使用普通线程池 ThreadPoolExcutor
	 *                   FORKJOIN —— 返回使用ForkJoinPoll
	 *                   PRODUCERCONSUMER —— PRODUCER/CONSUMER模式实现
	 * @return
	 */
	public static Master getMasterInstance(
			String masterType,
			String fileDir,
			String fileName,
			int subFileSizeLimit,
			int readTaskNum,
			int writeTaskNum,
			int queueSize,
			int bufferSize) {
		if (masterType.equals(Constants.MASTER_TYPE_NORMAL_POOL)) {
			return new NormalPoolMaster(fileDir, fileName, subFileSizeLimit);
			//默认使用NormalPoolMaster
		} else if (masterType.equals(Constants.MASTER_TYPE_PRODUCER_CONSUMER)) {
			return new MutilThreadReadMaster(fileDir, fileName, subFileSizeLimit, readTaskNum, writeTaskNum, queueSize);
		} else if (masterType.equals(Constants.MASTER_TYPE_DISRUPTOR)) {
			return new DisruptorMaster(fileDir, fileName, subFileSizeLimit, readTaskNum, writeTaskNum, queueSize, bufferSize);
		} else {
			return new ForkJoinPoolMaster(fileDir, fileName, subFileSizeLimit);
		}
	}


	public static Master getMasterInstance(
			String masterType,
			String fileDir,
			String fileName,
			String splitSymbol,
			int splitKeywordLocation,
			String[] splitValues
			) {
		if (masterType.equals(Constants.MASTER_TYPE_NORMAL_POOL)) {
			return new NormalPoolMaster(fileDir, fileName, splitSymbol, splitKeywordLocation, splitValues);
			//默认使用NormalPoolMaster
		}  else {
			return null;
		}
	}

	/**
	 * 其他初始化操作
	 */
	public void init() {
		subFileMap = new HashMap<>();
	}

	/**
	 * 业务逻辑执行
	 */
	public abstract void excute();

	public abstract void excuteByKeyword();

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSubFileSizeLimit() {
		return subFileSizeLimit;
	}

	public void setSubFileSizeLimit(int subFileSizeLimit) {
		this.subFileSizeLimit = subFileSizeLimit;
	}

	public FileSpiltter getFileSpiltter() {
		return fileSpiltter;
	}

	public void setFileSpiltter(FileSpiltter fileSpiltter) {
		this.fileSpiltter = fileSpiltter;
	}

	public String getSplitSymbol() {
		return splitSymbol;
	}

	public void setSplitSymbol(String splitSymbol) {
		this.splitSymbol = splitSymbol;
	}

	public int getSplitKeywordLocation() {
		return splitKeywordLocation;
	}

	public void setSplitKeywordLocation(int splitKeywordLocation) {
		this.splitKeywordLocation = splitKeywordLocation;
	}

	public String[] getSplitValues() {
		return splitValues;
	}

	public void setSplitValues(String[] splitValues) {
		this.splitValues = splitValues;
	}


}
