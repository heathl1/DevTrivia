import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    // I am setting a starting port for the server to 8081.
    // I picked 8081 because 8080 is commonly used and could already be busy on my computer.
    private static int PORT = 8081;

    // This variable will store where my 'client' folder is located on my computer.
    // I am using it so the server knows where to find the HTML, CSS, and JS files to serve.
    private static Path CLIENT_ROOT;

    public static void main(String[] args) throws Exception {
        // First, I want to figure out where the 'client' folder is, starting from my current working directory.
        // This helps the server know where to look for the static files like index.html and game.html.
        CLIENT_ROOT = resolveClientRoot();
        System.out.println("[DevTrivia] Serving static files from: " + CLIENT_ROOT.toAbsolutePath());

        // Now I am creating a tiny HTTP server that listens on the port number above.
        // If the port is already in use, I will switch to port 3000 so I can still run my app without changing code elsewhere.
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            System.out.println("[DevTrivia] Port " + PORT + " busy, trying 3000…");
            PORT = 3000;
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
        }

        // Here I am creating a very simple API endpoint at /api/questions.
        // It sends back one hardcoded question so I can demo a working game quickly.
        server.createContext("/api/questions", new JsonHandler(exchange -> {
            // I am using a LinkedHashMap to keep the key order predictable when I convert it to JSON.
            Map<String, Object> q = new LinkedHashMap<>();
            q.put("id", 1); // This is the question ID that could be helpful later if I add more questions.
            q.put("question", "Which HTTP method is idempotent and typically used to retrieve data?"); // This is the actual question text.
            q.put("choices", Arrays.asList("POST", "GET", "PATCH", "CONNECT")); // These are the answer choices the user will see.
            q.put("answerIndex", 1); // This is the index of the correct answer in the list above (0 = POST, 1 = GET, etc.).
            // I am writing the response as a JSON array with a single question for now.
            writeJson(exchange, Collections.singletonList(q));
        }));

        // This context serves all the static files from the 'client' folder.
        // It handles paths like '/', '/index.html', and '/game.html' and returns the matching file contents.
        server.createContext("/", exchange -> {
            try {
                // I am reading the path from the browser request to figure out which file to serve.
                String uriPath = exchange.getRequestURI().getPath();

                // If the user just requests '/', I will give them index.html by default.
                if (uriPath.equals("/")) uriPath = "/index.html";

                // I am combining the requested path with the client root to get the actual file location on disk.
                Path file = CLIENT_ROOT.resolve(uriPath.substring(1)).normalize();

                // I am making sure the file actually exists and is inside the client folder.
                // If any of these checks fail, I will return a 404 Not Found response.
                if (!file.startsWith(CLIENT_ROOT) || !Files.exists(file) || Files.isDirectory(file)) {
                    send404(exchange);
                    return;
                }

                // I am trying to guess the correct Content-Type header (like text/html or text/css) based on the file name.
                String mime = URLConnection.guessContentTypeFromName(file.getFileName().toString());
                if (mime == null) mime = "application/octet-stream"; // This is a safe default if the type is unknown.

                // I am reading the whole file into memory and sending it to the browser.
                byte[] data = Files.readAllBytes(file);
                exchange.getResponseHeaders().add("Content-Type", mime);
                exchange.sendResponseHeaders(200, data.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(data); }
            } catch (Exception e) {
                // If something goes wrong here, I will print the error and return a 500 status code.
                e.printStackTrace();
                send500(exchange, e.getMessage());
            }
        });

        // These messages help me see that the server actually started and which port it is using.
        System.out.println("[DevTrivia] Running at http://localhost:" + PORT);
        System.out.println("[DevTrivia] Open /game.html for the mini demo.");

        // Finally, I am starting the server so it begins accepting requests from the browser.
        server.start();
    }

    // ---------- helpers ----------

    // This interface lets me write small lambda-style handlers for JSON endpoints.
    // It keeps my code organized and avoids repeating boilerplate.
    interface JsonEndpoint { void handle(HttpExchange exchange) throws Exception; }

    // This class wraps the HTTP handler and makes sure my JSON endpoints only accept GET for now.
    // It also sets the Content-Type to application/json and catches errors in a consistent way.
    static class JsonHandler implements HttpHandler {
        private final JsonEndpoint endpoint;

        // I am passing the actual endpoint logic into this handler.
        JsonHandler(JsonEndpoint ep) { this.endpoint = ep; }

        @Override public void handle(HttpExchange exchange) throws IOException {
            try {
                // Right now I only want to support GET requests for this simple demo.
                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    exchange.sendResponseHeaders(405, -1); // 405 means Method Not Allowed.
                    return;
                }
                // I am telling the browser that I am sending back JSON.
                exchange.getResponseHeaders().add("Content-Type", "application/json");

                // This calls the custom logic I provided when I created the endpoint.
                endpoint.handle(exchange);
            } catch (Exception e) {
                // If something breaks in the endpoint, I will send a 500 error back to the client.
                e.printStackTrace();
                send500(exchange, e.getMessage());
            }
        }
    }

    // This method converts a Java object into JSON text and writes it to the HTTP response.
    // I am doing this manually to avoid pulling in any extra libraries for this small prototype.
    static void writeJson(HttpExchange exchange, Object obj) throws IOException {
        String json = toJson(obj); // I am converting the object to a JSON string.
        byte[] bytes = json.getBytes("UTF-8"); // I am making sure the bytes are UTF-8 encoded.
        exchange.sendResponseHeaders(200, bytes.length); // I am sending a 200 OK with the content length.
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); } // I am writing the body to the response stream.
    }

    // This is a tiny JSON serializer that handles Strings, Numbers, Booleans, Maps, and Iterables.
    // It is not as powerful as a real JSON library, but it is enough for this demo.
    static String toJson(Object obj) {
        if (obj == null) return "null"; // I am representing null values as the JSON null literal.

        if (obj instanceof String)
            // I am escaping backslashes and double quotes so the JSON stays valid.
            return "\"" + ((String) obj).replace("\\", "\\\\").replace("\"", "\\\"") + "\"";

        if (obj instanceof Number || obj instanceof Boolean)
            // Numbers and booleans can be written directly without quotes.
            return obj.toString();

        if (obj instanceof Map) {
            // I am building a JSON object by joining "key:value" pairs with commas.
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : ((Map<?, ?>) obj).entrySet()) {
                if (!first) sb.append(",");
                sb.append(toJson(e.getKey().toString())).append(":").append(toJson(e.getValue()));
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }

        if (obj instanceof Iterable) {
            // I am building a JSON array by joining item values with commas inside square brackets.
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Object o : (Iterable<?>) obj) {
                if (!first) sb.append(",");
                sb.append(toJson(o));
                first = false;
            }
            sb.append("]");
            return sb.toString();
        }

        // If I get an object type I did not plan for, I will just convert it to a string safely.
        return toJson(obj.toString());
    }

    // This method returns a standard 404 Not Found response.
    // I am sending back a small message so I can quickly tell what happened in the browser.
    static void send404(HttpExchange exchange) throws IOException {
        byte[] msg = "Not Found".getBytes();
        exchange.sendResponseHeaders(404, msg.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(msg); }
    }

    // This method returns a 500 Server Error response when something unexpected happens.
    // I am including the error message to make debugging easier while I develop.
    static void send500(HttpExchange exchange, String message) throws IOException {
        byte[] msg = ("Server Error: " + (message == null ? "" : message)).getBytes();
        exchange.sendResponseHeaders(500, msg.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(msg); }
    }

    // This method tries to find the 'client' folder by walking up from my working directory.
    // I am checking up to six levels in case I opened the wrong folder in IntelliJ by accident.
    static Path resolveClientRoot() {
        Path start = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        for (int i = 0; i < 6; i++) {
            Path candidate = start.resolve("client");
            if (Files.isDirectory(candidate)) return candidate; // If I find the client folder, I will return it right away.
            start = start.getParent() != null ? start.getParent() : start; // If not, I will go up one directory level and try again.
        }
        // If I never find the client folder, I will throw an error so I know to fix my working directory.
        throw new IllegalStateException("Could not find a 'client' folder near working directory.");
    }
}
