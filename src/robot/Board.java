package robot;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private int width;
    private int height;
    private Color color;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Map<Position, Object> getMatrices() {
        return matrices;
    }

    public void setMatrices(Map<Position, Object> matrices) {
        this.matrices = matrices;
    }

    public Map<Position, Terrain> getTerrains() {
        return terrains;
    }

    public void setTerrains(Map<Position, Terrain> terrains) {
        this.terrains = terrains;
    }

    private Map<Position, Object> matrices;
    private Map<Position, Terrain> terrains;

    public Board(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.matrices = new HashMap<>();
        this.terrains = new HashMap<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Position pos = new Position(i, j);
                Terrain terrain = new Terrain();
                this.terrains.put(pos, terrain);
                this.matrices.put(pos, terrain);
            }
        }
    }

    public Object getAt(Position pos) {
        return this.matrices.get(pos);
    }

    public void setAt(Position pos, Object element) {
        this.matrices.put(pos, element);
    }

    public void restoreTerrain(Position pos) {
        this.matrices.put(pos, this.terrains.get(pos));
    }
}