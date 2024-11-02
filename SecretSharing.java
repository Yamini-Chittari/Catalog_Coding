import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    public static long decodeValue(String value, int base) {
        return Long.parseLong(value, base);
    }

    public static double lagrangeInterpolation(List<Point> points, double x) {
        double total = 0.0;
        int k = points.size();

        for (int i = 0; i < k; i++) {
            Point pointI = points.get(i);
            double xi = pointI.x;
            double yi = pointI.y;
            double li = 1.0;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    Point pointJ = points.get(j);
                    double xj = pointJ.x;
                    li *= (x - xj) / (xi - xj);
                }
            }

            total += yi * li;
        }

        return total;
    }

    public static void main(String[] args) {
        try {
                    String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject json = new JSONObject(content);

            int n = json.getJSONObject("keys").getInt("n");
            int k = json.getJSONObject("keys").getInt("k");

                 List<Point> points = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                JSONObject root = json.getJSONObject(String.valueOf(i));
                int base = root.getInt("base");
                String value = root.getString("value");

                long decodedY = decodeValue(value, base);
                points.add(new Point(i, decodedY));
            }

            
            List<Point> selectedPoints = points.subList(0, k);

            double secretC = lagrangeInterpolation(selectedPoints, 0.0);

            
            System.out.println("The secret (c) is: " + (int)secretC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    static class Point {
        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
