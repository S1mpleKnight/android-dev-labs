package com.myrunner.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {
    private final static String BLOCKED_PROPERTY = "blocked";
    private final Vector2 velocity;
    private final float speed;
    private final float gravity;
    private final TiledMapTileLayer collisionLayer;
    private final float tileWidth;
    private float increment;
    private final float tileHeight;
    private boolean canJump;

    public Player(Sprite sprite, TiledMapTileLayer collisionLayer) {
        super(sprite);
        this.collisionLayer = collisionLayer;
        this.tileWidth = collisionLayer.getTileWidth();
        this.tileHeight = collisionLayer.getTileHeight();
        velocity = new Vector2(0, 0);
        speed = 70 * 2;
        gravity = 60 * 1.8f;
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float delta) {
        velocity.y -= gravity * delta;

        if (velocity.y > speed)
            velocity.y = speed;
        else if (velocity.y < -speed)
            velocity.y = -speed;

        float oldX = getX(), oldY = getY();
        boolean collisionX = false, collisionY = false;

        setX(getX() + velocity.x * delta);

        increment = collisionLayer.getTileWidth();
        increment = getWidth() < increment ? getWidth() / 2 : increment / 2;

        if (velocity.x < 0) // going left
            collisionX = collidesLeft();
        else if (velocity.x > 0) // going right
            collisionX = collidesRight();

        if (collisionX) {
            setX(oldX);
            velocity.x = 0;
        }

        setY(getY() + velocity.y * delta * 5f);

        increment = collisionLayer.getTileHeight();
        increment = getHeight() < increment ? getHeight() / 2 : increment / 2;

        if (velocity.y < 0) // going down
            canJump = collisionY = collidesBottom();
        else if (velocity.y > 0) // going up
            collisionY = collidesTop();

        if (collisionY) {
            setY(oldY);
            velocity.y = 0;
        }
    }

    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(BLOCKED_PROPERTY);
    }

    public boolean collidesRight() {
        for (float step = 0; step <= getHeight(); step += increment)
            if (isCellBlocked(getX() + getWidth(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft() {
        for (float step = 0; step <= getHeight(); step += increment)
            if (isCellBlocked(getX(), getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop() {
        for (float step = 0; step <= getWidth(); step += increment)
            if (isCellBlocked(getX() + step, getY() + getHeight()))
                return true;
        return false;

    }

    public boolean collidesBottom() {
        for (float step = 0; step <= getWidth(); step += increment)
            if (isCellBlocked(getX() + step, getY()))
                return true;
        return false;
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                if (canJump) {
                    velocity.y = speed / 1.8f;
                    canJump = false;
                }
                break;
            case Input.Keys.A:
                velocity.x = -speed;
                break;
            case Input.Keys.D:
                velocity.x = speed;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.D:
                velocity.x = 0;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (screenY > (Gdx.graphics.getHeight() / 2)) {
            if ((Gdx.graphics.getWidth() / 2) > screenX) {
                velocity.x = -speed;
            } else if ((Gdx.graphics.getWidth() / 2) < screenX) {
                velocity.x = speed;
            }
        }
        if ((screenY < (Gdx.graphics.getHeight() / 2)) && canJump) {
            velocity.y = speed / 1.8f;
            canJump = false;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (screenY > getY()) {
            velocity.x = 0;
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
