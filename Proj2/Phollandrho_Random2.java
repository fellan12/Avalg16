import java.lang.*;
import java.math.*;
import java.util.*;
import java.io.*;

class Phollandrho_Random2{
    static BigInteger SEVEN = new BigInteger("7");
    static BigInteger FIVE = new BigInteger("5");
	static BigInteger THREE = new BigInteger("3");
    static BigInteger TWO = new BigInteger("2");
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger ZERO = BigInteger.ZERO;
    static Random random = new Random();
    static ArrayList<BigInteger> primes = new ArrayList<BigInteger>(); 
    static boolean cant_prime = false;
    
    private static BigInteger rho(BigInteger N, Deadline deadline) {
        BigInteger x1 = TWO;
        BigInteger x2 = TWO;
        while(x1.equals(x2)){                               //x1 and x2 cant be the same
            x1 = new BigInteger(N.bitLength(),random);
            x2 = new BigInteger(N.bitLength(),random);
        }
        BigInteger divisor= ONE;
        BigInteger c  = ONE;                                // In the original algorithm, g(x)=(x^2 - 1) mod(n)
                                                            // Nowadays it's more common to use g(x)=(x^2 + 1) mod(n)

        if (N.mod(TWO).equals(ZERO)){                       //Get out the smaller primes
            return TWO;
        }

        if(N.mod(THREE).equals(ZERO)){                      //Get out the smaller primes
            return THREE;
        }

        if(N.mod(FIVE).equals(ZERO)){                       //Get out the smaller primes
            return FIVE;
        }

        if(N.mod(SEVEN).equals(ZERO)) {
            return SEVEN;
        }

        while((divisor.equals(ONE))){
            if(deadline.timeUntil() > 1.28*Math.pow(10,9)){    //1.28 sec remaining
                x1  = f(x1,N,c);
                x2 = f(f(x2,N,c),N,c);
                divisor = x1.subtract(x2).gcd(N);
            }else{
                cant_prime = true;
                return null;
            }
                
        }
        return divisor;
    }
    

    private static BigInteger f(BigInteger X, BigInteger N, BigInteger C){
        BigInteger x = X.multiply(X).mod(N);
        x = x.add(C).mod(N);
        return x;
    }

    private static void factor(BigInteger N, Deadline deadline){	
        if (N.equals(BigInteger.ONE)){
            return;
        }

        if (N.isProbablePrime(1))      //1 - 1/2^certainty that N is a prime
        {
            primes.add(N);
        	return; 
        }
        BigInteger divisor = rho(N, deadline);
        if(divisor == null){
            System.out.println("fail");
            return;
        }else if(divisor.equals(BigInteger.ONE)){
            return;
        }else{
            factor(divisor, deadline);
            factor(N.divide(divisor), deadline);
        }
    }

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);


        while(scan.hasNext()){
            Deadline deadline = new Deadline(15000000000L);     //15sec deadline
            try{
                BigInteger N = scan.nextBigInteger();
                factor(N, deadline);
               // control();
                if(!cant_prime){
                    for(BigInteger i : primes){
                        System.out.println(i);
                    }
                }

                if(scan.hasNext()){
                    System.out.println("");
                    cant_prime = false;
                    primes.clear();
                }
            }catch(InputMismatchException e){
                e.getStackTrace();
                System.err.println("Input - Wrong Format");
                break;
            }
        }        
    }

}