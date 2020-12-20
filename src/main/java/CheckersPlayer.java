import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

public class CheckersPlayer implements Runnable{

    //if 3 seconds for move - DEPTH = 6
    //if 5 seconds for move - DEPTH = 8
    //if 10 seconds for move - DEPTH = 9
    private static final int DEPTH = 9;
    private static final long SLEEP_TIME = 500;

    private PlayerInfo playerInfo;
    private GameInfo gameInfo;
    private MiniMaxAgent agent;

    private static final String BOT_NAME = "Киану_Ривз";

    public void connectToGame() throws IOException {
        HttpPostRaw post = new HttpPostRaw("http://localhost:8081/game?team_name="+BOT_NAME, "utf-8");
        post.addHeader("Accept", "application/json");
        post.setPostData("post");
        String response = post.finish();
        System.out.println(response);

        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(response, JsonObject.class).get("data").getAsJsonObject();
        playerInfo = gson.fromJson(obj, PlayerInfo.class);

        System.out.println("connected to game!");
    }

    public void getGameInfo() throws IOException {
        // Set the header
        HttpGet httpGet = new HttpGet("http://localhost:8081/game", "gbk");
        // Get the result
        String response = httpGet.finish();

        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(response, JsonObject.class).get("data").getAsJsonObject();
        gameInfo = gson.fromJson(obj, GameInfo.class);
        System.out.println(response);
    }

    public void move(int from, int to) throws IOException {

        HttpPostRaw post = new HttpPostRaw("http://localhost:8081/move", "utf-8");
        String json = String.format("{\n    \"move\": [%d, %d]\n}", from, to);
        post.setPostData(json);
        post.addHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Token " + playerInfo.getToken());
        String out = post.finish();
        System.out.println(out);
    }

    private void playGame() throws IOException, InterruptedException {
        getGameInfo();
        Board board = new Board(gameInfo.getBoard());

        agent = new MiniMaxAgent(playerInfo);
        //System.out.println("play game" + gameInfo.getStatus());
        while(gameInfo.is_started() && !gameInfo.is_finished()){
            if(gameInfo.getWhose_turn() != playerInfo.getColor()){
                agent.resetLastMove();
                Thread.sleep(SLEEP_TIME);
            }
            else{
                //your move
                Move move = agent.MinimaxDecision(board, DEPTH);

                System.out.println("RES Move : " + move.toString());
                move(move.getFrom(), move.getTo());
            }

            getGameInfo();
            board = new Board(gameInfo.getBoard());
        }
    }

//    public static void main(String[] args) throws IOException {
//
//        Thread first = new Thread(new CheckersPlayer());
//        Thread second = new Thread(new CheckersPlayer());
//
//        first.start();
//        second.start();
//    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CheckersPlayer player = new CheckersPlayer();
        player.connectToGame();
        player.playGame();

    }

    @Override
    public void run() {
        try {
            connectToGame();
            playGame();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}