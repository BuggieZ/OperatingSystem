import java.util.concurrent.Semaphore;

public class Bellhop extends Main implements Runnable {
   private String message;
   private Semaphore sem;
   private int BHno;
   private int BGuestNo;
   private int bagnum;
   public int BGuestRoom;

   Bellhop(Semaphore sem, int BHno, String msg) {
      message = msg; // System.out.println(msg);
      this.BHno = BHno;
      System.out.println(message);
   }

   @Override
   public void run() {
      try {
         while (true) {
            bagChekin.acquire();
            // System.out.println("in Bellhop queue is :" + GBqueue);
            mutex2.acquire();
            BGuestNo = GBqueue.remove();
            mutex2.release();

            // check if guest num already appear in luggage object
            // if yes, go deliver
            if (Luggagelist[BGuestNo] != null) {
               enterRoom.acquire();
               delivery();
               semBH.release();
            }

            // else do below
            else {
               getbags();
               semBH.release();
            }
         }

      } catch (InterruptedException e) {
      }

   }

   public void getbags() {
      BGuestRoom = myRoaster[BGuestNo].roomR; // find roomno for BGuestNo
      bagnum = bagList[BGuestNo].bagR; // get gust's bag num
      // add object into array.
      System.out.println("Bellhop " + BHno + " receives bags from guest " + BGuestNo);
      Luggage lobj = new Luggage(BGuestNo, BGuestRoom, bagnum, BHno);
      Luggagelist[BGuestNo] = lobj; // add to array of luggage
      myrecord rec = new myrecord(BGuestNo, BGuestRoom, bagnum, BHno);
      myrecList[BGuestNo] = rec; // a dd rtto record for guest ertrieving bags
   }

   public void delivery() {
      System.out.println("Bellhop " + BHno + " delivers bags to guest " + BGuestNo);
      Luggagelist[BGuestNo] = null; // remove luuage from luggagelist
   }

}
