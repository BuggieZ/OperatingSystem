import java.util.concurrent.Semaphore;

public class frontDesk extends Main implements Runnable{
   private String message;
   private int frontGuest;
   private int frontID;
   
    

    frontDesk( Semaphore sem, int j, String msg )
    {
      //frontID = sem; //??????????
      message = msg; //System.out.println(msg);
      System.out.println(message);  //front desk creation
      frontID = j;
    }
 
   
 @Override
    public void run()
    {

      try{
                  while (true)
            {
               custReady.acquire();
               mutex1.acquire();
               //System.out.println("peek: " +  GFqueue.peek());
                 
               //System.out.println("in frontdesk  queue is :"+ GFqueue);
               frontGuest = GFqueue.remove();
               //System.out.println("front guest: "+ frontGuest);
               roaster obj = new roaster(frontGuest, nextRoom, frontID);
               System.out.println("Front desk employee "+ frontID+" registers guest "+frontGuest+" and assigns room " + nextRoom);   //post incre nextroom
               ///System.out.println("obj created, adding to array ");
               myRoaster[frontGuest] = obj;
               nextRoom++;
               //System.out.println("myRoaster: "+myRoaster[frontGuest]
                mutex1.release();
                //System.out.println("before release of guestSem");
               guestSem[frontGuest].release();
               semFD.release();
               waitFrontDesk[frontGuest].release();   // signal bellhop guest was checked in
            // frontID++;  //increment frontdesk id      } 
         
         
            }   

         } catch (InterruptedException e)
         {
         }
   
      
   }
}