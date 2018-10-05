package com.john.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Vector;
import java.lang.String;

@SpringBootApplication
@Configuration
class Application {


    public static void main(String args[]) {

/*
        //#### 1. 테스트 프로듀서 컨슈머 쓰레딩
        // 1.1 프로듀서 실행
        Producer producer = new Producer();
        producer.start();
        // 1.2 컨슈머 실행
        new Consumer(producer).start();
*/


        //#### 2. synchronized 테스트
        //SyncDemo("ObjectLevel");
        //SyncDemo("ClassLevel");
        // 모니터 인스턴스가 다를경우, 행동 체크..
        SomeLib lib = new SomeLib();
        Sender sender = new Sender(lib);


        for(int i=0; i < 5; i++) {
            String message = "Hey" + i;
          //  System.out.println(message);
            new ThreadedSend(message, new Sender(lib)).start();
        }

        new ThreadedSend("Wow1", sender).start();
         new ThreadedSend("Wow2", sender).start();
        new ThreadedSend("Wow3", sender).start();


    }
}

/**
 * 쓰레드내에 특정 싱글턴 인스턴스 사용시 동기화 체크용
 */
class SomeLib {
    private int num = 0;
    public void increaseNum() {
        num = num+1;
    }
    public int getNum() {
        return num;
    }

}

/**
 * 모니터 대상 객체
 */
class Sender {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static int countSynced = 0;

    private int num = 0;
    SomeLib lib;

    Sender() {
        lib = new SomeLib();
    }

    Sender(SomeLib someLib) {
        this.lib = someLib;
    }

    public void send(String msg) {
        logger.debug("sending: {}", msg);
        try {
            countSynced++;
            lib.increaseNum();
            num = num + 1;
            logger.debug("sleep start for message : {}, num: {}, libNum: {}, countSynced: {}", msg, num, lib.getNum(), countSynced);
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.debug("Thread interrupted");
        }
        logger.debug("sent message: {}", msg);
    }
}

/**
 * 쓰레드 실행 클래스
 */
class ThreadedSend extends Thread {
    private String msg;
    private Thread t;

    Sender sender;

    ThreadedSend(String m, Sender obj) {
        msg = m;
        sender = obj;
    }

    public void run() {
        synchronized (sender) {
            sender.send(msg);
        }
    }
}
