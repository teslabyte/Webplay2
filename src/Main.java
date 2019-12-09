import org.atmosphere.nettosphere.*;
import webplay.Network.WebAPI;
import webplay.Security.Identification;

import java.util.Scanner;
import java.util.logging.*;


public class Main {

	static int port;

	public static void startNettosphere() {

		System.out.println("Starting Nettosphere server");
	    Nettosphere server = new Nettosphere.Builder().config(
                new Config.Builder()
                   .host("0.0.0.0")
                   .port(port)
                   .resource(WebAPI.class)
                   .resource("./resources")
                   .build())
                .build();
	    server.start();
	    System.out.println("Nettosphere server started");
        Logger.getLogger("io.netty").setLevel(Level.OFF);
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("Which port : ");
		port = new Scanner(System.in).nextInt();
		startNettosphere();
		/*Identification identification = new Identification();
		String t = identification.macData("","","");
		String m = identification.generateMac("c1tGcW7E/hqAHqzNMtasnc+tXUDwktR+UBBvF74QFcxEVXQ8mtNOcPZGgqKbYSsLtdPnfLuXcTR755MgY622Ww==",t);
		System.out.println(t+m);*/
	}
}

