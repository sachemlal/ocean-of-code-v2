import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 **/
class Player {

    static int mapWidth = 0;
    static int mapHeight = 0;

    private static void handleOpponentOrder(Map map, String opponentOrders) {
        String[] opponentOrdersList = opponentOrders.split(Pattern.quote("|"));
        Map.opponentLastMoves.clear();
        Map.opponentTorpedoCell = null;
        Map.hasOpponentTrigger = false;
        Map.hasOpponentTorpedo = false;
        Map.hasOpponentSurface = false;

        for (String opponentOrder : opponentOrdersList) {
            if (opponentOrder.startsWith("MOVE")) {
                Map.moveThenTorpedo = true;
                break;
            }
            if (opponentOrder.startsWith("TORPEDO")) {
                Map.moveThenTorpedo = false;
                break;
            }
        }

        for (String opponentOrder : opponentOrdersList) {
            int surfaceTestedByOpp = Integer
                    .parseInt(opponentOrder.substring(opponentOrder.length() - 1));
            if (opponentOrder.startsWith("SURFACE")) {
                Map.opponentSurface = surfaceTestedByOpp;
                Map.opponentSurfaceDone = true;
                Map.oppLastKnownLife--;
                Map.opponentLastKnownSurface = Map.opponentSurface;
                Map.opponentMoves.clear();
                Map.resetSearch(
                        Map.opponentXYPaths,
                        Map.opponentLastMoves,
                        Map.opponentCells);
                Map.hasOpponentSurface = true;
            }
            boolean silenceDone = false;
            if (opponentOrder.startsWith("SILENCE")) {
                Map.opponentHitCell = null;
                Map.opponentMoves.add(Map.X);
                Map.opponentLastMoves.add(Map.X);
                silenceDone = true;
            }
            if (opponentOrder.startsWith("TORPEDO")) {
                String[] opponentTorpedo = opponentOrder.substring(8).split(" ");
                Map.opponentTorpedoCell = new Cell(Integer.parseInt(opponentTorpedo[0]),
                        Integer.parseInt(opponentTorpedo[1]));
                Map.hasOpponentTorpedo = true;
            }
            if (opponentOrder.startsWith("MOVE")) {
                String opponentMove = opponentOrder.substring(opponentOrder.length() - 1);
                Integer oppMove = Map.DTIM.get(opponentMove);
                Map.opponentMoves.add(oppMove);
                Map.opponentLastMoves.add(oppMove);
                if (!silenceDone && !Map.moveThenTorpedo && Map.opponentTorpedoCell != null) {
                    Map.opponentTorpedoCell = Map
                            .findCellByDirection(Map.opponentTorpedoCell.getX(), Map.opponentTorpedoCell.getY(),
                                    oppMove);
                }
            }
            if (opponentOrder.startsWith("MINE")) {
                Map.opponentNbrOfMine = Map.opponentNbrOfMine + 1;
            }
            if (opponentOrder.startsWith("TRIGGER")) {
                Map.hasOpponentTrigger = true;
                Map.opponentNbrOfMine = Map.opponentNbrOfMine - 1;
            }
            if (opponentOrder.startsWith("SONAR")) {
                Map.sonarUsedByOpp = true;
                Map.surfaceTestedByOpp = surfaceTestedByOpp;
            }
        }
    }

    private static boolean isOpponentHit(int oppLastKnownLife, int oppCurrentLife) {
        return oppCurrentLife < oppLastKnownLife;
    }

    private static void assertCode() {

        Map map = new Map(15, 15);
        map.addIslandCell(1, 0);
        map.addIslandCell(2, 0);
        map.addIslandCell(3, 0);
        map.addIslandCell(1, 1);
        map.addIslandCell(2, 1);
        map.addIslandCell(3, 1);
        map.addIslandCell(13, 1);
        map.addIslandCell(14, 1);
        map.addIslandCell(13, 2);
        map.addIslandCell(14, 2);
        map.addIslandCell(4, 4);
        map.addIslandCell(5, 4);
        map.addIslandCell(4, 5);
        map.addIslandCell(5, 5);
        map.addIslandCell(7, 8);
        map.addIslandCell(8, 8);
        map.addIslandCell(10, 8);
        map.addIslandCell(11, 8);
        map.addIslandCell(7, 9);
        map.addIslandCell(8, 9);
        map.addIslandCell(10, 9);
        map.addIslandCell(11, 9);
        map.addIslandCell(3, 10);
        map.addIslandCell(4, 10);
        map.addIslandCell(5, 10);
        map.addIslandCell(10, 10);
        map.addIslandCell(11, 10);
        map.addIslandCell(3, 11);
        map.addIslandCell(4, 11);
        map.addIslandCell(5, 11);
        map.addIslandCell(10, 11);
        map.addIslandCell(11, 11);
        map.addIslandCell(3, 12);
        map.addIslandCell(4, 12);
        map.addIslandCell(5, 12);
        map.addIslandCell(4, 13);
        map.addIslandCell(5, 13);
        map.addIslandCell(11, 13);
        map.addIslandCell(12, 13);
        map.addIslandCell(11, 14);
        map.addIslandCell(12, 14);

        HashMap<Integer, List<List<Integer>>> opponentXYPaths = new HashMap<>();

        HashSet<Integer> opponentCells;
        List<String> opponentMoves = Arrays
                .asList("E", "X", "S", "X", "E", "X", "S", "X", "E", "X", "S", "X", "E", "X", "S", "X", "E",
                        "X", "S", "X", "E", "X", "S", "X");
    /*List<String> opponentMoves = Arrays
        .asList("E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "X");*/
        int i = 1;
        long start;
        long elapsed;
        for (String oppMove : opponentMoves) {
            int sizeBefore = opponentXYPaths.size();
            int nbrOfPaths = Map.calculateNbrOfPaths(opponentXYPaths);
            start = System.currentTimeMillis();
            Map.findOpponentCellsAdvanced(
                    opponentXYPaths,
                    Collections.singletonList(Map.DTIM.get(oppMove)),
                    0,
                    null,
                    null,
                    null,
                    0
            );
            elapsed = System.currentTimeMillis() - start;
            System.err
                    .printf("%s -> nbrOfPaths (%s) elapsed (%s) %n", i, nbrOfPaths, elapsed);
            i++;
        }

        stopExecution();
    }

    private static void stopExecution() {
        if (true) {
            throw new RuntimeException("stop here !");
        }
    }

    private static void debugTime(String tag, long start, boolean debugEnabled) {
        if (debugEnabled) {
            long elapsed = System.currentTimeMillis() - start;
            System.err.printf("elapsed %s -> (%s ms)%n", tag, elapsed);
        }
    }

    private static String doMove(String myOrder, String powerToCharge, Cell nextCell) {
        String pipe = "".equals(myOrder) ? "" : "|";
        myOrder +=
                pipe + String.format("MOVE %s %s", Map.ITDM.get(nextCell.getPreviousDirection()),
                        powerToCharge);
        Map.myVisibleMoves.add(nextCell.getPreviousDirection());
        Map.visitedCells.add(Map.getKeyFromCoordinates(nextCell));
        Map.myVisibleLastMoves.add(nextCell.getPreviousDirection());
        return myOrder;
    }

    private static String doSpecialMove(String myOrder, String powerToCharge,
                                        Integer previousDirection) {
        String pipe = "".equals(myOrder) ? "" : "|";
        myOrder +=
                pipe + String.format("MOVE %s %s", Map.ITDM.get(previousDirection),
                        powerToCharge);
        Map.myVisibleMoves.add(previousDirection);
        Map.myVisibleLastMoves.add(previousDirection);
        return myOrder;
    }

    private static String doSpecialSilence(String myOrder, Integer previousDirection, int nbrMoves) {
        Map.needSilence = false;
        String pipe = "".equals(myOrder) ? "" : "|";
        myOrder +=
                pipe + String.format("SILENCE %s %s",
                        Map.ITDM.get(previousDirection),
                        nbrMoves);

        Map.myVisibleMoves.add(Map.X);
        Map.myVisibleLastMoves.add(Map.X);

        return myOrder;
    }

