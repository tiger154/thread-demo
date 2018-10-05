package com.john.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Consumer extends Thread {
    private Logger logger = LoggerFactory.getLogger(getClass());

    Producer producer;


    Consumer(Producer p) {
        producer = p;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = producer.getMessage();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}