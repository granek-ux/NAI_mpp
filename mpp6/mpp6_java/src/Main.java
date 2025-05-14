import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Podaj N:");
        int n = sc.nextInt();

        int[] a = new int[n];

        boolean kont = true;

        while (true) {
//                printArray(a);
                int i = a.length-1;
                kont = checkifDone(a);
                if (!kont)
                    break;

                addToarr(a, i);


        }

        System.out.println("Stop");

    }

    public static boolean checkifDone(int[] n) {
        boolean czygit = false;
        for (int i : n) {
            if (i == 0) czygit = true;
        }
        return czygit;
    }

    public static void printArray(int[] a) {
        System.out.print("[");
        for (int w : a) {
            System.out.print(w + ", ");
        }
        System.out.println("]");
    }

    public static void addToarr(int[] a, int i)
    {
        if(a[i] == 0)
            a[i] = 1;
        else if (a[i] == 1)
        {
            a[i] = 0;
            addToarr(a, i - 1);
        }

    }
}