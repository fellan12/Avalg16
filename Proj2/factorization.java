import java.lang.*;
import java.math.*;
import java.util.*;
import java.io.*;

class factorization{
	static BigInteger TWO = new BigInteger("2");
	static BigInteger origin;
    
    /** get divisor **/
    private static BigInteger rho(BigInteger N) {
        BigInteger x1 = TWO, x2 = TWO, divisor=BigInteger.ZERO;        
        if (N.mod(TWO) == BigInteger.ZERO) 
            return TWO;
        while (divisor == BigInteger.ONE) 
        {	
            x1 = f(x1).mod(N);
            x2 = f(f(x2)).mod(N);
            divisor = gcd(x1.subtract(x2).abs(), N);
        };
        /** return divisor **/
        return divisor;
    }

    /** function X * X + C, change value of C as required **/
     private static BigInteger f(BigInteger X){
        return X.multiply(X).add(BigInteger.ONE);
    }
    
    /** GCD of two numbers **/
    private static  BigInteger gcd(BigInteger p, BigInteger q){
        if (p.mod(q) == BigInteger.ZERO)
            return q;
        return gcd(q, p.mod(q));
    }

    /** Check if num is prime **/
    private static boolean isPrime(BigInteger N){
        for (BigInteger i = TWO; i.intValue() <= Math.sqrt(N.intValue()); i = i.add(BigInteger.ONE))
            if (N.mod(i) == BigInteger.ZERO)
                return false;
        return true;
    }

    /** get all factors **/
    private static void factor(BigInteger N){	
        if (N == BigInteger.ONE)
            return;
        if (isPrime(N)) 
        {
        	if(N.equals(origin)){
        		System.out.println("fail");
        		return;
        	}else{
            	System.out.println(N);
            	return; 
        	}
        }
        BigInteger divisor = rho(N);
        factor(divisor);
        factor(N.divide(divisor));
    }

    /** Main function **/
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()){
        	try{
	        	BigInteger N = scan.nextBigInteger();
	        	origin = N;
	        	factor (N);
	        	if(scan.hasNext()){
	        		System.out.println();
	        	}
        	}catch(InputMismatchException e){
        		e.getStackTrace();
        		System.err.println("Input - Wrong Format");
        		break;
        	}
        }        
    }
}