package model;

import java.util.Arrays;

public class Matrix {

    static final boolean TESTING = false;

    Double [][] mat;

    Double [][] rref;

    /**
     * The inverse of the given matrix. Exists if and only if mat is an nxn matrix with a non-zero determinant.
     */
    Double [][] inverse;

    long time;

    long start;
    long end;

    /**
     * This value represents the modification of the determinant. In particular, every row operation modifies this value dependent upon the row operation.
     *
     * a) If B is a matrix obtained by interchanging two rows of A, then det(B) = - det(A)
     * b) If B is a matrix obtained by multiplying each entry of some row of A by a scalar k, then det (B) = k * det(A)
     * c) If B is a matrix obtained by adding a multiple of some row of A to a different row, then det(B) = det(A)
     *
     * Thus, by following these rules, we can obtain the det(A) from the det(B).
     *
     * Suppose that the rref(A) = B. Then we can obtain det(A) by following the above rules and applying them (inversely) to the det(B).
     *
     * The det(B), where B is in rref, is easy to compute, as RREF guarantees that the matrix is in an upper triangular matrix, and thus the determinant is merely the multiplication along the diagonals.
     *
     * For more information, refer to Linear Algebra theorems, in particular, theorems surrounding determinants. The above are critical theormes for the development of linear algebra algorithms.
     */
    double k;

    double determinant;

    public Matrix(Double[][] arr) {
        if(TESTING) {
            System.out.println("Made a new matrix!");
        }
        this.mat = arr;
        print(this.mat);
        this.rref = new Double[arr.length][arr[0].length];
        this.inverse = new Double[arr.length][arr[0].length];
        init();
        this.start = System.currentTimeMillis();
        rref_();
        this.end = System.currentTimeMillis();
        print(this.rref);
        System.out.println("Determinant is " + this.determinant);
        System.out.println("The inverse is as follows");
        print(this.inverse);
        System.out.println("Computational time : " + (this.end - this.start) + " ms");
    }

    /**
     * This method performs the following initalizations :
     *
     * a) Initalizations the inverse matrix to be the identity matrix.
     *     -> The inverse is obtained by perform the inverse elementary row operations which transform a given matrix A to its rref B
     *     -> Therefore, we can obtain an inverse in the same process as obtaining a rref.
     *
     * b) Initalization of the rref matrix to be the given matrix.
     */
    private void init() {
        this.k = 1;
        for(int i = 0; i<rref.length; i++) {
            for(int j = 0; j<rref[i].length; j++) {
                rref[i][j] = mat[i][j];
                inverse[i][j] = 0.0;
            }
            inverse[i][i] = 1.0;
        }
    }

    /**
     * A method which calculates the Reduced Row Echelon Form of the private property /mat/ and populates /rref/ with that data.
     *
     * Note that many properties of matrices can be obtained through following the RREF.
     *
     * a) By tracking elementary row operations, one can deduce the changes done to the determinant in each step.
     *
     * b) By applying the exact same elementary row operations to an identity matrix, one will obtain the inverse (or the "closest" inverse if a true inverse does not exist).
     *
     * RREF runs in O(n^3) runtime. We can see that here -- the outer while loop is n^2 as it iterates over two dimensions. Meanwhile, inside the outer loop there is an O(n) task that iterates over every row of a particular column. Therefore, we have O(n^3) runtime, which is the best possible runtime for a RREF algorithm in classical terms (There indeed exists an O(n^log2(7)) runtime, which is better than O(n^log2(8)) == O(n^3) )
     *
     */
    private void rref_() {
        if(TESTING) {
            System.out.println("Calculating the RREF");
        }
        int i = 0;
        int j = 0;

        while(i < rref.length && j < rref[i].length) {
            //find a non-zero element in the current row
            int row = nonzero(this.rref, i, j);
            if(row != -1 && row != i) {
                swap_row(this.rref, row, i);
                swap_row(this.inverse, row, i); //apply same changes to identity matrix to obtain inverse
                this.k = -this.k; //keep track of determinant changes
            }
            if(rref[i][j] != 0) {
                //there is a pivot at rref[i][j]
                scale_row(this.rref, i, 1/rref[i][j]); //ensure that rref[i][j] is a 1 -- i.e., a pivot
                scale_row(this.inverse, i, 1/rref[i][j]); //apply same changes to identity matrix to obtain inverse
                this.k = this.k / rref[i][j]; //keep track of determinant changes

                //we need to "kill" / reduce all elements in the current column other than rref[i][j] to be zero.
                for(int r = 0; r < rref.length; r ++) {
                    if(r != i && rref[r][j] != 0) {
                        double scalar = -rref[r][j];
                        add_rows(this.rref, i,r, scalar);
                        add_rows(this.inverse, i, r, scalar); //apply same changes to identity matrix to obtain inverse
                    }
                }

                i++;
                j++;
            }
            else {
                //col j is all zero or contains a free variable; the next pivot will be in the next col in the current row
                j++;
            }
        }
        determinant();
    }

