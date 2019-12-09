package webplay.ResponseClasses.Gashas;

import java.util.ArrayList;

public class GashasRootObject {
    private ArrayList<Gasha> gashas;
    public int processed_at;

    public ArrayList<Gasha> getGashas() {
        return gashas;
    }

    public void setGashas(ArrayList<Gasha> gashas) {
        this.gashas = gashas;
    }

    public int getProcessed_at() {
        return processed_at;
    }

    public void setProcessed_at(int processed_at) {
        this.processed_at = processed_at;
    }
}
