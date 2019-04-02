import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.text.StyledEditorKit;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A class that defines a term inside a polynomial.
 * DO NOT MODIFY!!
 */
class Term {
    public int coef;
    public int exp;

    public Term(int coef, int exp) {
        this.coef = coef;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return coef + "x^" + exp;
    }
}

/**
 * A Polynomial ADT
 */
public class Poly {

    private Term[] terms;   // array of terms, not sorted
    private int next = 0;   // denotes next available slot & (term count)

    /**
     * Creates a new polynomial which can hold up to `termCount` `Term`s.
     * @param termCount
     */
    public Poly(int termCount) {
        // you code goes here
        terms = new Term[termCount];
    }

    /**
     * Creates a new polynomial with given terms as parameters.
     * @param termCount number of terms
     * @param terms array of terms to be added.
     */
    public Poly(int termCount, Term... terms) {
        this(termCount);

        if (termCount < terms.length)
            throw new IllegalArgumentException("termCount < terms.length");

        for (int i = 0; i < terms.length; i++) {
            addTerm(terms[i].coef, terms[i].exp);
        }
    }

    /**
     * Returns the degree of this polynomial.
     * @return degree of polynomial
     */
    public int degree() {
        this.toString();
        return terms[0].exp;
    }

    /**
     * Returns the number of terms
     * @return the number of terms
     */
    public int getTermCount() {
        return next;
    }

    /**
     * Insert a new term into a given polynomial.
     * @param coef coefficient
     * @param exponent exponent
     */
    public void addTerm(int coef, int exponent) {
        // you code goes here
        if(coef != 0){
            terms[next++] = new Term(coef, exponent);
        }
    }

    /**
     * Adds the target polynomial object with the one given as a parameter.
     * As a result, the returned polynomial object will eventually represent
     * the sum of two polynomials (C = A.add(B). Note that A should not be
     * modified as a result of this operation.
     * @param other a polynomial
     * @return a new polynomial (`other` + `this`)
     */
    public Poly add(Poly other) {
        // you code goes here
        if(other.next==0){ return this; }
        if(this.next==0){ return other; }
        int first = this.next;
        int second = other.next;
        int j = first+second;
        int k = j;
        Poly poly = new Poly(first+second);
        for(int i=0; i<first; i++){
            poly.addTerm(this.terms[i].coef,this.terms[i].exp);

        }
        for(int w=0; w<second; w++){
            poly.addTerm(other.terms[w].coef,other.terms[w].exp);
        }
        poly.toString();
        Poly polyanswer = new Poly(poly.next);
        while(j>0){
            if(poly.terms[k-j].exp==poly.terms[k-j+1].exp){
                polyanswer.addTerm(poly.terms[k-j].coef+poly.terms[k-j+1].coef,poly.terms[k-j].exp);
                j -= 2;
            }
            else {
                polyanswer.addTerm(poly.terms[k-j].coef, poly.terms[k-j].exp);
                j--;
            }
        }
        polyanswer.toString();
        return polyanswer;
    }

    /**
     * Multiply the target polynomial object with the one given as a parameter.
     * As a result, the returned polynomial object will eventually represent
     * the product of two polynomials (C = A.mutiply(B). Note that A should not be
     * modified as a result of this operation.
     * @param other a polynomial
     * @return a new polynomial (`other` * `this`)
     */
    public Poly mult(Poly other) {
        // you code goes here
        if(other.next==0){ return this; }
        if(this.next==0){ return other; }
        int k = this.next * other.next - 1;
        int l = k;
        boolean chk = false;
        this.toString(); other.toString();
        int count = 0;
        int recount = 0;
        Poly poly = new Poly(this.next * other.next);
        for(int i=0; i<this.next; i++){
            for(int j=0; j<other.next; j++){
                poly.addTerm(this.terms[i].coef*other.terms[j].coef,this.terms[i].exp+other.terms[j].exp);
            }
        }
        poly.toString();
        Poly polyanswer = new Poly(poly.next);
        while(k>0){
            count = 0;
            recount = 0;
            if(poly.terms[l-k].exp == poly.terms[l-k+1].exp){
                chk = true;
            }
            while(chk){
                count += poly.terms[l-k].coef;
                k--;
                recount++;
                if(poly.terms[l-k].exp == poly.terms[l-k+1].exp){
                    chk = true;
                }
                else{
                    chk = false;
                }
            }
            if(count==0){
                polyanswer.addTerm(poly.terms[l-k].coef,poly.terms[l-k].exp);
                k--;
            }
            else{
                if(recount!=0){
                    polyanswer.addTerm(count+poly.terms[l-k].coef,poly.terms[l-k].exp);
                    k--;
                }
                else{
                    polyanswer.addTerm(count,poly.terms[l-k].exp);
                }
            }
        }
        polyanswer.addTerm(poly.terms[poly.terms.length-1].coef,poly.terms[poly.terms.length-1].exp);
        polyanswer.toString();
        return polyanswer;
    }

    @Override
    public String toString() {
        // Sample code ... you can freely modify this code if necessary
        Arrays.sort(terms, 0, next, (a, b) -> b.exp - a.exp);
        return Arrays.stream(terms)
                     .filter(i -> i != null)
                     .map(i -> i.toString())
                     .collect(Collectors.joining(" + "));
    }

    public static void main(String... args) {
        Poly poly1 = new Poly(4);
        poly1.addTerm(1,3);
        poly1.addTerm(2,2);
        poly1.addTerm(3,1);
        poly1.addTerm(4,0);
        System.out.println(poly1);

        Poly poly2 = new Poly(4);
        poly2.addTerm(2,1);
        poly2.addTerm(1,0);
        poly2.addTerm(3,2);
        poly2.addTerm(4,3);
        System.out.println(poly2);

        Poly poly3 = poly1.add(poly2);
        System.out.println(poly3);

        Poly poly4 = poly1.mult(poly2);
        System.out.println(poly4);

        Poly unitPoly1 = new Poly(0);
        Poly unitPoly2 = new Poly(0);

        Poly unit1 = unitPoly1.add(unitPoly2);
        Poly unit2 = unitPoly1.mult(unitPoly2);
        System.out.println(unit1);
        System.out.println(unit2);

        Poly unit3 = poly1.mult(unitPoly1);
        Poly unit4 = poly1.add(unitPoly1);
        System.out.println(unit3);
        System.out.println(unit4);

    }
}
