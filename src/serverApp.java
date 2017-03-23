import javafx.scene.layout.Priority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static Tools.printTools.*;
import static java.lang.Thread.currentThread;


public class serverApp {
    static int portnumber;
    public static boolean establishedConnection=false;


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

            //declare server socket
            ServerSocket serverSocket = null;
            try {
                //bind the port
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

                try {


                    String address = InetAddress.getLocalHost().toString();


                    //run waiter Thread;
                    Thread wait = new Thread(() -> {
                        int index = 0;
                        int dot = 0;
                        boolean done = false;
                        println(currentThread().toString());
                        while (!done) {
                            if (establishedConnection)
                                break;

                            else {
                                if (index == 60) {
                                    println("\nTimeout has occurred, to Retry Connection type \"y\", Else the program will Exit!");
                                    String choice = new Scanner(System.in).next();
                                    if ((choice.contains("y")) || (choice.contains("Y"))) {
                                        index = 0;
                                    } else {
                                        done = true;
                                        System.exit(0);
                                    }
                                }

                                try {
                                    //Runtime.getRuntime().exec("cls");
                                    println("Server is now listening on port:" + portnumber + " IP->" + address);
                                    for (int x = 0; x < 60; x++) {

                                        if (index == 60 || establishedConnection) {

                                            break;
                                        }
                                        index++;

                                        try {

                                            Thread.sleep(1000);
                                            if (index % 10 == 0) {
                                                print(60 - index + "");
                                                println("");
                                                continue;
                                            }
                                            print(60 - index + "...");

                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.getCause();
                                }
                            }
                        }
                    });


                    wait.setDaemon(true);
                    wait.start();


                    //Start listening
                    Socket clientSocket = serverSocket.accept();

                    establishedConnection = true;




                    //wait for other thread to kill itself
                    Thread.sleep(5000);

                    println("\n\nConnection Established [" + clientSocket.getInetAddress().toString() + "]");

                PrintWriter outToClient =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String message = "empty";


                outToClient.println("Hello! Welcome, to leave just type exit :)");
                println("\nHello! Welcome, to leave just type exit :)");




                //messaging handlers

                    //output
                while (Thread.currentThread().isAlive()) {
                    message = new Scanner(System.in).next();
                    //errln(message); //debug line

                    //check for exit
                    if(message.equalsIgnoreCase("exit")){System.exit(0);}
                    //send message;
                    outToClient.println(message);

                }


            } catch (IOException e) {
                errln("Error binding clientSocket! -> " + e.getCause() + " " + e.getLocalizedMessage() + " in " + e.getClass());
                reset = true;
            } catch (Exception e) {
            e.getCause();

        }

        }catch(Exception e){
            e.getCause();
        }

        }while(reset);
        }








}
