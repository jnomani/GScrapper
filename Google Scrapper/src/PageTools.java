import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.ParserConfigurationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

public class PageTools {

	public static final String MAP_NAME_TAG = "span[class=pp-place-title]";
	public static final String MAP_TELE_TAG = "span[class=telephone]";
	public static final String MAP_ADDR_TAG = "span[class=pp-headline-item pp-headline-address]";
	public static final String MAP_DIV = "div[class=noprint res]";

	public static String formatQuery(String query) {
		String str = "";

		for (int i = 0; i < query.length(); i++) {
			if (query.charAt(i) == ' ')
				str += '+';
			else
				str += query.charAt(i);
		}
		return str;
	}

	public static void verifyHTTPS(final String url) {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {

				if (hostname.equals(url))
					return true;
				return false;
			}
		});
	}

	public static Elements scrapePage(String divID, Document html) {
		Elements div = html.select("div#" + divID);
		return div;
	}

	static final String charset = "UTF-8";

	public static Document getWebResults(String query)
			throws ParserConfigurationException, SAXException, IOException {

		URL url = new URL("https://google.com/search?q=" + formatQuery(query));
		URLConnection con = url.openConnection();
		// con.addRequestProperty("User-Agent", "Mozilla/4.76");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

		String strLine = "";
		String html = "";

		while ((strLine = in.readLine()) != null) {
			html += strLine;
		}
		in.close();

		org.jsoup.nodes.Document doc = Jsoup.parse(html);

		return doc;
	}

	public static Document getMapResults(String query, int start)
			throws IOException {

		verifyHTTPS("www.maps.google.com");

		URL url = new URL("https://maps.google.com/maps?q="
				+ formatQuery(query) + "&start=" + start);
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));

		String strLine = "";
		String html = "";

		while ((strLine = in.readLine()) != null) {
			html += strLine;
		}
		in.close();

		org.jsoup.nodes.Document doc = Jsoup.parse(html);

		return doc;
	}

	public static int results(String query) {
		Document doc;
		try {
			doc = getMapResults(query, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return 0;
		}

		Elements num = doc.select("span[class=num_results]").select("b");
		try {
			return NumberFormat.getNumberInstance(Locale.US).parse(num.text())
					.intValue();
		} catch (ParseException e) {
			return 0;
		}
	}

	public static String getSite(String query) {
		Document doc;
		try {
			doc = getWebResults(query);
			return scrapePage("search", doc).select("div[class=rc]")
					.select("div[class=s]").select("cite").get(0).toString();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return "Unavailable";
		}

	}

}
