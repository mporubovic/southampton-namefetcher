import org.unbescape.html.HtmlEscape;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NameFetcher {

    Scanner scanner = new Scanner(System.in);

    public static void main(String args[]) throws IOException, InterruptedException {

        NameFetcher fetcher = new NameFetcher();

        System.out.println("Enter Cookie string: ");
        String cookie = fetcher.scanner.nextLine();

        while (true) {
            fetcher.fetchName(cookie);
        }
    }

    public void fetchName(String cookie) throws IOException, InterruptedException {
        System.out.println("Enter user name: ");
        String userName = scanner.nextLine();

        System.out.println("Fetching name for user " + userName + "...");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =
                HttpRequest.newBuilder(URI.create("https://secure.ecs.soton.ac.uk/people/" + userName)) //zs2g19
                        .header(
                                "Cookie", cookie)
                        .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Pattern p = Pattern.compile("itemprop='name'>(.+?)<");
        Matcher m = p.matcher(response.body().toString());

        if (m.find()) {
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            String parsedName = HtmlEscape.unescapeHtml(m.group(1)); // unescape quotes for example
            System.out.println("Found name: " + parsedName);
            System.out.println("");
        }
        else {
            System.out.println("Name not found");
        }
    }


}
