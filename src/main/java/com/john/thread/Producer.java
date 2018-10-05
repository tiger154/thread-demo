package com.john.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;
import java.util.Vector;

class Producer extends Thread {
    private Logger logger = LoggerFactory.getLogger(getClass());

    static final int MAXQUEUE = 5;
    private Vector messages = new Vector(); // 동기화 대상 데이터
    private int num = 0;

    @Override
    public void run() {
        try {
            while (true) {
                putMessage();
                //sleep(1);
            }
        } catch (InterruptedException e)  {
            e.printStackTrace();
        }
    }

    private synchronized void putMessage() throws InterruptedException {
        logger.debug("[> Producer] before queue size is: " + messages.size());
        if (messages.size() == MAXQUEUE) {
            logger.debug("Producer wait start...");
            wait();
        }

        num = num+1;
        String el = "num: " + Integer.toString(num);
        messages.addElement(el);
        logger.debug("[> Producer] after queue size is: {}, processing message: {}", messages.size(), el);
        if (messages.size() == MAXQUEUE) {
            notify();
        }

        // Later, when the necessary event happens, the thread that is running it calls notify() from a block synchronized on the same object.
    }

    public synchronized String getMessage() throws InterruptedException {
        logger.debug("[< Consumer] before queue size is: " + messages.size());
        if (messages.size() == 0) {
            logger.debug("Consumer wait start...");
            wait(); // By executing wait() from a synchronized block, a thread gives up its hold on the lock and goes to sleep
        }
        String message = (String) messages.firstElement();
        messages.remove(message);
        logger.debug("[< Consumer] after queue size is: {}, processing message: {}", messages.size(), message);
        notify();
        return message;
    }
}
