package de.hdm.group11.jabics.server.db;

import java.sql.Connection;
import java.sql.DriverManager;

import com.google.appengine.api.utils.SystemProperty;

public class DBConnection {

	/**
	 * Die Klasse DBConnection wird nur einmal instantiiert. Man spricht hierbei von
	 * einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 * 
	 * @see ContactMapper
	 * @see PValueMapper
	 * @see ContactListMapper
	 * @see PropertyMapper
	 * @see UserMapper
	 */
	private static Connection con = null;

	private static String url;
	private static String googleUrl = "jdbc:google:mysql://it-projekt-jabics:europe-west3:jabics/jabics?user=root&password=ThieskesOberesDrittel!";
	private static String localUrl = "jdbc:mysql://35.198.159.112:3306/jabics?verifyServerCertificate=false&useSSL=true";

	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>DBConnection.connection()</code>. Sie stellt die Singleton-Eigenschaft
	 * sicher, indem Sie dafür sorgt, dass nur eine einzige Instanz von
	 * <code>DBConnection</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> DBConnection sollte nicht mittels <code>new</code> instantiiert
	 * werden, sondern stets durch Aufruf dieser statischen Methode.
	 * <p>
	 * 
	 * <b>Nachteil:</b> Bei Zusammenbruch der Verbindung zur Datenbank - dies kann
	 * z.B. durch ein unbeabsichtigtes Herunterfahren der Datenbank ausgelöst werden
	 * - wird keine neue Verbindung aufgebaut, so dass die in einem solchen Fall die
	 * gesamte Software neu zu starten ist. In einer robusten Lösung würde man hier
	 * die Klasse dahingehend modifizieren, dass bei einer nicht mehr
	 * funktionsfähigen Verbindung stets versucht würde, eine neue Verbindung
	 * aufzubauen. Dies würde allerdings ebenfalls den Rahmen dieses Projekts
	 * sprengen.
	 * 
	 * @return DAS <code>DBConncetion</code>-Objekt.
	 * @see con
	 */
	public static Connection connection() {

		if (con == null) {
			url = null;

			try {
				if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
					Class.forName("com.mysql.jdbc.GoogleDriver");
					url = googleUrl;
				} else {
					Class.forName("com.mysql.jdbc.Driver");
					url = localUrl;
				}

				con = DriverManager.getConnection(url, "root", "ThieskesOberesDrittel!");
			} catch (Exception e) {
				con = null;
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return con;
	}
}