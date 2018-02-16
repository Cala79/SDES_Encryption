
public class AES_Preliminary {
    /**
     * S-BOX table
     */
    public static final int[][] sbox = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76}, 
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0}, 
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15}, 
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75}, 
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84}, 
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf}, 
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8}, 
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2}, 
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73}, 
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb}, 
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79}, 
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08}, 
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a}, 
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e}, 
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf}, 
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};

    /**
     * RCon array
     */
    public static final int[] rcon = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c};

    public static void AES(int []key)
    {
        int [][] w = null;
        int [][] wc = null;
        int [][] printInfo = new int[4][4];

        printInitialFourSquare(key);

        w=split(key,w);

        printFirstFour(w);

        for(int i = 0; i < 10; i++)
        {
            wc=new int[4][4];
            wc[0]=XOR(g(w[3],(i+1)%12,printInfo),w[0]);
            wc[1]=XOR(wc[0],w[1]);
            wc[2]=XOR(wc[1],w[2]);
            wc[3]=XOR(wc[2],w[3]);

            printInfo(w[3],printInfo,(i*4+3));
            
            w=wc;
        }
    }

    public static int [] g(int [] a, int num, int[][] printInfo)
    {
        int tmp;
        int [] r = {0,0,0,0};
        int [] b = new int[a.length];
        String s1, s2,s3= "";

        for(int i = 0; i < a.length; i++)
        {
            b[i]=a[i];
        }

        tmp = b[0];
        for(int i = 0; i < 3; i++)
        {
            b[i]=b[(i+1)];
        }
        b[3]=tmp;
        
        for(int i = 0; i < b.length; i++)
        {
            printInfo[0][i]=b[i];
        }

        for(int i = 0; i < 4; i++)
        {
            b[i]=sbox[b[i] / 16][b[i] % 16];
        }
        
        for(int i = 0; i < b.length; i++)
        {
            printInfo[1][i]=b[i];
        }
        
        r[0]=rcon[num];
        
        for(int i = 0; i < b.length; i++)
        {
            printInfo[2][i]=r[i];
        }
        
        b=XOR(b,r);
        
        for(int i = 0; i < b.length; i++)
        {
            printInfo[3][i]=b[i];
        }

        return b;
    }

    public static int[] XOR(int []a, int []c)
    {
        String s1="", s2= "" ,s3 = "",t1,t2;
        int [] b = new int[c.length];

        for(int i = 0; i < c.length; i++)
        {
            b[i]=c[i];
        }

        for(int i = 0; i < 4; i++)
        {
            t1=Integer.toBinaryString(a[i]);
            t2=Integer.toBinaryString(b[i]);

            while(t1.length()<8)
            {
                t1 = "0"+t1;
            }
            while(t2.length()<8)
            {
                t2 = "0"+t2;
            }
            s1=s1+t1;
            s2=s2+t2;
        }

        for(int i = 0; i < s1.length(); i++)
        {
            if(s1.charAt(i)==s2.charAt(i))
            {
                s3 = s3 + "0";
            }
            else
            {
                s3 = s3 + "1";
            }
        }

        b[0]=Integer.parseInt(s3.substring(0,8),2);
        b[1]=Integer.parseInt(s3.substring(8,16),2);
        b[2]=Integer.parseInt(s3.substring(16,24),2);
        b[3]=Integer.parseInt(s3.substring(24,32),2);

        return b;
    }

    public static void printFirstFour(int[][]w)
    {
        System.out.printf("Initial Key Matrix (hex):\n");
        for(int i = 0; i < 4; i++)
        {
            System.out.printf("w[%d] = ",i);
            for(int j = 0; j < 4; j++)
            {
                System.out.printf("%02x " , w[i][j]);
            }

            System.out.println();
        }

    }

    public static void printInfo(int[]w, int [][] wc,int num)
    {
        System.out.printf("Word(%d) and g(w) (hex):\n",num);
        for(int i = 0; i < 4; i++)
        {
            System.out.printf("%02x " , w[i]);
        }

        System.out.println();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                System.out.printf("%02x " , wc[i][j]);
            }

            System.out.println();
        }

    }

    public static void printInitialFourSquare(int [] key)
    {
        System.out.printf("Initial Key Matrix (hex):\n");
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                System.out.printf("%02x " , key[(i)+(j*4)]);
            }

            System.out.println();
        }
    }

    public static int[][] split(int []key, int [][]w)
    {
        w = new int[4][4];

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                w[j][i]=key[i+(j*4)];
            }
        }

        return w;
    }

    public static int [] keyStringConvertor(String sKey)
    {
        int [] key = new int [sKey.length()/2];

        for(int i = 0; i < key.length; i++)
        {
            key[i]=Integer.parseInt(sKey.substring(i*2, i*2+2), 16);
        }

        return key;
    }

    /**
     */
    public static void main(String args[])
    {

        String key = "0f1571c947d9e8590cb7add6af7f6798";

        /*
        // extract the value "0f" from the key and represent as hex
        int value = Integer.parseInt(key.substring(0, 2), 16);
        System.out.printf("Key representation: key1(10)= %d \tkey1(16)= %02x\n", value, value);        

        // check the s_box
        int s_box_mapping = sbox[value / 16][value % 16];
        System.out.printf("S-box lookup: sbox[%2x][%2x] = %2x\n", value/16, value%16, s_box_mapping);

        // check rcon
        System.out.printf("Rcon lookup: rc[%d] = %2x\n", 9, rcon[9]);
         */

        AES(keyStringConvertor(key));

        /*
        int [] w = keyStringConvertor("b1ae7ec0");
        int [] c = keyStringConvertor("c0afdf39");
        int [] a;
        a=XOR(g(w,4),c);
        for(int p = 0; p < a.length; p++)
        {
        System.out.print(a[p]+ " ");
        }
        for(int p = 0; p < w.length; p++)
        {
        System.out.print(w[p]+ " ");
        }
         */

    }

}