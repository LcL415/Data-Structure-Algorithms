
//合并两个有序数组或者一个数组内部排序

import java.util.Arrays;

public class Main {

    public static void merge(int[] a1, int i, int iend, int j, int jend, int[] a2, int k) {
        if(i>iend){
            System.arraycopy(a1,j,a2,k,jend-j+1);
            return;
        }
        if(j>jend){
            System.arraycopy(a1,i,a2,k,iend-i+1);
            return;
        }
        if(a1[i]<a2[j])
        {
            a2[k]=a1[i];
            merge(a1,i+1,iend,j,jend,a2,k+1);
        }
        else {
            a2[k]=a2[j];
            merge(a1, i, iend,j+1,jend,a2,k+1);
        }
    }



    public static void main(String[] args) {

      int a[]={1,3,4,5,2,4,6,9};
      int b[]=new int[a.length];
      merge(a,0,3,4,8,b,0);
      System.out.printf(Arrays.toString(b));



    }


}