    private static String doSpecialTargeting(
            String myOrder,
            String powerToCharge,
            Cell nextCell,
            Cell opponentCell
    ) {

        if (Map.specialMoveBeforeSilence) {
            if (Map.specialNbrMove == 1) {
                Map.isMyMoveDone = true;
                myOrder = doSpecialMove(myOrder, powerToCharge, Map.specialMoveDirection);
            }
            if (Map.specialNbrSilence >= 1) {
                Map.isMySilenceDone = true;
                myOrder = doSpecialSilence(myOrder, Map.specialSilenceDirection, Map.specialNbrSilence);
            }
        } else {
            if (Map.specialNbrSilence >= 1) {
                Map.isMySilenceDone = true;
                myOrder = doSpecialSilence(myOrder, Map.specialSilenceDirection, Map.specialNbrSilence);
            }
            if (Map.specialNbrMove == 1) {
                Map.isMyMoveDone = true;
                myOrder = doSpecialMove(myOrder, powerToCharge, Map.specialMoveDirection);
            }
        }

        myOrder = doTorpedo(myOrder, opponentCell, nextCell, true);

        Map.myVisibleMoves.add(nextCell.getPreviousDirection());
        Map.visitedCells.add(Map.getKeyFromCoordinates(nextCell));
        Map.myVisibleLastMoves.add(nextCell.getPreviousDirection());

        return myOrder;
    }

    private static String doTorpedo(String myOrder, Cell opponentHitCell, Cell nextCell, boolean
            myVisibleMoveThenTorpedo) {
        String pipe = "".equals(myOrder) ? "" : "|";
        myOrder +=
                pipe + String.format("TORPEDO %s %s", opponentHitCell.getX(), opponentHitCell.getY());
        Map.myVisibleTorpedoCell = myVisibleMoveThenTorpedo ? opponentHitCell :
                Map.findCellByDirection(opponentHitCell.getX(),
                        opponentHitCell.getY(), nextCell.getPreviousDirection());
        Map.opponentHitCells.add(Map.getKeyFromCoordinates(opponentHitCell));
        Map.didITorpedo = true;

        return myOrder;
    }

    private static String doSurface() {

        String myOrder = "SURFACE";

        Map.myVisibleSurface = Map.findSurfaceByCell(Map.myCurrentCell.getX(),
                Map.myCurrentCell.getY());

        for (Integer xyHash : Map.myPossibleCellsFoundByOpp) {
            int xPos = xyHash % Map.width;
            int yPos = (xyHash - xPos) / Map.width;
            Map.myVisibleXYPaths
                    .put(Map.getKeyFromCoordinates(xPos, yPos),
                            Collections.singletonList(new ArrayList<>()));
        }

        Map.myVisibleStartingXYPositions = Map.myPossibleCellsFoundByOpp;
        Map.visitedCells.clear();
        Map.visitedCells.add(Map.getKeyFromCoordinates(Map.myCurrentCell));
        Map.myVisibleMoves.clear();
        Map.myVisibleLastMoves.clear();
        Map.didISurface = true;

        return myOrder;
    }

    private static String doSilence(String myOrder, Cell nextCell, int myLife) {
        Map.needSilence = false;
        Map.lastKnownLife = myLife;
        String pipe = "".equals(myOrder) ? "" : "|";
        Cell loopCell = nextCell.cloneCell();

        int nbrMoves = 0;
        int randomNbrMoves = (int) (Math.random() * 2);
        for (int i = 1; i < randomNbrMoves; i++) {
            loopCell = Map.findCellByDirection(loopCell.getX(), loopCell.getY(),
                    nextCell.getPreviousDirection());
            if (loopCell == null || !Map.isAccessibleCell(loopCell)) {
                break;
            }
            nbrMoves++;
            Map.visitedCells.add(Map.getKeyFromCoordinates(loopCell));
        }

        myOrder +=
                pipe + String.format("SILENCE %s %s",
                        Map.ITDM.get(nextCell.getPreviousDirection()),
                        nbrMoves);

        Map.myVisibleMoves.add(Map.X);
        Map.myVisibleLastMoves.add(Map.X);

        return myOrder;
    }

    private static String doTrigger(String myOrder, Cell mineHitCell) {
        String pipe = "".equals(myOrder) ? "" : "|";

        myOrder += pipe + String.format("TRIGGER %s %s", mineHitCell.getX(),
                mineHitCell.getY());
        Map.mineCells.remove(Map.getKeyFromCoordinates(mineHitCell));
        Map.opponentHitCells.add(Map.getKeyFromCoordinates(mineHitCell));
        Map.didITrigger = true;

        return myOrder;
    }

    private static String doMine(String myOrder, Cell nextCell) {
        String pipe = "".equals(myOrder) ? "" : "|";
        myOrder += pipe + String.format("MINE %s", Map.ITDM.get(nextCell.getPreviousDirection()));
        Map.mineCells.add(Map.getKeyFromCoordinates(nextCell));

        return myOrder;
    }

