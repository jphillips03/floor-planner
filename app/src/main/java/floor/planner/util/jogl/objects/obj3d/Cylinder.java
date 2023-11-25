package floor.planner.util.jogl.objects.obj3d;

import com.jogamp.opengl.GL2;

import floor.planner.util.jogl.material.Lambertian;
import floor.planner.util.jogl.material.Material;
import floor.planner.util.jogl.objects.Color;
import floor.planner.util.jogl.raytracer.Aabb;
import floor.planner.util.jogl.raytracer.IntersectRecord;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;

public class Cylinder extends DrawableElement3D {
    private static float angleStepsize = 0.1f;
    private static float height = 1f;
    private static float radius = 0.0625f;

    private Vector center;
    private float x;
    private float y;
    private Material mat;

    public Cylinder(float x, float y) {
        this.x = x + 0.5f; // move to middle of tile where cylinder drawn
        this.y = y + 0.5f; // move to middle of tile where cylinder drawn
        this.center = new Vector(this.x, this.y, 0.5);
        // default to red for now...
        this.color = new Color(1f, 0f, 0f);
        this.materialColor = new float[]{ 0.0f, 0.7f, 0.0f, 1f };
        this.mat = new Lambertian(new Color(this.materialColor));
        this.boundingBox = new Aabb();
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
        double a = (r.getDirection().getX() * r.getDirection().getX()) + (r.getDirection().getY() * r.getDirection().getY());
        double b = 2 * r.getDirection().getX() * (r.getOrigin().getX() - this.center.getX()) + 2 * r.getDirection().getY() * (r.getOrigin().getY() - this.center.getY());
        double c = Math.pow(r.getOrigin().getX() - this.center.getX(), 2) + Math.pow(r.getOrigin().getY() - this.center.getY(), 2) - radius * radius;

        double discriminant = b*b - (4*a*c);
        //if(Math.abs(discriminant) < 0.001) return false;
        if(discriminant < 0.0) return false;
        
        // find nearest root that lies in acceptable range
        double halfB = b / 2;
        double sqrtD = (double) Math.sqrt((double) discriminant);
        double root = (-halfB - sqrtD) / a;
        if (!rayT.surrounds(root)) {
            root = (-halfB + sqrtD) / a;
            if (!rayT.surrounds(root)) {
                return false;
            }
        }

        Vector point = Vector.add(r.getOrigin(), r.getDirection().multiply(root));
        if (point.getZ() < 0.05 || point.getZ() > 1) {
            return false;
        }

        rec.setT(root);
        rec.setP(r.at(rec.getT()));
        Vector outwardNormal = Vector.subtract(rec.getP(), this.center).divide(radius);
        rec.setFaceNormal(r, outwardNormal);
        rec.setMaterial(this.mat);
        return true;
    }

    public Point3D getMidPoint() {
        return new Point3D(this.center);
    }
}
