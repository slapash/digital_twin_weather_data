package com.example;



import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

	/**Contient les m�thodes pour l'ins�rtion de donn�es dans la base de donn�es.
	 */
	public class Insertion {
		private String base;
		private String retentionPolicy;
		private String url;
		private String user;
		private String password;
		
		public Insertion(String base, String retentionPolicy, String url, String user, String password) {
			this.base = base;
			this.retentionPolicy = retentionPolicy;
			this.url = url;
			this.user = user;
			this.password = password;

		}
		
		/**Cr�ation d'un objet client qui permet de se connecter � la base de donn�es et d'y ins�rer des donn�es.
		 * 
		 * @return client objet de type InfluxDBClient. 
		 */
		public InfluxDBClient createClient() {
			InfluxDBClient client = InfluxDBClientFactory.createV1(url,
	                user,
	                password.toCharArray(),
	                base,
	                retentionPolicy);
			
			return client;
			
		}
		/**Ins�rtion de donn�es dans la BDD. 
		 * 
		 * @param client objet de type InfluxDBClient.
		 * @param data Double contenant la valeur � ins�rer.
		 * @param timestamp Double heure de collecte de la valeur � inserer en millisecondes.
		 * @param topic chaine de caract�re pour filtrer les donn�es re�ues par sujet (topic en anglais).
		 */
		public void write(InfluxDBClient client, Double data, Double timestamp, String topic) {	
			try (WriteApi writeApi = client.getWriteApi()) {

	        	Point point = Point.measurement(topic)
	                    .addField("value", data)
	                    .time(timestamp, WritePrecision.MS);

	            System.out.println(point.toLineProtocol());

	            writeApi.writePoint(point);
	        }
		}
		
		public void close(InfluxDBClient client) {
			client.close();
		}
}