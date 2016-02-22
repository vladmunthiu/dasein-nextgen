package core.actions;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface Func<TResult> {
    TResult call() throws Exception;
}