    public static void main(String[] args) {

        //assertCode();

        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        int height = in.nextInt();

        Map map = new Map(width, height);

        mapWidth = width;
        mapHeight = height;
        int previousOppLife = 6;
        int previousMyVisibleLife = 6;
        boolean debugEnabled = false;

        if (in.hasNextLine()) {
            in.nextLine();
        }

        for (int y = 0; y < height; y++) {
            String line = in.nextLine();
            for (int x = 0; x < line.length(); x++) {
                Character xChar = 'x';
                if (xChar.equals(line.charAt(x))) {
                    map.addIslandCell(x, y);
                    System.err.printf("map.addIslandCell(%s, %s);%n", x, y);
                }
            }
        }

        // Write an action using System.out.println()
        // To debug: System.err.println("Debug messages...");
        //System.out.println("7 7");

        Cell startCell = map.getStartCell();
        System.out.printf("%s %s%n", startCell.getX(), startCell.getY());
        Map.visitedCells.add(Map.getKeyFromCoordinates(startCell));
        Map.myCurrentCell = startCell;

        for (int x = 0; x < Map.width; x++) {
            for (int y = 0; y < Map.height; y++) {
                if (Map.isAccessibleToOpponent(x, y)) {
                    Map.opponentXYPaths
                            .put(Map.getKeyFromCoordinates(x, y),
                                    Collections.singletonList(new ArrayList<>()));
                    Map.myVisibleXYPaths
                            .put(Map.getKeyFromCoordinates(x, y),
                                    Collections.singletonList(new ArrayList<>()));
                }
            }
        }

        int myPreviousDirection = startCell.getPreviousDirection();
        Map.lastHorizontalDirection = Map.W;
        Map.lastVerticalDirection = Map.S;

        // game loop
        while (true) {

            Map.sonarUsedByOpp = false;
            Map.isMyMoveDone = false;
            Map.isMySilenceDone = false;
            Map.opponentTargetedCell = null;
            Map.specialTargeting = false;
            Map.specialNbrMove = 0;
            Map.specialNbrSilence = 0;

            String pipe = "";

            int x = in.nextInt();
            int y = in.nextInt();
            int myLife = in.nextInt();
            int oppLife = in.nextInt();
            int torpedoCooldown = in.nextInt();
            int sonarCooldown = in.nextInt();
            int silenceCooldown = in.nextInt();
            int mineCooldown = in.nextInt();
            String sonarResult = in.next();
            if (in.hasNextLine()) {
                in.nextLine();
            }
            String opponentOrders = in.nextLine();

            long startTime = System.currentTimeMillis();

            Map.torpedoCooldown = torpedoCooldown;
            Map.sonarCooldown = sonarCooldown;
            Map.silenceCooldown = silenceCooldown;
            Map.mineCooldown = mineCooldown;
            Map.myLife = myLife;
            Map.oppLife = oppLife;
            Map.myCurrentCell = new Cell(x, y, myPreviousDirection);
            boolean print = false;

            if (print) {
                System.err.printf("x,y : %s,%s%n", x, y);
                System.err.printf("myLife %s , oppLife %s%n", myLife, oppLife);
                System.err.printf("torpedoCooldown %s%n", torpedoCooldown);
                System.err.printf("sonarCooldown %s%n", sonarCooldown);
                System.err.printf("silenceCooldown %s%n", silenceCooldown);
                System.err.printf("mineCooldown %s%n", mineCooldown);
                System.err.printf("sonarResult %s%n", sonarResult);
                System.err.printf("opponentOrders %s%n", opponentOrders);
            }

            Map.opponentSurface = 0;
            Map.opponentTorpedoCell = null;

            handleOpponentOrder(map, opponentOrders);

            if (!isOpponentHit(previousOppLife, oppLife)
                    || Map.hasOpponentTorpedo
                    || Map.hasOpponentTrigger
                    || Map.hasOpponentSurface) {
                Map.opponentHitCells.clear();
            }
            previousOppLife = oppLife;
            boolean wasIShot = false;
            if (!isOpponentHit(previousMyVisibleLife, myLife)) {
                Map.myVisibleHitCells.clear();
            } else {
                wasIShot = true;
                Map.needSilence = true;
            }

            previousMyVisibleLife = myLife;

            Map.didITorpedo = false;
            Map.didITrigger = false;
            Map.didISurface = false;

            Map.findOpponentCellsAdvanced(
                    Map.opponentXYPaths,
                    Map.opponentLastMoves,
                    Map.opponentSurface,
                    Map.opponentTorpedoCell,
                    Map.opponentHitCells,
                    Map.opponentCells,
                    startTime
            );

            Map.opponentCells = Map.getPossibleCellsFoundByOpp(
                    Map.opponentXYPaths,
                    Map.opponentLastMoves,
                    Map.opponentCells,
                    startTime);

            Cell nextCell = map
                    .findNextAvailableAdvancedCell(Map.myCurrentCell, Map.opponentCells, silenceCooldown);
            Cell opponentCell = null;
            if (Map.opponentCells.size() == 1) {
                opponentCell = Map.getCellFromKey(Map.opponentCells.iterator().next());
            }

            Integer horizontalDirection = null;
            Integer verticalDirection = null;
            if (nextCell != null) {
                if (nextCell.getPreviousDirection().equals(Map.E)
                        || nextCell.getPreviousDirection().equals(Map.W)) {
                    horizontalDirection = nextCell.getPreviousDirection();
                }
                if (nextCell.getPreviousDirection().equals(Map.N)
                        || nextCell.getPreviousDirection().equals(Map.S)) {
                    verticalDirection = nextCell.getPreviousDirection();
                }

                if (verticalDirection != null && verticalDirection.equals(Map.N)
                        && Map.lastHorizontalDirection.equals(Map.E)) {
                    Map.watchDirection = false;
                }
                if (verticalDirection != null && verticalDirection.equals(Map.N)
                        && Map.lastHorizontalDirection.equals(Map.W)) {
                    Map.watchDirection = true;
                }

                if (horizontalDirection != null && (nextCell.getPreviousDirection().equals(Map.E)
                        || nextCell.getPreviousDirection().equals(Map.W))) {
                    Map.lastHorizontalDirection = horizontalDirection;
                }
                if (verticalDirection != null && (nextCell.getPreviousDirection().equals(Map.N)
                        || nextCell.getPreviousDirection().equals(Map.S))) {
                    Map.lastVerticalDirection = verticalDirection;
                }

                myPreviousDirection = nextCell.getPreviousDirection();
            }

            String myOrder = "";

            if (nextCell == null) {
                myOrder = doSurface();
                System.out.println(myOrder);
                continue;
            }

            String powerToCharge = map.findPowerToCharge(nextCell, Map.myCurrentCell, opponentCell);

            Cell opponentHitCell = Map.findCellInRange(Map.myCurrentCell, Map.opponentCells);
            Cell opponentHitCellFromNextCell = Map.findCellInRange(nextCell, Map.opponentCells);
            boolean isMyMoveDone = false;
            boolean isMySilenceDone = false;

            if (torpedoCooldown == 0) {
                if (Map.specialTargeting) {
                    myOrder = doSpecialTargeting(myOrder, powerToCharge, nextCell,
                            Map.opponentTargetedCell);
                    isMyMoveDone = Map.isMyMoveDone;
                    isMySilenceDone = Map.isMySilenceDone;
                } else {
                    if (opponentHitCell != null) {
                        myOrder = doTorpedo(myOrder, opponentHitCell, nextCell, false);
                    } else if (opponentHitCellFromNextCell != null) {
                        myOrder = doMove(myOrder, powerToCharge, nextCell);
                        myOrder = doTorpedo(myOrder, opponentHitCellFromNextCell, nextCell, true);
                        isMyMoveDone = true;
                    }
                }
            }

            Cell mineHitCell = map.findMineInRange(Map.mineCells, Map.opponentCells, Map.myCurrentCell);
            if (mineHitCell != null) {
                myOrder = doTrigger(myOrder, mineHitCell);
            }

            if (Map.mineCooldown == 0) {
                myOrder = doMine(myOrder, nextCell);
            }

            if (!isMyMoveDone && !Map.specialTargeting) {
                myOrder = doMove(myOrder, powerToCharge, nextCell);
            }

            if (
                    silenceCooldown == 0 &&
                            !isMySilenceDone &&
                            (wasIShot ||
                                    Map.needSilence ||
                                    Map.myPossibleCellsFoundByOpp.size() < 8)
            ) {
                myOrder = doSilence(myOrder, nextCell, myLife);
            }

            Map.findOpponentCellsAdvanced(
                    Map.myVisibleXYPaths,
                    Map.myVisibleLastMoves,
                    Map.myVisibleSurface,
                    Map.myVisibleTorpedoCell,
                    Map.myVisibleHitCells,
                    Map.myPossibleCellsFoundByOpp,
                    startTime
            );

            Map.myPossibleCellsFoundByOpp = Map.getPossibleCellsFoundByOpp(
                    Map.myVisibleXYPaths,
                    Map.myVisibleLastMoves,
                    Map.myPossibleCellsFoundByOpp,
                    startTime);

            pipe = "".equals(myOrder) ? "" : "|";


            Integer oppUniqPos = Map.opponentCells.size() == 1 ? Map.opponentCells.iterator().next() : null;
            String oppCoordXY = oppUniqPos != null ? "["+Map.getCellFromKey(oppUniqPos).getX() + "," + Map.getCellFromKey(oppUniqPos).getY() + "]" :  "";

            Integer myUniqPos = Map.myPossibleCellsFoundByOpp.size() == 1 ? Map.myPossibleCellsFoundByOpp.iterator().next() : null;
            String myCoordXY = myUniqPos != null ? "["+Map.getCellFromKey(myUniqPos).getX() + "," + Map.getCellFromKey(myUniqPos).getY() + "]" :  "";

            myOrder += pipe + String.format("MSG opp(%s %s) - me(%s %s)", Map.opponentCells.size(),oppCoordXY,Map.myPossibleCellsFoundByOpp.size(), myCoordXY);

            // My Order
            System.out.println(myOrder);

            Map.visitedCells.add(Map.getKeyFromCoordinates(nextCell));
            Map.myVisibleSurface = 0;
            Map.myVisibleLastMoves.clear();
        }
    }
}

class Cell {

    int x;
    int y;
    Integer previousDirection;

    public Cell cloneCell() {
        return new Cell(x, y);
    }

    public boolean isEqualToCell(Cell cell) {
        return cell.getX() == x && cell.getY() == y;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, Integer direction) {
        this.x = x;
        this.y = y;
        this.previousDirection = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Integer getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(Integer previousDirection) {
        this.previousDirection = previousDirection;
    }

    public String print() {
        return String.format("%s;%s (%s)", x, y, Map.ITDM.get(previousDirection));
    }

    public String toString() {
        return String.format("%s;%s", x, y);
    }
}

class Map {

