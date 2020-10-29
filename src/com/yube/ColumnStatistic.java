package com.yube;

public class ColumnStatistic {

    private boolean possibleBasis;
    private Boolean positive;
    private Boolean normalized;
    private Integer index;

    public ColumnStatistic(boolean possibleBasis) {
        this.possibleBasis = possibleBasis;
    }

    public boolean isPossibleBasis() {
        return possibleBasis;
    }

    public void setPossibleBasis(boolean possibleBasis) {
        this.possibleBasis = possibleBasis;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }

    public Boolean getNormalized() {
        return normalized;
    }

    public void setNormalized(Boolean normalized) {
        this.normalized = normalized;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
