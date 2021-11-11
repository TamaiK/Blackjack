import java.util.ArrayList;
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
        YES("Y"), 
        NO("N");

        public final String name;

        public static final String ENUM_RANGE = "Y/N";

        private Reply(String setName) {
            name = setName;
        }

        public static boolean isMatchFromName(String fromName) {

            for (Reply reply : Reply.values()) {

                if (reply.name.equals(fromName)) {

                    return true;
                }
            }

            return false;
        }

        public static Reply getReplyFromName(String fromName) {

            for (Reply reply : Reply.values()) {

                if (reply.name.equals(fromName)) {

                    return reply;
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

    private static ArrayList<ArrayList<Rank>> handCardsList;

    // 定数

    private static final int COURT_CARDS_SCORE = 10;
    private static final int ACE_LARGE_SCORE = 11;
    private static final int ACE_SMALL_SCORE = 1;
    private static final int MAX_SCORE = 21;
    private static final int DEALER_HIT_CONTINUE_LINE = 17;

    private static final String MESSAGE_FOR_BLANK = "";

    private static final String MESSAGE_FORMAT_FOR_HIT_CARD = "%sに「%s」が配られました。";
    private static final String MESSAGE_FORMAT_FOR_SCORE = "%sの合計は %d(%s) です。";

    private static final String MESSAGE_FORMAT_FOR_ASK_HIT = "もう1枚カードを引きますか？(%s)：";
    private static final String MESSAGE_FORMAT_FOR_ERROR_FORMAT = "注意：%sで入力してください。";

    private static final String MESSAGE_FOR_MAX_SCORE = "21になりました！";
    private static final String MESSAGE_FORMAT_FOR_BUST = "%sはバストしました……";

    private static final String MESSAGE_FOR_RESULT = "最終結果";
    private static final String MESSAGE_FOR_RESULT_WIN = "あなたの勝ちです！";
    private static final String MESSAGE_FOR_RESULT_LOSE = "あなたの負けです……";
    private static final String MESSAGE_FOR_RESULT_DRAW = "引き分けです。";

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

        handCardsList = new ArrayList<ArrayList<Rank>>();
        handCardsList.add(new ArrayList<Rank>());
        handCardsList.add(new ArrayList<Rank>());
    }

    private static void fin() {
        scanner.close();
    }

    // 関数

    private static void playBlackjack() {

        startProcess();

        playerTurn();

        dealerTurn();

        resultProcess();
    }

    private static void startProcess() {

        hitCard(Unit.PLAYER);
        hitCard(Unit.DEALER);
        hitCard(Unit.PLAYER);
        hitCard(Unit.DEALER);

        dipsBlankLine();
    }

    private static void playerTurn() {

        dispScore(Unit.DEALER);
        dispScore(Unit.PLAYER);

        int score = getScore(Unit.PLAYER);
        Reply reply = Reply.YES;
        while (needHit(reply)) {

            if (isMaxScore(score)) {
                dispMaxScore(score);
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

    private static void dispMaxScore(int score) {
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

        if (isBust(myScore)) {
            return GameResult.LOSE;
        }

        if (isBust(opponentScore)) {
            return GameResult.WIN;
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

    // 複数個所で使う関数

    private static void hitCard(Unit unit) {

        ArrayList<Rank> handCards = getHandCards(unit);
        hitCard(unit, handCards);
    }

    private static ArrayList<Rank> getHandCards(Unit unit) {
        return handCardsList.get(unit.number);
    }

    private static void hitCard(Unit unit, ArrayList<Rank> cardList) {

        Rank card = createRandomRank();
        dispHitCard(unit, card);
        cardList.add(card);
    }

    private static void dispHitCard(Unit unit, Rank card) {
        println(MESSAGE_FORMAT_FOR_HIT_CARD, unit.name, card.name);
    }

    private static void dispScore(Unit unit) {

        ArrayList<Rank> handCards = getHandCards(unit);
        dispScore(unit, handCards);
    }

    private static void dispScore(Unit unit, ArrayList<Rank> cardList) {

        int score = getScore(cardList);
        String dispCards = getCardListString(cardList);

        println(MESSAGE_FORMAT_FOR_SCORE, unit.name, score, dispCards);
    }

    private static int getScore(ArrayList<Rank> cardList) {

        int score = 0;
        int aceCount = 0;

        for (Rank rank : cardList) {

            switch (rank) {

            case ACE:
                score += ACE_SMALL_SCORE;
                aceCount++;
                break;

            case JACK:
            case QUEEN:
            case KING:
                score += COURT_CARDS_SCORE;
                break;

            default:
                score += rank.number;
                break;
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

    private static int getScore(Unit unit) {
        return getScore(getHandCards(unit));
    }

    private static String getCardListString(ArrayList<Rank> cardList) {

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

    private static Rank createRandomRank() {
        int number = createRandomNumber(Rank.first().number, Rank.last().number);
        return Rank.getRankFromNumber(number);
    }

    private static void dipsBlankLine() {
        println(MESSAGE_FOR_BLANK);
    }

    // 汎用関数

    private static void print(String str) {
        System.out.print(str);
    }

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
