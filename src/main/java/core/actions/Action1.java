package core.actions;

/**
 * Created by vmunthiu on 11/23/2015.
 */
public interface Action1<T> {
    void call(T target) throws Exception;
}
