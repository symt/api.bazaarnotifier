package dev.meyi.bn.hypixel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dev.meyi.bn.config.SecretsManager;
import dev.meyi.bn.entity.Player;
import dev.meyi.bn.hypixel.json.Profiles;
import dev.meyi.bn.hypixel.json.SlayerBoss;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Check the unlocked recipes for a particular user's selected profile
 */
public class CollectionCheck {

  private static final Gson gson = new GsonBuilder().create();
  // it's a secret!
  private static final String apiKey = SecretsManager.getInstance().getApiKey();
  // ehh, this could probably be more "scientific" in determining the number of concurrent requests
  private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

  private static final HttpClient httpClient = HttpClient.newBuilder()
      .executor(executorService)
      .build();

  /**
   * Given a player entity, retrieve the list of unlocked collections and slayers
   *
   * @param player entity with UUID for the request
   * @return List of unlocked recipes (or null if an error)
   */
  public static List<String> getCollections(Player player) {
    try {
      HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(
              "https://api.hypixel.net/v2/skyblock/profiles?uuid=%s".formatted(player.getUuid())))
          .header("Api-Key", apiKey).build();

      HttpResponse<InputStream> response;
      response = httpClient.send(request, BodyHandlers.ofInputStream());

      JsonReader jsonReader = new JsonReader(
          new BufferedReader(new InputStreamReader(response.body())));
      jsonReader.setLenient(true);

      Profiles results = gson.fromJson(jsonReader, Profiles.class);

      int profileIndex = 0;
      if (results == null || !results.success || results.profiles == null
          || results.profiles.size() == 0) {
        throw new Exception("Player " + player.getUuid() + " has invalid results.");
      }

      for (int i = 0; i < results.profiles.size(); i++) {
        if (results.profiles.get(i).selected) {
          profileIndex = i;
          break;
        }
      }

      // f'ing naming... why did i do this...
      dev.meyi.bn.hypixel.json.Player playerDataFromAPI = results.profiles.get(
          profileIndex).members.get(player.getUuid());

      if (playerDataFromAPI.player_data == null ||
          playerDataFromAPI.player_data.unlocked_coll_tiers == null ||
          playerDataFromAPI.slayer == null ||
          playerDataFromAPI.slayer.slayer_bosses == null) {
        throw new Exception("Could not load collections from api for player: " + player.getUuid());
      }

      List<String> unlockedCollections = new ArrayList<>(
          playerDataFromAPI.player_data.unlocked_coll_tiers);

      Map<String, SlayerBoss> slayer = playerDataFromAPI.slayer.slayer_bosses;

      for (Map.Entry<String, SlayerBoss> entry : slayer.entrySet()) {
        for (Map.Entry<String, Boolean> entry2 : slayer.get(
            entry.getKey()).claimed_levels.entrySet()) {
          unlockedCollections.add(
              entry.getKey().toUpperCase() + entry2.getKey().replace("level", "").toUpperCase());
        }
      }

      return unlockedCollections;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
