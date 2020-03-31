package sidescroller.scene;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.Canvas;
import sidescroller.animator.AnimatorInterface;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import sidescroller.entity.sprite.tile.Tile;
import utility.RandUtil;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

//I'm using JavaFx library 11.0.
public class MapScene implements MapSceneInterface {

    private Tuple count;
    private Tuple size;
    private double scale;
    private AnimatorInterface animator;
    private List <Entity> players;
    private List <Entity> staticShapes;
    private BooleanProperty drawBounds;
    private BooleanProperty drawFPS;
    private BooleanProperty drawGrid;
    private Entity background;

    public MapScene(){
        drawBounds = new SimpleBooleanProperty(true);
        drawFPS = new SimpleBooleanProperty(true);
        drawGrid= new SimpleBooleanProperty(true);
        staticShapes = new ArrayList<>();
        players = new ArrayList<>();
    }

    @Override
    public BooleanProperty drawFPSProperty() {
        return this.drawFPS;
    }

    @Override
    public boolean getDrawFPS() {
        return this.drawFPS.get();
    }

    @Override
    public BooleanProperty drawBoundsProperty() {
        return this.drawBounds;
    }

    @Override
    public boolean getDrawBounds() {
        return this.drawBounds.get();
    }

    @Override
    public BooleanProperty drawGridProperty() {
        return this.drawGrid;
    }

    @Override
    public boolean getDrawGrid() {
        return this.drawGrid.get();
    }

    @Override
    public MapScene setRowAndCol(Tuple count, Tuple size, double scale) {
        this.count = count;
        this.size = size;
        this.scale = scale;
        return this;
    }

    @Override
    public Tuple getGridCount() {
        return count;
    }

    @Override
    public Tuple getGridSize() {
        return size;
    }

    @Override
    public void start() {

        if(animator != null)
            animator.start();
    }

    @Override
    public void stop() {
        if(animator != null)
        animator.stop();
    }

    @Override
    public List<Entity> staticShapes() {
        return this.staticShapes;
    }

    @Override
    public List<Entity> players() {
        return this.players;
    }

    /**
     * <p>
     * this method creates the static entities in the game.
     * <br>
     * use {@link MapBuilder#createBuilder()} to get and instance of MapBuilder called mb.
     * <br>
     * on mb call methods {@link MapBuilder#setCanvas(Canvas)}, {@link MapBuilder#setGrid(Tuple, Tuple)}, and {@link MapBuilder#setGridScale(double)}.
     * <br>
     * call all or any combination of build methods in MapBuilder to create custom map, does not have to be complex. one landmass and a tree is good enough.
     * <br>
     * call {@link MapBuilder#getBackground()} and {@link MapBuilder#getEntities(List)} to retrieve the built entities.
     * </p>
     * @param canvas
     * @return
     */
    @Override
    public MapScene createScene(Canvas canvas) {
        MapBuilder mb = MapBuilder.createBuilder();
        mb.setCanvas(canvas);
        mb.setGrid(count, size);
        mb.setGridScale(scale);

       //make background
        BiFunction<Integer, Integer, Tile> callback = (row, col) -> {
            if (row < 3) {
                int probability = RandUtil.getInt(100);
                if(probability <30)
                return BackgroundTile.EVENING_CLOUD;
            }
            return BackgroundTile.EVENING;
        };
                    mb.buildBackground(callback);
                    mb.buildLandMass(8, 14, 1, 6);
                    mb.buildLandMass(10,4,4,9);
                    mb.buildLandMass(10,21,4,10);
                    mb.buildLandMass(12,13,2,8);
                    mb.buildTree(3, 4, FloraTile.TREE);
                    mb.buildTree(3, 24, FloraTile.TREE);
                    mb.buildTree(8,23,FloraTile.SUNFLOWER_LONG);
                    mb.buildTree(8,11,FloraTile.SUNFLOWER_LONG);
                    background = mb.getBackground();
                    mb.getEntities(staticShapes());
                    return this;
    }

    @Override
    public Entity getBackground() {
        return background;
    }

    @Override
    public double getScale() {
        return scale;
    }

    public boolean inMap(HitBox hitbox) {
        return background.getHitBox().containsBounds(hitbox);
    }

    public MapScene setAnimator(AnimatorInterface newAnimator) {
        if(animator != null){
            animator.stop();
        }
        animator = newAnimator;
        return this;
    }
}
