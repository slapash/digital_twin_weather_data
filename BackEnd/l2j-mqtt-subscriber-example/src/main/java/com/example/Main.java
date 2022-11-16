package com.example;

import java.io.File;

import org.ini4j.Wini;

import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

public class Main {
	public static void main (String [] args) {
    	try {
    	/** Contient le chemin pour le dossier depuis lequel ce programme est lancé.	
    	 */
    	String absolutePath = System.getProperty("user.dir");
    	/** Objet pour utiliser le fichier de configuration ini .
    	 */
		Wini ini = new Wini(new File(absolutePath+"/configJava.ini")); 
		/** Le port du broker mqtt.
		 */
		int port = ini.get("SERVERCONFIG", "port", int.class);
		/** Adresse du broker mqtt.
		 */
		String broker = ini.get("SERVERCONFIG", "broker");
		/** Identifiant du client mqtt.
		 */
		String id = ini.get("SERVERCONFIG", "id");
		/** Topic température.
		 */
		String topicT = ini.get("DATAINFO", "topict");
		/** Topic pression atmosphrique.
		 */
		String topicP = ini.get("DATAINFO", "topicp");
		/** Topic humidité.
		 */
		String topicH = ini.get("DATAINFO", "topich");
		/** Topic connexion.
		 */
		String topicC = ini.get("DATAINFO", "topicc");
		
		
		
		Communication mqtt = new  Communication( port, broker, id);
		Mqtt5Client	client=mqtt.createClient();
		mqtt.connectClient(client);
		mqtt.subscribe(client , topicT);
		mqtt.subscribe(client , topicH);
		mqtt.subscribe(client , topicP);
		mqtt.subscribe(client , topicC);
		
		
		System.out.println(                  );
    	 }catch(Exception e) {
 			System.err.println(e.getMessage());
 		}
    	
    }

}