

# Linear Algebra Library #

The code contained within is an implementation of standard Linear Algebra functions / operations.

*This library was created independetly from other libraries (in the sense that a student astutely studied linear algebra and created this library on their own, without the assistance of outside help).*

As of 1/22/24, the following are fully implemented and use the most efficient methods for obtaining each : 

a) Reduced Row Echelon Form (RREF)

b) Inverse

c) Determinant


The following are to be implemented in future versions :

a) Eigenvectors / Eigenvalues

b) Diagonalization

c) Steps to Obtain Solution

d) Matrix Multiplication

e) Matrix Addition / Subtraction


## Reduced Row Echelon Form (RREF) ##

The RREF of a given matrix is computed using the standard cubic runtime algorithm (O(n^3)), wherein each diagonal is attempted to form a pivot, and entries below and above that pivot are reduced to zero.

If an attempted diagonal entry is non-zero, the algorithm tries to find a non-zero beneath it. If one is found, the rows are swapped, and the elements above/below are reduced to zero. If none is found, then the column either contains a free variable, or the entire column is zero. In either case, a pivot is not found, and the next pivot element will be at the current row (and thus, only the column is incremeneted in the algorithm).

For more infomration, we recommend either looking at the source code, or contacting us. 

## Determinant ##

The determinant is computed in the same process as computing the RREF of a matrix. Linear Algebra provides theorems for keeping track of changes made to the determinant with each row operation, and these changes are tracked in our RREF algorithim. Therefore, we can calculate a determinant in polynomial (O(n^3)) runtime, as opposed to the standard co-factor expansion method (which is used in *many* courses as the defacto method to compute a determinant -- this runs in factorial runtime!)

## Inverse ## 

The inverse of a given matrix is also computed at the same time as computing the RREF, similar to the determinant. This is because all one needs to do is apply these same elementary row operations to the Identity matrix to obtain an Inverse.


