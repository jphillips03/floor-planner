package floor.planner.util.jogl.raytracer;

import floor.planner.util.jogl.objects.Color;

public class PPMImageFormatter {

    public static String write(Color[][] image) {
        StringBuilder builder = new StringBuilder();
        // P3 means colors are in ASCII
        builder.append("P3\n");
        // add width and height of image
        builder.append(String.format("%d %d\n", image[0].length, image.length));
        // 255 for max color
        builder.append("255\n");

        // render RGB triplets
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                // scale rgb values from 0.0 to 1.0 to be from 0 to 255
                int r = (int) (image[i][j].getRed() * 255.999);
                int g = (int) (image[i][j].getGreen() * 255.999);
                int b = (int) (image[i][j].getBlue() * 255.999);

                // write each value as RGB triplets
                builder.append(String.format("%d %d %d \n", r, g, b));
            }
        }

        return builder.toString();
    }
}
