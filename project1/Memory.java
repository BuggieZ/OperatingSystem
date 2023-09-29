import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Runtime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TO COMPILE THIS FILE: javac memory.java
public class Memory{
    
  private static java.io.PrintWriter logger;
    public static void main(String[] args) {
        // System.out.println("in here"); 
        try {
            logger = new java.io.PrintWriter("logOfMemory.log");
            int [] values = new int [2000];
            fileRead(values);  
            logMethod("Memory created successfully");

            Scanner sc = new Scanner(System.in);
            String flagIn, adr, data = "";
            // testing returned command
            while(true)
            {
                flagIn = sc.nextLine();
                //logMethod("flagin is:1" +flagIn);
                //decide to read or write
                if (flagIn.equals("0")  )     /******read mode******/
               {
                    //flagIn = sc.nextLine(); // unused
                    adr= sc.nextLine();
                    int arrIndex = Integer.parseInt(adr);
                    logMethod("Command recieved: "+adr+"Sent CPU array at "  + adr+" is " + values[arrIndex]);        //should have 3 args
                    // send value of requested array
                    System.out.println(values[arrIndex]);

               } 

               else if (flagIn.equals("1"))     /*****write mode******/
               {
                    //flagIn = sc.nextLine(); // unused
                    adr = sc.nextLine();
                    data = sc.nextLine();
                    int arrIndex = Integer.parseInt(adr);
                    values[arrIndex] = Integer.parseInt(data);
                    logMethod("address recieved: "+adr+"Set to value " + values[arrIndex]);        //should have 3 args
                }

               else
               logMethod(" Invalid flag");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
/****************************Process Brgines********************************/
  

    public static void fileRead (int [] val) {
        String filePath = "C:\\Users\\maooz\\OneDrive\\Documents\\Fall2023\\OS_Concept\\project1\\sample1.txt";
        //String filePath = "C:\\Users\\maooz\\OneDrive\\Documents\\Fall2023\\OS_Concept\\project1\\sample2.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int number;
            int i = 0; 
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()&&  !line.trim().startsWith("//")) { // Check if the line is not empty or line onkly have   " //data"
                    String [] arr =  line.split(" ");   // split by " " and put into array
                    //isFound = line.contains(".");
                        if (!Character.isDigit(line.charAt(0))){   //check if line starts with digit
                       // if line is //
                       // if line is .
                        String[] part = line. split("\\.", 2);// if . present parse into array
                        number = Integer.parseInt(part[1]); //   number  contains adderess
                        //System.out.println(" part [1] at line is "+ part[1]);
                        // System.out.println(" part [0] at line is "+ part[0]);
                        // System.out.println("numberis "+ number);
                          //System.out.println("i is "+ i);
                            String line2 = br.readLine();
                             String [] arr2 =  line2.split(" ");    //get the next line, sepeated by space
                             int number2 = Integer.parseInt(arr2[0]);   //arr2[0] is data is part[1] address
                              val[number] = number2;    // the next integer following a decimal number goes to arr[]
                             i++;
                        }
                        else{
                        number = Integer.parseInt(arr[0]);
                        val[i] = number;  // if is regular integer, add to array
                        }
                        i++;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Print the list of numerical values
    /*    for (int j= 0; j<2000 ;j++) {
           System.out.println("Numerical value: at  " + j +"is "+ val[j] );
        } */
       // System.out.println("memeory at 1000 is " + val[1000]);
        
    }

    private static void logMethod( String s)
    {
        logger.println(s); //log lines
        logger.flush();
    }

}
            
        