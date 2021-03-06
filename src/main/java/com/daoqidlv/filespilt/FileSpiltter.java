package com.daoqidlv.filespilt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.daoqidlv.filespilt.single.forkjoin.ForkFileWriteTask;
import com.daoqidlv.filespilt.single.normal.FileWriteTask;

/**
 * 文件拆分期，只支持单线程操作
 * @author daoqidelv
 * @CreateDate 2017年5月4日
 *
 */
public class FileSpiltter {

	/**********必填************/

	/**
	 * 拆分出来的子文件所在路径
	 */
	private String fileDir;

	/**
	 * 待生成的子文件全名称模板
	 */
	private String fileNameTemplate;


	/**********选填************/

	/**
	 * 拆分出来的子文件的大小上限值
	 */
	private int subFileSizeLimit;
	
	/**
	 * 当前cache文件内容，大小小于subFileSizeLimit
	 */
	private List<String> fileCache;
	
	/**
	 * 当前cache的大小值，小于subFileSizeLimit
	 */
	private int fileCacheSize;
	
	/**
	 * 已拆分的子文件个数
	 * 鉴于是单线程操作，故这里未使用原子计数器
	 */
	private int subFileCounter;

	/**
	 * 间隔符
	 */
	private String splitSymbol;

	/**
	 * 拆分关键字位置
	 */
	private int splitKeywordLocation;

	/**
	 * 拆分关键字值
	 */
	private String[] splitValues;



	public static class Builder {

		private String fileDir;
		private String fileNameTemplate;
		private int subFileSizeLimit;
		private List<String> fileCache;
		private int fileCacheSize;
		private int subFileCounter;
		private String splitSymbol;
		private int splitKeywordLocation;
		private String[] splitValues;

		public  Builder(String fileDir, String fileNameTemplate) {
			this.fileDir = fileDir;
			this.fileNameTemplate = fileNameTemplate;
		}

		public Builder bySize(int subFileSizeLimit) {
			this.subFileSizeLimit = subFileSizeLimit;
			this.fileCacheSize = 0;
			this.fileCache = new ArrayList<String>();
			return this;
		}

		public Builder byKeyword(String splitSymbol,
							   int splitKeywordLocation,
							   String[] splitValues) {
			this.splitSymbol = splitSymbol;
			this.splitKeywordLocation = splitKeywordLocation;
			this.splitValues = splitValues;
			return this;
		}

		public FileSpiltter build() {
			return new FileSpiltter(this);
		}

	}

	private FileSpiltter(Builder builder) {
		this.fileDir = builder.fileDir;
		this.fileNameTemplate = builder.fileNameTemplate;
		this.fileCache = builder.fileCache;
		this.splitSymbol = builder.splitSymbol;
		this.splitKeywordLocation = builder.splitKeywordLocation;
		this.splitValues = builder.splitValues;
	}



	public HashMap<String, FileWriteTask> fileWriteTaskHashMap = new HashMap<>();



	private FileSpiltter(int subFileSizeLimit, String fileDir, String fileNameTemplate) {
		this.fileDir = fileDir;
		this.fileNameTemplate = fileNameTemplate;
		this.subFileSizeLimit = subFileSizeLimit;
		this.fileCacheSize = 0;
		this.fileCache = new ArrayList<String>();
	}


	public FileSpiltter(String fileDir,
						String fileNameTemplate,
						String splitSymbol,
						int splitKeywordLocation,
						String[] splitValues) {
		this.fileDir = fileDir;
		this.fileNameTemplate = fileNameTemplate;
		this.splitSymbol = splitSymbol;
		this.splitKeywordLocation = splitKeywordLocation;
		this.splitValues = splitValues;
	}

/*	public FileSpiltter getFileSplitterByKeyword(String fileDir,
												 String fileNameTemplate,
												 String splitSymbol,
												 int splitKeywordLocation,
												 String[] splitValues) {

	}*/

	
	public FileWriteTask spilt(String lineContent) {
		int totalSize = this.fileCacheSize + lineContent.length();
		//当前行加入后，缓存的文件内容大于上限值，则生成一个新的Task
		if(totalSize >= subFileSizeLimit) {
			this.subFileCounter++;
			String subFileName = genSubFileName();
			List<String> fileCacheCopy = new ArrayList<String>();
			fileCacheCopy.addAll(this.fileCache);
			FileWriteTask fileWriteTask = new FileWriteTask(this.fileDir, subFileName, fileCacheCopy, this.fileCacheSize);
			//重置文件缓存和大小
			this.fileCache.clear();
			this.fileCacheSize = 0;
			return fileWriteTask;
		} else {
			this.fileCache.add(lineContent);
			this.fileCacheSize += lineContent.length();
			return null;
		}
	}