    static HashSet<Integer> islandsCells = new HashSet<>();
    static HashSet<Integer> visitedCells = new HashSet<>();
    static HashSet<Integer> mineCells = new HashSet<>();
    static List<Integer> opponentMoves = new ArrayList<>();
    static HashSet<Integer> myVisibleStartingXYPositions = new HashSet<>();
    static int opponentLastKnownSurface = 0;
    static boolean opponentSurfaceDone = false;
    static boolean moveThenTorpedo = false;
    static boolean firstIn = false;

    static HashSet<Integer> opponentCells = new HashSet<>();
    static HashSet<Integer> myPossibleCellsFoundByOpp = new HashSet<>();

    static HashMap<Integer, List<List<Integer>>> opponentXYPaths = new HashMap<>();
    static HashMap<Integer, List<List<Integer>>> myVisibleXYPaths = new HashMap<>();
    static List<Integer> opponentLastMoves = new ArrayList<>();
    static List<Integer> myVisibleLastMoves = new ArrayList<>();
    static HashSet<Integer> opponentHitCells = new HashSet<>();
    static HashSet<Integer> myVisibleHitCells = new HashSet<>();

    static Integer lastHorizontalDirection = 0;
    static Integer lastVerticalDirection = 1;
    static int opponentNbrOfMine = 0;
    static boolean needSilence = false;
    static Cell opponentTargetedCell = null;

    // INT_TO_DIRECTIONS_MAPPING
    static final java.util.Map<Integer, String> ITDM =
            new HashMap<Integer, String>() {{
                put(E, "E");
                put(S, "S");
                put(W, "W");
                put(N, "N");
                put(X, "X");
            }};


    // DIRECTIONS_TO_INT_MAPPING
    static final java.util.Map<String, Integer> DTIM =
            new HashMap<String, Integer>() {{
                put("E", E);
                put("S", S);
                put("W", W);
                put("N", N);
                put("X", X);
            }};

    static final int E = 0;
    static final int S = 1;
    static final int W = 2;
    static final int N = 3;
    static final int X = 4;
    static boolean watchDirection = false;
    static int opponentSonarCooldown = 4;
    static int opponentSilenceCooldown = 6;
    static int opponentMineCooldown = 3;

    static List<Integer> myVisibleMoves = new ArrayList<>();
    static int opponentSurface;
    static int myVisibleSurface;
    static Cell opponentTorpedoCell;
    static Cell myVisibleTorpedoCell;
    static Cell opponentUniqCell = null;
    static Cell myVisibleUniqCell = null;
    static boolean hasOpponentTrigger = false;
    static boolean hasOpponentTorpedo = false;
    static boolean hasOpponentSurface = false;
    static boolean didITrigger = false;
    static boolean didITorpedo = false;
    static boolean didISurface = false;
    /* done when opponent position identified and can be
    reached with silence */
    static boolean specialTargeting = false;
    static boolean isMyMoveDone = false;
    static boolean isMySilenceDone = false;
    static int specialNbrSilence = 0;
    static Integer specialSilenceDirection = 0;
    static int specialNbrMove = 0;
    static Integer specialMoveDirection = 0;
    static boolean specialMoveBeforeSilence = false;

    static Cell myCurrentCell;
    static Cell mineInRange;
    static Cell opponentHitCell;


    static int width;
    static int height;
    static int torpedoCooldown = -1;
    static int sonarCooldown = -1;
    static int silenceCooldown = -1;
    static int mineCooldown = -1;
    static int lastKnownLife = 6;
    static int oppLastKnownLife = 6;
    static boolean sonarUsedByOpp = false;
    static int surfaceTestedByOpp = 0;
    static int myLife = 6;
    static int oppLife = 6;

    public static void resetSearch(
            HashMap<Integer, List<List<Integer>>> opponentXYPaths,
            List<Integer> opponentLastMoves,
            HashSet<Integer> opponentCells) {
        opponentXYPaths.clear();
        opponentLastMoves.clear();
        for (Integer xyHash : opponentCells) {
            int x = xyHash % width;
            int y = (xyHash - x) / width;
            opponentXYPaths
                    .put(Map.getKeyFromCoordinates(x, y), Collections.singletonList(new ArrayList<>()));
        }
    }

    public static void updatePossibleCellsCore(int x, int y, List<Integer> path,
                                               HashSet<Integer> result) {
        Cell cell = calculateFinalPosition(x, y, path);
        if (cell != null) {
            result.add(getKeyFromCoordinates(cell));
        }
    }

    public static void updatePossibleCells(Entry entry, long startTime, HashSet<Integer> result)
            throws Exception {
        int x = ((Integer) entry.getKey()) % width;
        int y = (((Integer) entry.getKey()) - x) / width;
        List<List<Integer>> paths = (List<List<Integer>>) entry.getValue();
        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed > 46) {
            throw new Exception("time elapsed !");
        }

        for (List<Integer> path : paths) {
            updatePossibleCellsCore(x, y, path, result);
        }
    }

    public static int calculateNbrOfPaths(HashMap<Integer, List<List<Integer>>> opponentXYPaths) {
        int nbrOfPaths = 0;
        for (java.util.Map.Entry<Integer, List<List<Integer>>> entry : opponentXYPaths.entrySet()) {
            nbrOfPaths += entry.getValue().size();
        }

        return nbrOfPaths;
    }

    public static HashSet<Integer> getPossibleCellsFoundByOpp(
            HashMap<Integer, List<List<Integer>>> opponentXYPaths,
            List<Integer> opponentLastMoves,
            HashSet<Integer> opponentCells,
            long startTime) {
        HashSet<Integer> result = new HashSet<>();

        try {
            for (java.util.Map.Entry<Integer, List<List<Integer>>> entry : opponentXYPaths.entrySet()) {
                updatePossibleCells(entry, startTime, result);
            }
        } catch (Exception e) {
            resetSearch(
                    opponentXYPaths,
                    opponentLastMoves,
                    opponentCells);
        }
        return result;
    }

    public static Integer getKeyFromCoordinates(int x, int y) {
        return x + (y * width);
    }

    public static Integer getKeyFromCoordinates(Cell cell) {
        return getKeyFromCoordinates(cell.getX(), cell.getY());
    }

    public static Cell getCellFromKey(Integer xyKey) {
        int x = xyKey % width;
        int y = (xyKey - x) / width;
        return new Cell(x, y);
    }

    public static boolean isAccessibleCell(int x, int y) {

        if(!Map.firstIn) {
            Map.firstIn = true;
        }
        return !islandsCells.contains(getKeyFromCoordinates(x, y))
                && !visitedCells.contains(getKeyFromCoordinates(x, y))
                && x >= 0
                && x < width
                && y >= 0
                && y < height
                && (Map.firstIn == true || (Arrays.asList(1,2,4,5).contains(findSurfaceByCell(x, y))));
    }

    public static boolean isAccessibleToOpponent(int x, int y) {
        return !islandsCells.contains(getKeyFromCoordinates(x, y))
                && x >= 0
                && x < width
                && y >= 0
                && y < height;
    }

    public static boolean isAccessibleCell(Cell cell) {
        return isAccessibleCell(cell.getX(), cell.getY());
    }

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Next direction to try
     */
    public List<Integer> getNextDirectionList(Integer previousDirection) {

        switch (previousDirection) {
            case S:
                return Map.watchDirection ? Arrays.asList(W, S, E, N) : Arrays.asList(E, S, W, N);
            case W:
                return Map.watchDirection ? Arrays.asList(N, W, S, E) : Arrays.asList(S, W, N, E);
            case N:
                return Map.watchDirection ? Arrays.asList(E, N, W, S) : Arrays.asList(W, N, E, S);
            case E:
                return Map.watchDirection ? Arrays.asList(S, E, N, W) : Arrays.asList(N, E, S, W);
            default:
                return null;
        }
    }

