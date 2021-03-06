package threadpool;


import threadpool.dto.DownloadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池 执行器
 *     线程创建和任务数量之间的关系：
 *     任务数量小于核心线程数，直接启动线程执行任务。
 *     超过核心线程线程数之后进行任务排队
 *     排队完成，队列满了（无界队列，永远不会满）。开启线程执行直到最大线程数
 * 多线程执行器：
 *    对于耗时比较长任务比较少的多任务，采用生成线程数量多一点
 *     CPU密集型时，任务可以少配置线程数，大概和机器的cpu核数相当，这样可以使得每个线程都在执行任务
 *    IO密集型时，大部分线程都阻塞，故需要多配置线程数，2*cpu核数
 *    https://www.cnblogs.com/ming-blogs/p/10897242.html
 *    对于耗时短，高并发任务线程数量设置
 *    高并发，耗时任务短的线程池的数量需要少一点，避免并发导致线程切换
 *    https://www.cnblogs.com/dangjunhui/p/5481435.html
 */
public class TestThreadPoolExecutor {
    //处理任务的行数
    private static int rowCount = 50;
    //处理任务的列数
    private static int levelCount = 10;
    //线程处理队列 是一个链表阻塞队列
    private static LinkedBlockingDeque linkedBlockingDeque = new LinkedBlockingDeque();
    //启动线程池进行处理
    //默认的拒绝策略是超过之后进行抛弃
    // AbortPolicy:抛弃并且抛出异常
    //DiscardPolicy：直接抛弃不抛出异常
    //DiscardOldestPolicy：抛弃等待最久的一个任务
    //CallerRunsPolicy:重试增加到当前任务，知道增加成功
   //BlockingDeque：阻塞队列
    //有边界无边界
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,
            20,
            20,
            TimeUnit.SECONDS, linkedBlockingDeque);
    //线程处理结
    private List<Future<String>> results = new LinkedList<>();
    //最大允许排队数是50
    private int maxQue = 50;
    //单个线程一次处理20个切片
    private int countSimple = 20;
    //子任务
    private List<Integer> needSub = new ArrayList<>(countSimple);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        TestThreadPoolExecutor testThreadPoolExecutor = new TestThreadPoolExecutor();
        for (int level = 0; level < levelCount; level++) {
            for (int row = 0; row < rowCount; row++) {
                if (row < rowCount - 1) {
                    testThreadPoolExecutor.addTask(false, false,level + " row " + row);
                } else {
                    if (level != levelCount - 1) {
                        testThreadPoolExecutor.addTask(false, true,level + " row " + row);
                    } else {
                        testThreadPoolExecutor.addTask(true, true,level + " row " + row);
                    }
                }
            }
        }
        //等待线程执行完成 继续完成其他工作。如果规定时间内没有完成任务将会结束
        threadPoolExecutor.awaitTermination(100,TimeUnit.DAYS);
    }

    /**
     * 任务中添加队列
     * @param allEnd 是否是全部都结束
     * @param tile  需要处理的数据
     * @param levelEnd  是否是某个级别结束
     * @throws ExecutionException 执行过程异常
     * @throws InterruptedException 中断异常
     */
    private void addTask(Boolean allEnd,Boolean levelEnd, String tile) throws ExecutionException, InterruptedException {
        needSub.add(30);
        if (levelEnd || needSub.size() >= countSimple - 1) {
            //进行提交任务
            submit(allEnd, tile, needSub);
            needSub = new ArrayList<>(countSimple);
        }
    }

    /**
     * 处理任务
     * @param allEnd
     * @param level
     * @param task
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void submit(Boolean allEnd, String level, List<Integer> task) throws ExecutionException, InterruptedException {
        //如果队列比较长了 进行循环
        results.add(threadPoolExecutor.submit(new DownloadTask(level, task)));
        if (allEnd) {
            threadPoolExecutor.shutdown();
            getResult(true);
        } else {
            while (linkedBlockingDeque.size() > maxQue) {
                getResult(false);
            }
        }
    }

    /**
     * 获取结果
     * @param isAllEnd
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void getResult(Boolean isAllEnd) throws ExecutionException, InterruptedException {
        Iterator<Future<String>> resultIter = results.iterator();
        while (resultIter.hasNext()) {
            try{
                //get操作会处于阻塞状态
                System.out.println(resultIter.next().get());
            }catch (Exception ex){
                 //输出异常
            }finally {
                //操作完成从结果集合中移出掉 不管有没有异常 都有返回
                resultIter.remove();
            }
            if (!isAllEnd) {
                if (linkedBlockingDeque.size() < maxQue) {
                    break;
                }
            }
        }
    }
}
