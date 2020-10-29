package com.yube;

public class SimplexTableIterationStatistic {

    private boolean optimal;
    private Integer in;
    private Integer out;

    public SimplexTableIterationStatistic(boolean optimal) {
        this.optimal = optimal;
    }

    public boolean isOptimal() {
        return optimal;
    }

    public void setOptimal(boolean optimal) {
        this.optimal = optimal;
    }

    public Integer getIn() {
        return in;
    }

    public void setIn(Integer in) {
        this.in = in;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }
}
