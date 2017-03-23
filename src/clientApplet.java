import Tools.printTools;

import java.net.InetAddress;
import java.util.StringTokenizer;

import static Tools.printTools.*;

public class clientApplet {
    private static String address,userName,password;
    private static int port;
    private static InetAddress serverAddress;
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
                            if(serverAddress.isReachable(5000)) {
                                println(serverAddress.toString());
                                println("Address is REACHABLE");
                            }
                            else{
                                println(serverAddress.toString());
                                errln("Address is NOT REACHABLE");
                        }
                        }
                        else if(address.contains("www") ||address.contains(".")){
                            println(address);


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