    public static Cell findCellByDirection(int x, int y, Integer nextDirection) {
        switch (nextDirection) {
            case E:
                return new Cell(x + 1, y, nextDirection);
            case S:
                return new Cell(x, y + 1, nextDirection);
            case W:
                return new Cell(x - 1, y, nextDirection);
            case N:
                return new Cell(x, y - 1, nextDirection);
            default:
                return null;
        }
    }

    public static int findSurfaceByCell(int xyHash) {
        int x = xyHash % width;
        int y = (xyHash - x) / width;
        return findSurfaceByCell(x, y);
    }

    public static int findSurfaceByCell(int x, int y) {
        return ((int) Math.floor(x / 5)) + 1 + (int) (3 * Math.floor(y / 5));
    }

    public static Cell calculateFinalPosition(int x, int y, List<Integer> moves) {
        Cell cell = new Cell(x, y);
        HashSet<Integer> visitedCells = new HashSet<>();
        visitedCells.add(getKeyFromCoordinates(x, y));
        for (Integer direction : moves) {
            cell = findCellByDirection(cell.getX(), cell.getY(), direction);
            if (cell == null || !Map.isAccessibleToOpponent(cell.getX(), cell.getY()) || visitedCells
                    .contains(getKeyFromCoordinates(cell))) {
                return null;
            }
            visitedCells.add(getKeyFromCoordinates(cell.getX(), cell.getY()));
        }

        return cell;
    }

    public static Integer getOppositeMove(Integer move) {
        switch (move) {
            case E:
                return W;
            case S:
                return N;
            case W:
                return E;
            case N:
                return S;
            default:
                return null;
        }
    }

    public static List<List<Integer>> generate16CoreNoMoves() {
        List<List<Integer>> result = new ArrayList<>();

        List<Integer> orientations = Arrays.asList(E, S, W, N);
        List<Integer> movesPlus1;
        List<Integer> movesPlus2;
        List<Integer> movesPlus3;
        List<Integer> movesPlus4;

        for (Integer orientation : orientations) {

            movesPlus1 = new ArrayList<>();
            movesPlus1.add(orientation);
            movesPlus2 = new ArrayList<>(movesPlus1);
            movesPlus2.add(orientation);
            movesPlus3 = new ArrayList<>(movesPlus2);
            movesPlus3.add(orientation);
            movesPlus4 = new ArrayList<>(movesPlus3);
            movesPlus4.add(orientation);

            result.add(movesPlus1);
            result.add(movesPlus2);
            result.add(movesPlus3);
            result.add(movesPlus4);

        }

        return result;
    }

    public static List<List<Integer>> generate13Core(List<Integer> moves) {
        List<List<Integer>> result = new ArrayList<>();

        if (moves.size() > 0) {
            result.add(new ArrayList<>(moves));
            Integer lastMove = moves.get(moves.size() - 1);
            List<Integer> orientations = Arrays.asList(E, S, W, N);
            List<Integer> movesPlus1;
            List<Integer> movesPlus2;
            List<Integer> movesPlus3;
            List<Integer> movesPlus4;

            for (Integer orientation : orientations) {
                if (!orientation.equals(getOppositeMove(lastMove))) {
                    movesPlus1 = new ArrayList<>(moves);
                    movesPlus1.add(orientation);
                    movesPlus2 = new ArrayList<>(movesPlus1);
                    movesPlus2.add(orientation);
                    movesPlus3 = new ArrayList<>(movesPlus2);
                    movesPlus3.add(orientation);
                    movesPlus4 = new ArrayList<>(movesPlus3);
                    movesPlus4.add(orientation);

                    result.add(movesPlus1);
                    result.add(movesPlus2);
                    result.add(movesPlus3);
                    result.add(movesPlus4);
                }
            }
        }

        return result;
    }

    public static List<Integer> getPartWithoutXByI(List<Integer> opponentMoves, int iPos) {
        List<Integer> result = new ArrayList<>();
        int count = 0;
        for (Integer opponentMove : opponentMoves) {
            if (opponentMove != X && count == iPos) {
                result.add(opponentMove);
            } else if (opponentMove == X) {
                count++;
            }
        }

        return result;
    }

    public static List<List<Integer>> generate13(List<List<Integer>> result,
                                                 List<Integer> opponentMoves,
                                                 int i,
                                                 List<List<Integer>> generate13i) {

        int silenceOccurrences = Collections.frequency(opponentMoves, X);
        if (i == 1 && opponentMoves.get(0) == X) {
            generate13i = generate16CoreNoMoves();
        }
        for (List<Integer> list : generate13i) {
            List<Integer> partWXi = getPartWithoutXByI(opponentMoves, i); //list + partWithoutX2
            List<Integer> listPlusPartWXi = new ArrayList<>(list);
            listPlusPartWXi.addAll(partWXi);

            if (i == silenceOccurrences) {
                result.add(listPlusPartWXi);
            } else {
                generate13(result, opponentMoves, i + 1, generate13Core(listPlusPartWXi));
            }
        }

        return result;
    }

    public static List<List<Integer>> findOpponentSilentMoves(List<Integer> opponentMoves) {
        List<List<Integer>> result = new ArrayList<>();
        int silenceOccurrences = Collections.frequency(opponentMoves, X);
        if (silenceOccurrences == 0) {
            result.add(new ArrayList<>(opponentMoves));
            return result;
        }
        List<Integer> partWX1 = getPartWithoutXByI(opponentMoves, 0);
        List<List<Integer>> generate13_0 = generate13Core(partWX1);
        generate13(result, opponentMoves, 1, generate13_0);

        return result;
    }

    public boolean arePossibleMoves(List<Integer> moves) {
        if (moves == null || moves.size() == 0) {
            return true;
        }
        Cell cell = new Cell(100, 100);
        HashSet<Integer> visitedCells = new HashSet<>();
        visitedCells.add(getKeyFromCoordinates(cell));
        int minX = cell.getX();
        int maxX = cell.getX();
        int minY = cell.getY();
        int maxY = cell.getY();

        for (Integer move : moves) {
            cell = findCellByDirection(cell.getX(), cell.getY(), move);
            if (cell == null || visitedCells.contains(getKeyFromCoordinates(cell))) {
                return false;
            }
            visitedCells.add(getKeyFromCoordinates(cell));
            if (cell.getX() < minX) {
                minX = cell.getX();
            }
            if (cell.getX() > maxX) {
                maxX = cell.getX();
            }
            if (cell.getY() < minY) {
                minY = cell.getY();
            }
            if (cell.getY() > maxY) {
                maxY = cell.getY();
            }
            if (maxX - minX > 14 || maxY - minY > 14) {
                return false;
            }
        }
        return true;
    }

    public boolean updateResults(HashSet<Integer> result, int x, int y,
                                 List<List<Integer>> opponentSilentMoves) throws Exception {
        boolean resultFound = false;
        for (List<Integer> silentMoves : opponentSilentMoves) {
            Cell cell = calculateFinalPosition(x, y, silentMoves);
            if (cell != null && Map.isAccessibleToOpponent(cell.getX(), cell.getY())) {
                result.add(Map.getKeyFromCoordinates(cell));
                resultFound = true;
                if (result.size() >= 300) {
                    throw new Exception("max result reached");
                }
            }
        }

        return resultFound;
    }

    public boolean findOpponentCellsCore(
            HashSet<Integer> result,
            int x,
            int y,
            int opponentSurface,
            List<List<Integer>> opponentSilentMoves,
            Cell opponentTorpedoCell
    ) throws Exception {
        boolean resultFound = false;
        if (isAccessibleToOpponent(x, y)) {
            if (opponentSurface > 0) {
                if (opponentSurface == findSurfaceByCell(x, y)) {
                    resultFound = updateResults(result, x, y, opponentSilentMoves);
                }
            } else if (opponentTorpedoCell != null) {
                // if torpedo then move it was already calculated (oppTorpedoCell updated)
                if (areCellsInRange(x, y, opponentTorpedoCell.getX(),
                        opponentTorpedoCell.getY())) {
                    resultFound = updateResults(result, x, y, opponentSilentMoves);
                }
            } else {
                resultFound = updateResults(result, x, y, opponentSilentMoves);
            }
        }

        return resultFound;
    }

