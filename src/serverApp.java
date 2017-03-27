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

    private static boolean hasDisconnect = false;
    private static boolean isInitial = true;


    public static ServerSocket listenerServer;

    private static List<Socket> socketList = new ArrayList<>();
    private static List<PrintWriter> outputs = new ArrayList<>();
    private static List<DataInputStream> inputs = new ArrayList<>();
    private static List<String> users = new ArrayList<>();

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

                println("\nServer open! ");

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





            try {

                //Live time Monitor
                Thread listener = new Thread(() -> {
                    int index = 0;
                    try {
                        listenerServer = new ServerSocket(portnumber);

                        while (Thread.currentThread().isAlive()) {
                            //setup base

                            Socket newSocket = listenerServer.accept();


                            if (hasDisconnect) {
                                hasDisconnect = false;//re-enable;
                            }

                            println("\n Incoming Connection");
                            try {
                                socketList.add(newSocket);


                                println("Ip Connected->" + newSocket.getInetAddress());
                                Thread.sleep(2000);


                            } catch (Exception e) {
                                errln("Listener Error ->" + e.getLocalizedMessage());
                            }
                            //send welcome message;
                            try {

                                //for Index sync reasons, interrupt ifConnection manger is still cleaning up
                                if (hasDisconnect) {
                                    while (hasDisconnect) {
                                        try {
                                            Thread.sleep(100);
                                        } catch (Exception e) {
                                            printStack(e);
                                        }
                                    }
                                }


                                //create printer/reader for new socket and store
                                PrintWriter printer = new PrintWriter(socketList.get(socketList.size() - 1).getOutputStream(), true);
                                outputs.add(printer);


                                DataInputStream inputReader = new DataInputStream(socketList.get(index).getInputStream());

                                inputs.add(inputReader);



                                //finally attempt to send message

                                outputs.get(outputs.size() - 1).println("Hello, Type exit to Exit!");


                            } catch (Exception e) {
                                errln("Stream Error, IntroStreams, Thread: Listener");
                            }


                            //advance
                            index++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });


                //Input handler Thread
                Thread conversationEngine = new Thread(() -> {
                    while (Thread.currentThread().isAlive()) {

                        if (hasDisconnect || isInitial) {  //is halt?
                            continue;
                        }
                        while (Thread.currentThread().isAlive()) {

                            try {
                                print(inputs.get(0).readUTF() + " <-Ready? ");
                                Thread.sleep(25);
                                //print(clientSocket.getInputStream().available() + " ");
                            } catch (Exception e) {
                                errprint(e.getMessage());
                                System.exit(0);
                            }
                        }
                    }


                });
                //end reader thread


                try {
                    Thread.sleep(2000);

                } catch (Exception e) {
                    errprint("Sleep Error");
                }
                ;

                Thread connectionManager = new Thread(() -> {

                    while (Thread.currentThread().isAlive()) {
                        int index = 0;


                        while (!hasDisconnect && !isInitial) {

                            for (int x = 0; x < socketList.size(); x++) {

                                try {


                                } catch (Exception e) {
                                    hasDisconnect = true;
                                    index = x;
                                    break;

                                }

                            }
                        }


                        if (hasDisconnect) {
                            println("Detected Leave of socket-> " + index);

                            //remove


                            outputs.remove(index);
                            socketList.remove(index);
                            if (socketList.size() == 0) {
                                errln("\n There Are No more Connected People, Going back into Listening Mode!");
                                while (hasDisconnect) {
                                    try {


                                        while (socketList.size() == 0) {
                                            try {
                                                Thread.sleep(500);
                                                // print("Waiting for Connection");
                                            } catch (InterruptedException ie) {
                                                errprint(ie.getLocalizedMessage());
                                            }
                                        }

                                    } catch (Exception e) {
                                        errln("Io Exception, Exiting \n Nerd Stuff-> " + e.getCause());
                                    }
                                }
                                //socketList = tempSockets;
                                //tempSockets.remove(0);
                                println("Disconnect Handled");

                            }
                            println("Disconnect Handled <1");
                            hasDisconnect = false;
                        }


                    }
                });


                //messaging handlers

                //More Threads;

                listener.setDaemon(true);
                listener.start();


                try {
                    String address = InetAddress.getLocalHost().toString();
                    while (socketList.size() < 1) {
                        isInitial = true;
                        //waits until one conncts
                    }


                    connectionManager.setDaemon(true);
                    connectionManager.start();


                    //Open Incoming message thread
                    conversationEngine.setDaemon(true);
                    conversationEngine.start();

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
                        isInitial = false;
                        print("Initializing...");
                    } catch (Exception e) {
                        errln("Wait error");
                    }


                    //output
                    while (Thread.currentThread().isAlive()) {
                        System.out.println("Number of members " + socketList.size());

                        message = scan.nextLine();
                        if (message.equalsIgnoreCase("exit")) {
                            System.exit(0);
                        }

                        //breaker to allow time for ports to be disconnected
                        if (hasDisconnect) {
                            errln("Disconnection Handling");
                            continue;
                        }


                        //check for exit

                        //send message to all users connected;
                        for (int x = 0; x < socketList.size(); x++) {
                            if (!hasDisconnect) {
                                try {

                                /*
                                PrintStream printer = new PrintStream(socketList.get(x).getOutputStream(), true);
                                printer.write(message.getBytes());
                                **/
                                    outputs.get(x).println(message);

                                    //errln(message); //debug line
                                } catch (Exception e) {
                                    errprint(e.getLocalizedMessage());
                                    printStack(e);
                                    System.exit(1);
                                }
                            }
                        }

                    }


                } catch (Exception e) {
                    printStack(e);
                    System.exit(1);

                }

            } catch (Exception e) {
                printStack(e);
                System.exit(1);
            }

        }while(reset);
    }








}
