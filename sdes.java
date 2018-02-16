//Simplified DES encryption and Decryption 
//Nicholas Cecala, 2018

import java.util.*;
public class sdes
{
    public static int finalEkey[] = new int[8];
    public static void main(){
        System.out.println("ENCRYPTION");
        System.out.println("****************************************");
        System.out.println("");
        int[] key = {1,1,1,0,0,0,1,1,1};
        int[] pText = {1,0,0,0,1,0,1,1,0,1,0,1};
        int[] left = new int[6];
        int[] right = new int[6];
        int[] cText = encrypt(pText, key, left, right);
        
        System.out.println("DECRYPTION");
        System.out.println("****************************************");
        System.out.println("");
        decrypt(cText, finalEkey, left, right);

    }

    static int[] encrypt(int pText[], int key[], int left[], int right[]){
        int rXORKey[] = new int[8];
        int expandedR[] = new int[8];
        final int[][] S1 = { {5,2,1,6,3,4,7,0} , {1,4,6,2,0,7,4,3}} ;
        final int[][] S2 = { {4,0,6,5,7,1,3,2},  {5,3,0,7,6,2,1,4}} ;
        int S1col, S2col;
        int S1row, S2row;
        int S1out;
        int S2out;
        int s1Result[] = new int[3];
        int s2Result[] = new int[3];
        int concat[] = new int[6];
        int[] t = new int[8];
        int finalE[] = new int[12];
        //split into left and right....create array to expand
        for (int i = 0; i < 6; i++)
        {
            left[i] = pText[i];
            right[i] = pText[i + 6];
            expandedR[i] = pText[i + 6];
        }

        //4 rounds
        for (int x = 1; x <= 4; x++)
        {
            
            System.out.println("Round # " + x + ":");
            System.out.print("Key: ");
            
            for (int i = 0; i < 8; i++)
            {
                System.out.print(key[i]);
            }
            
            System.out.println(" ");
            
            //expand R
            int temp = right[2];
            expandedR[0] = right[0];
            expandedR[1] = right[1];
            expandedR[2] = right[3];
            expandedR[3] = temp;
            expandedR[4] = right[3];
            expandedR[5] = right[2];
            expandedR[6] = right[4];
            expandedR[7] = right[5];
            
            //display the E(Ri)
            System.out.print("Expansion(R): ");
            for (int i = 0; i < 8; i++)
            {
                System.out.print(expandedR[i]);
            }
            
            System.out.println("");
            
            //perform the xor on R
            System.out.print("Expansion(R) XOR Key: ");
            for (int i = 0; i < 8; i++)
            {
                rXORKey[i] = expandedR[i] ^ key[i];
                System.out.print(rXORKey[i]);
            }
            
            //perm. for s1 and s2
            System.out.println("");
            System.out.print("S1: ");
            S1col = BinaryOp.BinToDec(rXORKey[1], rXORKey[2], rXORKey[3]);
            S1row = rXORKey[0];
            S1out = S1[S1row][S1col];
            S2col = BinaryOp.BinToDec(rXORKey[5], rXORKey[6], rXORKey[7]);
            S2row = rXORKey[4];
            S2out = S2[S2row][S2col];
            
            if (S1out==0){
                s1Result[0] = 0;
                s1Result[1] = 0;
                s1Result[2] = 0;
            }
            else if(S1out !=0){
                int index =2;
                for (int i = 0; i < 3; i ++){
                    {
                        s1Result[index--] = S1out%2;
                        S1out = S1out / 2;
                    }
                }
            }
            
            //display s1
            for (int i = 0; i < 3; i++)
            {
                System.out.print(s1Result[i]);
            }
            
            System.out.println("");

            if (S2out==0){
                s2Result[0] = 0;
                s2Result[1] = 0;
                s2Result[2] = 0;
            }
            else if(S2out !=0){
                int index =2;
                for (int i = 0; i < 3; i ++){
                    {
                        s2Result[index--] = S2out%2;
                        S2out = S2out / 2;
                    }
                }
            }

            //display s2
            System.out.print("S2: ");
            for (int i = 0; i < 3; i++)
            {
                System.out.print(s2Result[i]);
            }
            
            System.out.println("");
            
            //join s1 and s2
            for (int i = 0; i < 3; i++)
            {
                concat[i] = s1Result[i];
                concat[i+3] = s2Result[i];
            }

            //determine Ri and Li 
            for (int i = 0; i < 6; i++)
            {
                t[i] = right[i];
                right[i] = concat[i] ^ left[i];
                left[i] = t[i];
            }
            
            //print the (L R) for each round
            System.out.print("Encrypted (L R): ");
            for (int i = 0; i < 6; i++)
            {
                System.out.print(left[i]);
            }
            
            System.out.print(" ");
            
            for (int i = 0; i < 6; i++)
            {
                System.out.print(right[i]);
            }

            System.out.println("");
            
            //update key
            if (x <= 3)
            {
                t[0] = key[0];
                for (int i = 0; i <= 7; i++)
                {
                    key[i] = key[(i + 1) % 9];
                }
                key[8] = t[0];
            }
            
            System.out.println("");
            System.out.println("");
            
            //create an array containing the cipher text to be used in decryption
            for (int i = 0; i < 6; i++)
            {
                finalE[i] = right[i];
                finalE[i+6] = left[i];
            }
            
            //array holding the final key to be used for decryption
            for (int i = 0; i < 8; i++)
            {
               finalEkey[i] = key[i];
            }
            
        }
        return finalE;
    }

