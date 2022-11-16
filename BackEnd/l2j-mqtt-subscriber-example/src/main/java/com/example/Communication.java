package com.example;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttClientBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.influxdb.client.InfluxDBClient;

import java.io.File;

import org.ini4j.Wini;


	/**Contient les m�thodes pour la cr�ation d'un client mqtt et la r�ception de donn�es
	 */	
	public class Communication {
	    private int port;
	    private String broker;
	    private String id;
	    
	    
	    public Communication(int port, String broker, String id) {
	        this.port = port;
	        this.broker = broker;
	        this.id = id;
	    }

	    
	    /**Cr�e un objet client mqtt pour se connecter et recevoir des donn�es. 
	     * 
	     * @return un objet de type Mqtt5Client. 
	     */
	    public Mqtt5Client createClient() {
	        MqttClientBuilder clientBuilder = MqttClient.builder()
	                .identifier(id)
	                .serverHost(broker)
	                .serverPort(port);

	        Mqtt5Client client = clientBuilder.useMqttVersion5().build();
	        return client;
	    }
	    
	    /**Se connecter a un broker mqtt.
	     * 
	     * @param client objet de type Mqtt5Client. 
	     */
	    public void connectClient(Mqtt5Client client) {
	        client.toBlocking().connectWith()
	        .cleanStart(false)
	        .sessionExpiryInterval(TimeUnit.HOURS.toSeconds(1)) // buffer messages
	        .send();
	    }
	    /**Recevoir des donn�es depuis le broker auquel le client (type Mqtt5Client) est conn�ct�.
	     * 
	     * @param client objet de type Mqtt5Client.
	     * @param topic chaine de caract�re pour filtrer les donn�es re�ues par sujet (topic en anglais).
	     */
	    public void subscribe(Mqtt5Client client, String topic) {
	        client.toAsync().subscribeWith()
	            .topicFilter(topic)
	            .callback(publish -> {
	            doSomething( publish.getTopic().toString(), 
	                    new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8));
	            })
	            .send();
	    }
	    /**Fonction callback qui insere les donn�es re�ues dans une base de donn�e.
	     * 
	     * @param topic chaine de caract�re qui filtre les sujets dans {@link #subscribe} ici permet de diriger les insertions dans la BDD.
	     * @param msg chaine de caract�re qui rer�sente les donn�es re�ues.
	     */
	    private static void doSomething(String topic, String msg) {
	    	try {
	    	 String absolutePath = System.getProperty("user.dir");

	         Wini ini = new Wini(new File(absolutePath+"/configJava.ini")); 
	    		
	    	 String base = ini.get("DATABASE", "database");
	    	 String user = ini.get("DATABASE", "user");
	    	 String url = ini.get("DATABASE", "url");
	    	 String password = ini.get("DATABASE", "password");


			 System.out.println("Received message on topic " + topic + ": " + msg);
			 InfluxDBClient client;
			 Insertion newClient = new Insertion(base, "autogen", url,user,password);
			 client = newClient.createClient();
			 JsonObject objet = new JsonParser().parse(msg).getAsJsonObject();
			 Double data = Double.valueOf(objet.get("value").getAsString());
			 Double time = Double.valueOf(objet.get("date").getAsString());
			 newClient.write(client, data, time, topic);
			 newClient.close(client);
	       
	    	 }catch(Exception e) {
	  			System.err.println(e.getMessage());
	  		}
	}
	    
	    	      
}
	



