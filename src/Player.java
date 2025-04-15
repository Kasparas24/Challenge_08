import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private String name;
    private List<Card> playDeck = new ArrayList<>();
    private List<Card> pickedDeck = new ArrayList<>();
    private final int cardsInGame;

    public Player(int index, int cardsInGame) {
        this.name = "Player " + (index + 1);
        this.cardsInGame = cardsInGame;
    }

    public void collectCardToDeck(Card card) {
        playDeck.add(card);
    }

    public String getName() {
        return name;
    }

    public List<Card> getPlayDeck() {
        return playDeck;
    }

    public Card drawCard() {
        if (playDeck.isEmpty() && !pickedDeck.isEmpty()) {
            Collections.shuffle(pickedDeck);
            for (int i = 0; i < pickedDeck.size(); i++) {
                playDeck.add(pickedDeck.remove(i));

            }
        }
        if (playDeck.isEmpty()) {
            recordAction("is out without cards");
            return null;
        }
        Card card = playDeck.removeFirst();
        recordAction("draws " + card);
        return card;
    }

    public void collectAllRoundCards(Round round) {
        System.out.println(name + " won the round, collecting all the cards");
        pickedDeck.addAll(round.getGameCards().values());
        pickedDeck.addAll(round.roundDeck());
    }

    public boolean hasCards() {
        return !playDeck.isEmpty() || !pickedDeck.isEmpty();
    }

    public void recordAction(String action) {
        System.out.println(name + " " + action);
    }

    public void printStats() {
        System.out.println("---\n" + name + ":");
        if (hasCards()) {
            System.out.println("Cards in play deck: " + playDeck.size() + ", cards in picked deck: " + pickedDeck.size());
            System.out.println("Total cards: " + (playDeck.size() + pickedDeck.size()) + " out of " + cardsInGame);
        } else  {
            System.out.println(" - Out of the game without cards");
        }
    }
}
