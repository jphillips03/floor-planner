package floor.planner.util.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Matrix class defines convenience methods for matrices.
 * 
 * @author jphillips3
 * @version November 9, 2023
 */
public class Matrix {
    private static final Logger logger = LoggerFactory.getLogger(Matrix.class);
	
    /*
     * Creates and returns a copy of given matrix.
     */
    public static float[][] copy(float[][] val) {
        float[][] copy = new float[val.length][val[0].length];
        for (int i =0; i < val.length; i++) {
            for (int j= 0; j < val[0].length; j++) {
                copy[i][j] = val[i][j];
            }
        }
        return copy;
    }

    /**
     * Multiplies two matrices together and returns the result.
     *
     * @param mat1 The first matrix to multiply.
     * @param mat2 The second matrix to multiply.
     * @return The first matrix multiplied by the second.
     */
    public static float[][] multiply(float[][] mat1, float[][] mat2) {
        float[][] result = new float[mat1.length][mat1[0].length];
        for(int i = 0; i < mat1.length; i++) {
            for(int j = 0; j < mat1[0].length; j++) {
                float[] row = mat1[i];
                float[] col = getColumn(mat2,j);
                float res = 0;
                for(int k = 0; k < row.length; k++) {
                    res += row[k]*col[k];
                }
                result[i][j] = res;
            }
        }
        return result;
    }

    /**
     * Multiplies a matrix by an array.
     *
     * @param matrix
     * @param array
     * @return
     */
    public static float[] multiply(float[][] matrix, float[] array) {
        float[] result = new float[matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            float[] row = matrix[i];
            float res = 0;
            for(int j = 0; j < matrix[0].length; j++) {
                res += row[j]*array[j];
            }
            result[i] = res;
        }
        return result;
    }

    /**
     * Computes the inverse of the given matrix.
     *
     * @param matrix The matrix to compute the inverse.
     * @return The inverse of the given matrix.
     */
    public static float[][] inverse(float[][] matrix) {
        float[][] cof = cofactor(matrix);
        float det = determinant(matrix);
        for(int i = 0; i < cof.length; i++) {
            for(int j = 0; j < cof[0].length; j++) {
                cof[i][j] /= det;
            }
        }
        return cof;
    }

    /**
     * Rotates the given matrix vertices above the x-axis by the given amount
     * of degrees.
     *
     * @param matrix The matrix to rotate about the x-axis.
     * @param degrees The degrees to rotate.
     * @return
     */
    public static float[][] rotateX(float[][] matrix, float degrees) {
        logger.debug("Rotate matrix about x-axis");
        return rotate(matrix, degrees, 1, 2);
    }

    public static float[][] rotateY(float[][] matrix, float degrees) {
        logger.debug("Rotate matrix about y-axis");
        return rotate(matrix, degrees, 0, 2);
    }

    public static float[][] rotateZ(float[][] matrix, float degrees) {
        logger.debug("Rotate matrix about z-axis");
        return rotate(matrix, degrees, 0, 1);
    }

    public static float[][] rotate(float[][] matrix, float degrees, int coord1, int coord2) {
        float[][] copy = new float[matrix.length][matrix[0].length];
        double radians = Math.toRadians(degrees);
        double sinTheta = Math.sin(radians);
        double cosTheta = Math.cos(radians);
        for (int i = 0; i < copy.length; i++) {
            if (coord1 == 0 && coord2 == 2) {
                // rotate y
                copy[i][coord1] = (float) (cosTheta * matrix[i][coord1] + sinTheta * matrix[i][coord2]);
                copy[i][1] = matrix[i][1];
                copy[i][coord2] = (float) (-sinTheta * matrix[i][coord1] + cosTheta * matrix[i][coord2]);
            } else {
                // rotate x or z (formulas are same for both; just different coordinates...)
                int coord3;
                if (coord1 == 0) {
                    // we are rotating z
                    coord3 = 2;
                } else {
                    // we are rotating x
                    coord3 = 0;
                }

                copy[i][coord1] = (float) (cosTheta * matrix[i][coord1] - sinTheta * matrix[i][coord2]);
                copy[i][coord2] = (float) (sinTheta * matrix[i][coord1] + cosTheta * matrix[i][coord2]);
                copy[i][coord3] = matrix[i][coord3];
            }
        }
        return copy;
    }

    public static float[][] scaleX(float[][] matrix, float delta) {
        return scale(matrix, delta, 0);
    }

    public static float[][] scaleY(float[][] matrix, float delta) {
        return scale(matrix, delta, 1);
    }

    public static float[][] scaleZ(float[][] matrix, float delta) {
        return scale(matrix, delta, 2);
    }

    private static float[][] scale(float[][] matrix, float delta, int coord) {
        float[][] copy = copy(matrix);
        for (int i = 0; i < copy.length; i++) {
            copy[i][coord] *= delta;
        }
        return copy;
    }

    /**
     * Returns a copy of the given matrix moved in the X direction by the
     * given delta.
     *
     * @param matrix The matrix of matrix to move.
     * @param delta The amount to move the matrix.
     * @return A copy of the given matrix that have been moved.
     */
    public static float[][] translateX(float[][] matrix, float delta) {
        return translate(matrix, delta, 0);
    }

    /**
     * Returns a copy of the given matrix moved in the Y direction by the
     * given delta.
     *
     * @param matrix The matrix of matrix to move.
     * @param delta The amount to move the matrix.
     * @return A copy of the given matrix that have been moved.
     */
    public static float[][] translateY(float[][] matrix, float delta) {
        return translate(matrix, delta, 1);
    }

