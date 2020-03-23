package com.daoqidlv.filespilt;


public class FileSplit {

    /**
     * 参数1：源文件所在目录
     * 参数2：原文件名
     * 参数3：执行模式：NORMAL -- 使用普通线程池，FORKJOIN -- 使用ForkJoinPool， PRODUCERCONSUMER --生产者-消费者模式；DISRUPTOR -- 生产者-消费者模式+Disruptor
     * 参数4：分隔符
     * 参数5：拆分关键字位置
     * 参数6 7  8 ... ：拆分关键字具体值
     *
     * @param args
     *
     */
    public static void fileSplitByKeyword(String[] args) {

        //获取参数
        if(args != null) {
            if (args.length >= 6) {

            } else {
                System.err.println("参数小于6个，示例：#fileDir, #fileName, #mode, #splitSymbol, #keywordLocation, [#value1, #value2, #value3, #value4]");
                return;
            }
        } else {
            System.err.println("参数为空，示例：#fileDir, #fileName, #subFileSizeLimit, #mode，");
            return;
        }

        String fileDir = args[0];
        String fileName = args[1];
        String mode = args[2];
        String splitSymbol = args[3];
        String keywordLocation = args[4];
        String[] splitValues = Util.removeStrs(args, 5);


        //参数合法性校验
        if(Util.isNull(fileDir, fileName, mode, splitSymbol, keywordLocation, splitValues)) {
            System.err.println("部分参数为空！示例：#fileDir, #fileName, #mode, #splitSymbol, #keywordLocation, [#value1, #value2, #value3, #value4]");
            return;
        }

        //文件名校验
        String[] fileNameItems = fileName.split(Constants.FILENAME_SEPARATOR);
        if(fileNameItems.length != 2) {
            System.err.println("参数fileName格式错误！示例：fileName.csv");
            return;
        }

        //校验拆分关键字值
        int splitKeywordLocation = 0;
        if ((keywordLocation.matches("[0-9]+"))&&(Integer.parseInt(keywordLocation)>=0)) {
            splitKeywordLocation = Integer.parseInt(keywordLocation);
        } else {
            System.err.println("拆分关键字值不正确！示例：#fileDir, #fileName, #mode, #splitSymbol, #keywordLocation, [#value1, #value2, #value3, #value4]");
            return;
        }


        if((mode != null &&
                !mode.equals(Constants.MASTER_TYPE_FORK_JOIN_POOL)
                && (!mode.equals(Constants.MASTER_TYPE_NORMAL_POOL))
                && (!mode.equals(Constants.MASTER_TYPE_PRODUCER_CONSUMER))
                && (!mode.equals(Constants.MASTER_TYPE_DISRUPTOR)))) {
            System.err.println("参数mode必须是'NORMAL' 或者 'FORKJOIN' 或者'PRODUCERCONSUMER'");
            return;
        }

        //通过工厂方法获取master实例
        Master master = Master.getMasterInstanceByKeyword(mode, fileDir, fileName, splitSymbol, splitKeywordLocation, splitValues);
        System.out.println("The master is: "+master.getClass().getName());
        master.init();
        //启动master
        //master.excute();
        master.excuteByKeyword();
    }
}
