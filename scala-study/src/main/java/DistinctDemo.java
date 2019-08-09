import com.sun.jnlp.JNLPClassLoaderIf;

import java.util.Scanner;

public class DistinctDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入字符串");
        String string = scanner.nextLine();
        scanner.close();

        if (string == null){
            return;
        }

        String reString = "";
        StringBuilder builder = new StringBuilder();
        char[] chars = string.toCharArray();

        for (int i= 0;i<chars.length;i++){
            if (!reString.contains(chars[i]+"")){
                reString +=(chars[i]+"");
                builder.append(chars[i]);
            }
        }
        System.out.println(builder);
        System.out.println(reString);

    }
}
