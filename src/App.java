import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class App {

    // 列挙型

    enum Rank {
        ACE(1, "A"), 
        DEUCE(2, "2"), 
        TREY(3, "3"), 
        CATER(4, "4"), 
        CINQUE(5, "5"), 
        SICE(6, "6"), 
        SEVEN(7, "7"),
        EIGHT(8, "8"), 
        NINE(9, "9"), 
        TEN(10, "10"), 
        JACK(11, "J"), 
        QUEEN(12, "Q"), 
        KING(13, "K");

        public final int number;
        public final String name;

        public static final String RANK_RANGE = "2～10、A、J、Q、K";

        private Rank(final int num, final String setName) {
            number = num;
            name = setName;
        }

        public static Rank first() {
            return ACE;
        }

        public static Rank last() {
            return KING;
        }

        public static Rank getRankFromNumber(int num) {

            for (Rank rank : Rank.values()) {

                if (rank.number == num) {

                    return rank;
                }
            }

            return null;
        }

        public static boolean isMatchFromNumber(int formNum) {

            for (Rank rank : Rank.values()) {

                if (rank.number == formNum) {

                    return true;
                }
            }

            return false;
        }
    }

    enum Unit {
        PLAYER(0, "あなた"), 
        DEALER(1, "ディーラー");

        public final int number;
        public final String name;

        private Unit(int setNum, String setName) {
            number = setNum;
            name = setName;
        }
    }

    enum Reply {
        YES("Y", "y"), 
        NO("N", "n");

        public final String[] names;

        public static final String ENUM_RANGE = "Y/N";

        private Reply(String... setNames) {
            names = setNames;
        }

        public static boolean isMatchFromName(String fromName) {

            for (Reply reply : Reply.values()) {

                for (String name : reply.names) {

                    if (name.equals(fromName)) {

                        return true;
                    }
                }
            }

            return false;
        }

        public static Reply getReplyFromName(String fromName) {

            for (Reply reply : Reply.values()) {

                for (String name : reply.names) {

                    if (name.equals(fromName)) {

                        return reply;
                    }
                }
            }

            throw new IllegalArgumentException();
        }
    }

    enum GameResult {
        NONE, 
        WIN, 
        LOSE, 
        DRAW, 
        WIN_BLACKJACK,
    }

    // 変数

    private static Random random;
    private static Scanner scanner;

    private static List<List<Rank>> handCardsList;

    private static int coinNum;
    private static int bettingCoins;

    // 定数

    private static final int COURT_CARDS_SCORE = 10;
    private static final int ACE_LARGE_SCORE = 11;
    private static final int ACE_SMALL_SCORE = 1;
    private static final int MAX_SCORE = 21;
    private static final int DEALER_HIT_CONTINUE_LINE = 17;
    private static final int FIRST_CARDS = 2;

    private static final int FIRST_COINS = 100;
    private static final int BET_COINS = 10;
    private static final int WIN_RATE = 2;
    private static final int WIN_BLACKJACK_RATE = 3;
    private static final int LOSE_RATE = 0;
    private static final int DRAW_RATE = 1;

    private static final String MESSAGE_FOR_BLANK = "";

    private static final String MESSAGE_FORMAT_FOR_HIT_CARD = "%sに「%s」が配られました。";
    private static final String MESSAGE_FORMAT_FOR_SCORE = "%sの合計は %d(%s) です。";

    private static final String MESSAGE_FORMAT_FOR_ASK_HIT = "もう1枚カードを引きますか？(%s)：";
    private static final String MESSAGE_FORMAT_FOR_ERROR_FORMAT = "注意：%sで入力してください。";

    private static final String MESSAGE_FOR_MAX_SCORE = "21になりました！";
    private static final String MESSAGE_FOR_BLACKJACK = "ブラックジャック！";
    private static final String MESSAGE_FORMAT_FOR_BUST = "%sはバストしました……";

    private static final String MESSAGE_FOR_RESULT = "最終結果";
    private static final String MESSAGE_FOR_RESULT_WIN = "あなたの勝ちです！";
    private static final String MESSAGE_FOR_RESULT_LOSE = "あなたの負けです……";
    private static final String MESSAGE_FOR_RESULT_DRAW = "引き分けです。";

    private static final String MESSAGE_FORMAT_FOR_FIRST_COINS = "最初の手持ちは%dコインです。";
    private static final String MESSAGE_FORMAT_FOR_BET_COINS = "1プレイ%dコインです。";
    private static final String MESSAGE_FOR_EXIT_TIMING = "コインが無くなったら終了です。";

    private static final String MESSAGE_FOR_BET = "ベット";
    private static final String MESSAGE_FORMAT_FOR_CHANGES_COIN = "%+dコイン";
    private static final String MESSAGE_FORMAT_FOR_NOW_COINS = "現在の手持ちは %dコイン です。";
    private static final String MESSAGE_FOR_LOST_COINS = "手持ちのコインが無くなりました……";

    static {
        init();
    }

    public static void main(String[] args) {

        playBlackjack();

        fin();
    }

    private static void init() {

        random = new Random();
        scanner = new Scanner(System.in);

        handCardsList = new ArrayList<>();
        handCardsList.add(new ArrayList<>());
        handCardsList.add(new ArrayList<>());

        coinNum = FIRST_COINS;
    }

    private static void fin() {
        scanner.close();
    }

    // 関数

    private static void playBlackjack() {

        dispExplanation();

        while (canNextGame()) {

            startProcess();

            playerTurn();

            dealerTurn();

            resultProcess();

            dispNowCoinNum();

            if (canNextGame()) {
                replayProcess();
            }
        }

        dispLostCoin();
    }

    private static boolean canNextGame() {
        return coinNum != 0;
    }

    private static void dispExplanation() {

        println(MESSAGE_FORMAT_FOR_FIRST_COINS, FIRST_COINS);
        println(MESSAGE_FORMAT_FOR_BET_COINS, BET_COINS);
        println(MESSAGE_FOR_EXIT_TIMING);
    }

    private static void startProcess() {

        dipsBlankLine();

        bet();

        for (int deal = 0; deal < FIRST_CARDS; deal++) {
            hitCardAllUnit();
        }

        dipsBlankLine();
    }

    private static void playerTurn() {

        dispScore(Unit.DEALER);
        dispScore(Unit.PLAYER);

        int score = getScore(Unit.PLAYER);
        Reply reply = Reply.YES;
        while (needHit(reply)) {

            if (isMaxScore(score)) {
                int cardsNum = getCountCard(Unit.PLAYER);
                dispMaxScore(score, cardsNum);
                break;
            }

            reply = inputPlayerReply();
            if (reply == Reply.NO) {
                break;
            }

            dipsBlankLine();

            hitCard(Unit.PLAYER);
            dispScore(Unit.DEALER);
            dispScore(Unit.PLAYER);

            score = getScore(Unit.PLAYER);
            if (isBust(score)) {
                dispBust(Unit.PLAYER);
                break;
            }
        }

        dipsBlankLine();
    }

    private static void dealerTurn() {

        int score = getScore(Unit.DEALER);

        if (!needHitDealer(score)) {
            return;
        }

        while (needHitDealer(score)) {

            hitCard(Unit.DEALER);
            dispScore(Unit.DEALER);

            score = getScore(Unit.DEALER);
            if (isBust(score)) {
                dispBust(Unit.DEALER);
                break;
            }

            if (needHitDealer(score)) {
                dipsBlankLine();
            }
        }

        dipsBlankLine();
    }

    private static void resultProcess() {

        dispResultMessage();
        dispScore(Unit.PLAYER);
        dispScore(Unit.DEALER);

        dipsBlankLine();

        GameResult result = getGameResult(Unit.PLAYER, Unit.DEALER);
        dispGameResult(result);

        int rtrnCoins = getReturnCoins(result);
        changesCoin(rtrnCoins);
        dispChangesCoin(rtrnCoins);
        dipsBlankLine();
    }

    private static void dispNowCoinNum() {
        println(MESSAGE_FORMAT_FOR_NOW_COINS, coinNum);
    }

    private static void replayProcess() {

        for (List<Rank> handCards : handCardsList) {
            handCards.clear();
        }
    }

    private static void dispLostCoin() {
        println(MESSAGE_FOR_LOST_COINS);
    }

    private static void bet() {

        bettingCoins = getBetCoin();
        changesCoin(-bettingCoins);

        dispBet();
        dispChangesCoin(-bettingCoins);
        dipsBlankLine();
    }

    private static int getBetCoin() {
        return coinNum < BET_COINS ? coinNum : BET_COINS;
    }

    private static void dispBet() {
        println(MESSAGE_FOR_BET);
    }

    private static void hitCardAllUnit() {

        for (Unit unit : Unit.values()) {
            hitCard(unit);
        }
    }

    private static Reply inputPlayerReply() {

        Reply reply = null;
        while (!isSelectedReply(reply)) {

            dispAskHit();

            String input = scanner.nextLine();

            if (!Reply.isMatchFromName(input)) {
                dispErrorFormat();
                continue;
            }

            reply = Reply.getReplyFromName(input);
        }

        return reply;
    }

    private static boolean isSelectedReply(Reply reply) {
        return reply != null;
    }

    private static void dispAskHit() {
        print(MESSAGE_FORMAT_FOR_ASK_HIT, Reply.ENUM_RANGE);
    }

    private static void dispErrorFormat() {
        println(MESSAGE_FORMAT_FOR_ERROR_FORMAT, Reply.ENUM_RANGE);
        dipsBlankLine();
    }

    private static boolean needHit(Reply reply) {
        return reply != Reply.NO;
    }

    private static void dispMaxScore(int score, int cardsNum) {

        if (isFirstHands(cardsNum)){
            println(MESSAGE_FOR_BLACKJACK);
            return;
        }

        println(MESSAGE_FOR_MAX_SCORE);
    }

    private static void dispBust(Unit unit) {
        println(MESSAGE_FORMAT_FOR_BUST, unit.name);
    }

    private static boolean needHitDealer(int score) {
        return score <= DEALER_HIT_CONTINUE_LINE;
    }

    private static void dispResultMessage() {
        println(MESSAGE_FOR_RESULT);
    }

    private static GameResult getGameResult(Unit myUnit, Unit opponentUnit) {

        int myScore = getScore(myUnit);
        int opponentScore = getScore(opponentUnit);
        int myHandCardNum = getCountCard(myUnit);
        int opponentHandCardNum = getCountCard(opponentUnit);

        if (isBust(myScore)) {
            return GameResult.LOSE;
        }

        if (isBlackjack(myScore, myHandCardNum)) {

            if (isBlackjack(opponentScore, opponentHandCardNum)) {
                return GameResult.DRAW;
            }

            return GameResult.WIN_BLACKJACK;
        }

        if (isBust(opponentScore)) {
            return GameResult.WIN;
        }

        if (isBlackjack(opponentScore, opponentHandCardNum)) {
            return GameResult.LOSE;
        }

        if (myScore == opponentScore) {
            return GameResult.DRAW;
        }

        if (myScore > opponentScore) {
            return GameResult.WIN;
        }

        return GameResult.LOSE;
    }

    private static void dispGameResult(GameResult result) {

        switch (result) {

        case WIN:
        case WIN_BLACKJACK:
            dispPlayerWin();
            break;

        case LOSE:
            dispPlayerLose();
            break;

        default:
        case DRAW:
            dispPlayerDraw();
            break;
        }
    }

    private static void dispPlayerWin() {
        println(MESSAGE_FOR_RESULT_WIN);
    }

    private static void dispPlayerLose() {
        println(MESSAGE_FOR_RESULT_LOSE);
    }

    private static void dispPlayerDraw() {
        println(MESSAGE_FOR_RESULT_DRAW);
    }

    private static int getReturnCoins(GameResult result){

        int rate;
        switch (result) {

        case WIN:
            rate = WIN_RATE;
            break;

        case WIN_BLACKJACK:
            rate = WIN_BLACKJACK_RATE;
            break;

        case LOSE:
            rate = LOSE_RATE;
            break;

        default:
        case DRAW:
            rate = DRAW_RATE;
            break;
        }

        return bettingCoins * rate;
    }

    // 複数個所で使う関数

    private static void hitCard(Unit unit) {

        List<Rank> handCards = getHandCards(unit);
        hitCard(unit, handCards);
    }

    private static List<Rank> getHandCards(Unit unit) {
        return handCardsList.get(unit.number);
    }

    private static void hitCard(Unit unit, List<Rank> cardList) {

        Rank card = createRandomRank();
        dispHitCard(unit, card);
        cardList.add(card);
    }

    private static void dispHitCard(Unit unit, Rank card) {
        println(MESSAGE_FORMAT_FOR_HIT_CARD, unit.name, card.name);
    }

    private static void dispScore(Unit unit) {

        List<Rank> handCards = getHandCards(unit);
        dispScore(unit, handCards);
    }

    private static void dispScore(Unit unit, List<Rank> cardList) {

        int score = getScore(cardList);
        String dispCards = getCardListString(cardList);

        println(MESSAGE_FORMAT_FOR_SCORE, unit.name, score, dispCards);
    }

    private static int getScore(List<Rank> cardList) {

        int score = 0;
        int aceCount = 0;

        for (Rank rank : cardList) {

            int cardScore = getCardScore(rank);
            score += cardScore;

            if (rank == Rank.ACE) {
                aceCount++;
            }
        }

        int aceSub = ACE_LARGE_SCORE - ACE_SMALL_SCORE;
        for (int i = 0; i < aceCount; i++) {

            if (!isBust(score + aceSub)) {
                score += aceSub;
                continue;
            }

            break;
        }

        return score;
    }

    private static int getCardScore(Rank rank) {

        switch (rank) {

        case ACE:
            return ACE_SMALL_SCORE;

        case JACK:
        case QUEEN:
        case KING:
            return COURT_CARDS_SCORE;

        default:
            return rank.number;
        }
    }

    private static int getScore(Unit unit) {
        return getScore(getHandCards(unit));
    }

    private static void changesCoin(int changesNum) {
        coinNum += changesNum;
    }

    private static int getCountCard(Unit unit) {
        return getHandCards(unit).size();
    }

    private static String getCardListString(List<Rank> cardList) {

        String str = "";
        int index = 0;
        while (index < cardList.size()) {

            str += cardList.get(index).name;

            index++;
            if (index < cardList.size()) {
                str += ",";
            }
        }

        return str;
    }

    private static boolean isBust(int score) {
        return score > MAX_SCORE;
    }

    private static boolean isMaxScore(int score) {
        return score == MAX_SCORE;
    }

    private static boolean isFirstHands(int cardsNum){
        return cardsNum == FIRST_CARDS;
    }

    private static boolean isBlackjack(int score, int handCardNum) {
        return isMaxScore(score) && isFirstHands(handCardNum);
    }

    private static Rank createRandomRank() {
        int number = createRandomNumber(Rank.first().number, Rank.last().number);
        return Rank.getRankFromNumber(number);
    }

    private static void dispChangesCoin(int ChangeNum) {
        println(MESSAGE_FORMAT_FOR_CHANGES_COIN, ChangeNum);
    }

    private static void dipsBlankLine() {
        println(MESSAGE_FOR_BLANK);
    }

    // 汎用関数

    private static void print(String str, Object... args) {
        System.out.print(String.format(str, args));
    }

    private static void println(String str) {
        System.out.println(str);
    }

    private static void println(String str, Object... args) {
        System.out.println(String.format(str, args));
    }

    private static int createRandomNumber(int min, int max) {

        int range = max - min + 1;
        return random.nextInt(range) + min;
    }
}
