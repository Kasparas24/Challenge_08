import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<Card> deck = Card.getStandardDeck();
//        Create players
        int numberOfPlayers = 2;
        System.out.println("Players in the game " + numberOfPlayers);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.add(new Player(i, deck.size()));
        }

//        Prepare cards
        Collections.shuffle(deck);
        Card.printDeck(deck, "Deck after shuffle:");
        Collections.rotate(deck, new Random().nextInt((int) Math.round(deck.size() * 0.25), (int) Math.round(deck.size() * 0.75)));
        Card.printDeck(deck, "Deck after rotation:");
        while (!deck.isEmpty()) {
            for (Player player : players) {
                if (!deck.isEmpty()) {
                    Card card = deck.removeFirst();
                    if (card != null) player.collectCardToDeck(card);
                }
            }
        }
        for (Player player : players) {
            Card.printDeck(player.getPlayDeck(), player.getName() + " game deck", 2);
        }

        System.out.println("-----\nStart the game\n------");
//      Play
        Player winner = null;
        while (winner == null) {
            System.out.println("-----\nNew round\n------");
            Round round = new Round(new HashMap<>(), new ArrayList<>());
//            Drop cards on the table
            for (Player player : players) {
                Card playCard = player.drawCard();
                if (playCard != null) round.addInitialPlayerCard(player, playCard);
            }
//            Compare winner, if war, play war
            while (true) {
                List<Integer> allRanks = round.gameCard().values().stream().map(Card::rank).toList();
                int maxRank = Collections.max(allRanks);
                int maxRankFrequency = Collections.frequency(allRanks, maxRank);

                if (maxRankFrequency > 1) {
                    List<Player> atWar = round.gameCard().entrySet().stream()
                            .filter(entry -> entry.getValue().rank() == maxRank)
                            .map(Map.Entry::getKey)
                            .toList();
                    System.out.println(atWar.size() + " card(s) at war");
                    System.out.println("---War---");
                    atWar.forEach(round::playWar);
                    round.moveCardsNotAtWarToRoundDeck(atWar);
                } else {
                    System.out.println("No cards with equal rank");
                    break;
                }
            }

//            Winner collects all
            round.getSortedGameDeck().keySet().stream().toList().getLast().collectAllRoundCards(round);

//            Print
            System.out.println("Interim results:");
            for (Player player : players) {
                player.printStats();
            }

//            Playing until one player collects all the cards
            List<Player> playersStillActive = players.stream().filter(Player::hasCards).toList();

            if (playersStillActive.size() == 1) {
                winner = playersStillActive.getFirst();
                System.out.println("----------------\n--Game winner - " + winner.getName());
            } else {
                System.out.println("Game continues");
            }
        }
        System.out.println("-----\nGame finished\n------");
    }
}