    public List<List<Integer>> filterValidMoves(List<List<Integer>> opponentSilentMoves) {
        List<List<Integer>> result = new ArrayList<>();
        for (List<Integer> moves : opponentSilentMoves) {
            if (arePossibleMoves(moves)) {
                result.add(moves);
            }
        }

        return result;
    }

    public static Cell arePossibleMovesTest(int xyHash, List<Integer> moves, int opponentSurface) {
        return new Cell(0, 0);
    }

    public static Cell arePossibleMoves(int xyHash, List<Integer> moves, int opponentSurface) {
        int x = xyHash % width;
        int y = (xyHash - x) / width;
        Cell cell = calculateFinalPosition(x, y, moves);

        if (cell != null && (opponentSurface == 0
                || findSurfaceByCell(cell.getX(), cell.getY()) == opponentSurface)) {
            return cell;
        }
        return null;
    }

    public static void updatePath(
            int xyHash, List<Integer> lastOpponentMoves,
            List<Integer> moves,
            int silenceOccurrences,
            int opponentSurface,
            Cell opponentTorpedoCell,
            HashSet<Integer> opponentHitCells,
            long startTime,
            List<List<Integer>> result) throws Exception {
        List<Integer> fullMoves = new ArrayList<>(moves);
        fullMoves.addAll(lastOpponentMoves);
        Cell cell;

        long elapsed = System.currentTimeMillis() - startTime;

        if (elapsed > 46) {
            throw new Exception("time elapsed !");
        }

        if (silenceOccurrences == 0) {
            cell = arePossibleMoves(xyHash, fullMoves, opponentSurface);
            if (cell != null &&
                    (opponentTorpedoCell == null || areCellsInRange(cell.getX(), cell.getY(),
                            opponentTorpedoCell.getX(),
                            opponentTorpedoCell.getY())) &&
                    (opponentHitCells.size() == 0 || areCellsInRangeOfMines(cell, opponentHitCells))
            ) {
                result.add(fullMoves);
            }
        } else {
            List<List<Integer>> opponentSilentMoves = Map.findOpponentSilentMoves(fullMoves);
            for (List<Integer> loopMoves : opponentSilentMoves) {
                //if (arePossibleMoves(xyHash, loopMoves, opponentSurface)) {
                cell = arePossibleMoves(xyHash, loopMoves, opponentSurface);
                if (cell != null &&
                        (opponentTorpedoCell == null || areCellsInRange(cell.getX(), cell.getY(),
                                opponentTorpedoCell.getX(),
                                opponentTorpedoCell.getY())) &&
                        (opponentHitCells.size() == 0 || areCellsInRangeOfMines(cell, opponentHitCells))) {
                    result.add(loopMoves);
                }
            }
        }
    }

    public static List<List<Integer>> updatePathsCore(java.util.Map.Entry entry,
                                                      List<Integer> lastOpponentMoves, int silenceOccurrences, int opponentSurface,
                                                      Cell opponentTorpedoCell, HashSet<Integer> opponentHitCells, long startTime)
            throws Exception {

        List<List<Integer>> movesList = (List<List<Integer>>) entry.getValue();
        final List<List<Integer>> finalMoves = new ArrayList<>();
        for (List<Integer> moves : movesList) {
            updatePath((int) entry.getKey(), lastOpponentMoves, moves, silenceOccurrences,
                    opponentSurface, opponentTorpedoCell, opponentHitCells, startTime, finalMoves);
        }

        return finalMoves;
    }

