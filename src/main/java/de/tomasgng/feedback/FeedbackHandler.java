package de.tomasgng.feedback;

import com.google.inject.Inject;
import de.tomasgng.DynamicSeasons;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FeedbackHandler {

    private final DynamicSeasons plugin;
    private boolean prevented = false;

    @Inject
    public FeedbackHandler(DynamicSeasons plugin) {
        this.plugin = plugin;
    }

    public void sendFeedback(Feedback feedback, Runnable onSuccess, Runnable onFailure) {
        if(prevented)
            return;

        final String url = "http://213.165.94.207:8080/feedback/post";

        Bukkit.getScheduler().runTaskAsynchronously(plugin, task -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                                                 .uri(URI.create(url))
                                                 .header("Content-Type", "application/json")
                                                 .POST(HttpRequest.BodyPublishers.ofString(getJson(feedback)))
                                                 .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    preventFromSending();
                    onSuccess.run();
                } else {
                    plugin.getLogger().severe("Error while sending feedback: " + response.body());
                    onFailure.run();
                }
            } catch (IOException | InterruptedException e) {
                plugin.getLogger().severe("Error while sending feedback: " + e.getMessage());
                onFailure.run();
            }
        });
    }

    private String getJson(Feedback feedback) {
        return "{\"type\":\"" + feedback.type().name() + "\", \"message\":\"" + feedback.message() + "\"}";
    }

    private void preventFromSending() {
        prevented = true;

        Bukkit.getScheduler().runTaskLater(plugin,
                                              task -> prevented = false,
                                              60 * 1000L);
    }

    public boolean isPrevented() {
        return prevented;
    }
}