    /**
     * Finds the first row that is beneath the given row which contains a non-zero element in the given column.
     *
     * If no such row exists, this method returns -1.
     *
     * @param row
     * @param col
     * @return
     */
    private int nonzero(Double[][] mat, int row, int col) {
        while(row < mat.length) {
            if(mat[row][col] != 0) {
                return row;
            }
            row++;
        }
        return -1;
    }

    private void scale_row(Double[][] mat, int row, double scalar) {
        if(TESTING) {
            System.out.println("Scaling the following array by a factor of " + scalar);
            System.out.println(Arrays.asList(mat[row]));
        }
        for(int col = 0; col < mat[row].length; col ++ ) {
            mat[row][col] = scalar * mat[row][col];
        }
    }
    private void swap_row(Double[][] mat, int row1, int row2) {
        if(TESTING) {
            System.out.println("Swapping the following two rows");
            System.out.println(Arrays.asList(mat[row1]));
            System.out.println(Arrays.asList(mat[row2]));
        }

        for(int i = 0; i<mat[row1].length; i++) {
            Double temp = mat[row1][i];
            mat[row1][i] = mat[row2][i];
            mat[row2][i] = temp;
        }
    }

    /**
     * Adds each entry in row1, scaled by a factor of scalar, to each entry of row2
     *
     * In other words, after this operation, mat[row2][index] = mat[row1][index] * scalar + mat[row2][index]
     * @param row1
     * @param row2
     */
    private void add_rows(Double[][] mat, int row1, int row2, double scalar) {
        if(TESTING) {
            System.out.println("Adding the first row to the second row, scaled by a factor of " + scalar);
            System.out.println(Arrays.asList(mat[row1]));
            System.out.println(Arrays.asList(mat[row2]));
        }
        for(int col = 0; col < mat[row1].length; col ++ ) {
            mat[row2][col] = (mat[row1][col] * scalar) + mat[row2][col];
        }
    }

    private void print(Double[][] arr) {
        for(int i = 0; i<arr.length; i++) {
            for(int j = 0; j<arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     *
     * @return the Reduced Row Ecehlon Form of the inital matrix.
     */
    public Double [][] rref() {
        return this.rref;
    }

    /**
     * Calculates the determinant of this matrix.
     */
    private void determinant() {
        if(TESTING) {
            System.out.println("Calculating the determinant...");
            System.out.println("k is " + k);
        }
        double det = 1;
        for(int i = 0; i < rref.length; i++) {
            det *= rref[i][i];
        }
        det *= k;
        this.determinant = det;
    }

    /**
     * Returns the determinant of this matrix.
     * @return
     */
    public Double det() {
        return this.determinant;
    }

    public Double[][] inverse() {
        if(determinant != 0) {
            return inverse;
        }
        else {
            return null; //inverse does not exist, determinat is 0
        }
    }
}

