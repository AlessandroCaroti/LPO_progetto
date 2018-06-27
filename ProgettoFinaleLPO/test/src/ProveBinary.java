public class ProveBinary {





    public static void main(String[] args) {
        String binString  = "ob0111";
        int bin = Integer.parseInt(binString.substring(2),2);
        System.out.println(bin);
    }
}
