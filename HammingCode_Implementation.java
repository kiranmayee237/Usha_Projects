import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataCom
 {
    //Function for Calculating  Mean, Standard_Deviation to findout Threshold
    public static double CalculateThreshold(ArrayList<Double> samples)
    {
        double sum=0,mean=0,sd=0;
        double threshold=0;
        for (int i = 0; i < 10000; i++)
            {
                sum = sum + samples.get(i);
            }
            mean = sum / 10000;
            sum = 0;

            // Calculating Standard_deviation
            for (int i = 0; i < 10000; i++)
            {
                sum += Math.pow((samples.get(i) - mean), 2);
            }
            sd = Math.sqrt(sum / 9999);
            threshold = (mean * 4 )+ (sd * 8);
            return threshold;
    }
    // Function to get a binary string from the signal 
    public static String ByteString(ArrayList<Double> samples, double threshold,int threshold_ln)
    {
            String s="";
            int i = threshold_ln;
            double temp = threshold;
            int pos=0;
            while (i + 100 < samples.size())
            {
                for (int j = i; j < i + 100; j++)
                {
                    if (samples.get(j) > temp)
                    {
                        temp = samples.get(j);
                        pos = j;

                    }

                }
                if (pos < i + 10)
                {
                    s += 0;
                }
                if (pos > i + 10)
                {
                    s += 1;
                }

                i += 100;
                temp = threshold;
            }
            // System.out.println("Noise");
            // for(int n=0;n<s.length()-7;n+=7)
            // {
               
            // System.out.print( s.substring(n,n+7)+" ");
            // }
            return s;
    }

    //Function to get the result from the binary string
    public static String TextString(String s)
    { 
        String ans="";
        String data_s="",char_s="";
        for (int i = 8; i < s.length() - 3; i = i + 7)
        {
            data_s += (s.substring(i, i + 4));

        }
        int data = 0;
       //
        //corrected_s=ErrorCorrection(data_s);

        for (int i = 0; i < data_s.length() - 8; i += 8)
        {
            data = Integer.parseInt(data_s.substring(i, i + 8), 2);
            char_s = Character.toString((char) data);
            ans += char_s;
        }
            return ans;
    }
    public static String ErrorCorrection(String s)
    {
        String corrected_string="00000000";
        int arr[]=new int[7];
        
        int[][] parity ={{1,0,1},{1,1,1},{1,1,0},{0,1,1},{1,0,0},{0,1,0},{0,0,1}};
        int[] code_vector=new int[7];
        int[] syndrome={0,0,0};
        int[] ref={0,0,0}; 
        

        for(int i=8;i<=s.length()-7;i+=7)
        {
            int k=i;
            for(int j=0;j<7;j++)
            {
                code_vector[j]=Character.getNumericValue(s.charAt(k));
                k+=1;
            }

            for(int r=0;r<3;r++)
            {
                syndrome[r]=0;
                for(int p=0;p<code_vector.length;p++)
                {
                syndrome[r]+=code_vector[p]*parity[p][r];
                }
                syndrome[r]=syndrome[r]%2;
                
    }

    if(!(Arrays.equals(syndrome,ref)))
    {
        for(int m=0;m<7;m++)
        {
            if(Arrays.toString(syndrome).equals(Arrays.toString(parity[m])))
            {
                code_vector[m]=code_vector[m]^1;
            }
        }

    }
    for(int z=0;z<code_vector.length;z++)
    {
    corrected_string+=(code_vector[z]);
    }
}
//corrected_string+=s.substring(s.length()-(s.length()%7));
// for(int n=0;n<s.length()-7;n+=7)
//            {
//            System.out.print( corrected_string.substring(n,n+7)+" ");
//            }
        return corrected_string;
    }
    public static void main(String args[])
    {
        
        double threshold = 0;
        int threshold_ln = 0;
        String s="",ans="", corrected_string="";
        File file = new File("C:\\Users\\Dell\\Music\\DataCom\\Assignments\\proj1_testsignal2.txt");
        File noise = new File("C:\\Users\\Dell\\Music\\DataCom\\Assignments\\proj2_noisesignal2.txt");
        ArrayList<Double> samples = new ArrayList<>();
        ArrayList<Double> noise_samples = new ArrayList<>();
        ArrayList<Double> final_samples = new ArrayList<>();
        Scanner read = null;
        Scanner read_noise = null;

        try
         {
            read = new Scanner(new FileReader(file));
            while (read.hasNextDouble())
            {
                samples.add(read.nextDouble());

            }
            read_noise=new Scanner(new FileReader(noise));

            while(read_noise.hasNextDouble())
            {
                 noise_samples.add(read_noise.nextDouble());
            }
            for(int i=0;i<samples.size();i++)
            {
                final_samples.add(samples.get(i)+noise_samples.get(i));
            }
            
            //Calculating  Mean, Standard_Deviation to findout Threshold

           threshold= CalculateThreshold(final_samples);
            
            for (int i = 10000; i < final_samples.size(); i++)
            {
                if (final_samples.get(i) > threshold)
                {
                    threshold_ln = i;
                    break;
                }
            }

            //Getting a binary string from the signal    
             s = ByteString(final_samples, threshold,threshold_ln);
             //System.out.println("Before changes"+s.length());
             
             

             corrected_string=ErrorCorrection(s);

             

             // Getting the result from the binary string
             ans=TextString(corrected_string);
            //ans=TextString(s);
            System.out.println("Output : " + ans);
        }

        catch (Exception e)
        {
            System.out.println(e);
        }

    }
}