	public void splitByKeyWord(String lineContent) {

		String[] contentStrs = lineContent.split(splitSymbol);
		int mapSize = fileWriteTaskHashMap.size();
		for (String value : splitValues) {
			if (value.equals(contentStrs[splitKeywordLocation])) {
				FileWriteTask task = fileWriteTaskHashMap.get(value);
				if (task == null) {
					String subFileName = genSubFileName();
					task = new FileWriteTask(this.fileDir, subFileName, new ArrayList<>(), 0);
					task.addFileContent(lineContent);
					fileWriteTaskHashMap.put(value, task);
					this.subFileCounter++;
				} else {
					task.addFileContent(lineContent);
				}

			}
		}
	}
	
	/**
	 * 生成子文件的名称
	 * @return 子文件名称
	 */
	private String genSubFileName() {
		String fileName = "";
		String[] fileNameItems = this.fileNameTemplate.split(Constants.FILENAME_SEPARATOR);
		if(fileNameItems.length == 1) {
			fileName = fileNameItems[0]+this.subFileCounter;
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(fileNameItems[0]).append(this.subFileCounter)
				.append(".").append(fileNameItems[1]);
			fileName = sb.toString();
		}
		return fileName;
	}
	
	/**
	 * 生成子文件的名称
	 * @param subFileNo —— 子文件编号
	 * @return 子文件名称
	 */
	public String genSubFileFullName(int subFileNo) {
		String fileName = "";
		String[] fileNameItems = this.fileNameTemplate.split(Constants.FILENAME_SEPARATOR);
		if(fileNameItems.length == 1) {
			fileName = fileNameItems[0]+subFileNo;
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append(fileNameItems[0]).append(subFileNo)
				.append(".").append(fileNameItems[1]);
			fileName = sb.toString();
		}
		return Util.genFullFileName(this.fileDir, fileName);
	}
	
	public ForkFileWriteTask spiltForFork(String lineContent) {
		int totalSize = this.fileCacheSize + lineContent.length();
		//当前行加入后，缓存的文件内容大于上限值，则生成一个新的Task
		if(totalSize >= subFileSizeLimit) {
			this.subFileCounter++;
			String subFileName = genSubFileName();
			List<String> fileCacheCopy = new ArrayList<String>();
			fileCacheCopy.addAll(this.fileCache);
			ForkFileWriteTask fileWriteTask = new ForkFileWriteTask(this.fileDir, subFileName, fileCacheCopy, this.fileCacheSize);
			//重置文件缓存和大小
			this.fileCache.clear();
			this.fileCacheSize = 0;
			return fileWriteTask;
		} else {
			this.fileCache.add(lineContent);
			this.fileCacheSize += lineContent.length();
			return null;
		}
	}
	
	/**
	 * 检查指定文件大小是否超限
	 * @return true —— 超过
	 * 		   false —— 不超过
	 */
	public boolean overFileLimit(int size) {
		return size >= this.subFileSizeLimit;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	public int getSubFileSizeLimit() {
		return subFileSizeLimit;
	}

	public void setSubFileSizeLimit(int subFileSizeLimit) {
		this.subFileSizeLimit = subFileSizeLimit;
	}

	public String getFileNameTemplate() {
		return fileNameTemplate;
	}

	public void setFileNameTemplate(String fileNameTemplate) {
		this.fileNameTemplate = fileNameTemplate;
	}

	
}
