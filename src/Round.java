import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record Round(Map<Player, Card> gameCard, List<Card> roundDeck) {

    public void addInitialPlayerCard(Player player, Card playCard) {
        this.gameCard.put(player, playCard);
    }

    public void playWar(Player player) {
        player.recordAction("plays war");
        roundDeck.add(gameCard.remove(player));
        Card cardUpsideDown = player.drawCard();
        if (cardUpsideDown != null)  {
            player.recordAction("adds " + cardUpsideDown + " upsidedown");
            roundDeck.add(cardUpsideDown);
        }
        Card cardForWar = player.drawCard();
        if (cardForWar != null) {
            player.recordAction("adds " + cardForWar + " for war");
            gameCard.put(player, cardForWar);
        }
    }

    public void moveCardsNotAtWarToRoundDeck(List<Player> playersAtWar) {
        List<Player> losers = gameCard.keySet().stream()
                .filter(player -> !playersAtWar.contains(player))
                .toList();
        for (Player loser : losers) {
            roundDeck.add(gameCard.remove(loser));
        }
    }

    public Map<Player, Card> getGameCards() {
        return gameCard;
    }

    public Map<Player, Card> getSortedGameDeck() {
        LinkedHashMap<Player, Card> sortedMap = new LinkedHashMap<>();
        ArrayList<Card> list = new ArrayList<>();
        for (Map.Entry<Player, Card> entry : gameCard.entrySet()) {
            list.add(entry.getValue());
        }
        list.sort(Comparator.comparingInt(Card::rank));

        for (Card card : list) {
            for (Map.Entry<Player, Card> entry : gameCard.entrySet()) {
                if (entry.getValue().equals(card)) {
                    sortedMap.put(entry.getKey(), card);
                }
            }
        }
        return sortedMap;
    }

}
