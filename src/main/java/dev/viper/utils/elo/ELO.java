package dev.viper.utils.elo;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public class ELO {
   public static @NotNull List<Integer> elo(@NotNull Ratings<Integer, Integer> ratings, @NotNull Winner winner, int kFactor) {
      int rating1 = ratings.player(1);
      int rating2 = ratings.player(2);
      double expectedScore1 = (double)1.0F / ((double)1.0F + Math.pow((double)10.0F, (double)(rating2 - rating1) / (double)400.0F));
      double expectedScore2 = (double)1.0F - expectedScore1;
      int score1 = winner.equals(Winner.ONE) ? 1 : 0;
      int score2 = winner.equals(Winner.TWO) ? 1 : 0;
      int newRating1 = (int)Math.round((double)rating1 + (double)kFactor * ((double)score1 - expectedScore1));
      int newRating2 = (int)Math.round((double)rating2 + (double)kFactor * ((double)score2 - expectedScore2));
      if (newRating1 < 0) {
         newRating1 = 0;
      }

      if (newRating2 < 0) {
         newRating2 = 0;
      }

      return List.of(newRating1, newRating2);
   }
}