    /**
     * Returns a copy of the given matrix moved in the Z direction by the
     * given delta.
     *
     * @param matrix The matrix of matrix to move.
     * @param delta The amount to move the matrix.
     * @return A copy of the given matrix that have been moved.
     */
    public static float[][] translateZ(float[][] matrix, float delta) {
        return translate(matrix, delta, 2);
    }

    /**
     * Returns a copy of the given matrix moved in the given coordinate
     * plane by the given delta.
     *
     * @param matrix The matrix of matrix to move.
     * @param delta The amount to move the matrix.
     * @param coord The coordinate plane to move (x, y, or z i.e. 0, 1, 2).
     * @return A copy of the given matrix that have been moved.
     */
    private static float[][] translate(
        float[][] matrix,
        float delta,
        int coord
    ) {
        float[][] copy = copy(matrix);
        for (int i = 0; i < copy.length; i++) {
            copy[i][coord] += delta;
        }
        return copy;
    }

    public static float[][] translatePartialX(
        float[][] matrix,
        float delta,
        int[] indexes
    ) {
        return translatePartial(matrix, delta, indexes, 0);
    }

    public static float[][] translatePartialY(
        float[][] matrix,
        float delta,
        int[] indexes
    ) {
        return translatePartial(matrix, delta, indexes, 1);
    }

    public static float[][] translatePartialZ(
        float[][] matrix,
        float delta,
        int[] indexes
    ) {
        return translatePartial(matrix, delta, indexes, 2);
    }

    private static float[][] translatePartial(
        float[][] matrix,
        float delta,
        int[] indexes,
        int coord
    ) {
        float[][] copy = copy(matrix);
        for (int i = 0; i < indexes.length; i++) {
            copy[indexes[i]][coord] += delta;
        }
        return copy;
    }

    /**
     * Computes the transpose of the given matrix.
     *
     * @param matrix The matrix to compute the transpose of.
     * @return The transpose of the given matrix.
     */
    public static float[][] transpose(float[][] matrix) {
        float[][] result = new float[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[j][i];
            }
        }
        return result;
    }

    /**
     * Converts the given array into a 4x4 matrix.
     *
     * @param vector The array to convert into a matrix.
     * @return A matrix representation of the array.
     */
    public static float[][] convertToMatrix(float[] vector) {
        float[][] matrix = new float[vector.length/4][vector.length/4];
        int index = 0;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = vector[index];
                index++;
            }
        }
        return matrix;
    }

    /**
     * Converts the given matrix to a string.
     *
     * @param matrix The matrix to convert to a string.
     */
    public static String toString(float[][] matrix) {
        String res = "";
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                res += matrix[i][j] + " ";
            }
            res += "\n";
        }
        return res;
    }

    //---Private Helper Methods-------------------------------------------------

    /**
     * Computes the determinant of a 4x4 matrix.
     *
     * @param matrix The matrix to compute the determinant.
     * @return The determinant of the give matrix.
     */
    private static float determinant(float[][] matrix) {
        float det = 0;
        for(int i = 0; i < matrix.length; i++) {
            if(i % 2 == 0) {
                det += matrix[0][i]*determinant3x3(getMatrix(matrix,0,i));
            }
            else {
                det -= matrix[0][i]*determinant3x3(getMatrix(matrix,0,i));
            }
        }
        return det;
    }

    /**
     * Calculates the determinant of a 3X3 matrix.
     *
     * @param matrix The matrix to calculate the determinant for.
     * @return The determinant of the given 3X3 matrix.
     */
    private static float determinant3x3(float[][] matrix) {
        float det = 0;
        for(int i = 0; i < matrix.length; i++) {
            if(i % 2 == 0) {
                det += matrix[0][i]*determinant2x2(getMatrix(matrix,0,i));
            }
            else {
                det -= matrix[0][i]*determinant2x2(getMatrix(matrix,0,i));
            }
        }
        return det;
    }

    /**
     * Computes the determinant of a 2X2 matrix.
     *
     * @param matrix The matrix to computer the determinant of.
     * @return The determinant of the matrix.
     */
    private static float determinant2x2(float[][] matrix) {
        return matrix[0][0]*matrix[1][1] - matrix[1][0]*matrix[0][1];
    }

    /**
     * Computes the cofactor of the given matrix.
     *
     * @param matrix The matrix to compute the cofactor.
     * @return The cofactor of the given matrix.
     */
    private static float[][] cofactor(float[][] matrix) {
        float[][] result = new float[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                if((i+j) % 2 == 0) {
                    result[j][i] = determinant3x3(getMatrix(matrix,i,j));
                }
                else {
                    result[j][i] = -determinant3x3(getMatrix(matrix,i,j));
                }
            }
        }
        return result;
    }

    /**
     * Returns a column from the given matrix as an array.
     *
     * @param mat The matrix to get the column from.
     * @param col The column to get from the matrix.
     * @return The column from the given matrix as an array.
     */
    private static float[] getColumn(float[][] mat, int col) {
        float[] column = new float[mat[0].length];
        for(int i = 0; i < mat.length; i++) {
            column[i] = mat[i][col];
        }
        return column;
    }

    /**
     * Returns a n-1Xn-1 matrix given a nXn matrix.
     *
     * @param matrix The matrix to get the n-1Xn-1 matrix from.
     * @param row The row not to include.
     * @param col The column not to include.
     * @return A n-1Xn-1 matrix with the row and column removed.
     */
    private static float[][] getMatrix(float[][] matrix, int row, int col) {
        float[][] result = new float[matrix.length-1][matrix.length-1];
        int r = 0;
        for(int i = 0; i < matrix.length; i++) {
            int c = 0;
            for(int j = 0; j < matrix[0].length; j++) {
                if(i != row && j != col) {
                    result[r][c] = matrix[i][j];
                    c++;
                }
            }
            if(i != row) r++;
        }
        return result;
    }
}
