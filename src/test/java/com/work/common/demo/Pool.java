package com.work.common.demo;

import java.util.concurrent.Semaphore;

class Pool {
   private static final int MAX_AVAILABLE = 100;
   private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

   public Object getItem() throws InterruptedException {
     available.acquire();
     return getNextAvailableItem();
   }

   public void putItem(Object x) {
     if (markAsUnused(x))
       available.release();
   }

   
   

   //以下为一个demo
   protected Object[] items = {"123",3,"31gsd",""};
   protected boolean[] used = new boolean[MAX_AVAILABLE];
   /**
    * 获取可用的item
    * @return
    */
   protected synchronized Object getNextAvailableItem() {
     for (int i = 0; i < MAX_AVAILABLE; ++i) {
       if (!used[i]) {
          used[i] = true;
          return items[i];
       }
     }
     return null; // not reached
   }

   /**
    * 标志该item为不可用，true表示标志成功，false表示标志失败
    */
   protected synchronized boolean markAsUnused(Object item) {
     for (int i = 0; i < MAX_AVAILABLE; ++i) {
       if (item == items[i]) {
          if (used[i]) {
            used[i] = false;
            return true;
          } else
            return false;
       }
     }
     return false;
   }
 }