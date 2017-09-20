import java.io.*;
import java.nio.file.Path;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.nio.file.Paths;
import java.security.MessageDigest;


public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

		//Make sure there are two arguments given (signing or verifying + filename)
		if (args.length != 2) {
			System.err.println("Please provide 2 arguments: (sign/verify) , (filename)");
			System.exit(1);
		}

		//Create a messageDigest Object and use it to read the file and make a hash
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		//Get the filename from the arguments and load it into a byte array, then convert it to a big int
		String file = args[1];
		Path path = Paths.get(file);
		byte[] fileByteArray = Files.readAllBytes(path);
		md.update(fileByteArray);
		byte[] byteData = md.digest();
		BigInteger digest = new BigInteger(1, byteData);

		//Create File objects for scanner for private and public keys
		File public_file = new File("e_n.txt");
		File private_file = new File("d_n.txt");


		//Create scanner objects from File objects
		Scanner sc_public = new Scanner(public_file);
		Scanner sc_private = new Scanner(private_file);

		//Load e, d, and n from key files
		BigInteger e = sc_public.nextBigInteger();
		BigInteger d = sc_private.nextBigInteger();
		BigInteger n = sc_private.nextBigInteger();

		//Close the Scanner objects
		sc_public.close();
		sc_private.close();



		//If the user specifies to sign the file
		if(args[0].equals("sign")){
			//create the .signed file and prints stuff out
			BigInteger decrypt = digest.modPow(d, n);
			FileOutputStream fileOutputStream = new FileOutputStream(file + ".signed");
			ObjectOutputStream signedOutput = new ObjectOutputStream(fileOutputStream);
			signedOutput.writeObject(decrypt);
			signedOutput.writeObject(digest);
		}

		//If the user specifies to verify the file
		else if(args[0].equals("verify")){
			try{
				//Read in file with .signed extension
				FileInputStream rsaFile = new FileInputStream(file);
				ObjectInputStream rsaObjectStream = new ObjectInputStream(rsaFile);

				//Use public key to verify whether or not the file is signed
				BigInteger decrypted = (BigInteger)rsaObjectStream.readObject();
				BigInteger signed = (BigInteger)rsaObjectStream.readObject();
				BigInteger encrypted = decrypted.modPow(e, n);

				//If the file (when decrypted using the public key) is the same as the original file, the 
				if(encrypted.compareTo(signed) == 0){
					System.out.println("The document is authentic!  The signature is valid");
				}
				else{
					System.out.println("The document has been modified!  Signature is invalid.");
				}
			} catch(Exception exc){
				System.out.println("Please provide a valid filepath to read from.");
			}
		}
		else{
			System.out.println("That isn't a valid input");
		}
	}
}