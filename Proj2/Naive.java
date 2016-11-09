import java.lang.*;
import java.math.*;
import java.util.*;
import java.io.*;

class Naive{
    static BigInteger TWO = new BigInteger("2");
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger ZERO = BigInteger.ZERO;
    static ArrayList<BigInteger> primes = new ArrayList<BigInteger>(); 
    static boolean cant_prime = false;


	public static void factor(BigInteger a, Deadline deadline){
	    for(BigInteger divisor = TWO; a.compareTo(ONE) > 0; divisor = divisor.add(ONE)){
	    	if(deadline.timeUntil() > 1.28*Math.pow(10,9)){    //1.28 sec remaining
				while(a.mod(divisor).equals(ZERO)){
					 	primes.add(divisor);
					 	a = a.divide(divisor);
				}
			}else{
				cant_prime = true;
				return;
			}
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
                }else{
                	System.out.println("fail");

                }

                if(scan.hasNext()){
                    System.out.println("");
                }
                cant_prime = false;
                primes.clear();

            }catch(InputMismatchException e){
                e.getStackTrace();
                System.err.println("Input - Wrong Format");
                break;
            }
        }        
    }

}