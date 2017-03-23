import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by demarcus-joachim on 3/23/2017.
 */
public class serverApp {
    static int portnumber;



    public static void main(String[] args){
        //master loop +control
        boolean reset;
        do{
            reset=false;

            println("we")


        //Grab PortNumber or Request IT.
        boolean portError=false;
        do {
            try {
                if(portError){
                    portError=false;
                    portnumber=new Scanner(System.in).nextInt();
                }
                else{
                    portnumber = Integer.parseInt(args[0]);
                }
            }catch(Exception e){
                portError=true;
                System.err.println("Error with port, or is not a Number, Please Re-enter");
            }
        }while(portError);

        try {
            ServerSocket serverSocket = new ServerSocket(portnumber);
        }catch(IOException ioe) {
            System.err.println("Port is in use, Sorry! Try Again? [y/n]");
            boolean correct = true;
            do {
                String choice = new Scanner(System.in).next();
                boolean isY = choice.equalsIgnoreCase("y");
                boolean isN = choice.equalsIgnoreCase("n");
                switch (isY + "-" + isN) {
                    case "true-false":
                        if(!correct) {
                            correct = true;
                        }
                        reset = true;
                        break;
                    case "false-true":
                        System.exit(0);
                        break;
                    default:
                        System.err.println("Invalid!");
                        correct=false;
                }
            } while (!correct);
        }
        }while(reset);
        }








}
