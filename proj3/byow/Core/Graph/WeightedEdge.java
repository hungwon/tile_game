package byow.Core.Graph;

public class WeightedEdge {
    private Double weight;
    private Integer start;
    private Integer end;

    public WeightedEdge(int v1, int v2, double weight) {
        start = v1;
        end = v2;
        this.weight = weight;
    }
    public Double weight() {
        return weight;
    }
    public Integer from() {
        return start;
    }

    public Integer to() {
        return end;
    }

    public static boolean isSame(WeightedEdge a, WeightedEdge b) {
        /*
        if (!a.weight.equals(b.weight)) {
            return false;
        }
         */

        if (a.from().equals(b.from())) { // (1->2, 0.1) and (1->2, 0.1) are same
            if (a.to().equals(b.to())) {
                return true;
            }
        } else if (a.from().equals(b.to())) { // (1->2, 0.1) and (2->1, 0.1) are same
            if (a.to().equals(b.from())) {
                return true;
            }
        }
        return false;
    }
}
