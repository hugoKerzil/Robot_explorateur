import command.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import robot.*;

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

    @Test
    void testBaseCommandIndividually() {
        Command baseCmd = new BaseCommand(new Position(4, 1));
        baseCmd.execute(robot);

        assertEquals(new Position(4, 1), robot.getPosition());
        assertSame(robot, board.getAt(new Position(4, 1)));
    }

    @Test
    void testDirectionCommandIndividually() {
        Command dirCmd = new DirectionCommand(Direction.NORTH);
        dirCmd.execute(robot);

        assertEquals(Direction.NORTH, robot.getDirection());
    }

    @Test
    void testMoveCommandIndividually() {
        robot.setPosition(new Position(4, 1));
        Command moveCmd = new MoveCommand(10);
        moveCmd.execute(robot);

        assertEquals(new Position(14, 1), robot.getPosition());
        assertSame(robot, board.getAt(new Position(14, 1)));
    }

    @Test
    void testDropCommandIndividually() {
        Position pos = new Position(4, 1);
        robot.setPosition(pos);

        Item chest = new Item("Chest");
        Command dropCmd = new DropCommand(chest);
        dropCmd.execute(robot);

        assertSame(chest, board.getAt(pos));
    }

    @Test
    void testCommandsAreRecordedInOrder() {
        robot.execute("startM\n" +
                "base 4 1\n" +
                "dir EAST\n" +
                "mov 2\n" +
                "dir NORTH\n" +
                "mov 3");

        // Vérifie que les 5 commandes du script sont enregistrées dans le bon ordre
        assertEquals(4, robot.getHistory().size());
        assertInstanceOf(DirectionCommand.class, robot.getHistory().get(0));
        assertInstanceOf(MoveCommand.class, robot.getHistory().get(1));
        assertInstanceOf(DirectionCommand.class, robot.getHistory().get(2));
        assertInstanceOf(MoveCommand.class, robot.getHistory().get(3));
    }

    @Test
    void testReplayExampleFromSubjectInScript() {
        // On place le robot en (4, 1) hors enregistrement (stopM) pour que le replay
        // ne téléporte pas le robot en (4, 1) lorsqu'on le replacera en (5, 1)
        robot.execute("base 4 1\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 2\n" +
                "mov 3\n" +
                "dir NORTH\n" +
                "mov 3\n" +
                "stopM\n" +
                "base 5 1\n" +
                "replay");

        assertEquals(new Position(10, 4), robot.getPosition());
    }

    @Test
    void testStopMAndStartMInScript() {
        // "mov 5" est exécuté après stopM : il déplace le robot mais n'est pas enregistré.
        // En replaçant le robot en (0, 0) avant "replay", seul "mov 3" (EAST) et "mov 2" (NORTH) sont rejoués.
        robot.execute("base 0 0\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 3\n" +
                "stopM\n" +
                "mov 5\n" +
                "startM\n" +
                "dir NORTH\n" +
                "mov 2\n" +
                "stopM\n" +
                "base 0 0\n" +
                "replay");

        assertEquals(new Position(3, 2), robot.getPosition());
    }

    @Test
    void testResMClearsHistoryInScript() {
        // "resM" vide l'historique : le "replay" qui suit après "base 2 2" ne doit rien faire
        robot.execute("base 0 0\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 4\n" +
                "resM\n" +
                "stopM\n" +
                "base 2 2\n" +
                "replay");

        assertTrue(robot.getHistory().isEmpty());
        assertEquals(new Position(2, 2), robot.getPosition());
    }

    @Test
    void testControlCommandsAreNotRecordedInScript() {
        robot.execute("base 0 0\n" +
                "resM\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 3\n" +
                "replay");

        // Seules "dir EAST" et "mov 3" doivent être enregistrées (aucune commande de contrôle)
        assertEquals(2, robot.getHistory().size());

        // "mov 3" exécuté une fois + rejoué une fois par "replay" = (6, 0)
        assertEquals(new Position(6, 0), robot.getPosition());
    }

    @Test
    void testWbackExampleFromSubjectInScript() {
        // On place le robot en (4, 1) hors enregistrement, puis on exécute l'exemple du sujet
        robot.execute("base 4 1\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 5\n" +
                "dir NORTH\n" +
                "mov 3\n" +
                "dir EAST\n" +
                "mov 4\n" +
                "wback");

        // Le robot doit être revenu à son point de départ (4, 1)
        assertEquals(new Position(4, 1), robot.getPosition());
    }

    @Test
    void testWbackInAllFourDirectionsInScript() {
        // Parcours utilisant les 4 directions (NORTH, WEST, SOUTH, EAST) avant de revenir
        robot.execute("base 10 10\n" +
                "startM\n" +
                "dir NORTH\n" +
                "mov 4\n" +
                "dir WEST\n" +
                "mov 3\n" +
                "dir SOUTH\n" +
                "mov 2\n" +
                "dir EAST\n" +
                "mov 5\n" +
                "wback");

        // Le robot doit retrouver sa position initiale (10, 10)
        assertEquals(new Position(10, 10), robot.getPosition());
    }

    @Test
    void testWbackOnlyReversesRecordedCommandsInScript() {
        // Le premier déplacement (mov 5 vers l'EST) n'est pas enregistré (stopM).
        // Le parcours enregistré démarre donc en (5, 0).
        robot.execute("base 0 0\n" +
                "dir EAST\n" +
                "mov 5\n" +
                "startM\n" +
                "dir NORTH\n" +
                "mov 4\n" +
                "dir EAST\n" +
                "mov 2\n" +
                "wback");

        // wback ne doit inverser que les commandes enregistrées et ramener le robot en (5, 0)
        assertEquals(new Position(5, 0), robot.getPosition());
    }

    @Test
    void testWbackWithDropCommandAndControlCommandsInScript() {
        // Vérifie que les opérations sans inverse évident (comme drop) et les commandes
        // de contrôle n'empêchent pas le retour au point de départ
        robot.execute("base 2 2\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 3\n" +
                "drop Chest\n" +
                "dir NORTH\n" +
                "mov 4\n" +
                "wback");

        // Le robot est bien revenu en (2, 2) et wback ne s'est pas enregistrée elle-même
        assertEquals(new Position(2, 2), robot.getPosition());
    }

    @Test
    void testWbackAfterResMDoesNothingInScript() {
        // Si l'historique est vidé par resM, wback n'a aucun parcours à inverser
        robot.execute("base 1 1\n" +
                "startM\n" +
                "dir EAST\n" +
                "mov 5\n" +
                "resM\n" +
                "wback");

        // Le robot reste en (6, 1)
        assertEquals(new Position(6, 1), robot.getPosition());
    }
}