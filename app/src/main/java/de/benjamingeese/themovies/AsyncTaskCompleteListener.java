package de.benjamingeese.themovies;

/**
 * Implement this to on the class using a AsyncTask to be able to extract the
 * AsyncTask into it's on file an prevent using an inner class.
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {

    /**
     * Invoked by the AsyncTask when completed
     * @param result The result of the async execution
     */
    public void onTaskComplete(T result);
}
