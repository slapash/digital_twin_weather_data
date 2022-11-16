package com.example;



import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;

	/**Contient les méthodes pour l'insértion de données dans la base de données.
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
		
		/**Création d'un objet client qui permet de se connecter à la base de données et d'y insérer des données.
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
		/**Insértion de données dans la BDD. 
		 * 
		 * @param client objet de type InfluxDBClient.
		 * @param data Double contenant la valeur à insérer.
		 * @param timestamp Double heure de collecte de la valeur à inserer en millisecondes.
		 * @param topic chaine de caractère pour filtrer les données reçues par sujet (topic en anglais).
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