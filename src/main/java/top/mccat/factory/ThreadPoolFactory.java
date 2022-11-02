package top.mccat.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Raven
 * @date 2022/09/25 18:41
 */
public class ThreadPoolFactory {
    private final int cpuCores;
    private ThreadFactory threadFactory;
    public ThreadPoolFactory(){
        cpuCores = Runtime.getRuntime().availableProcessors();
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("strength-plus-pool-%d").build();
    }

    /**
     * 获取线程池对象，遵循 Nthreads=Ncpu（CPU核心数）*Ucpu（CPU使用率）*(1+w/c)（等待时间除以计算时间） 公式
     * 那么理论上，对于ui的多线程应当为 动态获取cpu核心数方法*CPU使用率（默认分配50%）*（1 + 5000 毫秒/ 理论计算时间：200*7（约等于1500）（进度条方格）+ 200*5）
     * 即 假设当前cpu 4核8线程 即 8*0.5+5000/2500 = 8 ，即开设线程数设置为该cpu核心的一倍，不考虑单核cpu问题，最大线程池即线程数数+1
     * @return 线程池对象
     */
    public static ThreadPoolExecutor getThreadPool(){
//        通常来说，线程池的大小建议为cpu核心+1
        int cpuThread = Runtime.getRuntime().availableProcessors()*2;
//        创建线程的工厂类
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("strength-plus-pool-%d").build();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(cpuThread,cpuThread+1,5L,
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(3),factory,new ThreadPoolExecutor.DiscardOldestPolicy());
//        允许自动回收
        threadPool.allowCoreThreadTimeOut(true);
        return threadPool;
    }

    /**
     * 返回线程池对象
     * @param corePoolSize 子线程素含量
     * @param keepAliveTime 回收时间
     * @return 线程池对象
     */
    public ThreadPoolExecutor getThreadPool(int corePoolSize, int keepAliveTime){
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,cpuCores+1,keepAliveTime,
                TimeUnit.SECONDS,new LinkedBlockingDeque<>(3),threadFactory,new ThreadPoolExecutor.DiscardOldestPolicy());
        threadPool.allowCoreThreadTimeOut(true);
        return threadPool;
    }
}
