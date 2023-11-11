package floor.planner.models;

public class Light {
    /** The position of the light in the 3D view. */
	private float[] position; // = {1f, 0f, 8f, 0f};
	/** The diffuse lighting in the 3D view. */
	private float[] diffuse = {1f, 1f, 1f, 1f};
	/** The specular lighting in the 3D view. */
	private float[] specular = {1f, 1f, 1f, 1f};
	/** The ambient lighting in the 3D view. */
	private float[] ambient = { 1f, 0f, 0f, 1.0f };

    public Light(int width, int height, int numFloors) {
        float[] position = { (float) width / 2f, (float) height / 2f, numFloors * 2, 0f };
        this.position = position;
    }

    public float[] getPosition() {
        return this.position;
    }

    public void setPosition(float[] pos) {
        this.position = pos;
    }

    public float[] getAmbient() {
        return this.ambient;
    }

    public float[] getDiffuse() {
        return this.diffuse;
    }

    public float[] getSpecular() {
        return this.specular;
    }
}
