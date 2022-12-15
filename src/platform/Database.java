package platform;

import fileio.Input;

/**
 * Interface used for implementing databases that
 * store users / movies
 *
 * @author wh1ter0se
 */
public interface Database {
    /**
     * Method that initializes a database using data from input
     *
     * @param inputData data received at input
     */
    void initDatabase(Input inputData);
}
