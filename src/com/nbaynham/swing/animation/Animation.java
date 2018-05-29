package com.nbaynham.swing.animation;

import java.applet.Applet;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class Animation extends Applet implements Runnable {
    private static final int SIZE = 500;
    private Image image;
    private Graphics graphics;
    private static final long serialVersionUID = 1L;
    private List<Ball> balls = new ArrayList<>();

    private int MAX_BALL_SIZE = 5;
    private int MAX_SPEED = 2;
    private int ballCounter = 10;


    @Override
    public void run() {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        int ballCounter = 10;
        for (int i = 0; i < ballCounter; i++) {
            balls.add(Ball.getRandomBall());
        }

        while(true) {
            Ball.collisions(balls);
            for (Ball b : balls) {
                b.move();
            }

            repaint();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("Interrupt Encountered.");
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    public void update(Graphics g) {
        if (image == null) {
            image = createImage(this.getSize().width, this.getSize().height);
            graphics = image.getGraphics();
        }

        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, this.getSize().width, this.getSize().height);
        graphics.setColor(getForeground());
        for (Ball ball : balls) {
            ball.paint(graphics);
        }
        g.drawImage(image, 0, 0, this);
    }
}


class Ball {
    private static int SIZE = 500;
    private int radius;
    private int xPosition;
    private int yPosition;
    private int xDirection;
    private int yDirection;
    private Color color = Color.red;
    private int speed;
    private static Random generator = new Random();

    private Ball(int radius, int xPosition, int yPosition, int xDirection, int yDirection, Color color, int speed) {
        setRadius(radius);
        setxPosition(xPosition);
        setyPosition(yPosition);
        setxDirection(xDirection);
        setyDirection(yDirection);
        this.color = color;
        this.speed = speed;
    }

    private void setRadius(int radius) {
        this.radius = radius;
    }

    private void setxPosition(int position) {
        xPosition = position;
    }

    private void setyPosition(int position) {
        yPosition = position;
    }

    private void setxDirection(int direction) {
        xDirection = direction;
    }

    private void setyDirection(int direction) {
        yDirection = direction;
    }

    void move() {

        if (yPosition < 0 || yPosition > SIZE) yDirection = -yDirection;
        if (xPosition < 0 || xPosition > SIZE) xDirection = -xDirection;

        yPosition = yPosition + yDirection * speed;
        xPosition = xPosition + xDirection * speed;
    }

    void paint(Graphics g) {
        g.setColor(color);
        g.fillOval(xPosition - radius, yPosition - radius, 2 * radius, 2 * radius);
    }

    static Ball getRandomBall() {
        int radius = generator.nextInt(8) + 8; // random sized ball
        int xPosition = generator.nextInt(SIZE - radius)+10; // random x coordinate
        int yPosition = generator.nextInt(SIZE - radius)+10; // random y coordinate
        int xDirection = generator.nextInt(3)+1; // random x direction
        int yDirection = generator.nextInt(3)+1;
        float r = generator.nextFloat();
        float g = generator.nextFloat();
        float b = generator.nextFloat();
        Color color = new Color(r,g, b);
        int speed = generator.nextInt(3) + 1;
        return new Ball(radius, xPosition, yPosition, xDirection, yDirection, color, speed);
    }

    private static void changeColor(Ball ball) {
        float r = generator.nextFloat();
        float g = generator.nextFloat();
        float b = generator.nextFloat();
        ball.color = new Color(r,g, b);
    }

    static void collisions(List<Ball> balls) {
        for (Ball firstBall : balls) {
            for (Ball secondBall : balls) {
                if (firstBall != secondBall) {
                    isCollision(firstBall, secondBall);
                }
            }
        }
    }

    private static void isCollision(Ball first, Ball second) {
        int distanceX = Math.abs(first.xPosition - second.xPosition);
        int distanceY = Math.abs(first.yPosition - second.yPosition);
        int radius = first.radius + second.radius;
        if (distanceX < radius && distanceY < radius) {
            first.xDirection = -first.xDirection;
            first.yDirection = - first.yDirection;
            first.move();
            if (second.xDirection <= 0) {
                second.xDirection = -generator.nextInt(5) + 1;
            } else {
                second.xDirection = -second.xDirection;
            }
            if (second.yDirection <= 0) {
                second.yDirection = -generator.nextInt(5) + 1;
            } else {
                second.yDirection = -second.yDirection;
            }
            first.speed = generator.nextInt(3) + 1;
            second.speed = generator.nextInt(3) + 1;
            Ball.changeColor(first);
            Ball.changeColor(second);
            second.move();
        }
    }
}


