package floor.planner.util.objects.obj3d;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jogamp.opengl.GL2;

import floor.planner.util.math.Color;
import floor.planner.util.math.Interval;
import floor.planner.util.math.MathUtil;
import floor.planner.util.math.Point3D;
import floor.planner.util.math.Ray;
import floor.planner.util.math.Vector;
import floor.planner.util.raytracer.Aabb;
import floor.planner.util.raytracer.IntersectRecord;
import floor.planner.util.raytracer.material.Lambertian;
import floor.planner.util.raytracer.material.Material;

public class Cylinder extends DrawableElement3D {
    private static final Logger logger = LoggerFactory.getLogger(Cylinder.class);
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
            while(angle < 2 * MathUtil.PI) {
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
            while(angle < 2 * MathUtil.PI) {
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
        // check if we intersected the top of cylinder first...
        if (this.intersectDisk(r, rayT, rec)) {
            return true;
        }

        double a = (r.getDirection().getX() * r.getDirection().getX()) + (r.getDirection().getY() * r.getDirection().getY());
        double b = 2 * r.getDirection().getX() * (r.getOrigin().getX() - this.center.getX()) + 2 * r.getDirection().getY() * (r.getOrigin().getY() - this.center.getY());
        double c = Math.pow(r.getOrigin().getX() - this.center.getX(), 2) + Math.pow(r.getOrigin().getY() - this.center.getY(), 2) - radius * radius;

        double discriminant = b*b - (4*a*c);
        //if(Math.abs(discriminant) < 0.001) return false;
        if(discriminant < 0.0) return false;
        
        // find nearest root that lies in acceptable range
        double sqrtD = (double) Math.sqrt((double) discriminant);
        double root = (-b - sqrtD) / (2*a);

        if (!rayT.surrounds(root)) {
            root = (-b + sqrtD) / (2*a);
            if (!rayT.surrounds(root)) {
                return false;
            }
        }

        Vector point = r.getOrigin().add(r.getDirection().multiply(root));
        if (point.getZ() < 0.05 || point.getZ() > 1) {
            return false;
        }

        rec.setT(root);
        rec.setP(r.at(rec.getT()));
        Vector outwardNormal = new Vector(point.getX(), point.getY(), 0);
        rec.setNormal(outwardNormal);
        rec.setMaterial(this.mat);
        return true;
    }

    private boolean intersectDisk(
        Ray r,
        Interval rayT,
        IntersectRecord rec
    ) {
        float[] originf = new float[]{ this.x - 0.5f, this.y - 0.5f, 1f };
        float[] uf = new float[]{ this.x + 1, this.y - 0.5f, 1 };
        float[] vf = new float[]{ this.x - 0.5f, this.y + 1, 1 };
        Quad q = new Quad(originf, uf, vf, mat);
        if (q.intersect(r, rayT, rec)) {
            Vector p = r.getOrigin().add(r.getDirection().multiply(rec.getT()));
            Vector v1 = p.subtract(new Vector(this.x, this.y, 1));
            double d = Vector.dot(v1, v1);
            return Math.sqrt(d) <= radius;
        } else {
            return false;
        }
    }

    public Point3D getMidPoint() {
        return new Point3D(this.center);
    }
}
