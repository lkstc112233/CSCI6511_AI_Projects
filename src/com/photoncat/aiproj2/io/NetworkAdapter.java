package com.photoncat.aiproj2.io;

import com.google.gson.Gson;
import com.photoncat.aiproj2.interfaces.Board;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The adapter to the p2p gaming server api.
 * http://www.notexponential.com/aip2pgaming/api/index.php
 *
 * The document of the API can be found at:
 * https://docs.google.com/presentation/d/1apI8JBF4FK8nm1xFmj0ud4O51VAJnjkiZxmbIeBqADE/edit#slide=id.g336cc86b3f_0_5
 *
 * Our team ID is 1102 and I'm hard wiring the ID into the code.
 *
 * The API requires an auth whose required information will be loaded from a file therefore won't be check in.
 */
public class NetworkAdapter implements Adapter {
    private final static String SERVER_URL = "http://www.notexponential.com/aip2pgaming/api/index.php";
    // I'm storing these sensitive data here. It's against some of the security rules, but hey, we are
    // using http and basic auth, so screw security rules!
    private final String basicAuthPayload;
    private final String userId;
    private final String apiKey;
    private final Gson gson = new Gson();

    public NetworkAdapter(File sensitive) {
        String username = "";
        String password = "";
        try (Scanner scanner = new Scanner(sensitive)) {
            username = scanner.nextLine();
            password = scanner.nextLine();
            userId = scanner.nextLine();
            apiKey = scanner.nextLine();
        } catch (FileNotFoundException e) {
            // Rethrow the exception.
            throw new UncheckedIOException(e);
        } catch (NoSuchElementException e) {
            System.err.println("Please check your input file format.\n Format: username, password, userId, api key, each in a separate line, without any leading or following spaces.");
            throw e;
        }
        String usernameColonPassword = username + ":" + password;
        basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
    }

    /**
     * A wrapper for the other get. It parses the args into the string format.
     *
     * There's NO protection. Do not pass attacking strings into this function.
     */
    private String get(Map<String, String> args) {
        StringBuilder params = new StringBuilder();
        char leadingChar = '?';
        for (var key : args.keySet()) {
            params.append(leadingChar);
            params.append(key);
            params.append('=');
            params.append(args.get(key));
            leadingChar = '&';
        }
        return get(params.toString());
    }

    /**
     * Send a get request to the api.
     *
     * @return the get result.
     */
    private String get(String args) {
        // Connect to the web server endpoint
        try {
            URL serverUrl = new URL(SERVER_URL + args);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

            // Set HTTP method as GET
            urlConnection.setRequestMethod("GET");

            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);

            // Include other payloads
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("userid", userId);
            urlConnection.addRequestProperty("x-api-key", apiKey);

            StringBuilder result = new StringBuilder();
            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            try (var httpResponseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String lineRead;
                while ((lineRead = httpResponseReader.readLine()) != null) {
                    result.append(lineRead);
                    result.append('\n');
                }
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            // Rethrow so the program crashes.
            throw new UncheckedIOException(e);
        }
    }

    /**
     * A wrapper for the other post. It parses the args into the string format.
     *
     * There's NO protection. Do not pass attacking strings into this function.
     */
    private String post(Map<String, String> args) {
        StringBuilder params = new StringBuilder();
        String leadingChar = "";
        for (var key : args.keySet()) {
            params.append(leadingChar);
            params.append(key);
            params.append('=');
            params.append(args.get(key));
            leadingChar = "&";
        }
        return post(params.toString());
    }

    /**
     * Send a post request to the api.
     *
     * @return the post result.
     */
    private String post(String args) {
        // Connect to the web server endpoint
        try {
            URL serverUrl = new URL(SERVER_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

            // Set HTTP method as POST
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            // Include the HTTP Basic Authentication payload
            urlConnection.addRequestProperty("Authorization", basicAuthPayload);

            // Include other payloads
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("userid", userId);
            urlConnection.addRequestProperty("x-api-key", apiKey);

            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            try (var httpRequestWriter = new DataOutputStream(urlConnection.getOutputStream())) {
                // This call assumes that all it's content is in UTF8 charset. This is possible because
                // we basically is only using standard ASCII characters and it's same across all platforms.
                // Plus the API didn't request any charset inputs.
                httpRequestWriter.write(args.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder result = new StringBuilder();
            // Read response from web server, which will trigger HTTP Basic Authentication request to be sent.
            try (var httpResponseReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                String lineRead;
                while ((lineRead = httpResponseReader.readLine()) != null) {
                    result.append(lineRead);
                    result.append('\n');
                }
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            // Rethrow so the program crashes.
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Stores the result of create game.
     */
    private static class CreateGameResult {
        String code;
        int gameId;
    }


    /**
     * Returns the result of Create Game Operation.
     * @param otherTeamId The method needs only other team's ID, since our teamId is fixed.
     * @return gameId created.
     */
    @Override
    public int createGame(int otherTeamId, int boardSize, int target) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "game");
        params.put("teamId1", "1102");
        params.put("teamId2", Integer.toString(otherTeamId));
        params.put("gameType", "TTT");
        params.put("boardSize", Integer.toString(boardSize));
        params.put("target", Integer.toString(boardSize));
        String result = post(params);
        var parsed = gson.fromJson(result, CreateGameResult.class);
        if (!parsed.code.equals("OK")) {
            System.err.println("Create game failed!");
            System.err.println("with params: ");
            System.err.println("otherTeamId: " + otherTeamId);
            System.err.println("boardSize: " + boardSize);
            System.err.println("target: " + target);
            return -1;
        }
        return parsed.gameId;
    }

    /**
     * Stores the result of make move.
     */
    private static class MoveOperationResult {
        String code;
        int moveId;
    }

    @Override
    public void moveAt(int gameId, int x, int y) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "move");
        params.put("teamId", "1102");
        params.put("gameId", Integer.toString(gameId));
        params.put("move", x + "," + y);
        String result = post(params);
        var parsed = gson.fromJson(result, MoveOperationResult.class);
        if (!parsed.code.equals("OK")) {
            System.err.println("Move failed!");
            System.err.println("with params: ");
            System.err.println("gameId: " + gameId);
            System.err.println("x: " + x);
            System.err.println("y: " + y);
            // Continue execution.
        }
        // Discard the move id since we hardly need it.
    }

    /**
     * Stores the result of make move.
     */
    private static class LastMoveResult {
        private static class Moves{
            int moveId;
            int gameId;
            int teamId;
            int moveX;
            int moveY;
            String move;
            String symbol;
        }
        String code;
        Moves[] moves;
    }


    @Override
    public Board.PieceType getLastMove(int gameId) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "moves");
        params.put("gameId", Integer.toString(gameId));
        params.put("count", "5");
        String result = get(params);
        var parsed = gson.fromJson(result, LastMoveResult.class);
        if (!parsed.code.equals("OK")) {
            System.err.println("Get move failed!");
            return Board.PieceType.NONE;
        }
        if (parsed.moves.length < 1) {
            return Board.PieceType.NONE;
        }
        if (parsed.moves[0].symbol.equalsIgnoreCase("O")) {
            return Board.PieceType.CIRCLE;
        } else {
            return Board.PieceType.CROSS;
        }
    }

    @Override
    public Board getBoard(int gameId) {
        return null;
    }
}
