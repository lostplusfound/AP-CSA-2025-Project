import java.util.Arrays;

public class Matrix {
    private double[][] mat;

    public Matrix(int numRows, int numCols) {
        mat = new double[numRows][numCols];
    }

    public Matrix(double[][] mat) {
        this.mat = new double[mat.length][mat[0].length];
        for (int i = 0; i < mat.length; i++) {
            this.mat[i] = mat[i].clone();
        }
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

    public Matrix add(Matrix other) {
        if (other.numRows() != this.numRows() || other.numCols() != this.numCols())
            throw new IllegalArgumentException("Matrix addition cannot be performed on different sized matrices");
        Matrix resultant = new Matrix(this.numRows(), this.numCols());
        for (int row = 0; row < this.numRows(); row++) {
            for (int col = 0; col < this.numCols(); col++) {
                resultant.setElement(row, col, mat[row][col] + other.getElement(row, col));
            }
        }
        return resultant;
    }

    public Matrix subtract(Matrix other) {
        other = other.multiplyByScalar(-1.0);
        return this.add(other);
    }

    public Matrix multiplyByScalar(double scalar) {
        Matrix resultant = new Matrix(this.numRows(), this.numCols());
        for (int row = 0; row < this.numRows(); row++) {
            for (int col = 0; col < this.numCols(); col++) {
                resultant.setElement(row, col, scalar * mat[row][col]);
            }
        }
        return resultant;
    }

    public double findDet() {
        if (numRows() != numCols()) {
            throw new IllegalArgumentException("Determinant is only defined for square matrices.");
        }
        int n = numRows();

        if (n == 1) {
            return mat[0][0];
        }
        if (n == 2) {
            return (mat[0][0] * mat[1][1]) - (mat[0][1] * mat[1][0]);
        }

        double det = 0;
        for (int i = 0; i < n; i++) {
            Matrix subMatrix = subMatrix(i);
            det += mat[0][i] * Math.pow(-1, i) * subMatrix.findDet();
        }
        return det;
    }

    private Matrix subMatrix(int excludedCol) {
        int size = numRows();
        double[][] subMat = new double[size - 1][size - 1];
        int r = 0;
        for (int i = 1; i < size; i++) {
            int c = 0;
            for (int j = 0; j < size; j++) {
                if (j == excludedCol)
                    continue;
                subMat[r][c] = mat[i][j];
                c++;
            }
            r++;
        }
        return new Matrix(subMat);
    }

    public Matrix multiply(Matrix other) {
        Matrix failureMatrix = new Matrix(1000, 1000);
        if (this.numRows() == other.numCols() && this.numCols() == other.numRows()) {
            Matrix productMatrix = new Matrix(this.numRows(), this.numRows());
            //go through every row of this
            for (int r = 0; r < this.numRows(); r++) {
                //go through every column of other (nested)
                for (int c = 0; c < other.numCols(); c++) {
                   
                    double dotProduct = 0.0;
 
 
                    for(int i = 0; i < this.numCols(); i++){
                        dotProduct += (this.mat[r][i] * other.getElement(i, c));    
                    }                  
 
 
                    productMatrix.setElement(r, c, dotProduct);
 
 
                }
            }
            return productMatrix;
        }
        return failureMatrix;
 
 
    }

    public Matrix swapRows(int row1, int row2) {
        Matrix resultant = new Matrix(mat);
        if (row1 == row2)
            return resultant;
        for (int col = 0; col < numCols(); col++) {
            resultant.setElement(row1, col, mat[row2][col]);
            resultant.setElement(row2, col, mat[row1][col]);
        }
        return resultant;
    }

    public Matrix multiplyRow(int row, double scalar) {
        Matrix resultant = new Matrix(this.mat);
        for (int col = 0; col < resultant.numCols(); col++) {
            resultant.setElement(row, col, scalar * mat[row][col]);
        }
        return resultant;
    }

    public Matrix addMultipleOfRowToAnother(int row, int otherRow, double scalar) {
        Matrix resultant = new Matrix(this.mat);
        for (int col = 0; col < resultant.numCols(); col++) {
            resultant.setElement(otherRow, col, scalar * mat[row][col] + mat[otherRow][col]);
        }
        return resultant;
    }

    public double[] solve() {
        Matrix m = new Matrix(mat);
        for (int pivot = 0; pivot < this.numCols() - 2; pivot++) {
            int pivotRow = pivot;
            for (int i = pivot + 1; i < this.numRows(); i++) {
                if (Math.abs(m.getElement(i, pivot)) > Math.abs(m.getElement(pivotRow, pivot)))
                    pivotRow = i;
            }
            m = m.swapRows(pivot, pivotRow);
            for (int row = pivot + 1; row < this.numRows(); row++) {
                double factor = -m.getElement(row, pivot) / m.getElement(pivot, pivot);
                m = m.addMultipleOfRowToAnother(pivot, row, factor);
            }
        }
        if (Math.abs(m.getElement(m.numRows() - 1, m.numCols() - 2)) < 1e-14)
            return null;
        double factor = 1 / m.getElement(m.numRows() - 1, m.numCols() - 2);
        m = m.multiplyRow(m.numRows() - 1, factor);
        double[] result = new double[this.numRows()];
        result[result.length - 1] = m.getElement(m.numRows() - 1, m.numCols() - 1);
        for (int i = m.numRows() - 2; i >= 0; i--) {
            double sum = m.getElement(i, m.numCols() - 1);
            for (int j = m.numCols() - 2; j > i; j--) {
                sum += -m.getElement(i, j) * result[j];
            }
            result[i] = sum / m.getElement(i, i);
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix other = (Matrix) obj;
        if (!Arrays.deepEquals(mat, other.mat))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(mat);
    }
}