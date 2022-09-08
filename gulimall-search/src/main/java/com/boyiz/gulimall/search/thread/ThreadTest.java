package com.boyiz.gulimall.search.thread;

import java.util.concurrent.*;

public class ThreadTest {

    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // System.out.println("main......start.....");
        // Thread thread = new Thread01();
        // thread.start();
        // System.out.println("main......end.....");

        // Runable01 runable01 = new Runable01();
        // new Thread(runable01).start();

        // FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
        // new Thread(futureTask).start();
        // System.out.println(futureTask.get());

        // service.execute(new Runable01());
        // Future<Integer> submit = service.submit(new Callable01());
        // submit.get();

        System.out.println("main......start.....");
        // CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        //     System.out.println("当前线程：" + Thread.currentThread().getId());
        //     int i = 10 / 2;
        //     System.out.println("运行结果：" + i);
        // }, executor);

        /**
         * 方法完成后的感知
         */
//         CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//             System.out.println("当前线程：" + Thread.currentThread().getId());
//             int i = 10 / 0;
//             System.out.println("运行结果：" + i);
//             return i;
//         }, executor).whenComplete((res,exception) -> {
//             //虽然能得到异常信息，但是没法修改返回数据
//             System.out.println("异步任务成功完成了...结果是：" + res + "异常是：" + exception);
//         }).exceptionally(throwable -> {
//             //可以感知异常，同时返回默认值
//             return 10;
//         });

        /**
         * 方法执行完后的处理
         */
        // CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        //     System.out.println("当前线程：" + Thread.currentThread().getId());
        //     int i = 10 / 2;
        //     System.out.println("运行结果：" + i);
        //     return i;
        // }, executor).handle((result,thr) -> {
        //     if (result != null) {
        //         return result * 2;
        //     }
        //     if (thr != null) {
        //         System.out.println("异步任务成功完成了...结果是：" + result + "异常是：" + thr);
        //         return 0;
        //     }
        //     return 0;
        // });


        /**
         * 线程串行化
         * 1、thenRun：不能获取上一步的执行结果
         * 2、thenAcceptAsync：能接收上一步结果，无返回值
         * 3、thenApplyAsync：能接收上一步结果，又有返回值
         *
         */
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果：" + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            System.out.println("任务2启动了..." + res);
//            return "Hello" + res;
//        }, executor);
//        System.out.println("main......end....." + future.get());
        /**
         * 两个都完成再执行第三个
         */
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("plan1：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("plan1_over：" + i);
//            return i;
//        }, executor);
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("plan2：" + Thread.currentThread().getId());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            int i = 10 / 2;
//            System.out.println("plan2_over：" + i);
//            return "hello " + i;
//        }, executor);

//        //组合plan1和plan2，完成plan1和plan2之后开始第三个任务，无法感知结果
//        future01.runAfterBothAsync(future02, () -> {
//            System.out.println("plan3_start");
//        }, executor);
//
//        //可感知结果，    void accept(T t, U u);
//        future01.thenAcceptBothAsync(future02, (f1, f2) -> {
//            System.out.println("plan3_start");
//            System.out.println("plan1 result：" + f1);
//            System.out.println("plan2 result：" + f2);
//        }, executor);
//
//        //再进阶，合并多个任务，可指定返回值
//        //        R apply(T t, U u);
//        CompletableFuture<String> future03 = future01.thenCombineAsync(future02, (f1, f2) -> {
//            System.out.println("plan3_start");
//            return "result：" + f1 + "--> " + f2;
//        }, executor);
//        System.out.println(future03.get());

        /**
         * 任意一个future完成时，执行任务3
         */
//        //不感知结果,无返回值
//        future01.runAfterEitherAsync(future02, () -> {
//            System.out.println("plan3_start");
//            //future2设置Thread.sleep()测试效果。
//        },executor);
//        //感知结果,无返回值
//        future01.acceptEitherAsync(future02, (res) -> {
//            System.out.println("plan3_start");
//            //future2设置Thread.sleep()测试效果。
//            System.out.println("res:"+ res);
//        },executor);
//        //感知结果,也有返回值
//        CompletableFuture<String> future03 = future01.applyToEitherAsync(future02, (res) -> {
//            System.out.println("plan3_start");
//            //future2设置Thread.sleep()测试效果。
//            return res + "  plan3";
//        }, executor);
//        System.out.println(future03.get());
        /**
         * 多任务组合
         */
        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(()->{
            System.out.println("查询图片信息");
            return "hello.jpg";
        },executor);
        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("查询属性信息");
            return "黑色 128G";
        },executor);
        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(()->{
            System.out.println("查询介绍信息");
            return "Huawei";
        },executor);
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
        allOf.get();    //等待所有结果完成
        System.out.println("结束。。。");
        System.out.println(futureImg.get());
        System.out.println(futureAttr.get());
        System.out.println(futureDesc.get());

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get();    //只要有一个完成
        System.out.println(anyOf.get());

    }





    private static void threadPool() {

        ExecutorService threadPool = new ThreadPoolExecutor(
                200,
                10,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(10000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        //定时任务的线程池
        ExecutorService service = Executors.newScheduledThreadPool(2);
    }


    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }


    public static class Runable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
        }
    }


    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果：" + i);
            return i;
        }
    }

}