    public static void initialiseOpponentXYPaths(
            HashMap<Integer, List<List<Integer>>> opponentXYPaths,
            List<Integer> lastOpponentMoves,
            long startTime
    ) throws Exception {
        if (opponentXYPaths.size() == 0) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (isAccessibleToOpponent(x, y)) {
                        int xyHash = x + (y * width);
                        opponentXYPaths.put(xyHash, Arrays.asList(lastOpponentMoves));
                    }
                }
            }

            updatePaths(opponentXYPaths, new ArrayList<>(), 0, 0, null, new HashSet<>(), startTime);
        }
    }

    public static void updatePaths(
            HashMap<Integer, List<List<Integer>>> opponentXYPaths,
            List<Integer> lastOpponentMoves,
            int silenceOccurrences,
            int opponentSurface,
            Cell opponentTorpedoCell,
            HashSet<Integer> opponentHitCells,
            long startTime) throws Exception {
        HashMap<Integer, List<List<Integer>>> resultTmp = new HashMap<>();

        //long s = System.currentTimeMillis();
        List<List<Integer>> updateResult;
        for (java.util.Map.Entry<Integer, List<List<Integer>>> entry : opponentXYPaths.entrySet()) {
            updateResult = updatePathsCore(entry,
                    lastOpponentMoves, silenceOccurrences, opponentSurface, opponentTorpedoCell,
                    opponentHitCells, startTime);
            if (updateResult.size() > 0) {
                resultTmp.put(entry.getKey(), updateResult);
            }
        }

        opponentXYPaths.clear();
        opponentXYPaths.putAll(resultTmp);
    }

    public static void findOpponentCellsAdvanced(
            HashMap<Integer, List<List<Integer>>> opponentXYPaths,
            List<Integer> opponentLastMoves,
            int opponentSurface,
            Cell opponentTorpedoCell,
            HashSet<Integer> opponentHitCells,
            HashSet<Integer> opponentCells,
            long startTime
    ) {
        try {
            if (opponentXYPaths.size() == 0) {
                initialiseOpponentXYPaths(opponentXYPaths, opponentLastMoves, startTime);
            } else {
                int silenceOccurrences = Collections.frequency(opponentLastMoves, Map.X);
                updatePaths(opponentXYPaths, opponentLastMoves, silenceOccurrences, opponentSurface,
                        opponentTorpedoCell, opponentHitCells, startTime);
            }

        } catch (Exception e) {
            resetSearch(
                    opponentXYPaths,
                    opponentLastMoves,
                    opponentCells);
        }

    }

    public List<Cell> findNextCells(Cell cell) {
        List<Integer> directions = getNextDirectionList(cell.getPreviousDirection());
        List<Cell> cellsByPriority = new ArrayList<>();
        Iterator iterator = directions.iterator();
        while (iterator.hasNext()) {
            Integer nextDirection = (Integer) iterator.next();
            cellsByPriority.add(findCellByDirection(cell.getX(), cell.getY(), nextDirection));
        }
        return cellsByPriority;
    }

    public Cell findNextAvailableCell(Cell currentCell, Cell excludedCell) {
        if (currentCell == null) {
            return null;
        }

        List<Cell> cellsByPriority;
        Cell nextAvailableCell = null;
        cellsByPriority = findNextCells(currentCell);
        //Map.printCells(cellsByPriority);
        Iterator iterator = cellsByPriority.iterator();
        while (iterator.hasNext()) {
            Cell nextCell = (Cell) iterator.next();
            if (isAccessibleCell(nextCell) && (excludedCell == null || !nextCell
                    .isEqualToCell(excludedCell))) {
                nextAvailableCell = nextCell;
                break;
            }
        }
        return nextAvailableCell;
    }

    public boolean isOneWayPath(Cell cell) {
        if (cell == null) {
            return true;
        }
        switch (cell.getPreviousDirection()) {
            case E:
            case W:
                return !isAccessibleCell(cell.getX(), cell.getY() - 1) && !isAccessibleCell(cell.getX(),
                        cell.getY() + 1);
            case S:
            case N:
                return !isAccessibleCell(cell.getX() - 1, cell.getY()) && !isAccessibleCell(cell.getX() + 1,
                        cell.getY());
            default:
                return true;
        }
    }

    public static List<List<Integer>> generateBasicList(Integer direction1,
                                                        Integer direction2, boolean silenceFirst) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> list;

        list = Arrays.asList(direction1);
        result.add(list);

        if (silenceFirst) {
            list = Arrays.asList(direction2);
            result.add(list);
            list = Arrays.asList(direction2, direction2);
            result.add(list);
            list = Arrays.asList(direction2, direction2, direction2);
            result.add(list);
            list = Arrays.asList(direction2, direction2, direction2, direction2);
            result.add(list);
            list = Arrays.asList(direction2, direction1);
            result.add(list);
            list = Arrays.asList(direction2, direction2, direction1);
            result.add(list);
            list = Arrays.asList(direction2, direction2, direction2, direction1);
            result.add(list);
            list = Arrays.asList(direction2, direction2, direction2, direction2, direction1);
            result.add(list);
        } else {
            list = Arrays.asList(direction1, direction2);
            result.add(list);
            list = Arrays.asList(direction1, direction2, direction2);
            result.add(list);
            list = Arrays.asList(direction1, direction2, direction2, direction2);
            result.add(list);
            list = Arrays.asList(direction1, direction2, direction2, direction2, direction2);
            result.add(list);
        }

        return result;
    }

    public static List<List<Integer>> generateForkedDirectionsCore(Integer direction1,
                                                                   Integer direction2) {
        List<List<Integer>> result = new ArrayList<>();

        if (direction1 == direction2) {
            result.addAll(generateBasicList(direction1, direction2, true));
            result.addAll(generateBasicList(direction1, direction2, false));
        } else {
            result.addAll(generateBasicList(direction1, direction2, true));
            result.addAll(generateBasicList(direction1, direction2, false));
            result.addAll(generateBasicList(direction2, direction1, true));
            result.addAll(generateBasicList(direction2, direction1, false));
        }

        return result;
    }

    public static List<List<Integer>> generateForkedDirections(Cell cell1, Cell cell2) {
        return generateForkedDirections(cell1.getX(), cell1.getY(), cell2.getX(), cell2.getY());
    }

    public static List<List<Integer>> generateForkedDirections(int x1, int y1, int x2, int y2) {

        if (x1 == x2) {
            if (y1 < y2) {
                return generateForkedDirectionsCore(S, S);
            } else {
                return generateForkedDirectionsCore(N, N);
            }
        }
        if (y1 == y2) {
            if (x1 < x2) {
                return generateForkedDirectionsCore(E, E);
            } else {
                return generateForkedDirectionsCore(W, W);
            }
        }
        if (x1 < x2) {
            if (y1 < y2) {
                return generateForkedDirectionsCore(E, S);
            } else {
                return generateForkedDirectionsCore(E, N);
            }
        } else {
            if (y1 < y2) {
                return generateForkedDirectionsCore(W, S);
            } else {
                return generateForkedDirectionsCore(W, N);
            }
        }

    }

    public static void updateOptionsByMoves(List<Integer> moves) {

        if (moves.size() == 1) {
            Map.specialNbrSilence = 0;
            Map.specialSilenceDirection = 0;
            Map.specialNbrMove = 1;
            Map.specialMoveDirection = moves.get(0);
            Map.specialMoveBeforeSilence = true;
        } else if (moves.size() > 1) {
            if (!moves.get(0).equals(moves.get(1))) {
                int silenceCount = 0;
                for (Integer move : moves) {
                    if (move.equals(moves.get(1))) {
                        silenceCount++;
                    }
                }
                Map.specialNbrSilence = silenceCount;
                Map.specialSilenceDirection = moves.get(1);
                Map.specialNbrMove = 1;
                Map.specialMoveDirection = moves.get(0);
                Map.specialMoveBeforeSilence = true;
            } else {
                int silenceCounter = 0;
                int movesCounter = 0;
                Integer specialMoveDirection = 0;
                for (Integer move : moves) {
                    if (move.equals(moves.get(0)) && silenceCounter < 4) {
                        silenceCounter++;
                    } else {
                        movesCounter = 1;
                        specialMoveDirection = move;
                    }
                }
                Map.specialNbrSilence = silenceCounter;
                Map.specialSilenceDirection = moves.get(0);
                Map.specialNbrMove = movesCounter;
                Map.specialMoveDirection = specialMoveDirection;
                Map.specialMoveBeforeSilence = false;
            }
        }
    }

    public static Cell findNextAvailableTargetedCell(Cell myCell, Cell opponentCell) {
        Cell result = null;
        Cell myNextCell;
        List<List<Integer>> generatedForks = generateForkedDirections(myCell, opponentCell);
        for (List<Integer> moves : generatedForks) {
            myNextCell = calculateMyPosition(myCell.getX(), myCell.getY(), moves);
            if (myNextCell != null && areCellsInRange(myNextCell, opponentCell)) {

                updateOptionsByMoves(moves);

                Cell loopCell = new Cell(myCell.getX(), myCell.getY());
                for (Integer direction : moves) {
                    loopCell = findCellByDirection(loopCell.getX(), loopCell.getY(), direction);
                    visitedCells.add(getKeyFromCoordinates(loopCell));
                }
                visitedCells.add(getKeyFromCoordinates(myNextCell));
                result = myNextCell;
                break;
            }
        }

        return result;
    }

    public Cell findNextAvailableAdvancedCell(Cell myCell, HashSet<Integer> opponentCells,
                                              int silenceCooldown) {

        Cell nextAvailableCell = null;
        Cell opponentCell = null;
        if (opponentCells.size() == 1) {
            opponentCell = getCellFromKey(opponentCells.iterator().next());
        }

        if (silenceCooldown == 0 &&
                (Map.oppLife == 2 || Map.myLife > Map.oppLife) &&
                opponentCells.size() == 1 &&
                !areCellsInRange(myCell,
                        opponentCell)) {
            nextAvailableCell = findNextAvailableTargetedCell(myCell, opponentCell);
            Map.opponentTargetedCell = opponentCell;
        }

        if (nextAvailableCell == null) {
            nextAvailableCell = findNextAvailableCell(myCell, null);
            if (nextAvailableCell == null) {
                return null;
            } else {
                if (isOneWayPath(nextAvailableCell)) {
                    Cell oneWayCell = nextAvailableCell;
                    nextAvailableCell = findNextAvailableCell(myCell, nextAvailableCell);
                    if (nextAvailableCell == null) {
                        nextAvailableCell = oneWayCell;
                    }
                }
            }

            if (nextAvailableCell != null) {
                visitedCells.add(getKeyFromCoordinates(nextAvailableCell));
            }
        } else {
            Map.specialTargeting = true;
        }

        return nextAvailableCell;
    }

    public Cell getStartCell() {
      /*
      int x = width % 2 == 0 ? (width / 2) - 1 : (width - 1) / 2;
      int y = height % 2 == 0 ? (height / 2) - 1 : (height - 1) / 2;
      */

        int x = (int) (Math.random() * ((14) + 1));
        int y = (int) (Math.random() * ((14) + 1));
        Cell testCell = new Cell(x, y, W);
        while (islandsCells.contains(getKeyFromCoordinates(x, y))
                ||
                !Arrays.asList(4,5).contains(findSurfaceByCell(x, y))
                ||
                isOneWayPath(testCell)
        ) {
            x = (int) (Math.random() * ((14) + 1));
            y = (int) (Math.random() * ((14) + 1));
            testCell = new Cell(x, y, W);
        }

        //x = 3;
        //y = 14;

        Cell startCell = new Cell(x, y, W);
        if (!islandsCells.contains(getKeyFromCoordinates(startCell))) {
            return startCell;
        }
        return findNextAvailableAdvancedCell(startCell, new HashSet<>(), 6);
    }

    public static String generateIsland(int index) {
        String island = "...........xx..\n"
                + "...............\n"
                + ".........xx.xx.\n"
                + "..x............\n"
                + ".......xx......\n"
                + "...xxx.xx..xx..\n"
                + "..xxxx.....xx..\n"
                + "..xxxx.........\n"
                + ".xxxx..........\n"
                + ".xxx...........\n"
                + ".xxx...........\n"
                + "..xx...........\n"
                + "...............\n"
                + "...x...........\n"
                + "...x...........";
        String lines[] = island.split("\\n");
        return lines[index];
    }

    public static Cell calculatePosition(int oX, int oY, List<Integer> moves) {
        if (!isAccessibleToOpponent(oX, oY)) {
            return null;
        }

        Cell cell = new Cell(oX, oY);
        for (Integer direction : moves) {

            cell = findCellByDirection(cell.getX(), cell.getY(), direction);
            if (cell == null || !isAccessibleToOpponent(cell.getX(), cell.getY())) {
                return null;
            }
        }

        return cell;
    }

    public static Cell calculateMyPosition(int oX, int oY, List<Integer> moves) {
        if (!isAccessibleToOpponent(oX, oY)) {
            return null;
        }

        Cell cell = new Cell(oX, oY);
        for (Integer direction : moves) {

            cell = findCellByDirection(cell.getX(), cell.getY(), direction);
            if (cell == null || !isAccessibleCell(cell.getX(), cell.getY())) {
                return null;
            }
        }

        return cell;
    }

    public static boolean areCellsInRange(int x1, int y1, int x2, int y2) {
        if (Math.abs(x1 - x2) + Math
                .abs(y1 - y2) <= 4) {
            return true;
        }
        return false;
    }

    public static boolean areCellsInRangeOfCell(Cell cell, HashSet<Integer> oppCells) {
        Cell loopCell;
        for (Integer oppHash : oppCells) {
            loopCell = getCellFromKey(oppHash);
            if (areCellsInRange(loopCell, cell)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areCellsInRangeOfMine(int x1, int y1, int x2, int y2) {
        int distance = Math.abs(x1 - x2) + Math
                .abs(y1 - y2);
        if (distance < 2 || (x1 != x2 && y1 != y2 && distance == 2)) {
            return true;
        }
        return false;
    }

    public static boolean areCellsInRangeOfMine(Cell cell1, Cell cell2) {
        return areCellsInRangeOfMine(cell1.getX(), cell1.getY(), cell2.getX(), cell2.getY());
    }

    public static boolean areCellsInRangeOfMines(Cell cell, HashSet<Integer> minesCells) {
        Cell loopCell;
        for (Integer mineHash : minesCells) {
            loopCell = getCellFromKey(mineHash);
            if (areCellsInRangeOfMine(loopCell, cell)) {
                return true;
            }
        }
        return false;
    }

    public static boolean areCellsInRange(Cell cell1, Cell cell2) {
        if (cell1 != null && cell2 != null) {
            return areCellsInRange(cell1.getX(), cell1.getY(), cell2.getX(), cell2.getY());
        }
        return false;
    }

    public static boolean isMaxReached(HashSet<Integer> opponentCells, int max) {
        boolean maxReached = false;
        HashSet<Integer> surfaceFound = new HashSet<>();
        for (Integer xyHash : opponentCells) {
            surfaceFound.add(findSurfaceByCell(xyHash));
            if (surfaceFound.size() > max) {
                maxReached = true;
                break;
            }
        }

        return maxReached;
    }

    public static Cell findCellInRange(Cell currentCell, HashSet<Integer> opponentCells) {

        Map.findSurfaceByCell(Map.myCurrentCell.getX(),
                Map.myCurrentCell.getY());
        boolean maxReached = isMaxReached(opponentCells, 1);

        if (maxReached) {
            return null;
        }

        List<Cell> possibleCells = new ArrayList<>();
        for (Integer xyHash : opponentCells) {
            int x = xyHash % Map.width;
            int y = (xyHash - x) / Map.width;
            if (areCellsInRange(currentCell.getX(), currentCell.getY(), x, y) &&
                    !areCellsInRangeOfMine(currentCell.getX(), currentCell.getY(), x, y)) {
                possibleCells.add(new Cell(x, y));
            }
        }

        if (possibleCells.size() > 0) {
            return possibleCells.get((int) Math.random() * (possibleCells.size() - 1));
        }

        return null;
    }

    public Cell findMineInRange(HashSet<Integer> mineCells, HashSet<Integer> opponentCells,
                                Cell myCurrentCell) {

        boolean maxReached = isMaxReached(opponentCells, 1);
        boolean maxReached2 = isMaxReached(opponentCells, 2);
        if (maxReached && (maxReached2 || mineCells.size() < 10)) {
            return null;
        }

        List<Cell> possibleCells = new ArrayList<>();
        for (Integer xyHash : opponentCells) {
            int x = xyHash % Map.width;
            int y = (xyHash - x) / Map.width;
            for (Integer mineXYHash : mineCells) {
                int xMine = mineXYHash % Map.width;
                int yMine = (mineXYHash - xMine) / Map.width;
                if (areCellsInRangeOfMine(xMine, yMine, x, y) &&
                        !areCellsInRangeOfMine(xMine, yMine, myCurrentCell.getX(), myCurrentCell.getY())) {
                    possibleCells.add(new Cell(xMine, yMine));
                }
            }
        }

        if (possibleCells.size() > 0) {
            return possibleCells.get((int) Math.random() * (possibleCells.size() - 1));
        }

        return null;
    }

    public boolean isOpponentInRangeOfMine(Cell cell) {
        if (cell != null) {
            if (mineCells
                    .contains(getKeyFromCoordinates(cell.getX(), cell.getY()))) {
                mineInRange = new Cell(cell.getX(), cell.getY());
                return true;
            }
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                    if (mineCells.contains(getKeyFromCoordinates(x, y))) {
                        mineInRange = new Cell(x, y);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String findPowerToCharge(Cell nextCell, Cell myCurrentCell, Cell opponentCell) {
        if (torpedoCooldown > 0 ||
                (myCurrentCell != null && areCellsInRange(myCurrentCell, opponentCell)) ||
                (opponentCell != null && areCellsInRange(nextCell, opponentCell))) {
            return "TORPEDO";
        } else if (silenceCooldown > 0) {
            return "SILENCE";
        } else if (Map.mineCells.contains(getKeyFromCoordinates(nextCell))) {
            if (silenceCooldown > 0) {
                return "SILENCE";
            } else {
                return "TORPEDO";
            }
        } else {
            return "MINE";
        }
    }

    public boolean isIsland(final int x, final int y) {
        return islandsCells.contains(getKeyFromCoordinates(x, y));
    }

    public boolean isVisited(final int x, final int y) {
        return visitedCells.contains(getKeyFromCoordinates(x, y));
    }

    public void addIslandCell(final int x, final int y) {
        this.islandsCells.add(getKeyFromCoordinates(x, y));
    }

    public void addVisitedCell(final int x, final int y) {
        this.visitedCells.add(getKeyFromCoordinates(x, y));
    }

    public static void printMoves(Iterable<Integer> moves) {
        for (Integer direction : moves) {
            System.err.print(ITDM.get(direction) + " -> ");
        }
        System.err.println("");
    }

    public static void printCells(List<Cell> cells) {
        for (Cell cell : cells) {
            System.err.println(String.format("cell %s", cell));
        }
        System.err.println();
    }

    public static void printCells(Iterable<Integer> xyCells) {
        Cell cell;
        for (Integer xyCell : xyCells) {
            cell = getCellFromKey(xyCell);
            System.err.println(String.format("cell %s (%s)", cell, xyCell));
        }
        System.err.println();
    }

    public static List<Integer> convertDirectionToIntList(List<String> moves) {
        List<Integer> intMoves = new ArrayList<>();
        for (String move : moves) {
            intMoves.add(DTIM.get(move));
        }

        return intMoves;
    }

}
