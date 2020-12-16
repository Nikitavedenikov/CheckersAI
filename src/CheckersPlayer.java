import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.*;

public class CheckersPlayer implements Runnable{


    private PlayerInfo playerInfo;
    private GameInfo gameInfo;
    private MiniMaxAgent agent;

    public void connectToGame() throws IOException {
        HttpPostRaw post = new HttpPostRaw("http://localhost:8081/game?team_name=Loom", "utf-8");
        post.addHeader("Accept", "application/json");
        post.setPostData("dick");
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
        //System.out.println(response);
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
        Board board = new Board(gameInfo);

        MiniMaxAgent agent = new MiniMaxAgent(playerInfo);

        while(gameInfo.is_started() && !gameInfo.is_finished()){
            if(!gameInfo.getWhose_turn().equals(playerInfo.getColor())){
                Thread.sleep(1000);
            }
            else{
                //your move
                Move move = agent.MinimaxDecision(board);
                System.out.println("Move : " + move.toString());
                move(move.getFrom(), move.getTo());
            }

            getGameInfo();
            board = new Board(gameInfo);
        }
    }

    public static void main(String[] args) throws IOException {

        Thread first = new Thread(new CheckersPlayer());
        Thread second = new Thread(new CheckersPlayer());

        first.start();
        second.start();
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        CheckersPlayer player = new CheckersPlayer();
//        player.playGame();
//
//    }

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

/*

{
   "status":"success",
   "data":{
      "status":"Game is playing",
      "whose_turn":"RED",
      "winner":null,
      "board":[
         {
            "color":"RED",
            "row":0,
            "column":0,
            "king":false,
            "position":1
         },
         {
            "color":"RED",
            "row":0,
            "column":1,
            "king":false,
            "position":2
         },
         {
            "color":"RED",
            "row":0,
            "column":2,
            "king":false,
            "position":3
         },
         {
            "color":"RED",
            "row":0,
            "column":3,
            "king":false,
            "position":4
         },
         {
            "color":"RED",
            "row":1,
            "column":0,
            "king":false,
            "position":5
         },
         {
            "color":"RED",
            "row":1,
            "column":1,
            "king":false,
            "position":6
         },
         {
            "color":"RED",
            "row":1,
            "column":2,
            "king":false,
            "position":7
         },
         {
            "color":"RED",
            "row":1,
            "column":3,
            "king":false,
            "position":8
         },
         {
            "color":"RED",
            "row":2,
            "column":0,
            "king":false,
            "position":9
         },
         {
            "color":"RED",
            "row":2,
            "column":1,
            "king":false,
            "position":10
         },
         {
            "color":"RED",
            "row":2,
            "column":2,
            "king":false,
            "position":11
         },
         {
            "color":"RED",
            "row":2,
            "column":3,
            "king":false,
            "position":12
         },
         {
            "color":"BLACK",
            "row":5,
            "column":0,
            "king":false,
            "position":21
         },
         {
            "color":"BLACK",
            "row":5,
            "column":1,
            "king":false,
            "position":22
         },
         {
            "color":"BLACK",
            "row":5,
            "column":2,
            "king":false,
            "position":23
         },
         {
            "color":"BLACK",
            "row":5,
            "column":3,
            "king":false,
            "position":24
         },
         {
            "color":"BLACK",
            "row":6,
            "column":0,
            "king":false,
            "position":25
         },
         {
            "color":"BLACK",
            "row":6,
            "column":1,
            "king":false,
            "position":26
         },
         {
            "color":"BLACK",
            "row":6,
            "column":2,
            "king":false,
            "position":27
         },
         {
            "color":"BLACK",
            "row":6,
            "column":3,
            "king":false,
            "position":28
         },
         {
            "color":"BLACK",
            "row":7,
            "column":0,
            "king":false,
            "position":29
         },
         {
            "color":"BLACK",
            "row":7,
            "column":1,
            "king":false,
            "position":30
         },
         {
            "color":"BLACK",
            "row":7,
            "column":2,
            "king":false,
            "position":31
         },
         {
            "color":"BLACK",
            "row":7,
            "column":3,
            "king":false,
            "position":32
         }
      ],
      "available_time":100000.04999999999,
      "is_started":true,
      "is_finished":false
   }
}
 */
