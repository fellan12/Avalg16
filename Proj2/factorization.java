import java.lang.*;
import java.math.*;
import java.util.*;
import java.io.*;

class factorization{
	static BigInteger TWO = new BigInteger("2");
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger ZERO = BigInteger.ZERO;
    static Random random = new Random();
	static BigInteger origin;
    
    private static BigInteger rho(BigInteger N) {
        BigInteger x1 = TWO, x2 = TWO, divisor= ONE;        
        BigInteger c  = new BigInteger("-1");           // In the original algorithm, g(x)=(x^2 - 1) mod(n)
                                                        // Nowadays it's more common to use g(x)=(x^2 + 1) mod(n)

        if (N.mod(TWO).equals(ZERO)){
            return TWO;
        }

        while((divisor.equals(ONE))){
            x1  = f(x1,N,c);
            x2 = f(f(x2,N,c),N,c);
            divisor = x1.subtract(x2).gcd(N);
        }

        return divisor;
    }

    private static BigInteger f(BigInteger X, BigInteger N, BigInteger C){
        BigInteger x = X.multiply(X).mod(N);
        x = x.add(C).mod(N);
        return x;
    }

    private static void factor(BigInteger N){	
        if (N.equals(BigInteger.ONE)){
            return;
        }

        if (N.isProbablePrime(10))      //1 - 1/2^certainty that N is a prime
        {
        	System.out.println(N);
        	return; 
        }
        BigInteger divisor = rho(N);
        if(divisor.equals(BigInteger.ONE)){
            return;
        }else{
            factor(divisor);
            factor(N.divide(divisor));
        }
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        while(scan.hasNext()){
        	try{
	        	BigInteger N = scan.nextBigInteger();
	        	origin = N;
	        	factor(N);
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