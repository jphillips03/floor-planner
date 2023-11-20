package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Interval;
import floor.planner.util.math.Ray;

public class Cylinder extends DrawableElement3D {
    private static float angleStepsize = 0.1f;
    private static float height = 1f;
    private static float radius = 0.0625f;

    private float x;
    private float y;

    public Cylinder(float x, float y) {
        this.x = x + 0.5f; // move to middle of tile where cylinder drawn
        this.y = y + 0.5f; // move to middle of tile where cylinder drawn
        // default to red for now...
        this.color = new Color(1f, 0f, 0f);
        this.materialColor = new float[]{ 0.0f, 0.7f, 0.0f, 1f };
    }

    @Override
    public void draw(GL2 gl) {
        // Draw tube...
        gl.glBegin(GL2.GL_QUAD_STRIP);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
            float angle = 0.0f;
            while(angle < 2 * Math.PI) {
                float x = radius * (float) Math.cos(angle) + this.x;
                float y = radius * (float) Math.sin(angle) + this.y;
                gl.glNormal3f(x, y, 0f);
                gl.glVertex3f(x, y , height);
                gl.glVertex3f(x, y , 0.05f);
                angle = angle + angleStepsize;
            }

            // at the right angle the cylinder turns white, so normal is
            // definitely not right (not sure whether that's here or above...)
            gl.glVertex3f(radius + this.x, this.y, height);
            gl.glVertex3f(radius + this.x, this.y, 0.05f);
        gl.glEnd();

        // Draw the circle on top of tube
        gl.glBegin(GL2.GL_POLYGON);
            gl.glNormal3f(0f, 0f, 1f); // this is the top so normal should be up...
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, materialColor, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specularColor, 0);
            gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, shininess, 0);
            angle = 0.0f;
            while(angle < 2 * Math.PI) {
                float x = radius * (float) Math.cos(angle) + this.x;
                float y = radius * (float) Math.sin(angle) + this.y;
                gl.glVertex3f(x, y , (float) height);
                angle = angle + angleStepsize;
            }
            gl.glVertex3f(radius + this.x, this.y, height);
        gl.glEnd();
    }

    @Override
    public boolean intersect(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        return false;
    }
}
