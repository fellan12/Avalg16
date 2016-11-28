import java.lang.*;
import java.math.*;
import java.util.*;
import java.io.*;

class Trial_dividion{
    static BigInteger TWO = new BigInteger("2");
    static BigInteger ONE = BigInteger.ONE;
    static BigInteger ZERO = BigInteger.ZERO;
    static ArrayList<BigInteger> primes = new ArrayList<BigInteger>(); 
    static boolean cant_prime = false;
    static BigInteger[] smallerPrimes = new BigInteger[]{
      TWO, new BigInteger("3"), new BigInteger("5"), new BigInteger("7"), 
      new BigInteger("11"), new BigInteger("13"), new BigInteger("17"), 
      new BigInteger("19"), new BigInteger("23"), new BigInteger("29"), 
      new BigInteger("31"), new BigInteger("37"), new BigInteger("41"), 
      new BigInteger("43"), new BigInteger("47"), new BigInteger("53"), 
      new BigInteger("59"), new BigInteger("61"), new BigInteger("67"), 
      new BigInteger("71"), new BigInteger("73"), new BigInteger("79"), 
      new BigInteger("83"), new BigInteger("89"), new BigInteger("97"), 
      new BigInteger("101"), new BigInteger("103"), new BigInteger("107"), 
      new BigInteger("109"), new BigInteger("113"), new BigInteger("127"), 
      new BigInteger("131"), new BigInteger("137"), new BigInteger("139"), 
      new BigInteger("149"), new BigInteger("151"), new BigInteger("157"), 
      new BigInteger("163"), new BigInteger("167"), new BigInteger("173"), 
      new BigInteger("179"), new BigInteger("181"), new BigInteger("191"), 
      new BigInteger("193"), new BigInteger("197"), new BigInteger("199"), 
      new BigInteger("211"), new BigInteger("223"), new BigInteger("227"), 
      new BigInteger("229"), new BigInteger("233"), new BigInteger("239"), 
      new BigInteger("241"), new BigInteger("251"), new BigInteger("257"), 
      new BigInteger("263"), new BigInteger("269"), new BigInteger("271"), 
      new BigInteger("277"), new BigInteger("281"), new BigInteger("283"), 
      new BigInteger("293"), new BigInteger("307"), new BigInteger("311"), 
      new BigInteger("313"), new BigInteger("317"), new BigInteger("331"), 
      new BigInteger("337"), new BigInteger("347"), new BigInteger("349"), 
      new BigInteger("353"), new BigInteger("359"), new BigInteger("367"), 
      new BigInteger("373"), new BigInteger("379"), new BigInteger("383"), 
      new BigInteger("389"), new BigInteger("397"), new BigInteger("401"), 
      new BigInteger("409"), new BigInteger("419"), new BigInteger("421"), 
      new BigInteger("431"), new BigInteger("433"), new BigInteger("439"), 
      new BigInteger("443"), new BigInteger("449"), new BigInteger("457"), 
      new BigInteger("461"), new BigInteger("463"), new BigInteger("467"), 
      new BigInteger("479"), new BigInteger("487"), new BigInteger("491"), 
      new BigInteger("499"), new BigInteger("503"), new BigInteger("509"), 
      new BigInteger("521"), new BigInteger("523"), new BigInteger("541")
    };


  public static void factor(BigInteger a, Deadline deadline){
      if (a.isProbablePrime(1)){
        primes.add(a);
        return;
      }

      for (BigInteger num : smallerPrimes) {      //Smaller primes
          while(a.mod(num).equals(ZERO)){
            primes.add(num);
            a = a.divide(num);
          }
      }

      //7 to sqrt(n)
      for(BigInteger divisor = smallerPrimes[smallerPrimes.length-1]; a.compareTo(ONE) > 0; divisor = divisor.add(ONE)){
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
                factor(scan.nextBigInteger(), deadline);
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
