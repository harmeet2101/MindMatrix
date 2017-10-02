package Interfaces;

/**
 * Created by pavan on 28-06-2015.
 */
public interface AsyncTaskCompleteListener {

    void onTaskCompleted(String response, int serviceCode);

    void onTaskFailure(int serviceCode);
}
