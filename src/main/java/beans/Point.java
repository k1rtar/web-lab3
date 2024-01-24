package beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
public class Point implements Serializable {
    private double x;
    private double y;
    private double r;
    private boolean hit;
    private long attemptTime;
    private double executionTime;
    private transient ResultTable table;
    private Double hiddenY;

    public Point(double x, double y, double r, boolean hit, long attemptTime, double executionTime) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.attemptTime = attemptTime;
        this.executionTime = executionTime;
        this.hiddenY = y;
    }


    public void check() {
        long start = System.nanoTime();
        long attemptTime = System.currentTimeMillis();
        if (hiddenY==null || hiddenY == 0 || hiddenY%1==0) this.hiddenY = y;
        boolean hit = ((x >= 0) && (x <= r/2) && (hiddenY <= 0) && (hiddenY >= -r))
                || ((x >= 0) && (x <= r / 2) && (hiddenY >= 0) && (hiddenY <= r / 2) && (hiddenY<=-x+r/2))
                || (x <= 0 && x >= -r && hiddenY <= 0 && hiddenY>=-r && x*x+hiddenY*hiddenY<=r*r && y>=-r & y<=0);
        long executionTime = System.nanoTime() - start;
        table.addPoint(new Point(x, hiddenY, r, hit, attemptTime, executionTime));
        this.hiddenY = y;
    }




}
