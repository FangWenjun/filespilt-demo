package com.daoqidlv.filespilt;

import com.daoqidlv.filespilt.disruptor.DisruptorMaster;
import com.daoqidlv.filespilt.mutil.FileWriteTask;
import com.daoqidlv.filespilt.mutil.MutilThreadReadMaster;
import com.daoqidlv.filespilt.single.forkjoin.ForkJoinPoolMaster;
import com.daoqidlv.filespilt.single.normal.NormalPoolMaster;
import java.util.HashMap;


public abstract class Master {

	private String fileDir;

	private String fileName;

	private HashMap<String, FileWriteTask> subFileMap;


	public Master(String fileDir, String fileName) {
		this.fileDir = fileDir;
		this.fileName = fileName;
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
			return new NormalPoolMaster.Builder(fileDir, fileName).bySize(subFileSizeLimit).build();
			//默认使用NormalPoolMaster
		} else if (masterType.equals(Constants.MASTER_TYPE_PRODUCER_CONSUMER)) {
			return null;
	//		return new MutilThreadReadMaster(fileDir, fileName, subFileSizeLimit, readTaskNum, writeTaskNum, queueSize);
		} else if (masterType.equals(Constants.MASTER_TYPE_DISRUPTOR)) {
			return new DisruptorMaster(fileDir, fileName, subFileSizeLimit, readTaskNum, writeTaskNum, queueSize, bufferSize);
		} else {
			return new ForkJoinPoolMaster(fileDir, fileName, subFileSizeLimit);
		}
	}


	public static Master getMasterInstanceByKeyword(
			String masterType,
			String fileDir,
			String fileName,
			String splitSymbol,
			int splitKeywordLocation,
			String[] splitValues
			) {
		if (masterType.equals(Constants.MASTER_TYPE_NORMAL_POOL)) {
			return new NormalPoolMaster.Builder(fileDir, fileName).byKeyword(splitSymbol, splitKeywordLocation, splitValues).build();
			//默认使用NormalPoolMaster
		} else if (masterType.equals(Constants.MASTER_TYPE_PRODUCER_CONSUMER)) {
			return null;
		//	return MutilThreadReadMaster.getMutilThreadReadMasterByKeyword(fileDir, fileName, splitSymbol, splitKeywordLocation, splitValues);
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


}
