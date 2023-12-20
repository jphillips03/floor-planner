package floor.planner.util.raytracer.pdf;

import floor.planner.util.math.Random;
import floor.planner.util.math.Vector;

public class MixturePdf extends Pdf {
    private Pdf[] pdfs;

    public MixturePdf(Pdf p0, Pdf p1) {
        this.pdfs = new Pdf[]{
            p0, p1
        };
    }

    public double value(Vector direction) {
        return 0.5 * this.pdfs[0].value(direction) + 0.5 * pdfs[1].value(direction);
    }

    public Vector generate() {
        if (Random.randomDouble() < 0.5) {
            return this.pdfs[0].generate();
        } else {
            return this.pdfs[1].generate();
        }
    }

    /*
     * public:
    vec3 generate() const override {
        if (random_double() < 0.5)
            return p[0]->generate();
        else
            return p[1]->generate();
    }
     */
}
