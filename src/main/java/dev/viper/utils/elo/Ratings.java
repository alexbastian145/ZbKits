package dev.viper.utils.elo;

public class Ratings<A, B> {
   private int playerElo1;
   private int playerElo2;

   public Ratings(int playerElo1, int playerElo2) {
      this.playerElo1 = playerElo1;
      this.playerElo2 = playerElo2;
   }

   public static Ratings<Integer, Integer> of(int playerElo1, int playerElo2) {
      return new Ratings<Integer, Integer>(playerElo1, playerElo2);
   }

   public int player(int playerNumber) {
      switch (playerNumber) {
         case 1 -> {
            return this.playerElo1;
         }
         case 2 -> {
            return this.playerElo2;
         }
         default -> throw new RuntimeException("The only params you can provide for player(int playerNumber) are 0, 1");
      }
   }

   public Ratings<A, B> playerElo1(int playerElo1) {
      this.playerElo1 = playerElo1;
      return this;
   }

   public Ratings<A, B> playerElo2(int playerElo2) {
      this.playerElo2 = playerElo2;
      return this;
   }
}
