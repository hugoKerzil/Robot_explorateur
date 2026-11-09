package robot;

import view.Observer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private int width;
    private int height;
    private Color color;
    private Map<Position, Object> matrices;
    private Map<Position, Terrain> terrains;
    private Map<Position, Item> items;
    private final List<Observer> observers = new ArrayList<Observer>();

    public Board(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.matrices = new HashMap<>();
        this.terrains = new HashMap<>();
        this.items = new HashMap<>();

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Position pos = new Position(i, j);
                Terrain terrain = new Terrain();
                this.terrains.put(pos, terrain);
                this.matrices.put(pos, terrain);
            }
        }
    }

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

    public Object getAt(Position pos) {
        return this.matrices.get(pos);
    }

    public Item getItemAt(Position pos) {
        return this.items.get(pos);
    }

    public void setAt(Position pos, Object element) {
        this.matrices.put(pos, element);
        if (element instanceof Item item) {
            this.items.put(pos, item);
        }
        notifyObserver();
    }

    public void removeAt(Position pos) {
        this.items.remove(pos);
        this.matrices.remove(pos);
        notifyObserver();
    }

    public void restoreTerrain(Position pos) {
        Item item = this.items.get(pos);
        this.matrices.put(pos, item == null ? this.terrains.get(pos) : item);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObserver() {
        for (Observer observer : observers) {
            observer.updated();
        }
    }
}