import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {

    private Board board;
    private Robot robot;

    @BeforeEach
    void setUp() {
        // Création d'un plateau de 20x20 avant chaque test
        board = new Board(20, 20, Color.LIGHT_GRAY);
        robot = new Robot(board);
    }

    @Test
    void testInitialDirectionIsEast() {
        assertEquals(Direction.EAST, robot.getDirection());
    }

    @Test
    void testMoveEastExampleFromSubject() {
        robot.setPosition(new Position(4, 1));
        robot.move(10);

        assertEquals(new Position(14, 1), robot.getPosition());
        assertSame(robot, board.getAt(new Position(14, 1)));
    }

    @Test
    void testTerrainIsRestoredWhenRobotMoves() {
        Position startPos = new Position(4, 1);
        Position endPos = new Position(14, 1);

        // On récupère la référence du terrain initial sur la case (4, 1)
        Object initialTerrain = board.getAt(startPos);
        assertInstanceOf(Terrain.class, initialTerrain);

        // On place le robot en (4, 1) : il occupe désormais la case
        robot.setPosition(startPos);
        assertSame(robot, board.getAt(startPos));

        // Le robot avance de 10 cases vers l'est
        robot.move(10);

        // Le terrain d'origine doit être restauré en (4, 1) et le robot doit être en (14, 1)
        assertSame(initialTerrain, board.getAt(startPos));
        assertSame(robot, board.getAt(endPos));
    }

    @Test
    void testMoveInAllFourDirections() {
        robot.setPosition(new Position(5, 5));

        // Déplacement vers le NORD (+Y)
        robot.setDirection(Direction.NORTH);
        robot.move(3);
        assertEquals(new Position(5, 8), robot.getPosition());
        assertSame(robot, board.getAt(new Position(5, 8)));
        assertInstanceOf(Terrain.class, board.getAt(new Position(5, 5)));

        // Déplacement vers l'OUEST (-X)
        robot.setDirection(Direction.WEST);
        robot.move(2);
        assertEquals(new Position(3, 8), robot.getPosition());
        assertSame(robot, board.getAt(new Position(3, 8)));
        assertInstanceOf(Terrain.class, board.getAt(new Position(5, 8)));

        // Déplacement vers le SUD (-Y)
        robot.setDirection(Direction.SOUTH);
        robot.move(4);
        assertEquals(new Position(3, 4), robot.getPosition());
        assertSame(robot, board.getAt(new Position(3, 4)));
        assertInstanceOf(Terrain.class, board.getAt(new Position(3, 8)));

        // Déplacement vers l'EST (+X)
        robot.setDirection(Direction.EAST);
        robot.move(5);
        assertEquals(new Position(8, 4), robot.getPosition());
        assertSame(robot, board.getAt(new Position(8, 4)));
        assertInstanceOf(Terrain.class, board.getAt(new Position(3, 4)));
    }

    @Test
    void testScriptWithOutDrop() {
        robot.execute("base 4 1\n" +
                "dir EAST\n" +
                "mov 2\n" +
                "mov 3\n" +
                "dir NORTH\n" +
                "mov 3");

        assertEquals(new Position(9, 4), robot.getPosition());
    }

    @Test
    void testScriptWithDrop() {
        robot.execute("base 4 1\n" +
                "dir EAST\n" +
                "mov 2\n" +
                "mov 3\n" +
                "dir NORTH\n" +
                "mov 3\n" +
                "drop Chest");

        Item item = (Item) robot.getBoard().getAt(new Position(9, 4));

        assertEquals(new Position(9, 4), robot.getPosition());
        assertEquals(new Item("Chest").getName(), item.getName());
    }
}