import javafx.scene.layout.Priority;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Tools.printTools.*;
import static java.lang.Thread.currentThread;


public class serverApp {
    static int portnumber;
    public static boolean establishedConnection=false;


    private static List<Socket> socketList = new ArrayList<>();
    private static List<PrintWriter> outputs = new ArrayList<>();

    public static void main(String[] args){
        //master loop +control
        boolean reset;
        do {
            reset = false;

            println("Welcome to the Server!\n");
            try {
                Thread.sleep(1500);
                print("Getting Port");
                for (int x = 0; x < 3; x++) {
                    Thread.sleep(300);
                    try {
                        Integer.parseInt(args[0]);
                        if (x == 2) {
                            print(".\n");
                        } else {
                            print(".");
                        }
                    } catch (Exception e) {
                        errprint(".");
                    }


                }
            } catch (Exception e) {
                errln(e.getMessage());
            }


            //Grab PortNumber or Request IT.
            boolean portError = false;
            do {
                try {
                    if (portError) {
                        portError = false;
                        portnumber = new Scanner(System.in).nextInt();
                    } else {
                        portnumber = Integer.parseInt(args[0]);
                    }
                } catch (Exception e) {
                    portError = true;
                    System.err.println("\nError with port, or is not a Number, Please Re-enter");
                }
            } while (portError);


            //ServerSocket serverSocket = null; //PART OF OLD


            try {
                //bind the port
                //Live time connection Thread
                Thread listener = new Thread(() -> {
                    int index = 0;
                    try {
                        ServerSocket listenerServer = new ServerSocket(portnumber);
                        while (Thread.currentThread().isAlive()) {
                            //setup base

                            Socket newSocket = listenerServer.accept();

                            println("\n Incoming Connection");
                            try {
                                socketList.add(newSocket);
                                Thread.sleep(2000);

                                println("Ip Connected->" + newSocket.getInetAddress());


                            } catch (Exception e) {
                                errln("Listener Error ->" + e.getLocalizedMessage());
                            }
                            //send welcome message;
                            try {
                                //create printer for new socket and store
                                PrintWriter printer = new PrintWriter(socketList.get(index).getOutputStream(), true);
                                outputs.add(printer);

                                //finally attempt to send message

                                //outputs.get(index).println("Hello, Type exit to Exit!");


                            } catch (IOException e) {
                                errln("Stream Error, IntroStreams, Thread: Listener");
                            }


                            //advance
                            index++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });


                listener.setDaemon(true);

                listener.start();


                //OLD IMPLEMENTATION
                /**
                 try {
                 serverSocket = new ServerSocket(portnumber);
                 } catch (IOException ioe) {
                 System.err.println("Port is in use, Sorry! Try Again? [y/n]");
                 boolean correct = true;
                 do {
                 String choice = new Scanner(System.in).next();
                 boolean isY = choice.equalsIgnoreCase("y");
                 boolean isN = choice.equalsIgnoreCase("n");
                 switch (isY + "-" + isN) {
                 case "true-false":
                 if (!correct) {
                 correct = true;
                 }
                 reset = true;
                 throw new Exception("Reset-Bypass");
                 case "false-true":
                 System.exit(0);
                 break;
                 default:
                 System.err.println("Invalid!");
                 correct = false;
                 }
                 } while (!correct);
                 }
                 **/
                try {


                    String address = InetAddress.getLocalHost().toString();


                    while (socketList.size() != 1) {
                        establishedConnection = false;
                        //waits until one conncts
                    }


                    establishedConnection = true;





                    //wait for other thread to kill itself


                    println("\n\nConnection Established [" + socketList.get(0).getInetAddress().toString() + "]");
                /*
                PrintWriter outToClient =
                        new PrintWriter(initialSocket.getOutputStream(), true);
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(initialSocket.getInputStream()));



                    outToClient.println("Hello! Welcome, to leave just type exit :)" + "~");
                println("\nHello! Welcome, to leave just type exit :)");
                **/

                    String message = "empty";
                //messaging handlers
                    Scanner scan = new Scanner(System.in);

                    try {
                        currentThread().sleep(3000);
                        print("Waiting");
                    } catch (Exception e) {
                        errln("Wait error");
                    }


                    //output
                while (Thread.currentThread().isAlive()) {
                    System.out.println("Number of clients " + socketList.size());

                    message = scan.nextLine();
                    //errln(message); //debug line

                    //check for exit
                    if(message.equalsIgnoreCase("exit")){System.exit(0);}
                    //send message to all users connected;
                    for (int x = 0; x < socketList.size(); x++) {
                        if (1 == 1) {




                            try {


                                PrintStream printer = new PrintStream(socketList.get(x).getOutputStream(), true);
                                printer.write(message.getBytes());


                                outputs.get(x).println(message);
                                errln(message);
                            } catch (Exception e) {
                                e.getLocalizedMessage();
                                System.exit(1);
                            }
                        }
                    }

                }



            } catch (Exception e) {

                    System.exit(1);

        }

        }catch(Exception e){

                System.exit(1);
        }

        }while(reset);
        }








}
