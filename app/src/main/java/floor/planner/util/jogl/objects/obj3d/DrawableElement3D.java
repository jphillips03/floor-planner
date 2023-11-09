package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import java.util.List;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.objects.DrawableElement;

public abstract class DrawableElement3D extends DrawableElement {
    public Color color;

    public void drawPolygon(GL2 gl, List<float[]> points) {
        gl.glColor3f(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
        gl.glBegin(GL2.GL_POLYGON);
        for (float[] point : points) {
            gl.glVertex3f(point[0], point[1], point[2]);
        }
        gl.glEnd();
    }
}