    static void decrypt(int cText[], int key[], int left[], int right[]){
        for(int x = 3; x >= 0; x--){
            
            //set left and right
            for(int i = 0; i < 6; i ++){
                left[i] = cText[i];
                right[i] = cText[i+6];
            }

            System.out.println("Round # " + x + ":");
            System.out.print("Key: ");
            
            for (int i = 0; i < 8; i++)
            {
                System.out.print(key[i]);
            }
            
            System.out.println(" ");
            
            //expand right
            int expandedR[] = new int[8];
            int temp = right[2];
            expandedR[0] = right[0];
            expandedR[1] = right[1];
            expandedR[2] = right[3];
            expandedR[3] = temp;
            expandedR[4] = right[3];
            expandedR[5] = right[2];
            expandedR[6] = right[4];
            expandedR[7] = right[5];
            
            //display E(Ri)
            System.out.print("Expansion(R): ");
            for (int i = 0; i < 8; i++)
            {
                System.out.print(expandedR[i]);
            }
            
            System.out.println("");
            
            //XOR E(R) with Key
            int rXORk[] = new int[8];
            int curKey[] = new int[8];
            
            //display E(R) xor with key
            System.out.print("Expansion(R) XOR Key: ");
            for(int i = 0; i < 8; i ++){
                rXORk[i] = expandedR[i] ^ key[i];
                curKey[i] = key[i];
                System.out.print(rXORk[i]);
            }
            
            System.out.println("");
            
            //use first 4 bits for s1 and last 4 bits for s2 get S1 and S2
            final int[][] S1 = { {5,2,1,6,3,4,7,0} , {1,4,6,2,0,7,4,3}} ;
            final int[][] S2 = { {4,0,6,5,7,1,3,2},  {5,3,0,7,6,2,1,4}} ;
            int S1col, S2col;
            int S1row, S2row;
            int S1out;
            int S2out;
            int s1Result[] = new int[3];
            int s2Result[] = new int[3];
            S1col = BinaryOp.BinToDec(rXORk[1], rXORk[2], rXORk[3]);
            S1row = rXORk[0];
            S1out = S1[S1row][S1col];
            S2col = BinaryOp.BinToDec(rXORk[5], rXORk[6], rXORk[7]);
            S2row = rXORk[4];
            S2out = S2[S2row][S2col];
            if (S1out==0){
                s1Result[0] = 0;
                s1Result[1] = 0;
                s1Result[2] = 0;
            }
            else if(S1out !=0){
                int index =2;
                for (int i = 0; i < 3; i ++){
                    {
                        s1Result[index--] = S1out%2;
                        S1out = S1out / 2;
                    }
                }
            }
            
            //display s1
            System.out.print("S1: ");
            for (int i = 0; i < 3; i++)
            {
                System.out.print(s1Result[i]);
            }
            System.out.println("");
            if (S2out==0){
                s2Result[0] = 0;
                s2Result[1] = 0;
                s2Result[2] = 0;
            }
            else if(S2out !=0){
                int index =2;
                for (int i = 0; i < 3; i ++){
                    {
                        s2Result[index--] = S2out%2;
                        S2out = S2out / 2;
                    }
                }
            }
            
            //display s2
            System.out.print("S2: ");
            for (int i = 0; i < 3; i++)
            {
                System.out.print(s2Result[i]);
            }
            System.out.println("");
            
            //concatenate s1 and s2
            int concat[] = new int[6];
            for (int i = 0; i < 3; i++)
            {
                concat[i] = s1Result[i];
                concat[i+3] = s2Result[i];
            }
            
            //determine Ri and Li
            int lPrev[] = new int[6];
            int R_L[] = new int[12];
            for (int i = 0; i < 6; i ++){
                lPrev[i] = left[i]; 
                left[i] = right[i];
                right[i] = lPrev[i] ^ concat[i];
                R_L[i] = right[i];
                R_L[i+6] = left[i];
            }

            //update key for next round
            int holder = curKey[7];
            for(int i = 0; i <= 6; i++){
                key[i+1] = curKey[i];
            }
            key[0] = holder;
            
            //update ciphertext for next round
            for (int i = 0; i < 6; i ++){
                cText[i] = left[i];
                cText[i+6] = right[i];
            }
            
            //print the (L R) for each round
            System.out.print("Decrypted (L R): ");
            for (int i = 0; i < 6; i++)
            {
                System.out.print(cText[i+6]);
            }
            
            System.out.print(" ");
            for (int i = 0; i < 6; i++)
            {
                System.out.print(cText[i]);
            }
            
            System.out.println("");
            System.out.println("");
            
        }
    }
   
}

