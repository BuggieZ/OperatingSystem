import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;


public class CPU
{
    public static PrintWriter pw ;
    public static Scanner sc;
    public static int PC,SP,IR,AC,X,Y; 
    public static int flag = 0;    //set mode for Memory
    public static int temp;

    public static void main( String [] args ){
      
    //It will have these registers:  PC, SP, IR, AC, X, Y referencing to Memory 
       
         SP = 1000;
      //iniytialize stack ptr to bottom of stack
        
        try
        {            
            Runtime rt = Runtime.getRuntime();


            Process proc = rt.exec("java Memory");

            InputStream is = proc.getInputStream();
            OutputStream os = proc.getOutputStream();
            pw = new PrintWriter(os);
            sc = new Scanner(is);
            // send eacah array to cpu

          // while(values.hasNext()) {
           
              //  PC initally is 0, sebnd PC -> Mem, Mem -> arr[PC] 
              PC = 0;
              while(flag == 0)
             {
                IR= requestData(PC);    //get next data
                PC++;                   // incre PC
                execute(IR);               // execute IR
             }
               //pw.printf("Exit\n");
               // pw.flush(); 


            //proc.waitFor();

            //int exitVal = proc.exitValue();

            //System.out.println("Process exited: " + exitVal);

        } 
        catch (Throwable t)
        {
        t.printStackTrace();
        }  
           
    }

   
     //  read
     public static int requestData(int location)
    {            
         pw.println(Integer.toString(flag));   //flag initialzed to be 0
         pw.flush();
         pw.println(Integer.toString(location)); // ********CHANGE ********test out sending value[1] datat to memory for process
         pw.flush();
        // read  from CPU io
        String receivdata = sc.nextLine();  
        //System.out.println(" CPU Read back " + receivdata);
        return Integer.valueOf(receivdata);
    }

    public static void writeData(int flag, int location, int data)
    {
        //pw.write(flag);         //Memory need to check flag first 
        pw.println(Integer.toString(flag));       // send memory data
        pw.println(Integer.toString(location));
        pw.println(Integer.toString(data));
       // send memory location
        pw.flush();
    }

    public static void push(int d){
        SP-=1;
        flag = 1;
        writeData(1, SP, d);     // pushing pc to stack 
        flag = 0;       //reset flag
      // System.out.println(" pushing data: " + d +" to stack at address " + SP);
        
    }
    public static int pop(int d){
        temp = requestData(d);          // read from top of the stack
        flag = 1;   //writeData (1, data, location)
        writeData(1, SP,0 );     // Pop
        flag = 0;    //reset flag
        SP+=1; 
        //System.out.println("Poping data at"+ d +" data is  " + temp + " SP points to "+SP);
        return temp;
    }


    public static void execute( int insReg)
    {
         switch(insReg){

            //--Load the value into the AC
            case 1: 
            AC = requestData(PC); //--fetch next value from Mem
            PC++;   //skip a line
            break;

           /*case 3: //Load the value from the address found in the given address into the AC
            IR = requestData(insReg);   // fetch the 
            AC = IR;
            break;, */ 

            case 4:     // LoadIdxX 32 (load from A-Z table) +X to AC
            int index = requestData(PC);    // at address 32, index is 65
           /* if (PC == 5)    // at this point when PC = 5, increment 1 to skip the line as input?
            {
                PC++;
            }*/ 
            PC = 4;
            AC = requestData(index + X);
           // System.out.println("index is "+ index);
           // System.out.println("AC: "+ AC);;
            PC++;   //skip next line
            break;
            //System.out.println("index is "+index);

           
            case 5:     //Load the value at (address+Y) into the AC
            int index5 = requestData(PC);
            AC = requestData(index5 + Y);
            PC++;   // skip a line of argument
            break;

            
            case 6:     //Load from (Sp+X) into the AC (if SP is 990, and X is 1, load from 991).
            AC = requestData(SP +X); // load from memory to AC
            //System.out.println("AC: " +AC);
           // System.out.println(requestData(999));
           // System.out.println(requestData(998));
           // System.out.println(requestData(997));
            //System.out.println(requestData(996));
            break;

           
            case 9:
            /*If port=1, writes AC as an int to the screen
            If port=2, writes AC as a char to the screen */
                int port = requestData(PC);
                PC++;   // skip a line
                if ( port == 1){
                    System.out.print(AC);
                }
                    
                else if ( port == 2){
                    char ch = (char)AC;
                    System.out.print(ch);
                }
                    
                else
                System.out.println("error reading port ");
            break;

            case 11:    //Add the value in Y to the AC
                AC = AC + Y;
            break;

            case 14:    //Copy the value in the AC to X
                X = AC;
            break;

            case 15:    //Copy the value in X to the AC
            AC = X;
            break;

            case 16:    //Copy the value in the AC to Y
                Y = AC;
            break;

            case 20:    //Jump to the address
               // System.out.println("  requestData(PC): "+  requestData(PC));
                PC = requestData(PC);  // PC = requestData (11) = 4
            break;

            case 21:    //Jump to the address only if the value in the AC is zero
                if (AC == 0){   // if AC = 0, go get next inetrcution and set PC
                    PC = requestData(PC);       // offset for the correct location  requestData(12+1)
                }
               else
                    PC++;   //else , only increment PC to skip line
            break;

            case 22:   //Jump to the address only if the value in the AC is not zero
                temp = requestData(PC);     //get next val 

                if(AC!=0)
                {  
                    PC = temp;   // jump to the addr
                }
                else
                {
                    PC++;       // PC++    
  
                } 
            break;

            case 23:         //Push return address onto stack, jump to the address
            int dat = requestData(PC);  //15 , 30, 51,86....163
            push(PC); 
            //flag = 0;         // reset flag for read and write
            PC = dat;       // jump to data received , first one 
            break;

            case 24:    //Pop return address from the stack, jump to the address
            temp = pop(SP);
            PC = temp;      //jump to address
           // System.out.println("PC points to "+PC);
            PC+=1;
            break;

            case 25:
            X+=1;
            break;

            case 26:    //Decrement the value in X
            X-=1;
            break;


            case 27:    //Push AC onto stack
            push(AC);
            //System.out.println("Pushed AC "+ AC + "to stack");
            break;

            case 28:// Pop from stack into AC, read vau then increment SP
            AC = pop(SP);
            break;

            case 50:    //End execution, TODO:send msg to CPU to exit
            System.exit(0);
            break;

            default:
            System.out.println(" Missing IR: "+insReg );
            break;
        }
        //System.out.println("in cpu");
    }
   
        
       

        //Proceses
       /*  process theProcess = null;
        BufferedReader inStream = null;
 
        System.out.println("programcaller.main() invoked");
        */

       // PC = memory [0];
       // IR = instruction;
        //operand = file read 

/******************Logic Session************** */

       
        }
    

        

