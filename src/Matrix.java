public class Matrix {
    private double[][] mat;

    public Matrix(int numRows, int numCols) {
        mat = new double[numRows][numCols];
    }

    public int numRows() {
        return mat.length;
    }

    public int numCols() {
        return mat[0].length;
    }

    public double getElement(int row, int col) {
        return mat[row][col];
    }

    public void setElement(int row, int col, double value) {
        mat[row][col] = value;
    }

    public boolean add(Matrix other) {
        if (other.numRows() != this.numRows() || other.numCols() != this.numCols())
            return false;
        for(int row = 0; row < this.numRows(); row++) {
            for(int col = 0; col < this.numCols(); col++) {
                mat[row][col] += other.getElement(row, col);
            }
        }
        return true;
    }
}
