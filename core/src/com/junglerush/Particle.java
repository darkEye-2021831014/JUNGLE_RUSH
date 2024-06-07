package com.junglerush;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class Particle {
    public float x, y;
    public float angle;
    public float radius;
    public float speed;

    public Particle(float centerX,float centerY,float initialRadius, float initialAngle, float speed) {
        this.angle = initialAngle;
        this.radius = initialRadius;
        this.speed = speed;
        updatePosition(centerX, centerY);
    }

    public void update(float deltaTime, float centerX, float centerY) {
        // Update angle
        angle += speed * deltaTime;
        // Ensure angle stays within 0-360 degrees
        if (angle > 360) {
            angle -= 360;
        }
        // Update position
        updatePosition(centerX, centerY);
    }

    private void updatePosition(float centerX, float centerY) {
        x = centerX + radius * (float) Math.cos(Math.toRadians(angle));
        y = centerY + radius * (float) Math.sin(Math.toRadians(angle));
    }

    public void draw(ShapeRenderer shapeRenderer, Color color, float radius)
    {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(this.x, this.y, radius); // Adjust particle size as needed
    }
}
