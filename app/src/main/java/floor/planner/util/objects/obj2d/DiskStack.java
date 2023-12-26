package floor.planner.util.objects.obj2d;

import com.jogamp.opengl.GL2;

import floor.planner.constants.Orientation;
import floor.planner.util.math.Color;
import floor.planner.util.math.Point2D;

public class DiskStack extends DrawableElement2D {
    private float colorDelta;
    private Color colorStart;
    private double radius;

    @Override
    public void setColor(Color c) {
        this.colorStart = new Color(c.multiply(colorDelta));
    }

    public DiskStack(Point2D point, Orientation orientation, double radius) {
        super(point, orientation);
        this.radius = radius;
        this.colorDelta = 0.125f;
        this.colorStart = new Color(this.color.multiply(0.125));
    }

    public void initPoints() {}

    @Override
    public void draw(GL2 gl) {
        Color color = this.colorStart;
        double radius = this.radius;
        for (int i = 0; i < 9; i++) {
            gl.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
            Disk.draw(gl, this.point.getX(), this.point.getY(), radius);
            color = new Color(
                (color.getRed() + this.colorDelta) * this.color.getRed(),
                (color.getGreen() + this.colorDelta) * this.color.getGreen(),
                (color.getBlue() + this.colorDelta) * this.color.getBlue()
            );
            radius -= 0.025;
        }
    }
}
