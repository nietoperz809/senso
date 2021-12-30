import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Random5x4 {
    private List<Integer> solution = new ArrayList<>();

    public Random5x4()
    {
        for (int i=0; i < 4; i++) {
            solution.add(i);
            solution.add(i);
            solution.add(i);
            solution.add(i);
            solution.add(i);
        }
        Collections.shuffle(solution);
    }

    public int get (int idx)
    {
        return solution.get(idx);
    }
}
