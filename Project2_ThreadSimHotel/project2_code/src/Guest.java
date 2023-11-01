import java.util.concurrent.Semaphore;

public class Guest extends Main implements Runnable {
   private String message;
   public int custnr;
   public int[] keychain;
   // private static Semaphore sem = new Semaphore( 0, true );
   public int frontID;
   public int roomNo;

   private int bagNum;

   Guest(String msg, int index, int bagNum) {
      message = msg; // System.out.println(msg);
      custnr = index; // initizalize customer index
      this.bagNum = bagNum;
      bag bobj = new bag(bagNum);
      System.out.println(message);
      bagList[custnr] = bobj; // add "bagNum" and "custnr" to array of bag
      guestSem[custnr] = new Semaphore(0, false);
      waitFrontDesk[custnr] = new Semaphore(0, false);
   }

   @Override
   public void run() {
      try {
            semFD.acquire();
            enterHotel(); // guest enetrs hotel
            custReady.release();
            //Give bag to bellhop
            if (bagNum > 2) // else bagNum < 2, proceed to room
            {
               
               semBH.acquire(); // find next free Bellhop
               waitFrontDesk[custnr].acquire();   //wait until front desk get to guest
               requestHelp();
               bagChekin.release(); // signal Bellhop that guest need help checkbags

            } 

            //System.out.println(" waiting on key_rec semaphore on guest " + custnr);
            guestSem[custnr].acquire();
            //System.out.println("after release of guestSem");
            getkey();
            enterRoom();
            enterRoom.release(); // signal bellhop to deliver bags
            readyexit.release();
            if (Luggagelist[custnr] != null) {
               semBH.acquire(); // wait for a free Bellhop
               checkout(); // enqueue GBqueue
               bagChekin.release(); // call bellhop AGAIn
               System.out.println("Guest " + custnr + " receives bags from bellhop " + myrecList[custnr].rBHno + " and gives tip");
            }
            //readyexit.acquire();
            exit();
            return;

         }
       catch (InterruptedException e) {
         }
   
      }
   public void enterHotel() {
      try {
         mutex1.acquire();
         GFqueue.add(custnr);
         mutex1.release(); // GFqueue mutex
         System.out.println("Guest " + custnr + " enters hotel with " + bagNum + " bags");
         // System.out.println("Guest "+custnr+" receives key from " ???bellhop);
      } catch (InterruptedException e) {
         System.out.println("interectption ");
      }

   }

   public void requestHelp() {
      System.out.println("Guest " + custnr + " request help with bags ");
      try {
         mutex2.acquire();
         GBqueue.add(custnr);
         mutex2.release();
         // enqueue custnr
      } catch (InterruptedException e) {

      }

   }

   public void getkey() {

      roomNo = myRoaster[custnr].roomR; // ISSUE:
      // System.out.println("roomNo: "+roomNo);
      frontID = myRoaster[custnr].frontR;
      System.out.println("Guest " + custnr + " receives room key for room " + roomNo + " from front desk employee " + frontID);
      
   }

   public void enterRoom() {

      System.out.println("Guest " + custnr + " enters room " + roomNo);
   }

   public void checkout() {
      try {
         mutex2.acquire();
         GBqueue.add(custnr);
         mutex2.release();
      } catch (InterruptedException e) {
      }
   }

   public void exit() {
      //System.out.println("here in exit");
      System.out.println("Guest " + custnr + " retires for the evening ");
   }

}
