package floor.planner.util.math;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VectorUnitTest {
    private Vector unitVector = new Vector(1, 1, 1);

    @Test
    public void testGetXShouldReturnExpectedValue() {
        assertEquals(1, this.unitVector.getX());
    }

    @Test
    public void testGetYShouldReturnExpectedValue() {
        assertEquals(1, this.unitVector.getY());
    }

    @Test
    public void testGetZShouldReturnExpectedValue() {
        assertEquals(1, this.unitVector.getZ());
    }

    @Test
    public void testGetValuesShouldReturnExpectedValue() {
        assertArrayEquals(
            new double[]{ 1, 1, 1 },
            this.unitVector.getValues()
        );
    }

    @Test
    public void testGetFloatValuesShouldReturnExpectedValue() {
        assertArrayEquals(
            new float[]{ 1f, 1f, 1f },
            this.unitVector.getFloatValues()
        );
    }

    @Test
    public void testAddDoubleValuesShouldReturnNewVectorWithSummedValues() {
        // setup: expected result
        Vector expected = new Vector(2, 3, 4);

        // and: array of double values to add with
        double[] addArray = new double[]{ 1, 2, 3 };

        // when: add method called with double values array
        Vector actual = this.unitVector.add(addArray);

        // then: the expected result should match the actual one
        assertEquals(expected, actual);
    }

    @Test
    public void testAddVectorShouldReturnNewVectorWithSummedValues() {
        // setup: expected result
        Vector expected = new Vector(2, 3, 4);

        // and: Vector to add with
        Vector addVector = new Vector(1, 2, 3);

        // when: add method called with above vector
        Vector actual = this.unitVector.add(addVector);

        // then: the expected result should match the actual one
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtractDoubleValuesShouldReturnNewVectorWithSubtractedValues() {
        // setup: expected result
        Vector expected = new Vector(0, -1, -2);

        // and: array of double values to subtract with
        double[] subArray = new double[]{ 1, 2, 3 };

        // when: subtract method called with above array
        Vector actual = this.unitVector.subtract(subArray);

        // then: the expected result should match the actual one
        assertEquals(expected, actual);
    }

    @Test
    public void testSubtractVectorShouldReturnNewVectorWithSubtractedValues() {
        // setup: expected result
        Vector expected = new Vector(0, -1, -2);

        // and: Vector of values to subtract with
        Vector subVector = new Vector(1, 2, 3);

        // when: subtrat method called with above Vector
        Vector actual = this.unitVector.subtract(subVector);

        // then: the expected result should match the actual one
        assertEquals(expected, actual);
    }
}
