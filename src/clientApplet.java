import Tools.printTools;

import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import static Tools.printTools.*;

public class clientApplet {
    private static String address,userName,password;
    private static int port;
    private static InetAddress serverAddress;
    private static Socket clientSocket;
    public static void main(String[] args){
        try{
            if(args[0].equalsIgnoreCase("help")){
                println("To use this program the syntax is as follows \n" +
                        " java appName serverAddress port userName password");
                throw new Exception("Bypass");
            }

            //check all args
            try{
                for(int x=0;x<4;x++){
                    if(x==0){
                        address=args[x];
                        StringTokenizer st=new StringTokenizer(address,".");
                        if(st.countTokens()==4 && !address.contains("www"))
                        {
                            for(int token=0;token<st.countTokens();token++){
                                Integer.parseInt(st.nextToken());
                            }
                            serverAddress=InetAddress.getByName(address);

                            //test Connection
                            if(serverAddress.isReachable(5000)) {
                                println(serverAddress.toString());
                                println("Address is REACHABLE!");
                            }
                            else{
                                println(serverAddress.toString());
                                errln("Address is NOT REACHABLE!");
                        }
                        }
                        else if(address.contains("www") ||address.contains(".")){
                            serverAddress=InetAddress.getByName(address);

                            //test Connection
                            if(serverAddress.isReachable(5001)) {
                                println(serverAddress.toString());
                                println("Address is REACHABLE!");
                            }
                            else {
                                println(serverAddress.toString());
                                errln("Address is NOT REACHABLE!");

                            }
                        }
                    }

                    if(x==1){
                        port=Integer.parseInt(args[x]);
                        clientSocket=new Socket();
                        SocketAddress hostName=new InetSocketAddress(serverAddress.toString().substring(1,serverAddress.toString().length()),port);
                        println("Attempting to connect to server at->"+hostName.toString());
                        try {
                            clientSocket.connect(hostName, 5000);
                        }catch(SocketTimeoutException |ConnectException e){
                            errln("Timeout/Host Unreachable");
                        }


                        //Input handler Thread
                        Thread conversationEngine=new Thread(()->{
                            try {
                                InputStream inbox=clientSocket.getInputStream();
                                List<Character> rawMessage = new ArrayList<>();
                                StringBuilder incomingMessage=new StringBuilder();
                                boolean isContent = true;

                                InputStreamReader reader = new InputStreamReader(inbox);
                                while (1 == 1) {

                                    while (inbox.available() != -1) {
                                        System.out.print((char) reader.read());
                                    }
                                    println("\n");


                                    //errln("Content Received");

                                }



                            }catch(IOException ioe){
                                errprint(ioe.getMessage());
                                printStack(ioe);
                            }
                        });
                        conversationEngine.setDaemon(true);
                        conversationEngine.start();
                        try{
                            Thread.sleep(2000);
                            println("\nConnection Established...\n");
                        }catch(Exception e){errprint("Sleep Error");};





                        //messaging handlers
                        String message;
                        //output
                        while (Thread.currentThread().isAlive()) {
                            message = new Scanner(System.in).next();
                            //errln(message); //debug line

                            //check for exit
                            if(message.equalsIgnoreCase("exit")){System.exit(0);}
                            //send message;
                            clientSocket.getOutputStream().write(message.getBytes());

                        }


                    }
                }







            }catch(Exception e){
                errln("Seems Like you've incorrectly done your Arguments, for help, just execute java appName help...\n ");

                printStack(e);
            }
        }catch(Exception e){
            e.getCause();
        }
    }


}
