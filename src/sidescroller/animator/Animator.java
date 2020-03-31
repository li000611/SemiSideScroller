package sidescroller.animator;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sidescroller.entity.player.Player;
import sidescroller.entity.property.Drawable;
import sidescroller.entity.property.Entity;
import sidescroller.entity.property.HitBox;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

//I'm using JavaFx library 11.0.
public class Animator extends AbstractAnimator{

    private Color background = Color.ANTIQUEWHITE;

    public void handle(GraphicsContext gc, long now){
       updateEntities();
       clearAndFill(gc,background);
       drawEntities(gc);
    }

    public void drawEntities(GraphicsContext gc){
        Consumer<Entity> draw = (e) ->{
            if(e!=null && e.isDrawable()){
               e.getDrawable().draw(gc);
                   if(map.getDrawBounds()&& e.hasHitbox()){
                e.getHitBox().getDrawable().draw(gc);
            }
        } };
        draw.accept(map.getBackground());

        for( Entity staticShape : map.staticShapes()){
            draw.accept(staticShape);
        }

        for(Entity player : map.players()) {
            draw.accept(player);
        }
    }

    public void updateEntities(){
        List<Entity> players = map.players();
        List<Entity> staticShapes = map.staticShapes();

        for(Entity player : players){
            player.update();
        }

        for(Entity staticShape : staticShapes){
            staticShape.update();
        }

        if(map.getDrawBounds()){
            for(Entity player:players){
                player.getHitBox().getDrawable().setStroke(Color.RED);
            }
        }

        for(Entity staticShape : staticShapes){
            proccessEntityList(map.players().iterator(), staticShape.getHitBox());
        }
    }

    @Override
    public void proccessEntityList(Iterator<Entity> iterator, HitBox shapeHitBox) {
       while(iterator.hasNext()) {
           Entity entity = iterator.next();
           HitBox bounds = entity.getHitBox();
           if (!map.inMap(bounds)) {
            updateEntity(entity, iterator);
           } else if (shapeHitBox!=null && bounds.intersectBounds(shapeHitBox)){
               if (map.getDrawBounds()) {
                   bounds.getDrawable().setStroke(Color.BLUEVIOLET);
               }
               updateEntity(entity, iterator);
            }
        }
    }

    //public void processEntityList(Iterator<Entity>iterator, HitBox shapeHitBox){}

    public void updateEntity(Entity entity, Iterator<Entity>iterator){
        if(entity instanceof Player)
        ((Player) entity).stepBack();
    }
    }

