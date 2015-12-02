package com.aimeizhuyi.users.analysis.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangyuelin on 15/7/9.
 */
public class ThreadPool {


        private static ExecutorService pool ;

        public static void submit(Runnable r){
            if(pool==null){
                pool = Executors.newSingleThreadExecutor();
            }
            pool.submit(r);
        };

}
