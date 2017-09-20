import java.math.BigInteger;
import java.util.*;
import java.io.PrintWriter;



public class Main {


      //Get random number from seed
      private final static Random rand = new Random(System.currentTimeMillis());

      //Function that generates and returns a number between 1 and n to test for checkPrime
      private static BigInteger getFermatTestNumber(BigInteger n)
      {
          //Keep looping until this num is < n and > 1
          while (true)
          {
              final BigInteger num = new BigInteger (n.bitLength(), rand);

              //Make sure num is between 1 and n and return it if so
              if (BigInteger.ONE.compareTo(num) <= 0 && num.compareTo(n) < 0)
              {
                  return num;
              }
          }
      }


      //Actual Fermat Test that checks for (testCount) numbers
      public static boolean fermatTest(BigInteger n, int testCount)
      {

          //Base case if n is 1 then simply return false
          if (n.equals(BigInteger.ONE))
              return false;

          //Check testCount times for a^(n-1) mod n = 1
          for (int i = 0; i < testCount; i++)
          {
              //Getting your random numbers from 1 to n using getFermatTestNumber
              BigInteger a = getFermatTestNumber(n);

              //Making a = a^(n-1) mod n (should equal 1)
              a = a.modPow(n.subtract(BigInteger.ONE), n);

              //If a != 1 the number is not prime
              if (!a.equals(BigInteger.ONE))
                  return false;
          }

          //Otherwise return true
          return true;
      }


    public static void main(String[] args) {

        //Creating two random number generators with different seeds for p and q
        final Random rand1 = new Random(System.currentTimeMillis());
        final Random rand2 = new Random(System.currentTimeMillis()*10);
        final Random rand3 = new Random(System.currentTimeMillis()*20);



        //Create two 512-bit (155 digit) numbers p and q at random
        BigInteger p = new BigInteger(512, rand1);
        BigInteger q = new BigInteger(512, rand2);


        //Use FermatTest to check/reproduce p until prime (with 20 steps of certainty)
        while (fermatTest(p, 20) == false) {
            p = new BigInteger(512, rand1);
            //System.out.println(p);
        }

        //Use FermatTest to check/reproduce q until prime (with 20 steps of certainty)
        while (fermatTest(q, 20) == false) {
            q = new BigInteger(512, rand2);
            //System.out.println(q);
        }

        //Create n (p * q)
        BigInteger n = p.multiply(q);

        //Create phi(n) from (p-1) * (q-1)
        BigInteger p_1 = p.subtract(new BigInteger("1"));
        BigInteger q_1 = q.subtract(new BigInteger("1"));
        BigInteger phi = p_1.multiply(q_1);

        //Creating the random seed e as a 1024 bit number
        BigInteger e = new BigInteger(1024, rand3);


        while (true) {

            BigInteger GCD = phi.gcd(e);

            if (GCD.equals(BigInteger.ONE)) {
                break;
            }

            e = new BigInteger(1024, rand3);
        }

        //Create d from d * e == 1 mod phi
        BigInteger d = e.modInverse(phi);


        try{

            //Creates PrintWriter Objects for the 3 text files
            PrintWriter p_q = new PrintWriter( "p_q.txt" );
            PrintWriter e_n = new PrintWriter( "e_n.txt" );
            PrintWriter d_n = new PrintWriter( "d_n.txt" );

            //Outputs p and q on separate lines to p_q.txt
            p_q.println(p);
            p_q.println(q);

            //Outputs e and n on separate lines to e_n.txt
            e_n.println(e);
            e_n.println(n);

            //Outputs d and n on separate lines to d_n.txt
            d_n.println(d);
            d_n.println(n);

            //Closes txt files
            p_q.close();
            e_n.close();
            d_n.close();


        } catch (Exception ex) {
            System.out.println(ex.getClass());
        }

    }



}
