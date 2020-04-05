package sidescroller.scene;

import javafx.scene.canvas.Canvas;
import sidescroller.entity.GenericEntity;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;
import sidescroller.entity.property.Sprite;
import sidescroller.entity.sprite.*;
import sidescroller.entity.sprite.tile.BackgroundTile;
import sidescroller.entity.sprite.tile.FloraTile;
import sidescroller.entity.sprite.tile.PlatformTile;
import sidescroller.entity.sprite.tile.Tile;
import utility.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class MapBuilder implements MapBuilderInterface {
    private Tuple rowColCount;
    private Tuple dimension;
    private double scale;
    private Canvas canvas;
    private Entity background;
    private List<Entity> landMass;
    private List<Entity> other;

 
    //Initialize the lists inside of the constructor.
    protected MapBuilder() {
        landMass = new ArrayList<>();
        other = new ArrayList<>();
    }

    //the static method only returns an instance of MapBuilder.
    public static MapBuilder createBuilder() {
        MapBuilder mapBuilder = new MapBuilder();
        return mapBuilder;
    }

    /**
     * set the canvas which this builder will use.
     *
     * @param canvas - instance of canvas.
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder setCanvas(Canvas canvas) {
        this.canvas = canvas;
        return this;
    }

    /**
     * set the number of rows and columns which will be in this application.
     * plus the height and width of each cell.
     *
     * @param rowColCount - use Tuple::pair and pass max rows and counts
     * @param dimension   - use Tuple::pair and pass width and height.
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder setGrid(Tuple rowColCount, Tuple dimension) {
        this.rowColCount = rowColCount;
        this.dimension = dimension;
        return this;
    }

    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Background". save in a {@link BackgroundSprite} variable.
     * <br>
     * on the return call {@link BackgroundSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( 0, 0).
     * <br>
     * on the return call {@link BackgroundSprite#createSnapshot(Canvas, Tuple, BiFunction)} and pass canvas, rowColCount, and callback.
     * <br>
     * use {@link HitBox#build(double, double, double, double)} and pass 0, 0, scale * dimension.x() * rowColCount.y(), and scale * dimension.y() * rowColCount.x().
     * <br>
     * finally instantiate background using {@link GenericEntity#GenericEntity(Sprite, HitBox)}.
     * <br>
     * </p>
     *
     * @param callback - a lambda which takes two arguments (row and col) and returns a Tile enum of type {@link BackgroundTile}. will be used to generate background.
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder buildBackground(BiFunction<Integer, Integer, Tile> callback) {
        BackgroundSprite background = SpriteFactory.get("Background");
        background.init(scale, dimension, Tuple.pair(0, 0));
        background.createSnapshot(canvas, rowColCount, callback);
        HitBox hitBox = HitBox.build(0, 0, scale * dimension.x() * rowColCount.y(), scale * dimension.y() * rowColCount.x());
        this.background = new GenericEntity(background, hitBox);
        return this;
    }

    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Land". save in a {@link LandSprite} variable.
     * <br>
     * on the return call {@link LandSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( colPos, rowPos).
     * <br>
     * on the return call {@link LandSprite#createSnapshot(Canvas, int, int)} and pass canvas, rowConut, and colCount.
     * <br>
     * use {@link HitBox#build(double, double, double, double)} and pass colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, and scale * dimension.y() * rowConut.
     * <br>
     * finally add the instance of {@link GenericEntity#GenericEntity(Sprite, HitBox)} to landMass list.
     * <br>
     * </p>
     *
     * @param rowPos   - first row from the top which the land will start.
     * @param colPos   - first column from the left which the land will start.
     * @param rowConut - number of rows which the land mass will cover.
     * @param colCount - number of columns which the land mass will cover.
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder buildLandMass(int rowPos, int colPos, int rowConut, int colCount) {
        LandSprite land = SpriteFactory.get("Land");
        land.init(scale, dimension, Tuple.pair(colPos, rowPos));
        land.createSnapshot(canvas, rowConut, colCount);
        HitBox hitBox = HitBox.build(colPos * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * colCount, scale * dimension.y() * rowConut);
        landMass.add(new GenericEntity(land, hitBox));
        return this;
    }

    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Tree". save in a {@link TreeSprite} variable.
     * <br>
     * on the return call {@link TreeSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( colPos, rowPos).
     * <br>
     * on the return call {@link TreeSprite#createSnapshot(Canvas, Tile)} and pass canvas and tile.
     * <br>
     * by default there is no hitbox.
     * <br>
     * finally add the instance of {@link GenericEntity#GenericEntity(Sprite, HitBox)} to other list.
     * <br>
     * </p>
     *
     * @param rowPos - first row from the top.
     * @param colPos - first column from the left.
     * @param tile   - a tree type from enum {@link FloraTile}.
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder buildTree(int rowPos, int colPos, Tile tile) {
        TreeSprite tree = SpriteFactory.get("Tree");
        tree.init(scale, dimension, Tuple.pair(colPos, rowPos));
        tree.createSnapshot(canvas, tile);
        other.add(new GenericEntity(tree, null));
        return this;
    }

    /**
     * <p>
     * build the background sprite.<br>
     * use {@link SpriteFactory#get(String)} and pass to it "Platform". save in a {@link PlatformSprite} variable.
     * <br>
     * on the return call {@link PlatformSprite#init(double, Tuple, Tuple)} and pass scale, dimension, and Tuple.pair( colPos, rowPos).
     * <br>
     * on the return call {@link PlatformSprite#createSnapshot(Canvas, Tile, int)} and pass canvas, tile, and length.
     * <br>
     * use {@link HitBox#build(double, double, double, double)} and pass (colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), and scale * dimension.y() / 2.
     * <br>
     * finally add the instance of {@link GenericEntity#GenericEntity(Sprite, HitBox)} to other list.
     * <br>
     * </p>
     *
     * @param rowPos - first row from the top.
     * @param colPos - first column from the left.
     * @param length - number of columns which the platform will stretch.
     * @param tile   - a platform type from enum {@link PlatformTile}.
     * @return
     */
    @Override
    public MapBuilder buildPlatform(int rowPos, int colPos, int length, Tile tile) {
        PlatformSprite platform = SpriteFactory.get("Platform");
        platform.init(scale, dimension, Tuple.pair(colPos, rowPos));
        platform.createSnapshot(canvas, tile, length);
        HitBox hitBox = HitBox.build((colPos + .5) * dimension.x() * scale, rowPos * dimension.y() * scale, scale * dimension.x() * (length - 1), scale * dimension.y() / 2);
        other.add(new GenericEntity(platform, hitBox));
        return this;
    }

    /**
     * pass a list which will be populated by landmass first then other objects.
     *
     * @param list - a list to be populated by the created entities.
     * @return the populated list.
     * @throws NullPointerException if list is null which means it was not initialized.
     */
    @Override
    public List<Entity> getEntities(List<Entity> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        list.addAll(landMass);
        list.addAll(other);
        return list;
    }

    /**
     * set the scale value for this builder. this scale will be used to
     * multiply all widths and heights.
     *
     * @param scale - a double value
     * @return the current instance of this object.
     */
    @Override
    public MapBuilder setGridScale(double scale) {
        this.scale = scale;
        return this;
    }

    /**
     * @return the background Entity.
     */
    @Override
    public Entity getBackground() {
        return this.background;
    }
}
