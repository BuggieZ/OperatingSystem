import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
  // public static Semaphore sem = new Semaphore( 0, true );
  public static Queue<Integer> GFqueue = new LinkedList<Integer>(); // queue from guest -> frontdesk
  public static Queue<Integer> GBqueue = new LinkedList<Integer>(); // queue guest -> bellhop
  public static Semaphore semFD = new Semaphore(2, true); // 2 front desk
  public static Semaphore semBH = new Semaphore(2, true); // 2 hellhops
  public static Semaphore mutex1 = new Semaphore(1, true);
  public static Semaphore mutex2 = new Semaphore(1, true);
  public static Semaphore custReady = new Semaphore(0, true);
  public static Semaphore bagChekin = new Semaphore(0, true);
  public static Semaphore enterRoom = new Semaphore(0, true);
  public static Semaphore readyexit= new Semaphore(0, true);
  public static Semaphore[] guestSem = new Semaphore[25];

    public static Semaphore [] waitFrontDesk = new Semaphore[25];
  // public static Semaphore
  // pass semaphore to class to use them y reference
  public static int nextRoom = 0;
  public static roaster[] myRoaster = new roaster[25];
  public static Luggage[] Luggagelist = new Luggage[25];
  public static myrecord[] myrecList = new myrecord[25];
  public static bag[] bagList = new bag[25]; // store bags

   public static Guest gobj[] = new Guest[25]; // init objects
   public static frontDesk fobj[] = new frontDesk[2];
   public static Bellhop bobj[] = new Bellhop[2];
   public static Thread gThread[] = new Thread[25]; // init thread
   public static Thread fThread[] = new Thread[2];
   public static Thread bThread[] = new Thread[2];
   public static String Gmsg;
   public static String Fmsg;
   public static String Bmsg;
   public static Random random;
  public static void main(String args[]) {

    /*
     * String msg1 ="msg1, ";
     * String msg2= "+ msg2";
     */

    // invoke 25 thread of guest
   
    random = new Random();
    
    threadInit();
    threadjoin();
    System.out.println("simulation ends");
    return;
  }

  public static void threadInit() {

    for (int i = 0; i < 25; i++) // testng with only 5 guest
    {
    
      int bagNum = random.nextInt(5) + 1;
      // System.out.println("random number is " + bagNum);
      Gmsg = "Guest " + i + " created";
      // invoke Guest obj with
      gobj[i] = new Guest(Gmsg, i, bagNum); // create 25 thread pass object
      gThread[i] = new Thread(gobj[i]);
      gThread[i].start();
    }
    // init frontdesk
    for (int j = 0; j < 2; j++) {
      Fmsg = "Front desk " + j + " created";
      fobj[j] = new frontDesk(semFD, j, Fmsg);
      fThread[j] = new Thread(fobj[j]);
      fThread[j].setDaemon(true);
      fThread[j].start();

    }

    for (int k = 0; k < 2; k++) {
      Bmsg = "Bellhop " + k + " created";
      bobj[k] = new Bellhop(semBH, k, Bmsg);
      bThread[k] = new Thread(bobj[k]);
      bThread[k].setDaemon(true);
      bThread[k].start();

    }
  }

  public static void threadjoin() {
    for (int i = 0; i < 25; i++) // testng with only 5 guest
    {
      try {
        gThread[i].join();
        System.out.println("Guest " + i + " joined");
      }
      // System.out.println("successfully joined guest Thread");

      catch (InterruptedException e) {
        System.out.print("error joining gthreads");
      }
    }
  }



  public class roaster {
    int custR, roomR, frontR;

    roaster(int custnr, int roomNo, int frontID) {
      custR = custnr;
      roomR = roomNo;
      frontR = frontID;
      //System.out.println(" new roaster obj : " + custR + " " + roomR + " " + frontR);
      // System.out.println("here");
    }

  }

  public class Luggage {
    int custR, roomR, bagR, BHno;

    Luggage(int custnr, int roomNo, int bagr, int BHno) {
      this.custR = custnr;
      this.roomR = roomNo;
      this.bagR = bagr;
      this.BHno = BHno;
    }
  }

  public class myrecord {
    int rcustR, rroomR, rbagR, rBHno;

    myrecord(int custnr, int roomNo, int bagr, int BHno) {
      this.rcustR = custnr;
      this.rroomR = roomNo;
      this.rbagR = bagr;
      this.rBHno = BHno;
    }
  }

  public class bag {
    int bagR;

    bag(int bg) {
      this.bagR = bg;
    }
  }
}